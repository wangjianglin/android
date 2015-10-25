package lin.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date May 5, 2015 3:55:43 PM
 *
 */
@ResourceClass(R.class)
@ResourceId(id="lin_core_form_section")
public class FormSection extends ResourceView{

	public FormSection(Context context) {
		super(context);
	}

	public FormSection(Context context, AttributeSet attrs,
			int defStyle) {
		super( context, attrs, defStyle);
	}

	public FormSection(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@ViewById(id="form_section_container")
	private ViewGroup containerView;
//	private View header;
//	private View footer;
	@Override
	protected void onViewCreate() {
//		containerView = (ViewGroup) this.findViewById(R.id.form_section_container);
//		header = this.findViewById(R.id.form_section_header);
//		footer = this.findViewById(R.id.form_section_footer);
		
//		header.setMinimumHeight(0);
//		header.setLayoutParams(new LinearLayout.LayoutParams(0,0,.0f));
//		footer.setMinimumHeight(0);
//		footer.setLayoutParams(new LinearLayout.LayoutParams(0,0,.0f));
//		header.setVisibility(View.INVISIBLE);
//		footer.setVisibility(View.INVISIBLE);
		minimumHeight = (int)(25 * this.getContext().getResources().getDisplayMetrics().density+0.5 );
		this.setMinimumHeight(minimumHeight);
	}
	
	private int minimumHeight;
	private List<View> rows = new ArrayList<View>();
	@SuppressLint("NewApi")
	@Override
	protected void addViewItem(View item) {
//		int h = item.getMeasuredHeight();
//		System.out.println("h:"+h);
//		item.setLayoutParams(new LinearLayout.LayoutParams(
//	            ViewGroup.LayoutParams.FILL_PARENT,
//	            ViewGroup.LayoutParams.WRAP_CONTENT,
//	        1.0F));
//		DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
//		float scale = displayMetrics.density;
//		item.setMinimumHeight((int)(90 * scale+0.5 ));
		if(rows.size() > 0){
			View border = new View(this.getContext());
			border.setLayoutParams(new LinearLayout.LayoutParams(
		            ViewGroup.LayoutParams.MATCH_PARENT,
		            (int)(2 * this.getContext().getResources().getDisplayMetrics().density+0.5 ),
		        1.0F));
			border.setRight(10);
			border.setLeft(10);
//			border.setBackgroundColor(0xffdadada);
			border.setBackground(this.getResources().getDrawable(R.drawable.lin_core_form_section_separate));
			
			containerView.addView(border,containerView.getChildCount()-2);
			minimumHeight += 2;
		}
		containerView.addView(item,containerView.getChildCount()-2);
//		this.setMinimumHeight((int)(600 * scale+0.5));
//		minimumHeight += item.getSuggestedMinimumHeight();
		rows.add(item);
		
		minimumHeight += getViewMininumHeight(item);
		this.setMinimumHeight(minimumHeight);
	}
	
	
	private int getViewMininumHeight(View view){
		try {
			
			Field f = View.class.getDeclaredField("mMinHeight");
			f.setAccessible(true);
			Object r = f.get(view);
			return (Integer)r;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}
}
