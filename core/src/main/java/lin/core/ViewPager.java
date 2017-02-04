package lin.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import lin.core.viewpager.ViewPagerRightProcess;
import lin.core.viewpager.ViewPagerProcess;
import lin.core.viewpager.ViewPagerLeftProcess;
import lin.core.viewpager.indicator.ViewPagerContentIndicator;
import lin.core.viewpager.indicator.ViewPagerIndicator;

/**
 * Created by lin on 09/01/2017.
 */

public class ViewPager extends android.support.v4.view.ViewPager {

    public ViewPager(Context context) {
        super(context);
        this.init();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }


    private ViewPagerContentIndicator mIndicator = new ViewPagerContentIndicator();

    private ViewPagerLeftProcess mLeftProcess = null;
    private ViewPagerRightProcess mRightProcess = null;

    private boolean mDisableWhenVerticalMove = true;//表示是否禁止垂直 move， true 禁止,false允许

    // disable when detect moving horizontally
    private boolean mPreventForVertical = false;//表示当禁止水平move时，是否检测到水平 move
    private int mPagingTouchSlop = 0;

    private void init(){
        if(this.getBackground() == null){
            this.setBackgroundColor(0xffbbbbbb);
        }
        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

        mLeftProcess = new ViewPagerLeftProcess(this,mIndicator, new ViewPagerProcess.OnListener() {
            @Override
            public void move(int delta) {
                ViewPager.this.setScrollX(ViewPager.this.getScrollX()-delta);
            }

            @Override
            public void sendEvent(MotionEvent e) {
                dispatchTouchEventSupper(e);
            }
        });

        mRightProcess = new ViewPagerRightProcess(this,mIndicator, new ViewPagerProcess.OnListener() {
            @Override
            public void move(int delta) {
                ViewPager.this.setScrollX(ViewPager.this.getScrollX()+delta);
            }

            @Override
            public void sendEvent(MotionEvent e) {
                dispatchTouchEventSupper(e);
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (!isEnabled() || this.getChildCount() == 0) {
            return dispatchTouchEventSupper(e);
        }

        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIndicator.onRelease();
                if(mLeftProcess.onRelease() || mRightProcess.onRelease()){
                    return true;
                }

                return dispatchTouchEventSupper(e);

            case MotionEvent.ACTION_DOWN:

                mIndicator.onPressDown(e.getX(), e.getY());
                mLeftProcess.onPressDown();
                mRightProcess.onPressDown();

                mPreventForVertical = false;

                dispatchTouchEventSupper(e);
                return true;

            case MotionEvent.ACTION_MOVE:

                mIndicator.onMove(e);
                float offsetX = mIndicator.getOffsetX();
                float offsetY = mIndicator.getOffsetY();

                ViewPagerIndicator mLeftIndicator = mLeftProcess.getPtrIndicator();
                ViewPagerIndicator mRightIndicator = mRightProcess.getPtrIndicator();
                if (mDisableWhenVerticalMove && !mPreventForVertical//表示是否需要检测水平move
                        && (Math.abs(offsetY) > mPagingTouchSlop && Math.abs(offsetX) < Math.abs(offsetY))//检测是否有水平move
                        ) {
                    if (mLeftIndicator.isInStartPosition()) {//如果是处理开始位置，则不处理
                        mPreventForVertical = true;
                    }
                }
                if (mPreventForVertical) {//如果禁止水平move，并且已经检测到了水平move
                    return dispatchTouchEventSupper(e);
                }

                boolean moveDown = offsetX > 0;//向右 move
                boolean moveUp = !moveDown;//向左 move

                // disable move when header not reach top
                if ((moveDown && mRightIndicator.isInStartPosition()
                        && !isLeft())
                        || (moveUp && mLeftIndicator.isInStartPosition()
                        && !isRight())
                        ) {
                    return dispatchTouchEventSupper(e);
                }

                boolean canMoveUp = mLeftIndicator.hasLeftStartPosition();
                boolean canMoveDown = mRightIndicator.hasLeftStartPosition();


                if (((moveUp && canMoveUp) || moveDown)
                        && !canMoveDown) {
                    mLeftProcess.move(offsetX);
                    return true;
                }

                if((moveDown && canMoveDown) || moveUp){
                    mRightProcess.move(-offsetX);
                    return true;
                }
        }
        return dispatchTouchEventSupper(e);
    }

    private boolean isLeft(){
        return this.getCurrentItem() == 0
                && this.getScrollX() <= 0;
    }

    private boolean isRight(){
        if(this.getAdapter() == null){
            return false;
        }
        int count = this.getAdapter().getCount();

        if(count == 0 || this.getCurrentItem() != count -1){
            return false;
        }
        return this.getScrollX() >= this.getWidth() * (count-1);
    }

    private boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLeftProcess.onDetached();
        mRightProcess.onDetached();
    }
}


//package lin.core.ptr;
//
//        import android.content.Context;
//        import android.util.AttributeSet;
//        import android.view.Gravity;
//        import android.view.MotionEvent;
//        import android.view.View;
//        import android.view.ViewConfiguration;
//        import android.view.ViewGroup;
//        import android.widget.Scroller;
//        import android.widget.TextView;
//
//        import lin.core.ptr.indicator.ViewPagerContentIndicator;
//        import lin.core.ptr.indicator.PtrBottomIndicator;
//        import lin.core.ptr.indicator.PtrTopIndicator;
//
///**
// * Created by lin on 08/01/2017.
// */
///*
//
//1、调试有时界面会出现卡死的情况
//
//
// */
//public class PullToRefreshView extends ViewGroup {
//    // status enum
//    private final static byte PTR_STATUS_INIT = 1;
//    public final static byte PTR_STATUS_PREPARE = 2;
//    public final static byte PTR_STATUS_LOADING = 3;
//    public final static byte PTR_STATUS_COMPLETE = 4;
//    private static final boolean DEBUG_LAYOUT = false;
//    public static final boolean DEBUG = false;
//    private static int ID = 1;
//    protected final String LOG_TAG = "ptr-frame-" + ++ID;
//    // auto refresh status
//    private final static byte FLAG_AUTO_REFRESH_AT_ONCE = 0x01;
//    private final static byte FLAG_AUTO_REFRESH_BUT_LATER = 0x01 << 1;
//    private final static byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 0x01 << 2;
//    private final static byte FLAG_PIN_CONTENT = 0x01 << 3;
//    private final static byte MASK_AUTO_REFRESH = 0x03;
//
//
//    private byte mStatus = PTR_STATUS_INIT;//表示当前状态
//    private byte mRefreshOrMode = 0;//0、表示即没有下拉刷新与没有上拉加载更多，1、表示下拉刷新状态中，2、表示上拉加载更多中
//
//    private int mFlag = 0x00;
//    private int mPagingTouchSlop = 0;
//    private MotionEvent mLastMoveEvent;
//
//    public PullToRefreshView(Context context) {
//        super(context);
//        this.init();
//    }
//
//    public PullToRefreshView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.init();
//    }
//
//    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.init();
//    }
//
//    private void init(){
////        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        final ViewConfiguration conf = ViewConfiguration.get(getContext());
//        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
//    }
//
//    private boolean mKeepHeaderWhenRefresh = true;//刷新的时候，显示 header view
//    private boolean mDisableWhenVerticalMove = false;//表示是否禁止水平 move， true 禁止,false允许
//
//    // disable when detect moving horizontally
//    private boolean mPreventForVertical = false;//表示当禁止水平move时，是否检测到水平 move
//
//    private boolean mHasSendCancelEvent;
//
//    private int mHeaderId = 0;
//    private  int mFooterId = 0;
//    private int mContentId = 0;
//
//    private View mHeaderView = null;
//    private View mFooterView = null;
//    private View mContent = null;
//
//    private ViewPagerContentIndicator mIndicator = new ViewPagerContentIndicator();
//    private PtrTopIndicator mTopIndicator = new PtrTopIndicator();
//    private PtrBottomIndicator mBottomIndicator = new PtrBottomIndicator();
//
//    @Override
//    protected void onFinishInflate() {
//        final int childCount = getChildCount();
//        if (childCount > 3) {
//            throw new IllegalStateException("PtrFrameLayout can only contains 2 children");
//        } else if (childCount >= 2) {
//
//            if (mHeaderId != 0 && mHeaderView == null) {
//                mHeaderView = findViewById(mHeaderId);
//            }
//            if(mFooterId != 0 && mFooterView == null){
//                mFooterView = findViewById(mFooterId);
//            }
//            if (mContentId != 0 && mContent == null) {
//                mContent = findViewById(mContentId);
//            }
//
//            View child1 = getChildAt(0);
//            View child2 = getChildAt(1);
//            View child3 = getChildAt(2);
//
//            int isHandler = child1 instanceof PtrUIHandler ?1:0;
//            isHandler += child2 instanceof PtrUIHandler?1:0;
//            isHandler += child3 instanceof PtrUIHandler?1:0;
//            if (mHeaderView == null) {
//                if (isHandler <= 1
//                        || (child1 instanceof PtrUIHandler && child1 != mContent && child1 != mFooterView)) {
//                    mHeaderView = child1;
//                } else if (child2 instanceof PtrUIHandler && child2 != mContent && child2 != mFooterView) {
//                    mHeaderView = child2;
//                } else {
//                    mHeaderView = child3;
//                }
//            }
//
//            if (mContent == null) {
//                if (!(child1 instanceof PtrUIHandler) && child1 != mHeaderView && child1 != mFooterView) {
//                    mContent = child1;
//                } else if (!(child2 instanceof PtrUIHandler) && child2 != mHeaderView && child2 != mFooterView) {
//                    mContent = child2;
//                } else if (!(child3 instanceof PtrUIHandler) && child3 != mHeaderView && child3 != mFooterView){
//                    mContent = child3;
//                }
//                if(mContent == null){
//                    for (int i = 0; i < getChildCount(); i++) {
//                        View child = getChildAt(i);
//                        if (child != mHeaderView && child != mFooterView) {
//                            mContent = child;
//                            break;
//                        }
//                    }
//                }
//            }
//
//            if (mFooterView == null) {
//                if (child1 instanceof PtrUIHandler && child1 != mContent && child1 != mHeaderView) {
//                    mFooterView = child1;
//                } else if (child2 instanceof PtrUIHandler && child2 != mContent && child2 != mHeaderView) {
//                    mFooterView = child2;
//                } else {
//                    mFooterView = child3;
//                }
//            }
//
//        } else if (childCount == 1) {
//            mContent = getChildAt(0);
//        } else {
//            TextView errorView = new TextView(getContext());
//            errorView.setClickable(true);
//            errorView.setTextColor(0xffff6600);
//            errorView.setGravity(Gravity.CENTER);
//            errorView.setTextSize(20);
//            errorView.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
//            mContent = errorView;
//            addView(mContent);
//        }
//        if (mHeaderView != null) {
//            mHeaderView.bringToFront();
//        }
//        if(mFooterView != null){
//            mFooterView.bringToFront();
//            mFooterView.setBackgroundColor(0xff00ffff);
//        }
//        if(mContent != null){
//            mContent.setBackgroundColor(0xff666666);
//        }
//        super.onFinishInflate();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
////        if (mScrollChecker != null) {
////            mScrollChecker.destroy();
////        }
////
////        if (mPerformRefreshCompleteDelay != null) {
////            removeCallbacks(mPerformRefreshCompleteDelay);
////        }
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//
//        if (mHeaderView != null) {
//            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
//            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
//            int mTopHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            mTopIndicator.setHeaderHeight(mTopHeaderHeight);
//        }
//
//        if(mFooterView != null){
//            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
//            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
//            int mBottomHeaderHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            mBottomIndicator.setHeaderHeight(mBottomHeaderHeight);
//        }
//
//        if (mContent != null) {
//            measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
//        }
//    }
//
//    private void measureContentView(View child,
//                                    int parentWidthMeasureSpec,
//                                    int parentHeightMeasureSpec) {
//        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//
//        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
//        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
//                getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);
//
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//    }
//
//    @Override
//    protected void onLayout(boolean flag, int i, int j, int k, int l) {
//        layoutChildren();
//    }
//
//    private void layoutChildren() {
//        int offset = mTopIndicator.getCurrentPosY();
//        int paddingLeft = getPaddingLeft();
//        int paddingTop = getPaddingTop();
//
//        if (mHeaderView != null) {
//            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
//            final int left = paddingLeft + lp.leftMargin;
//            // enhance readability(header is layout above screen when first init)
//            final int top = -(mTopIndicator.getHeaderHeight() - paddingTop - lp.topMargin - offset);
//            final int right = left + mHeaderView.getMeasuredWidth();
//            final int bottom = top + mHeaderView.getMeasuredHeight();
//            mHeaderView.layout(left, top, right, bottom);
//
//        }
//        if(mFooterView != null){
//            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
//            final int left = paddingLeft + lp.leftMargin;
//            final int right = left + mFooterView.getMeasuredWidth();
//            int bottom = mContent.getMeasuredHeight() - mBottomIndicator.getCurrentPosY();
//            mFooterView.layout(left,bottom,right,bottom + mFooterView.getMeasuredHeight());
//        }
//        if (mContent != null) {
//            if (isPinContent()) {
//                offset = 0;
//            }
//            MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
//            final int left = paddingLeft + lp.leftMargin;
//            final int top = paddingTop + lp.topMargin + offset;
//            final int right = left + mContent.getMeasuredWidth();
//            final int bottom = top + mContent.getMeasuredHeight();
////            if (isDebug()) {
////                PtrCLog.d(LOG_TAG, "onLayout content: %s %s %s %s", left, top, right, bottom);
////            }
//            mContent.layout(left, top, right, bottom);
//        }
//    }
//
//    public boolean isPinContent() {
//        return (mFlag & FLAG_PIN_CONTENT) > 0;
//    }
//
//    public boolean dispatchTouchEventSupper(MotionEvent e) {
//        return super.dispatchTouchEvent(e);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent e) {
//        if (!isEnabled() || mContent == null || (mHeaderView == null && mFooterView == null)) {
//            return dispatchTouchEventSupper(e);
//        }
//
//        int action = e.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mIndicator.onRelease();
//                if (mTopIndicator.hasLeftStartPosition()) {
//
//                    //onRelease(false);
//                    if (mTopIndicator.hasMovedAfterPressedDown()) {
//                        sendCancelEvent();
//                        return true;
//                    }
//                } else if (mBottomIndicator.hasLeftStartPosition()) {
//
//
//                    //onRelease(false);
//                    if (mBottomIndicator.hasMovedAfterPressedDown()) {
//                        sendCancelEvent();
//                        return true;
//                    }
//                }
//
//                return dispatchTouchEventSupper(e);
//
//            case MotionEvent.ACTION_DOWN:
//                mHasSendCancelEvent = false;
//                mIndicator.onPressDown(e.getX(), e.getY());
//                mTopIndicator.onPressDown();
//                mBottomIndicator.onPressDown();
//
//                //mScrollChecker.abortIfWorking();
//
//                mPreventForVertical = false;
//                // The cancel event will be sent once the position is moved.
//                // So let the event pass to children.
//                // fix #93, #102
//                dispatchTouchEventSupper(e);
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//                mLastMoveEvent = e;
//                mIndicator.onMove(e.getX(), e.getY());
//                float offsetX = mIndicator.getOffsetX();
//                float offsetY = mIndicator.getOffsetY();
//
//                if (mDisableWhenVerticalMove && !mPreventForVertical//表示是否需要检测水平move
//                        && (Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))//检测是否有水平move
//                        ) {
//                    if (mTopIndicator.isInStartPosition()) {//如果是处理开始位置，则不处理
//                        mPreventForVertical = true;
//                    }
//                }
//                if (mPreventForVertical) {//如果禁止水平move，并且已经检测到了水平move
//                    return dispatchTouchEventSupper(e);
//                }
//
//                boolean moveDown = offsetY > 0;//向下 move
//                boolean moveUp = !moveDown;//向上 move
//
//
//
//                // disable move when header not reach top
//                if ((moveDown && !PtrHandler.checkCanDoRefresh(mContent)
//                        && mBottomIndicator.isInStartPosition())
//                        || (moveUp && !PtrHandler.checkCanDoLoadMore(mContent)
//                        && mTopIndicator.isInStartPosition())
//                        ) {
//                    return dispatchTouchEventSupper(e);
//                }
//
//                boolean canMoveUp = mTopIndicator.hasLeftStartPosition();
//                boolean canMoveDown = mBottomIndicator.hasLeftStartPosition();
//
//                if (((moveUp && canMoveUp) || moveDown)
//                        && mHeaderView != null
//                        && !canMoveDown) {
//                    moveTopPos(offsetY);
//                    return true;
//                }
//
//                if(((moveDown && canMoveDown) || moveUp)
//                        && mFooterView != null){
//                    moveBottomPos(-offsetY);
//                    return true;
//                }
//        }
//        return dispatchTouchEventSupper(e);
//    }
//
//    /**
//     * if deltaY > 0, move the content down
//     *
//     * @param deltaY
//     */
//    private void moveTopPos(float deltaY) {
//        // has reached the top
//        if ((deltaY < 0 && mTopIndicator.isInStartPosition())) {
//
//            return;
//        }
//
//        int to = mTopIndicator.getCurrentPosY() + (int) deltaY;
//
//        // over top
//        if (mTopIndicator.willOverTop(to)) {
//
//            to = PtrTopIndicator.POS_START;
//        }
//
//        mTopIndicator.setCurrentPos(to);
//        int change = to - mTopIndicator.getLastPosY();
//        updateTopPos(change);
//    }
//
//    private void updateTopPos(int change) {
//        if (change == 0) {
//            return;
//        }
//
//        boolean isUnderTouch = mIndicator.isUnderTouch();
//
//        // once moved, cancel event will be sent to child
//        if (isUnderTouch && !mHasSendCancelEvent && mTopIndicator.hasMovedAfterPressedDown()) {
//            mHasSendCancelEvent = true;
//            sendCancelEvent();
//        }
//
//        // leave initiated position or just refresh complete
//        if ((mTopIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT) ||
//                (mTopIndicator.goDownCrossFinishPosition() && mStatus == PTR_STATUS_COMPLETE && isEnabledNextPtrAtOnce())) {
//
//            mStatus = PTR_STATUS_PREPARE;
//            //mPtrUIHandlerHolder.onUIRefreshPrepare(this);
//
//        }
//
//        // back to initiated position
//        if (mTopIndicator.hasJustBackToStartPosition()) {
//            //tryToNotifyReset();
//
//            // recover event to children
//            if (isUnderTouch) {
//                sendDownEvent();
//            }
//        }
//
//        // Pull to Refresh
//        if (mStatus == PTR_STATUS_PREPARE) {
//            // reach fresh height while moving from top to bottom
////            if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
////                    && mTopIndicator.crossRefreshLineFromTopToBottom()) {
////                tryToPerformRefresh();
////            }
////            // reach header height while auto refresh
////            if (performAutoRefreshButLater() && mTopIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
////                tryToPerformRefresh();
////            }
//        }
//
//
//        mHeaderView.offsetTopAndBottom(change);
//        if (!isPinContent()) {
//            mContent.offsetTopAndBottom(change);
//        }
//        invalidate();
//
////        if (mPtrUIHandlerHolder.hasHandler()) {
////            mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, mStatus, mTopIndicator);
////        }
////        onPositionChange(isUnderTouch, mStatus, mTopIndicator);
//    }
//
//    private void moveBottomPos(float deltaY) {
//        // has reached the top
//        if ((deltaY < 0 && mBottomIndicator.isInStartPosition())) {
//
//            return;
//        }
//
//        int to = mBottomIndicator.getCurrentPosY() + (int) deltaY;
//
//        // over top
//        if (mBottomIndicator.willOverTop(to)) {
//
//            to = PtrTopIndicator.POS_START;
//        }
//
//        mBottomIndicator.setCurrentPos(to);
//        int change = to - mBottomIndicator.getLastPosY();
//        updateBottomPos(change);
//    }
//
//    private void updateBottomPos(int change) {
//        if (change == 0) {
//            return;
//        }
//
//        boolean isUnderTouch = mIndicator.isUnderTouch();
//
//        // once moved, cancel event will be sent to child
//        if (isUnderTouch && !mHasSendCancelEvent && mBottomIndicator.hasMovedAfterPressedDown()) {
//            mHasSendCancelEvent = true;
//            sendCancelEvent();
//        }
//
//        // leave initiated position or just refresh complete
//        if ((mBottomIndicator.hasJustLeftStartPosition() && mStatus == PTR_STATUS_INIT) ||
//                (mBottomIndicator.goDownCrossFinishPosition() && mStatus == PTR_STATUS_COMPLETE
//                        && isEnabledNextPtrAtOnce())) {
//
//            mStatus = PTR_STATUS_PREPARE;
//            //mPtrUIHandlerHolder.onUIRefreshPrepare(this);
//            if (DEBUG) {
//                //PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", mFlag);
//            }
//        }
//
//        // back to initiated position
//        if (mBottomIndicator.hasJustBackToStartPosition()) {
////            tryToNotifyReset();
//
//            // recover event to children
//            if (isUnderTouch) {
//                sendDownEvent();
//            }
//        }
//
//        // Pull to Refresh
//        if (mStatus == PTR_STATUS_PREPARE) {
//            // reach fresh height while moving from top to bottom
////            if (isUnderTouch && !isAutoRefresh() && mPullToRefresh
////                    && mBottomIndicator.crossRefreshLineFromTopToBottom()) {
////                tryToPerformRefresh();
////            }
////            // reach header height while auto refresh
////            if (performAutoRefreshButLater() && mBottomIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
////                tryToPerformRefresh();
////            }
//        }
//
//        if (DEBUG) {
////            PtrCLog.v(LOG_TAG, "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s",
////                    change, mBottomIndicator.getCurrentPosY(), mBottomIndicator.getLastPosY(), mContent.getTop(), mTopHeaderHeight);
//        }
//
//        mFooterView.offsetTopAndBottom(-change);
//        if (!isPinContent()) {
//            mContent.offsetTopAndBottom(-change);
//        }
//        invalidate();
////        this.postInvalidate();
//
////        if (mPtrUIHandlerHolder.hasHandler()) {
//        //mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, mStatus, mTopIndicator);
////        }
//        //onPositionChange(isUnderTouch, mStatus, mTopIndicator);
//    }
//    private void sendCancelEvent() {
////        if (DEBUG) {
////            PtrCLog.d(LOG_TAG, "send cancel event");
////        }
//        // The ScrollChecker will update position and lead to send cancel event when mLastMoveEvent is null.
//        // fix #104, #80, #92
//        if (mLastMoveEvent == null) {
//            return;
//        }
//        MotionEvent last = mLastMoveEvent;
//        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
//        dispatchTouchEventSupper(e);
//    }
//
//    private void onRelease(boolean stayForLoading) {
//
//        tryToPerformRefresh();
//
//        if (mStatus == PTR_STATUS_LOADING) {
//            // keep header for fresh
//            if (mKeepHeaderWhenRefresh) {
//                // scroll header back
//                if (mTopIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
//                    mScrollChecker.tryToScrollTo(mTopIndicator.getOffsetToKeepHeaderWhileLoading(), mDurationToClose);
//                } else {
//                    // do nothing
//                }
//            } else {
//                tryScrollBackToTopWhileLoading();
//            }
//        } else {
//            if (mStatus == PTR_STATUS_COMPLETE) {
//                notifyUIRefreshComplete(false);
//            } else {
//                tryScrollBackToTopAbortRefresh();
//            }
//        }
//    }
//
//    private boolean tryToPerformRefresh() {
//        if (mStatus != PTR_STATUS_PREPARE) {
//            return false;
//        }
//
//        //
////        if ((mTopIndicator.isOverOffsetToKeepHeaderWhileLoading() && isAutoRefresh()) || mTopIndicator.isOverOffsetToRefresh()) {
////            mStatus = PTR_STATUS_LOADING;
////            performRefresh();
////        }
//        return false;
//    }
//
//    private void performRefresh() {
////        mLoadingStartTime = System.currentTimeMillis();
////        if (mPtrUIHandlerHolder.hasHandler()) {
////            mPtrUIHandlerHolder.onUIRefreshBegin(this);
////            if (DEBUG) {
////                PtrCLog.i(LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
////            }
////        }
////        if (mPtrHandler != null) {
////            mPtrHandler.onRefreshBegin(this);
////        }
//    }
//
//    private void tryScrollBackToTop() {
//        if (!mIndicator.isUnderTouch()) {
//            mScrollChecker.tryToScrollTo(PtrTopIndicator.POS_START, mDurationToCloseHeader);
//        }
//    }
//
//    /**
//     * just make easier to understand
//     */
//    private void tryScrollBackToTopWhileLoading() {
//        tryScrollBackToTop();
//    }
//
//    /**
//     * just make easier to understand
//     */
//    private void tryScrollBackToTopAfterComplete() {
//        tryScrollBackToTop();
//    }
//
//    /**
//     * just make easier to understand
//     */
//    private void tryScrollBackToTopAbortRefresh() {
//        tryScrollBackToTop();
//    }
//
//    private void sendDownEvent() {
////        if (DEBUG) {
////            PtrCLog.d(LOG_TAG, "send down event");
////        }
//        final MotionEvent last = mLastMoveEvent;
//        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
//        dispatchTouchEventSupper(e);
//    }
//
//    public boolean isEnabledNextPtrAtOnce() {
//        return (mFlag & FLAG_ENABLE_NEXT_PTR_AT_ONCE) > 0;
//    }
//
//    class ScrollChecker implements Runnable {
//
//        private int mLastFlingY;
//        private Scroller mScroller;
//        private boolean mIsRunning = false;
//        private int mStart;
//        private int mTo;
//
//        public ScrollChecker() {
//            mScroller = new Scroller(getContext());
//        }
//
//        public void run() {
//            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
//            int curY = mScroller.getCurrY();
//            int deltaY = curY - mLastFlingY;
//            if (DEBUG) {
//                if (deltaY != 0) {
//                }
//            }
//            if (!finish) {
//                mLastFlingY = curY;
//                moveTopPos(deltaY);
//                post(this);
//            } else {
//                finish();
//            }
//        }
//
//        private void finish() {
//            if (DEBUG) {
//            }
//            reset();
//            onPtrScrollFinish();
//        }
//
//        private void reset() {
//            mIsRunning = false;
//            mLastFlingY = 0;
//            removeCallbacks(this);
//        }
//
//        private void destroy() {
//            reset();
//            if (!mScroller.isFinished()) {
//                mScroller.forceFinished(true);
//            }
//        }
//
//        public void abortIfWorking() {
//            if (mIsRunning) {
//                if (!mScroller.isFinished()) {
//                    mScroller.forceFinished(true);
//                }
//                onPtrScrollAbort();
//                reset();
//            }
//        }
//
//        public void tryToScrollTo(int to, int duration) {
//            if (mTopIndicator.isAlreadyHere(to)) {
//                return;
//            }
//            mStart = mTopIndicator.getCurrentPosY();
//            mTo = to;
//            int distance = to - mStart;
//
//            removeCallbacks(this);
//
//            mLastFlingY = 0;
//
//            // fix #47: Scroller should be reused, https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
//            if (!mScroller.isFinished()) {
//                mScroller.forceFinished(true);
//            }
//            mScroller.startScroll(0, 0, 0, distance, duration);
//            post(this);
//            mIsRunning = true;
//        }
//    }
//
//    @Override
//    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
//        return p != null && p instanceof lin.core.ptr.PullToRefreshView.LayoutParams;
//    }
//
//    @Override
//    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
//        return new lin.core.ptr.PullToRefreshView.LayoutParams(lin.core.ptr.PullToRefreshView.LayoutParams.MATCH_PARENT, lin.core.ptr.PullToRefreshView.LayoutParams.MATCH_PARENT);
//    }
//
//    @Override
//    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
//        return new lin.core.ptr.PullToRefreshView.LayoutParams(p);
//    }
//
//    @Override
//    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new lin.core.ptr.PullToRefreshView.LayoutParams(getContext(), attrs);
//    }
//    public static class LayoutParams extends MarginLayoutParams {
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        @SuppressWarnings({"unused"})
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(ViewGroup.LayoutParams source) {
//            super(source);
//        }
//    }
//    /**
//     * 对外暴露的下拉刷新的接口
//     */
//    public static interface OnRefreshListener {
//        void onRefresh();
//    }
//
//    /**
//     * 对外暴露的上拉加载的接口
//     */
//    public static interface OnLoadMoreListener {
//        void onLoadMore();
//    }
//}
