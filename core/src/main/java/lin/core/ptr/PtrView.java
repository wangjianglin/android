package lin.core.ptr;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import lin.core.AttrType;
import lin.core.Attrs;
import lin.core.ContentView;
import lin.core.ptr.indicator.ContentIndicator;
import lin.core.ptr.indicator.PtrIndicator;
import lin.core.R;

/**
 * Created by lin on 08/01/2017.
 * @BindingAdapter({"app:ptr_load_more"})
public static void setOnLoadMoreListener(PtrView view, PtrView.OnLoadMoreListener listener) {
view.setOnLoadMoreListener(listener);
}

 */

@BindingMethods({
        @BindingMethod(type = PtrView.class, attribute = "ptr_load_more", method = "setOnLoadMoreListener"),
        @BindingMethod(type = PtrView.class, attribute = "ptr_refresh", method = "setOnRefreshListener")
})
public class PtrView extends ContentView {

    public static enum Status{
        Init, Prepare, Loading, Complete
    }
    public static enum  Mode{
        Disable, Refresh, LoadMore, Both
    }
    private Mode mMode = Mode.Disable;
    private int mFlag = 0x00;
    private final static byte FLAG_PIN_CONTENT = 0x01 << 3;

//    private static final boolean DEBUG_LAYOUT = false;
//    public static final boolean DEBUG = false;

    private PtrUIHandlerHolder mTopPtrUIHandlerHolder = PtrUIHandlerHolder.create();
    private PtrUIHandlerHolder mBottomPtrUIHandlerHolder = PtrUIHandlerHolder.create();
    private int mPagingTouchSlop = 0;
    private boolean mAutoRefresh = false;
    private boolean mAutoLoadMode = false;

    private ContentIndicator mIndicator = new ContentIndicator();
    private PtrTopProcess mTopProcess = null;

    private PtrBottomProcess mBottomProcess = null;

    private int mLoadState = 0;//1、表示处于refresh状态，2、表示处于 load more 状态

    public PtrView(Context context) {
        super(context);
        this.init();
    }

    public PtrView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PtrView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init(){
        if(this.getBackground() == null){
            this.setBackgroundColor(0xffeeeeee);
        }

        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

        mTopProcess = new PtrTopProcess(this,mIndicator, new PtrProcess.OnListener() {
            @Override
            public void move(int delta) {
                if(mHeaderView != null) {
                    mHeaderView.offsetTopAndBottom(delta);
                }
                if (!isPinContent()) {
                    mContentView.offsetTopAndBottom(delta);
                }
                invalidate();
            }

            @Override
            public void sendEvent(MotionEvent e) {
                dispatchTouchEventSupper(e);
            }

            @Override
            public void onLoading() {
                mBottomProcess.disable();
                mLoadState = 1;
                if(mOnRefreshListener != null){
                    if(mFooterView != null) {
                        mFooterView.setVisibility(View.INVISIBLE);
                    }
                    mOnRefreshListener.onRefresh(PtrView.this);
                }else{
                    defaultOnRefreshListener.onRefresh(PtrView.this);
                }
            }
        });

        mBottomProcess = new PtrBottomProcess(this,mIndicator, new PtrProcess.OnListener() {
            @Override
            public void move(int delta) {
                if(mFooterView != null) {
                    mFooterView.offsetTopAndBottom(-delta);
                }
                if (!isPinContent()) {
                    mContentView.offsetTopAndBottom(-delta);
                }
                invalidate();
            }

            @Override
            public void sendEvent(MotionEvent e) {
                dispatchTouchEventSupper(e);
            }

            @Override
            public void onLoading() {
                mTopProcess.disable();
                mLoadState = 2;
                if(mOnLoadMoreListener != null){
                    mOnLoadMoreListener.onLoadMore(PtrView.this);
                    if(mHeaderView != null) {
                        mHeaderView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    defaultOnLoadMoreListener.onLoadMore(PtrView.this);
                }
            }
        });

        mTopProcess.mPtrUIHandler = mTopPtrUIHandlerHolder;
        mBottomProcess.mPtrUIHandler = mBottomPtrUIHandlerHolder;

        Attrs attrs = this.getAttrs();
        int ptrMode = attrs.getInt(R.styleable.ptr,R.styleable.ptr_ptr_mode,0);
        switch (ptrMode){
            case 1:
                this.setMode(Mode.Refresh);
                break;
            case 2:
                this.setMode(Mode.LoadMore);
                break;
            case 4:
                this.setMode(Mode.Both);
                break;
            default:
                this.setMode(Mode.Disable);
        }

        setPullToRefresh(attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_pull_to_refresh,isPullToRefresh()));
        setPullToLoadMore(attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_pull_to_load_more,isPullToLoadMore()));


        mDisableWhenHorizontalMove = attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_disable_horizontal_move, mDisableWhenHorizontalMove);

        this.setPinContent(attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_pin_content, this.isPinContent()));
        mAutoRefresh = attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_auto_refresh, mAutoRefresh);
        mAutoLoadMode = attrs.getBoolean(R.styleable.ptr,R.styleable.ptr_ptr_auto_load_more, mAutoLoadMode);
    }

    private boolean mDisableWhenHorizontalMove = true;//表示是否禁止水平 move， true 禁止,false允许

    // disable when detect moving horizontally
    private boolean mPreventForHorizontal = false;//表示当禁止水平move时，是否检测到水平 move


    private int mHeaderId = 0;
    private  int mFooterId = 0;
    private int mContentId = 0;

    private View mHeaderView = null;
    private View mFooterView = null;
    private View mContentView = null;


    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount > 3) {
            throw new IllegalStateException("PtrFrameLayout can only contains 2 children");
        } else if (childCount >= 2) {

            if (mHeaderId != 0 && mHeaderView == null) {
                mHeaderView = findViewById(mHeaderId);
            }
            if(mFooterId != 0 && mFooterView == null){
                mFooterView = findViewById(mFooterId);
            }
            if (mContentId != 0 && mContentView == null) {
                mContentView = findViewById(mContentId);
            }

            View child1 = getChildAt(0);
            View child2 = getChildAt(1);
            View child3 = getChildAt(2);

            int isHandler = child1 instanceof PtrUIHandler?1:0;
            isHandler += child2 instanceof PtrUIHandler?1:0;
            isHandler += child3 instanceof PtrUIHandler?1:0;
            if (mHeaderView == null) {
                if (isHandler <= 1
                    || (child1 instanceof PtrUIHandler && child1 != mContentView && child1 != mFooterView)) {
                    mHeaderView = child1;
                } else if (child2 instanceof PtrUIHandler && child2 != mContentView && child2 != mFooterView) {
                    mHeaderView = child2;
                } else {
                    mHeaderView = child3;
                }
            }

            if (mContentView == null) {
                if (!(child1 instanceof PtrUIHandler) && child1 != mHeaderView && child1 != mFooterView) {
                    mContentView = child1;
                } else if (!(child2 instanceof PtrUIHandler) && child2 != mHeaderView && child2 != mFooterView) {
                    mContentView = child2;
                } else if (!(child3 instanceof PtrUIHandler) && child3 != mHeaderView && child3 != mFooterView){
                    mContentView = child3;
                }
                if(mContentView == null){
                    for (int i = 0; i < getChildCount(); i++) {
                        View child = getChildAt(i);
                        if (child != mHeaderView && child != mFooterView) {
                            mContentView = child;
                            break;
                        }
                    }
                }
            }

            if (mFooterView == null) {
                if (child1 instanceof PtrUIHandler && child1 != mContentView && child1 != mHeaderView) {
                    mFooterView = child1;
                } else if (child2 instanceof PtrUIHandler && child2 != mContentView && child2 != mHeaderView) {
                    mFooterView = child2;
                } else {
                    mFooterView = child3;
                }
            }

        } else if (childCount == 1) {
            if(mContentView == null) {
                mContentView = getChildAt(0);
            }
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in PtrView is empty. Do you forget to specify its id in xml layout file?");
            mContentView = errorView;
            addView(mContentView);
        }
        if (mHeaderView != null) {
            mHeaderView.bringToFront();
        }
        if(mFooterView != null){
            mFooterView.bringToFront();
        }
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTopProcess.onDetached();
        mBottomProcess.onDetached();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int mTopHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mTopProcess.getPtrIndicator().setHeaderHeight(mTopHeaderHeight);
        }

        if(mFooterView != null){
            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            int mBottomHeaderHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            mBottomProcess.getPtrIndicator().setHeaderHeight(mBottomHeaderHeight);
        }

        if (mContentView != null) {
            measureContentView(mContentView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureContentView(View child,
                                    int parentWidthMeasureSpec,
                                    int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        layoutChildren();
    }

    private void layoutChildren() {
        int offset = mTopProcess.getPtrIndicator().getCurrentPosY();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null && mHeaderView.getVisibility() == View.VISIBLE) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            // enhance readability(header is layout above screen when first init)
            final int top = -(mTopProcess.getPtrIndicator().getHeaderHeight() - paddingTop - lp.topMargin - offset);
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);

        }
        if(mFooterView != null && mFooterView.getVisibility() == View.VISIBLE){
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int right = left + mFooterView.getMeasuredWidth();
            int bottom = this.getMeasuredHeight() - mBottomProcess.getPtrIndicator().getCurrentPosY();
            mFooterView.layout(left,bottom,right,bottom + mFooterView.getMeasuredHeight());
        }
        if(offset == 0){
            offset = -mBottomProcess.getPtrIndicator().getCurrentPosY();
        }
        if (mContentView != null) {
            if (isPinContent()) {
                offset = 0;
            }
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + offset;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            mContentView.layout(left, top, right, bottom);
        }
    }

    public boolean isPinContent() {
        return (mFlag & FLAG_PIN_CONTENT) > 0;
    }

    public void setPinContent(boolean pinContent) {
        if (pinContent) {
            mFlag = mFlag | FLAG_PIN_CONTENT;
        } else {
            mFlag = mFlag & ~FLAG_PIN_CONTENT;
        }
    }

    private boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        //(mHeaderView == null && mFooterView == null)
        if (!isEnabled() || mContentView == null) {
            return dispatchTouchEventSupper(e);
        }

        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIndicator.onRelease();
                if(mTopProcess.onRelease() || mBottomProcess.onRelease()){
                    return true;
                }

                return dispatchTouchEventSupper(e);

            case MotionEvent.ACTION_DOWN:

                mIndicator.onPressDown(e.getX(), e.getY());
                mTopProcess.onPressDown();
                mBottomProcess.onPressDown();

                mPreventForHorizontal = false;

                dispatchTouchEventSupper(e);
                return true;

            case MotionEvent.ACTION_MOVE:

                mIndicator.onMove(e);
                float offsetX = mIndicator.getOffsetX();
                float offsetY = mIndicator.getOffsetY();

                PtrIndicator mTopIndicator = mTopProcess.getPtrIndicator();
                PtrIndicator mBottomIndicator = mBottomProcess.getPtrIndicator();
                if (mDisableWhenHorizontalMove && !mPreventForHorizontal//表示是否需要检测水平move
                        && (Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))//检测是否有水平move
                        ) {
                    if (mTopIndicator.isInStartPosition()) {//如果是处理开始位置，则不处理
                        mPreventForHorizontal = true;
                    }
                }
                if (mPreventForHorizontal) {//如果禁止水平move，并且已经检测到了水平move
                    return dispatchTouchEventSupper(e);
                }

                boolean moveDown = offsetY > 0;//向下 move
                boolean moveUp = !moveDown;//向上 move

                // disable move when header not reach top
                if ((moveDown && mBottomIndicator.isInStartPosition()
                        && !PtrHandler.checkCanDoRefresh(mContentView))
                        || (moveUp && mTopIndicator.isInStartPosition()
                        && !PtrHandler.checkCanDoLoadMore(mContentView))
                        ) {
                    return dispatchTouchEventSupper(e);
                }

                boolean canMoveUp = mTopIndicator.hasLeftStartPosition();
                boolean canMoveDown = mBottomIndicator.hasLeftStartPosition();

                if(mTopIndicator.isInStartPosition() && mBottomProcess.getStatus() == Status.Init
                        && (mMode == Mode.Both || mMode == Mode.Refresh)){
                    mTopProcess.enable();
                    if(mHeaderView != null) {
                        mHeaderView.setVisibility(View.VISIBLE);
                    }
                }

                if(mBottomIndicator.isInStartPosition() && mTopProcess.getStatus() == Status.Init
                        && (mMode == Mode.Both || mMode == Mode.LoadMore)){
                    mBottomProcess.enable();
                    if(mFooterView != null) {
                        mFooterView.setVisibility(VISIBLE);
                    }
                }

                if (((moveUp && canMoveUp) || moveDown)
                        && !canMoveDown) {
                    mTopProcess.move(offsetY);
                    return true;
                }

                if((moveDown && canMoveDown) || moveUp){
                    mBottomProcess.move(-offsetY);
                    return true;
                }
        }
        return dispatchTouchEventSupper(e);
    }

    private void setHeaderAndFooterInfo(){

        if(mMode == Mode.Both
                || mMode == Mode.Refresh){
            if(mHeaderView != null) {
                mHeaderView.setVisibility(View.VISIBLE);
            }
            mTopProcess.enable();
        }else{
            if(mHeaderView != null) {
                mHeaderView.setVisibility(View.GONE);
            }
            mTopProcess.disable();
        }

        if(mMode == Mode.Both
                || mMode == Mode.LoadMore){
            if(mFooterView != null) {
                mFooterView.setVisibility(View.VISIBLE);
            }
            mBottomProcess.enable();
        }else{
            if(mFooterView != null) {
                mFooterView.setVisibility(View.GONE);
            }
            mBottomProcess.disable();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mAutoRefresh){
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoRefresh();
                }
            },100);
        }else if(mAutoLoadMode){
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoLoadMore();
                }
            },100);
        }
        mAutoRefresh = false;
        mAutoLoadMode = false;
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
        this.setHeaderAndFooterInfo();
    }

    public void autoRefresh() {
        mTopProcess.autoLoad();
    }

    public void autoLoadMore(){
        mBottomProcess.autoLoad();
    }

    public void addRefreshUIHandler(PtrUIHandler ptrUIHandler) {
        PtrUIHandlerHolder.addHandler(mTopPtrUIHandlerHolder, ptrUIHandler);
    }

    @SuppressWarnings({"unused"})
    public void removeRefreshUIHandler(PtrUIHandler ptrUIHandler) {
        mTopPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(mTopPtrUIHandlerHolder, ptrUIHandler);
    }

    public void addLoadMoreUIHandler(PtrUIHandler ptrUIHandler) {
        PtrUIHandlerHolder.addHandler(mBottomPtrUIHandlerHolder, ptrUIHandler);
    }

    @SuppressWarnings({"unused"})
    public void removeLoadMoreUIHandler(PtrUIHandler ptrUIHandler) {
        mBottomPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(mBottomPtrUIHandlerHolder, ptrUIHandler);
    }

    public void complete(){
        if(mLoadState == 1){
            mTopProcess.complete();
        }else if(mLoadState == 2){
            mBottomProcess.complete();
        }
        mLoadState = 0;
    }
//    public void refreshComplete() {
//        mTopProcess.complete();
//    }
//
//    public void loadMoreComplete(){
//        mBottomProcess.complete();
//    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View header) {
        if (mHeaderView != null && header != null && mHeaderView != header) {
            removeView(mHeaderView);
        }
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(lp);
        }
        mHeaderView = header;
        this.setHeaderAndFooterInfo();
        super.addView(header,-1,lp);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footer) {
        if (mFooterView != null && footer != null && mFooterView != footer) {
            removeView(mFooterView);
        }
        ViewGroup.LayoutParams lp = footer.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footer.setLayoutParams(lp);
        }
        mFooterView = footer;
        this.setHeaderAndFooterInfo();
        super.addView(footer,-1,lp);
    }

    public View getView() {
        return mContentView;
    }

    public void setContentView(View mContent) {
        if (mContentView != null) {
            removeView(mContentView);
        }
        ViewGroup.LayoutParams lp = mContent.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mContent.setLayoutParams(lp);
        }
        this.mContentView = mContent;
        super.addView(mContent,-1,lp);
    }

    public boolean isPullToRefresh(){
        return mTopProcess.isPullToLoad();
    }

    public void setPullToRefresh(boolean pullToRefresh){
        mTopProcess.setPullToLoad(pullToRefresh);
    }

    public boolean isPullToLoadMore(){
        return mBottomProcess.isPullToLoad();
    }

    public void setPullToLoadMore(boolean pullToLoadMore){
        mBottomProcess.setPullToLoad(pullToLoadMore);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    public void setOnRefreshListener(OnRefreshListener listener){
        this.mOnRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mOnLoadMoreListener = listener;
    }

    @Override
    protected void genAttrs() {
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_mode, AttrType.Int);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_pull_to_refresh, AttrType.Boolean);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_pull_to_load_more, AttrType.Boolean);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_disable_horizontal_move, AttrType.Boolean);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_pin_content, AttrType.Boolean);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_auto_refresh, AttrType.Boolean);
        this.addAttr(R.styleable.ptr,R.styleable.ptr_ptr_auto_load_more, AttrType.Boolean);
    }

    private static OnRefreshListener defaultOnRefreshListener = new OnRefreshListener(){
        @Override
        public void onRefresh(final PtrView ptr) {
            ptr.post(new Runnable() {
                @Override
                public void run() {
                    ptr.complete();
                }
            });
        }
    };

    private static OnLoadMoreListener defaultOnLoadMoreListener = new OnLoadMoreListener(){
        @Override
        public void onLoadMore(final PtrView ptr) {
            ptr.post(new Runnable() {
                @Override
                public void run() {
                    ptr.complete();
                }
            });
        }
    };
    /**
     * 对外暴露的下拉刷新的接口
     */
    public static interface OnRefreshListener {
        void onRefresh(PtrView ptr);
    }

    /**
     * 对外暴露的上拉加载的接口
     */
    public static interface OnLoadMoreListener {
        void onLoadMore(PtrView ptr);
    }
}
