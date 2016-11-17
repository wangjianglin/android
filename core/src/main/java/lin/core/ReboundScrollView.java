package lin.core;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.internal.NullLoadingLayout;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 8:11:20 PM
 * ScrollView反弹效果的实现 
 *
 */

public class ReboundScrollView extends PullToRefreshScrollView{
  public ReboundScrollView(Context context, AttributeSet attrs)
  {
      super(context,attrs, Mode.BOTH,NullLoadingLayout.class);

      
//      this.setScrollbarFadingEnabled(false);
////      this.getContext()
//      this.setMode(Mode.BOTH);
//      this.setOverScrollMode(OVER_SCROLL_NEVER);
//      this.setPullToRefreshOverScrollEnabled(false);
//      this.getRefreshableView().setHorizontalFadingEdgeEnabled(false);
////      this.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
//      
////      ScrollView.setHorizontalFadingEdgeEnabled(false);
//      this.setHorizontalFadingEdgeEnabled(false);
//      this.setVerticalFadingEdgeEnabled(false);

//      this.setNullLayout();
  }
}


//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.TranslateAnimation;
//import android.widget.ScrollView;
//
//
//public class ReboundScrollView extends ScrollView
//{
//    private View inner;
//
////    private float y;
//
//    private Rect normal = new Rect();;
//
//    public ReboundScrollView(Context context, AttributeSet attrs)
//    {
//        super(context, attrs);
//        
////        this.setFillViewport(true);
//
//    }
//
//    @Override
//    protected void onFinishInflate()
//    {
//        if (getChildCount() > 0)
//        {
//            inner = getChildAt(0);
//
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//	@Override
//    public boolean onTouchEvent(MotionEvent ev)
//    {
//        if (inner == null || !isNeedMove())
//        {
//            return super.onTouchEvent(ev);
//        }
//        else
//        {
//            commOnTouchEvent(ev);
//        }
//
//        return super.onTouchEvent(ev);
//    }
//
//    private float mLastY = -1; // save event y
//    public void commOnTouchEvent(MotionEvent ev)
//    {
//    	if (mLastY == -1) {
//			mLastY = ev.getRawY();
//		}
//    	
//        int action = ev.getAction();
//        switch (action)
//        {
//            case MotionEvent.ACTION_DOWN:
////                y = ev.getY();
//            	mLastY = ev.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//
//                if (isNeedAnimation())
//                {
//                    animation();
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:
////                final float preY = y;
//            	final float preY = mLastY;
//                float nowY = ev.getY();
//                int deltaY = (int) ((preY - nowY) * 0.6);
//                // 滚动
//                scrollBy(0, deltaY);
//
////                y = nowY;
//                mLastY = nowY;
//                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
//                if (isNeedMove())
//                {
//                    if (normal.isEmpty())
//                    {
//                        // 保存正常的布局位置
//                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
//
//                    }
//                    // 移动布局
//                    inner.layout(inner.getLeft(), inner.getTop() - deltaY, inner.getRight(), inner.getBottom() - deltaY);
//                }
//                break;
//
//            default:
//            	mLastY = -1;
//                break;
//        }
//        System.out.println("mLastY:"+mLastY);
//    }
//
//    // 开启动画移动
//
//    public void animation()
//    {
//        // 开启移动动画
//        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
//        ta.setDuration(200);
//        inner.startAnimation(ta);
//        // 设置回到正常的布局位置
//        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
//
//        normal.setEmpty();
//
//    }
//
//    // 是否需要开启动画
//    public boolean isNeedAnimation()
//    {
//        return !normal.isEmpty();
//    }
//
//    // 是否需要移动布局
//    public boolean isNeedMove()
//    {
//
//        int offset = inner.getMeasuredHeight() - getHeight();
//        int scrollY = getScrollY();
//        if (scrollY == 0 || scrollY == offset)
//        {
//            return true;
//        }
//        return false;
//    }
//
//}
