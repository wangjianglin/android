package lin.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * @author lin
 * @date Mar 10, 2015 3:10:34 PM
 *
 */
public abstract class ContentView extends ViewGroup implements LinView {

public ContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.attrs = new Attrs(context, attrs);
//		this.init();
	}

	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.attrs = new Attrs(context, attrs);
//		this.init();
	}

	public ContentView(Context context) {
		super(context);
		this.attrs = new Attrs(context, null);
//		this.init();
	}
	
//	private Integer viewWidth;
//	private void init(){
//		Width width = this.getClass().getAnnotation(Width.class);
//		if(width != null){
//			viewWidth = 
//		}
//	}

//	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cCount = getChildCount();
		for(int n=0;n<cCount;n++){
			View child = getChildAt(n);
			child.layout(0, 0, r-l, b-t);
		}
	}

	private Attrs attrs;
	@Override
	public Attrs getAttrs() {
		return attrs;
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
