package lin.core.ptr;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by lin on 08/01/2017.
 */

class PtrHandler {
    private static boolean canChildScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(-1);
        }
    }

//    /**
//     * Default implement for check can perform pull to refresh
//     *
//     * @param frame
//     * @param content
//     * @param header
//     * @return
//     */
//    private static boolean checkContentCanBePulledDown(PullToRefreshView frame, View content, View header) {
//        return !canChildScrollUp(content);
//    }

    public static boolean checkCanDoRefresh(View content) {
        return !canChildScrollUp(content);
    }

    public static boolean checkCanDoLoadMore(View content){
        return isChildScrollToBottom(content);
    }

    /**
     * 是否滑动到底部
     *
     * @return
     */
    private static boolean isChildScrollToBottom(View mTarget) {

        if (mTarget instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mTarget;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if(recyclerView.getAdapter() == null){
                return true;
            }
            int count = recyclerView.getAdapter().getItemCount();
            if(count == 0){
                return true;
            }
            if (layoutManager instanceof LinearLayoutManager && count > 0) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastItems = new int[2];
                staggeredGridLayoutManager
                        .findLastCompletelyVisibleItemPositions(lastItems);
                int lastItem = Math.max(lastItems[0], lastItems[1]);
                if (lastItem == count - 1) {
                    return true;
                }
            }
            return false;
        } else if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            if(absListView.getAdapter() == null){
                return true;
            }
            int count = absListView.getAdapter().getCount();
            int fristPos = absListView.getFirstVisiblePosition();
            if(absListView.getChildCount() == 0){
                return true;
            }
            if (fristPos == 0
                    && absListView.getChildAt(0).getTop() >= absListView
                    .getPaddingTop()) {
                return false;
            }
            int lastPos = absListView.getLastVisiblePosition();
            if (lastPos > 0 && count > 0 && lastPos == count - 1) {
                View view = absListView.getChildAt(absListView.getChildCount()-1);
                if(view != null){
                    int diff = (view.getBottom() - (absListView.getHeight() + absListView.getScrollY()));
                    if (diff > 0) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } else if (mTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mTarget;
            View view = (View) scrollView
                    .getChildAt(scrollView.getChildCount() - 1);
            if(view == null){
                return true;
            }
            if (view != null) {
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));
                if (diff <= 0) {
                    return true;
                }
            }
            return false;
        }else if (mTarget instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) mTarget;
            View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
            if(view == null){
                return true;
            }
            if (view != null) {
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
