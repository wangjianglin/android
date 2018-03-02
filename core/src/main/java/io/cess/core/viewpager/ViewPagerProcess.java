package io.cess.core.viewpager;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import io.cess.core.ViewPager;
import io.cess.core.viewpager.indicator.ViewPagerContentIndicator;
import io.cess.core.viewpager.indicator.ViewPagerIndicator;

/**
 * Created by lin on 09/01/2017.
 */

public class ViewPagerProcess {

    private int mDurationToCloseHeader = 800;

    private ScrollChecker mScrollChecker;
    private ViewPagerContentIndicator mContentIndicator;
    private ViewPagerIndicator mIndicator = new ViewPagerIndicator();

    private boolean mHasSendCancelEvent;
    private OnListener mOnListener;
    private Context mContext;
    private ViewPager mViewPager;


    public ViewPagerProcess(ViewPager view, ViewPagerContentIndicator indicator, OnListener onListener){
        this.mContentIndicator = indicator;
        this.mOnListener = onListener;
        this.mContext = view.getContext();
        this.mViewPager = view;
        mScrollChecker = new ScrollChecker();
    }

    public void move(float delta){
//        delta = (float) (delta * Math.cos(mIndicator.getCurrentPosY()*3.2/ mViewPager.getWidth()));
        delta = (float) (delta / Math.exp(mIndicator.getCurrentPosY()*2.7/ mViewPager.getWidth()));
        movePos(delta);
    }
    private void movePos(float delta) {
        if (delta < 0 && mIndicator.isInStartPosition()) {
            return;
        }

        int to = mIndicator.getCurrentPosY() + (int) delta;

        // over top
        if (mIndicator.willOverTop(to)) {
            to = ViewPagerIndicator.POS_START;
        }

        mIndicator.setCurrentPos(to);
        int change = to - mIndicator.getLastPosY();
        updatePos(change);
    }

    private void updatePos(int change) {
        if (change == 0) {
            return;
        }

        boolean isUnderTouch = mContentIndicator.isUnderTouch();

        // once moved, cancel event will be sent to child
        if (isUnderTouch && !mHasSendCancelEvent && mIndicator.hasMovedAfterPressedDown()) {
            mHasSendCancelEvent = true;
            sendCancelEvent();
        }

        // back to initiated position
        if (mIndicator.hasJustBackToStartPosition()) {

            // recover event to children
            if (isUnderTouch) {
                sendDownEvent();
            }
        }

        mOnListener.move(change);
    }
    private void sendCancelEvent() {
        MotionEvent mLastMoveEvent = mContentIndicator.getLastMoveEvent();
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());

        mOnListener.sendEvent(e);
    }

    public void onPressDown(){
        mHasSendCancelEvent = false;
        mIndicator.onPressDown();
    }

    public boolean onRelease(){
        if (mIndicator.hasLeftStartPosition()) {
            tryScrollBackToTop();
            if(mIndicator.hasMovedAfterPressedDown()){
                sendCancelEvent();
                return true;
            }
        }
        return false;
    }

    private void tryScrollBackToTop() {
        if (!mContentIndicator.isUnderTouch()) {
            mScrollChecker.tryToScrollTo(ViewPagerIndicator.POS_START, mDurationToCloseHeader);
        }
    }
    private void sendDownEvent() {

        final MotionEvent last = mContentIndicator.getLastMoveEvent();
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());

        mOnListener.sendEvent(e);
    }


    public void onDetached() {
        if (mScrollChecker != null) {
            mScrollChecker.destroy();
        }
    }


    private class ScrollChecker implements Runnable {

        private int mLastFlingY;
        private Scroller mScroller;
        private boolean mIsRunning = false;
        private int mStart;
        private int mTo;

        public ScrollChecker() {
            mScroller = new Scroller(mContext);
        }

        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int curY = mScroller.getCurrY();
            int deltaY = curY - mLastFlingY;

            if (!finish) {
                mLastFlingY = curY;
                movePos(deltaY);
                mViewPager.post(this);
            } else {
                finish();
            }
        }

        private void finish() {
            reset();
//            onPtrScrollFinish();
        }

        private void reset() {
            mIsRunning = false;
            mLastFlingY = 0;
            mViewPager.removeCallbacks(this);
        }

        private void destroy() {
            reset();
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
        }

        public void abortIfWorking() {
            if (mIsRunning) {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
//                onPtrScrollAbort();
                reset();
            }
        }

        public void tryToScrollTo(int to, int duration) {
            if (mIndicator.isAlreadyHere(to)) {
                return;
            }
            mStart = mIndicator.getCurrentPosY();
            mTo = to;
            int distance = to - mStart;

            mViewPager.removeCallbacks(this);

            mLastFlingY = 0;

            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, distance, duration);
            mViewPager.post(this);
            mIsRunning = true;
        }
    }

    public static interface OnListener{
        void move(int delta);
        void sendEvent(MotionEvent e);
    }

    public ViewPagerIndicator getPtrIndicator() {
        return mIndicator;
    }
}
