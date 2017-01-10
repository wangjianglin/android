//package lin.core;
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.internal.CustomLoadingLayout;
//
//import android.content.Context;
////import android.graphics.AvoidXfermode;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.ListView;
//
//import java.util.List;
//
//public class XListView extends com.handmark.pulltorefresh.library.PullToRefreshListView{
//
//
//	private boolean pullLoadEnable;
//	private boolean pullRefreshEnable;
//	private XListViewListener xListViewListener;
////	private Handler mHandler = new Handler();
//	public XListView(Context context) {
//		super(context,null,com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED, CustomLoadingLayout.class);
//		this.init();
//	}
//
//	public XListView(Context context, AttributeSet attrs) {
//		super(context,attrs, com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED, CustomLoadingLayout.class);
//		this.init();
//	}
//
//	private void init(){
////		this.setScrollingWhileRefreshingEnabled(false);
////		this.setListViewExtrasEnabled(true);
//
//		super.setState(State.MANUAL_REFRESHING,false);
//
//		ListView listView = this.getRefreshableView();
////		listView.setDividerHeight(0);
//		mode = Mode.DISABLED;
//		this.setOnRefreshListener(new OnRefreshListener2<ListView>(){
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				if(xListViewListener != null && refreshView.isRefreshing()){
//					xListViewListener.onRefresh();
//				}else{
////					refreshView.onRefreshComplete();
//				}
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				if(xListViewListener != null && refreshView.isRefreshing()){
//					xListViewListener.onLoadMore();
//				}else{
////					refreshView.onRefreshComplete();
//				}
//			}});
//
////		this.setOnRefreshListener(new OnRefreshListener<ListView>() {
////			@Override
////			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
////				if(xListViewListener != null && refreshView.isRefreshing()){
////					xListViewListener.onLoadMore();
////				}else{
////					refreshView.onRefreshComplete();
////				}
////			}
////		});
//	}
//	private Mode mode;
//	public void setPullMode(final Mode mode){
////		mHandler.post(new Runnable() {
////			@Override
////			public void run() {
//				if(mode == Mode.BOTH){
//					setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
//				}else if(mode == Mode.PULL_FROM_END){
//					setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_END);
//				}else if(mode == Mode.PULL_FROM_START){
//					setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
//				}else{
//					setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
//				}
////			}
////		});
//		this.mode = mode;
//	}
//	public Mode getPullMode(){
//		return this.mode;
//	}
//
//	private void setListModel(){
////		mHandler.post(new Runnable() {
////			@Override
////			public void run() {
//				if(pullLoadEnable && pullRefreshEnable){
//					setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
//				}else if(pullLoadEnable){
//					setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//				}else if(pullRefreshEnable){
//					setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//				}else{
//					setMode(PullToRefreshBase.Mode.DISABLED);
//				}
////			}
////		});
//
//	}
//
//	public void addHeaderView(View header){
//		this.getRefreshableView().addHeaderView(header);
//	}
////	@Deprecated
////	public boolean isPullLoadEnable() {
////		return pullLoadEnable;
////	}
////	@Deprecated
////	public void setPullLoadEnable(boolean pullLoadEnable) {
////		this.pullLoadEnable = pullLoadEnable;
////		setListModel();
////	}
////	@Deprecated
////	public boolean isPullRefreshEnable() {
////		return pullRefreshEnable;
////	}
////	@Deprecated
////	public void setPullRefreshEnable(boolean pullRefreshEnable) {
////		this.pullRefreshEnable = pullRefreshEnable;
////		setListModel();
////	}
//
//	public void setXListViewListener(XListViewListener xListViewListener) {
//		this.xListViewListener = xListViewListener;
//	}
//
//	public void complete(){
//		super.onRefreshComplete();
//	}
////	@Deprecated
////	public void stopLoadMore(){
////		this.onRefreshComplete();
////	}
////	@Deprecated
////	public void stopRefresh(){
////		this.onRefreshComplete();
////	}
//
//	public void setRefreshTime(String s){
//		this.getHeaderLayout().setLastUpdatedLabel("最后更新："+s);
//	}
//
//	public void setLastUpdatedLabel(String s){
//		this.getHeaderLayout().setLastUpdatedLabel(s);
//	}
//
//	public interface XListViewListener {
//		public void onRefresh();
//
//		public void onLoadMore();
//	}
//
//	public static enum Mode {
//
//		DISABLED,PULL_FROM_START,PULL_FROM_END,BOTH
//
//
//	}
//
//
//
//}
////import android.annotation.SuppressLint;
////import android.content.Context;
////import android.util.AttributeSet;
////import android.view.MotionEvent;
////import android.view.View;
////import android.view.ViewTreeObserver.OnGlobalLayoutListener;
////import android.view.animation.DecelerateInterpolator;
////import android.widget.AbsListView;
////import android.widget.AbsListView.OnScrollListener;
////import android.widget.ListAdapter;
////import android.widget.ListView;
////import android.widget.RelativeLayout;
////import android.widget.Scroller;
////import android.widget.TextView;
////
/////**
//// *
//// * @author lin
//// * @date Mar 11, 2015 5:35:24 PM
//// *
//// */
////public class XListView extends ListView implements OnScrollListener {
////
////	private float mLastY = -1; // save event y
////	private Scroller mScroller; // used for scroll back
////	private OnScrollListener mScrollListener; // user's scroll listener
////
////	// the interface to trigger refresh and load more.
////	private XListViewListener mListViewListener;
////
////	// -- header view
////	private XListViewHeader mHeaderView;
////	// header view content, use it to calculate the Header's height. And hide it
////	// when disable pull refresh.
////	private RelativeLayout mHeaderViewContent;
////	private TextView mHeaderTimeView;
////	private int mHeaderViewHeight; // header view's height
////	private boolean mEnablePullRefresh = true;
////	private boolean mPullRefreshing = false; // is refreashing.
////
////	// -- footer view
////	private XListViewFooter mFooterView;
////	private boolean mEnablePullLoad;
////	private boolean mPullLoading;
////	private boolean mIsFooterReady = false;
////
////	// total list items, used to detect is at the bottom of listview.
////	private int mTotalItemCount;
////
////	// for mScroller, scroll back from header or footer.
////	private int mScrollBack;
////	private final static int SCROLLBACK_HEADER = 0;
////	private final static int SCROLLBACK_FOOTER = 1;
////
////	private final static int SCROLL_DURATION = 400; // scroll back duration
////	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
////														// at bottom, trigger
////														// load more.
////	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
////													// feature.
////
////	/**
////	 * @param context
////	 */
////	public XListView(Context context) {
////		super(context);
////		initWithContext(context);
////	}
////
////	public XListView(Context context, AttributeSet attrs) {
////		super(context, attrs);
////		initWithContext(context);
////	}
////
////	public XListView(Context context, AttributeSet attrs, int defStyle) {
////		super(context, attrs, defStyle);
////		initWithContext(context);
////	}
////
////	private void initWithContext(Context context) {
////		mScroller = new Scroller(context, new DecelerateInterpolator());
////		// XListView need the scroll event, and it will dispatch the event to
////		// user's listener (as a proxy).
////		super.setOnScrollListener(this);
////
////		// init header view
////		mHeaderView = new XListViewHeader(context);
////		mHeaderViewContent = (RelativeLayout) mHeaderView
////				.findViewById(R.id.xlistview_header_content);
////		mHeaderTimeView = (TextView) mHeaderView
////				.findViewById(R.id.xlistview_header_time);
////		addHeaderView(mHeaderView);
////
////		// init footer view
////		mFooterView = new XListViewFooter(context);
////
////		// init header height
////		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
////				new OnGlobalLayoutListener() {
////					@Override
////					public void onGlobalLayout() {
////						mHeaderViewHeight = mHeaderViewContent.getHeight();
////						getViewTreeObserver()
////								.removeGlobalOnLayoutListener(this);
////					}
////				});
////	}
////
////	@Override
////	public void setAdapter(ListAdapter adapter) {
////		// make sure XListViewFooter is the last footer view, and only add once.
////		if (mIsFooterReady == false) {
////			mIsFooterReady = true;
////			addFooterView(mFooterView);
////		}
////		super.setAdapter(adapter);
////	}
////
////	public boolean isPullRefreshEnable(){
////		return mEnablePullRefresh;
////	}
////	/**
////	 * enable or disable pull down refresh feature.
////	 *
////	 * @param enable
////	 */
////	public void setPullRefreshEnable(boolean enable) {
////		mEnablePullRefresh = enable;
////		if (!mEnablePullRefresh) { // disable, hide the content
////			mHeaderViewContent.setVisibility(View.INVISIBLE);
////		} else {
////			mHeaderViewContent.setVisibility(View.VISIBLE);
////		}
////	}
////
////	public boolean isPullLoadEnable(){
////		return mEnablePullLoad;
////	}
////	/**
////	 * enable or disable pull up load more feature.
////	 *
////	 * @param enable
////	 */
////	public void setPullLoadEnable(boolean enable) {
////		mEnablePullLoad = enable;
////		if (!mEnablePullLoad) {
////			mFooterView.hide();
////			mFooterView.setOnClickListener(null);
////			//make sure "pull up" don't show a line in bottom when listview with one page
////			setFooterDividersEnabled(false);
////		} else {
////			mPullLoading = false;
////			mFooterView.show();
////			mFooterView.setState(XListViewFooter.STATE_NORMAL);
////			//make sure "pull up" don't show a line in bottom when listview with one page
////			setFooterDividersEnabled(true);
////			// both "pull up" and "click" will invoke load more.
////			mFooterView.setOnClickListener(new OnClickListener() {
////				@Override
////				public void onClick(View v) {
////					startLoadMore();
////				}
////			});
////		}
////	}
////
////	/**
////	 * stop refresh, reset header view.
////	 */
////	public void stopRefresh() {
////		if (mPullRefreshing == true) {
////			mPullRefreshing = false;
////			resetHeaderHeight();
////		}
////	}
////
////	/**
////	 * stop load more, reset footer view.
////	 */
////	public void stopLoadMore() {
////		if (mPullLoading == true) {
////			mPullLoading = false;
////			mFooterView.setState(XListViewFooter.STATE_NORMAL);
////		}
////	}
////
////	/**
////	 * set last refresh time
////	 *
////	 * @param time
////	 */
////	public void setRefreshTime(String time) {
////		mHeaderTimeView.setText(time);
////	}
////
////	private void invokeOnScrolling() {
////		if (mScrollListener instanceof OnXScrollListener) {
////			OnXScrollListener l = (OnXScrollListener) mScrollListener;
////			l.onXScrolling(this);
////		}
////	}
////
////	private void updateHeaderHeight(float delta) {
////		mHeaderView.setVisiableHeight((int) delta
////				+ mHeaderView.getVisiableHeight());
////		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
////			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
////				mHeaderView.setState(XListViewHeader.STATE_READY);
////			} else {
////				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
////			}
////		}
////		setSelection(0); // scroll to top each time
////	}
////
////	/**
////	 * reset header view's height.
////	 */
////	private void resetHeaderHeight() {
////		int height = mHeaderView.getVisiableHeight();
////		if (height == 0) // not visible.
////			return;
////		// refreshing and header isn't shown fully. do nothing.
////		if (mPullRefreshing && height <= mHeaderViewHeight) {
////			return;
////		}
////		int finalHeight = 0; // default: scroll back to dismiss header.
////		// is refreshing, just scroll back to show all the header.
////		if (mPullRefreshing && height > mHeaderViewHeight) {
////			finalHeight = mHeaderViewHeight;
////		}
////		mScrollBack = SCROLLBACK_HEADER;
////		mScroller.startScroll(0, height, 0, finalHeight - height,
////				SCROLL_DURATION);
////		// trigger computeScroll
////		invalidate();
////	}
////
////	private void updateFooterHeight(float delta) {
////		int height = mFooterView.getBottomMargin() + (int) delta;
////		if (mEnablePullLoad && !mPullLoading) {
////			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
////													// more.
////				mFooterView.setState(XListViewFooter.STATE_READY);
////			} else {
////				mFooterView.setState(XListViewFooter.STATE_NORMAL);
////			}
////		}
////		mFooterView.setBottomMargin(height);
////
//////		setSelection(mTotalItemCount - 1); // scroll to bottom
////	}
////
////	private void resetFooterHeight() {
////		int bottomMargin = mFooterView.getBottomMargin();
////		if (bottomMargin > 0) {
////			mScrollBack = SCROLLBACK_FOOTER;
////			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
////					SCROLL_DURATION);
////			invalidate();
////		}
////	}
////
////	private void startLoadMore() {
////		mPullLoading = true;
////		mFooterView.setState(XListViewFooter.STATE_LOADING);
////		if (mListViewListener != null) {
////			mListViewListener.onLoadMore();
////		}
////	}
////
////	@SuppressLint("ClickableViewAccessibility")
////	@Override
////	public boolean onTouchEvent(MotionEvent ev) {
////		if (mLastY == -1) {
////			mLastY = ev.getRawY();
////		}
////
////		switch (ev.getAction()) {
////		case MotionEvent.ACTION_DOWN:
////			mLastY = ev.getRawY();
////			break;
////		case MotionEvent.ACTION_MOVE:
////			final float deltaY = ev.getRawY() - mLastY;
////			mLastY = ev.getRawY();
////			if (getFirstVisiblePosition() == 0
////					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
////				// the first item is showing, header has shown or pull down.
////				updateHeaderHeight(deltaY / OFFSET_RADIO);
////				invokeOnScrolling();
////			} else if (getLastVisiblePosition() == mTotalItemCount - 1
////					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
////				// last item, already pulled up or want to pull up.
////				updateFooterHeight(-deltaY / OFFSET_RADIO);
////			}
////			break;
////		default:
////			mLastY = -1; // reset
////			if (getFirstVisiblePosition() == 0) {
////				// invoke refresh
////				if (mEnablePullRefresh
////						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
////					mPullRefreshing = true;
////					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
////					if (mListViewListener != null) {
////						mListViewListener.onRefresh();
////					}
////				}
////				resetHeaderHeight();
////			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
////				// invoke load more.
////				if (mEnablePullLoad
////				    && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
////				    && !mPullLoading) {
////					startLoadMore();
////				}
////				resetFooterHeight();
////			}
////			break;
////		}
////		return super.onTouchEvent(ev);
////	}
////
////	@Override
////	public void computeScroll() {
////		if (mScroller.computeScrollOffset()) {
////			if (mScrollBack == SCROLLBACK_HEADER) {
////				mHeaderView.setVisiableHeight(mScroller.getCurrY());
////			} else {
////				mFooterView.setBottomMargin(mScroller.getCurrY());
////			}
////			postInvalidate();
////			invokeOnScrolling();
////		}
////		super.computeScroll();
////	}
////
////	@Override
////	public void setOnScrollListener(OnScrollListener l) {
////		mScrollListener = l;
////	}
////
////	@Override
////	public void onScrollStateChanged(AbsListView view, int scrollState) {
////		if (mScrollListener != null) {
////			mScrollListener.onScrollStateChanged(view, scrollState);
////		}
////	}
////
////	@Override
////	public void onScroll(AbsListView view, int firstVisibleItem,
////			int visibleItemCount, int totalItemCount) {
////		// send to user's listener
////		mTotalItemCount = totalItemCount;
////		if (mScrollListener != null) {
////			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
////					totalItemCount);
////		}
////	}
////
////	public void setXListViewListener(XListViewListener l) {
////		mListViewListener = l;
////	}
////
////	/**
////	 * you can listen ListView.OnScrollListener or this one. it will invoke
////	 * onXScrolling when header/footer scroll back.
////	 */
////	public interface OnXScrollListener extends OnScrollListener {
////		public void onXScrolling(View view);
////	}
////
////	/**
////	 * implements this interface to get refresh/load more event.
////	 */
////	public interface XListViewListener {
////		public void onRefresh();
////
////		public void onLoadMore();
////	}
////
////	@Override
////	public boolean performItemClick(View view, int position, long id) {
////		return super.performItemClick(view, position-1, id);
////	}
////}
