package lin.core.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import lin.core.R;
import lin.core.ResView;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date May 5, 2015 3:55:43 PM
 *
 */
@ResCls(R.class)
@ResId(id="lin_core_form_section")
public class Section extends ResView {

	public Section(Context context) {
		super(context);
	}

	public Section(Context context, AttributeSet attrs,
			int defStyle) {
		super( context, attrs, defStyle);
	}

	public Section(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@ViewById(id="lin_core_form_section_container")
	private ViewGroup containerView;
//	private View header;
//	private View footer;
	@Override
//	protected void onViewCreate() {
	protected void onInited(){

//		minimumHeight = (int)(25 * this.getContext().getResources().getDisplayMetrics().density+0.5 );
//		this.setMinimumHeight(minimumHeight);
	}

	private List<View> rows = new ArrayList<View>();
	@SuppressLint("NewApi")
	@Override
	protected void addViewItem(View item,int index,ViewGroup.LayoutParams params) {

//		if(rows.size() > 0){
//			View border = new View(this.getContext());
//			border.setLayoutParams(new LinearLayout.LayoutParams(
//		            ViewGroup.LayoutParams.MATCH_PARENT,
//		            (int)(2 * this.getContext().getResources().getDisplayMetrics().density+0.5 ),
//		        1.0F));
//			border.setRight(10);
//			border.setLeft(10);
////			border.setBackgroundColor(0xffdadada);
//			border.setBackground(this.getResources().getDrawable(R.drawable.lin_core_form_section_separate));
//
//			containerView.addView(border,containerView.getChildCount()-2);
//			minimumHeight += 2;
//		}
		containerView.addView(item,containerView.getChildCount()-1,params);
////		this.setMinimumHeight((int)(600 * scale+0.5));
////		minimumHeight += item.getSuggestedMinimumHeight();
		rows.add(item);
//
//		minimumHeight += getViewMininumHeight(item);
//		this.setMinimumHeight(minimumHeight);
	}
	
	
//	private int getViewMininumHeight(View view){
//		try {
//
//			Field f = View.class.getDeclaredField("mMinHeight");
//			f.setAccessible(true);
//			Object r = f.get(view);
//			return (Integer)r;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
}
