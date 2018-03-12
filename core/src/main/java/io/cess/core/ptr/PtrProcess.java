package io.cess.core.ptr;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import io.cess.core.ptr.indicator.ContentIndicator;
import io.cess.core.ptr.indicator.PtrIndicator;

import static io.cess.core.ptr.PtrView.*;

/**
 * @author lin
 * @date 09/01/2017.
 */

public class PtrProcess {
    // auto refresh status
    private final static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;
    private final static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;
    private final static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
    private final static byte MASK_AUTO_REFRESH = 0x03;


    private int mFlag = 0x00;
    private int mDurationToClose = 200;
    private int mDurationToCloseHeader = 800;
    private boolean mKeepHeaderWhenRefresh = true;//刷新的时候，显示 header view
    private Status mStatus = Status.Init;//表示当前状态
    private boolean mPullToLoad = false;//表示超过指定高度就开始刷新

    private ScrollChecker mScrollChecker;
    private ContentIndicator mIndicator;
    private PtrIndicator mPtrIndicator = new PtrIndicator();

    private boolean mHasSendCancelEvent;
    private OnListener mOnListener;
    private Context mContext;
    private PtrView mPtrView;

    private int mLoadingMinTime = 500;
    private long mLoadingStartTime = 0;

    PtrUIHandlerHolder mPtrUIHandler;
    private boolean mDisableLoad = false;

    private PtrUIHandlerHook mRefreshCompleteHook;

    private Runnable mPerformRefreshCompleteDelay = new Runnable() {
        @Override
        public void run() {
            performRefreshComplete();
        }
    };



    public PtrProcess(PtrView view, ContentIndicator mIndicator, OnListener onListener){
        this.mIndicator = mIndicator;
        this.mOnListener = onListener;
        this.mContext = view.getContext();
        this.mPtrView = view;
        mScrollChecker = new ScrollChecker();
    }

    public void move(float delta){
//        delta = (float) (delta * Math.cos(mPtrIndicator.getCurrentPosY()*3.2/mPtrView.getMeasuredHeight()));
        delta = (float) (delta / Math.exp(mPtrIndicator.getCurrentPosY()*2.7/ mPtrView.getHeight()));
        movePos(delta);
    }
    private void movePos(float delta) {
        if (delta < 0 && mPtrIndicator.isInStartPosition()) {
            return;
        }

        int to = mPtrIndicator.getCurrentPosY() + (int) delta;

        // over top
        if (mPtrIndicator.willOverTop(to)) {
            to = PtrIndicator.POS_START;
        }

        mPtrIndicator.setCurrentPos(to);
        int change = to - mPtrIndicator.getLastPosY();
        updatePos(change);
    }

    private void updatePos(int change) {
        if (change == 0) {
            return;
        }

        boolean isUnderTouch = mIndicator.isUnderTouch();

        // once moved, cancel event will be sent to child
        if (isUnderTouch && !mHasSendCancelEvent && mPtrIndicator.hasMovedAfterPressedDown()) {
            mHasSendCancelEvent = true;
            sendCancelEvent();
        }

        // leave initiated position or just refresh complete
        if ((mPtrIndicator.hasJustLeftStartPosition() && mStatus == Status.Init) ||
                (mPtrIndicator.goDownCrossFinishPosition() && mStatus == Status.Complete
                        && isEnabledNextPtrAtOnce())) {

            mStatus = Status.Prepare;
            mPtrUIHandler.onUIPrepare(mPtrView);
        }

        // back to initiated position
        if (mPtrIndicator.hasJustBackToStartPosition()) {
            tryToNotifyReset();

            // recover event to children
            if (isUnderTouch) {
                sendDownEvent();
            }
        }

        // Pull to Refresh
        if (mStatus == Status.Prepare) {
            // reach fresh height while moving from top to bottom
            if (isUnderTouch && !isAutoLoad() && mPtrView.isPullToRefresh()
                    && mPtrIndicator.crossRefreshLineFromTopToBottom()) {
                tryToPerformRefresh();
            }
            // reach header height while auto refresh
            if (performAutoRefreshButLater() && mPtrIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
                tryToPerformRefresh();
            }
        }


        mOnListener.move(change);

        if(mPtrUIHandler.hasHandler()){
            mPtrUIHandler.onUIPositionChange(mPtrView,isUnderTouch,mStatus,mPtrIndicator);
        }

    }
    private void sendCancelEvent() {

        MotionEvent last = mIndicator.getLastMoveEvent();
        if (last == null) {
            return;
        }
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());

        mOnListener.sendEvent(e);
    }

    public void onPressDown(){
        mHasSendCancelEvent = false;
        mPtrIndicator.onPressDown();
    }

    public boolean onRelease(){
        if (mPtrIndicator.hasLeftStartPosition()) {
            onRelease(false);
            if(mPtrIndicator.hasMovedAfterPressedDown()){
                sendCancelEvent();
                return true;
            }
        }
        return false;
    }
    private void onRelease(boolean stayForLoading) {

        tryToPerformRefresh();

        if (mStatus == Status.Loading) {
            // keep header for fresh
            if (mKeepHeaderWhenRefresh) {
                // scroll header back
                if (mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
                    mScrollChecker.tryToScrollTo(mPtrIndicator.getOffsetToKeepHeaderWhileLoading(), mDurationToClose);
                } else {
                    // do nothing
                }
            } else {
                tryScrollBackToTopWhileLoading();
            }
        } else {
            if (mStatus == Status.Complete) {
                notifyUIRefreshComplete(false);
            } else {
                tryScrollBackToTopAbortRefresh();
            }
        }
    }

    private void tryToPerformRefresh() {
        if(mDisableLoad){
            return;
        }
        if (mStatus != Status.Prepare) {
            return;
        }

        //
        if ((mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoLoad()) || mPtrIndicator.isOverOffsetToRefresh()) {
            mStatus = Status.Loading;
            performRefresh();
        }
    }

    private void performRefresh() {
        mLoadingStartTime = System.currentTimeMillis();

        if(mPtrUIHandler.hasHandler()){
            mPtrUIHandler.onUIBegin(mPtrView);
        }
        mOnListener.onLoading();
    }

    private void notifyUIRefreshComplete(boolean ignoreHook) {
        /**
         * After hook operation is done, {@link #notifyUIRefreshComplete} will be call in resume action to ignore hook.
         */
        if (mPtrIndicator.hasLeftStartPosition() && !ignoreHook && mRefreshCompleteHook != null) {
            mRefreshCompleteHook.takeOver();
            return;
        }

        if(mPtrUIHandler.hasHandler()){
            mPtrUIHandler.onUIComplete(mPtrView);
        }
        mPtrIndicator.onUIRefreshComplete();
        tryScrollBackToTopAfterComplete();
        tryToNotifyReset();
    }

    /**
     * If at the top and not in loading, reset
     */
    private void tryToNotifyReset() {
        if ((mStatus == Status.Complete || mStatus == Status.Prepare) &&
                mPtrIndicator.isInStartPosition()) {

            if(mPtrUIHandler.hasHandler()){
                mPtrUIHandler.onUIReset(mPtrView);
            }
            mStatus = Status.Init;
            clearFlag();
        }
    }

    /*
     * 检测是否有FLAG_AUTO_REFRESH_BUT_LATER标识
     */
    private boolean performAutoRefreshButLater() {
        return (mFlag & MASK_AUTO_REFRESH) == FLAG_AUTO_REFRESH_BUT_LATER;
    }

    /*
     * 去掉自动刷新标识
     */
    private void clearFlag() {
        // remove auto fresh flag
        mFlag = mFlag & ~MASK_AUTO_REFRESH;
    }

    private void tryScrollBackToTop() {
        if (!mIndicator.isUnderTouch()) {
            mScrollChecker.tryToScrollTo(PtrIndicator.POS_START, mDurationToCloseHeader);
        }
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopWhileLoading() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAfterComplete() {
        tryScrollBackToTop();
    }

    /**
     * just make easier to understand
     */
    private void tryScrollBackToTopAbortRefresh() {
        tryScrollBackToTop();
    }

    private void sendDownEvent() {

        final MotionEvent last = mIndicator.getLastMoveEvent();
        if (last == null) {
            return;
        }
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());

        mOnListener.sendEvent(e);
    }

    public boolean isEnabledNextPtrAtOnce() {
        return (mFlag & FLAG_ENABLE_NEXT_PTR_AT_ONCE) > 0;
    }

    protected void onPtrScrollFinish() {
        if (mPtrIndicator.hasLeftStartPosition() && isAutoLoad()) {
            onRelease(true);
        }
    }

    protected void onPtrScrollAbort() {
        if (mPtrIndicator.hasLeftStartPosition() && isAutoLoad()) {
            onRelease(true);
        }
    }

    private boolean isAutoLoad(){
        return (mFlag & MASK_AUTO_REFRESH) > 0;
    }


    public void autoLoad() {
        autoLoad(true, mDurationToCloseHeader);
    }

//    public void autoLoad(boolean atOnce) {
//        autoLoad(atOnce, mDurationToCloseHeader);
//    }

    private void autoLoad(boolean atOnce, int duration) {

        if (mStatus != Status.Init) {
            return;
        }

        mFlag |= atOnce ? FLAG_AUTO_REFRESH_AT_ONCE : FLAG_AUTO_REFRESH_BUT_LATER;

        mStatus = Status.Prepare;
        if (mPtrUIHandler.hasHandler()) {
            mPtrUIHandler.onUIPrepare(mPtrView);
        }

        mScrollChecker.tryToScrollTo(mPtrIndicator.getHeaderHeight(), duration);
        if (atOnce) {
            mStatus = Status.Loading;
            performRefresh();
        }
    }

    final public void complete() {


        if (mRefreshCompleteHook != null) {
            mRefreshCompleteHook.reset();
        }

        int delay = (int) (mLoadingMinTime - (System.currentTimeMillis() - mLoadingStartTime));
        if (delay <= 0) {

            performRefreshComplete();
        } else {
            mPtrView.postDelayed(mPerformRefreshCompleteDelay, delay);

        }
    }

    /**
     * Do refresh complete work when time elapsed is greater than {@link #mLoadingMinTime}
     */
    private void performRefreshComplete() {
        mStatus = Status.Complete;

        // if is auto refresh do nothing, wait scroller stop
        if (mScrollChecker.mIsRunning && isAutoLoad()) {
            // do nothing
            return;
        }

        notifyUIRefreshComplete(false);
    }

    public void onDetached() {
        if (mScrollChecker != null) {
            mScrollChecker.destroy();
        }

        if (mPerformRefreshCompleteDelay != null) {
            mPtrView.removeCallbacks(mPerformRefreshCompleteDelay);
        }
    }

    public Status getStatus() {
        return mStatus;
    }

    public void disable(){
        mDisableLoad = true;
    }

    public boolean isDisableLoad(){
        return mDisableLoad;
    }

    public void enable(){
        mDisableLoad = false;
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
                mPtrView.post(this);
            } else {
                finish();
            }
        }

        private void finish() {
            reset();
            onPtrScrollFinish();
        }

        private void reset() {
            mIsRunning = false;
            mLastFlingY = 0;
            mPtrView.removeCallbacks(this);
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
                onPtrScrollAbort();
                reset();
            }
        }

        public void tryToScrollTo(int to, int duration) {
            if (mPtrIndicator.isAlreadyHere(to)) {
                return;
            }
            mStart = mPtrIndicator.getCurrentPosY();
            mTo = to;
            int distance = to - mStart;

            mPtrView.removeCallbacks(this);

            mLastFlingY = 0;

            // fix #47: Scroller should be reused, https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, distance, duration);
            mPtrView.post(this);
            mIsRunning = true;
        }
    }

    public boolean isPullToLoad() {
        return mPullToLoad;
    }

    public void setPullToLoad(boolean pullToLoad) {
        this.mPullToLoad = pullToLoad;
    }

    public static interface OnListener{
        void move(int delta);
        void sendEvent(MotionEvent e);
        void onLoading();
    }

    public PtrIndicator getPtrIndicator() {
        return mPtrIndicator;
    }
}
