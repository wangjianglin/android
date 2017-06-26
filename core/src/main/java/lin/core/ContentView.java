package lin.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author lin
 * @date Mar 10, 2015 3:10:34 PM
 *
 */
public abstract class ContentView extends ViewGroup implements AttrsView {

	public ContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.contentViewInit(context,attrs,defStyle);
	}

	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.contentViewInit(context,attrs,0);
	}

	public ContentView(Context context) {
		super(context);
		this.contentViewInit(context,null,0);
	}

	private void contentViewInit(Context context, AttributeSet attrs, int defStyle){
		this.attrs = new Attrs(context, attrs);
		this.attrs.addAttr(this);
		this.genAttrs();
		this.attrs.process();
//		lin.core.mvvm.Utils.processViewModel(this);
	}

	protected void genAttrs(){

	}

	final protected void addAttr(int[] attrs,int id,AttrType type){
		this.attrs.addAttr(attrs,id,type);
	}

	protected void genLayoutAttrs(){

	}

	private static List<Object[]> layoutAttrs = new ArrayList<>();
	final protected void addLayoutAttr(int[] attrs,int id,AttrType type){
		layoutAttrs.add(new Object[]{attrs,id,type});
	}
	private Attrs attrs;
	@Override
	public Attrs getAttrs() {
		return attrs;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = this.getChildCount();
		if (count > 0) {
			View child = getChildAt(0);
			child.measure(widthMeasureSpec, heightMeasureSpec);
			this.setMeasuredDimension(child.getMeasuredWidth(),child.getMeasuredHeight());
//			for (int index = 0; index < getChildCount(); index++) {
//				final View child = getChildAt(index);
//				child.measure(widthMeasureSpec, heightMeasureSpec);
//				child.getMeasuredHeight()
//			}
		}else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	private int measure(int measureSpec){
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		//设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
		int result = 0;
		if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
			result = specSize;
		}
		return result;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cCount = getChildCount();
		for(int n=0;n<cCount;n++){
			View child = getChildAt(n);
			child.layout(0, 0, r-l, b-t);
		}

	}

	public void requestLayout(){
		super.requestLayout();
		int cCount = getChildCount();
		for(int n=0;n<cCount;n++){
			getChildAt(n).requestLayout();
		}
	}


	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(this,getContext(), attrs);
	}


	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {

		if (lp instanceof LayoutParams) {
			return new LayoutParams((LayoutParams) lp);
		}

		return new LayoutParams(lp);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	public static class LayoutParams extends ViewGroup.LayoutParams{

		private Attrs attrs;

		private static boolean isGenLayoutAttrs = false;
		private static Lock lock = new ReentrantLock();

		public LayoutParams(int width, int height) {
			super(width, height);
			attrs = new Attrs(null,null);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
			attrs = new Attrs(null,null);
		}

		public LayoutParams(ContentView view,Context c, AttributeSet attributeSet) {
			super(c, attributeSet);
			attrs = new Attrs(c,attributeSet);
			attrs.addLayoutAttr(view);
			attrs.addLayoutAttr(this);
			if(isGenLayoutAttrs == false) {
				lock.lock();
				if(isGenLayoutAttrs == false) {
					view.genLayoutAttrs();
				}
				isGenLayoutAttrs = true;
				lock.unlock();
			}
			for(Object[] attr : view.layoutAttrs){
				attrs.addAttr((int[])(attr[0]),(int)(attr[1]), (AttrType) attr[2]);
			}

			this.genLayoutAttrs();

			attrs.process();
		}

		protected void genLayoutAttrs(){

		}

		final protected void addLayoutAttr(int[] attrs,int id,AttrType type){
			this.attrs.addAttr(attrs,id,type);
		}

		public Attrs getAttrs() {
			return attrs;
		}

	}


	public Activity getActivity(){
//		ActivityManager am = (ActivityManager) this.getContext().getSystemService(Activity.ACTIVITY_SERVICE);
////		am.getRunningTasks(1).get(0).
//		ComponentName cn = am.getRunningTasks(1).get(0).topActivity.;
		if(this.getContext() instanceof Activity){
			return (Activity)this.getContext();
		}

//		am.getAppTasks().get(0).getTaskInfo().
//
//		ActivityManager manager =  null;
//
//		Window w = null;
////		ActivityThread a;
//
//		ViewParent parent = this.getParent();
//		while(parent != null){
//			if(parent instanceof Activity){
//				return (Activity)parent;
//			}else if(parent instanceof Fragment){
//				return ((Fragment) parent).getActivity();
//			}
//
//			parent = parent.getParent();
//		}
		return ViewActivity.getActivity(this);
	}

	@Override
	public void setBackground(Drawable background) {
		super.setBackgroundDrawable(background);
	}

	//	private int minHeight;
//	@Override
//	public void setMinimumHeight(int minHeight){
//		super.setMinimumHeight(minHeight);
//		this.minHeight = minHeight;
//	}
//	
//	@SuppressLint("Override")
//	public int getMinimumHeight(){
//		return minHeight;
//	}
}
