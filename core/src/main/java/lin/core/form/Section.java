package lin.core.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import lin.core.AttrType;
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
	private LinearLayout containerView;


	@ViewById(id="lin_core_form_section_header")
	private ViewGroup headerView;

	@ViewById(id="lin_core_form_section_footer")
	private ViewGroup footerView;


	@ViewById(id="lin_core_form_section_header_text")
	private TextView headerTextView;

	@ViewById(id="lin_core_form_section_footer_text")
	private TextView footerTextView;

//	private View header;
//	private View footer;
	@Override
	protected void onInited(){

//		minimumHeight = (int)(25 * this.getContext().getResources().getDisplayMetrics().density+0.5 );
//		this.setMinimumHeight(minimumHeight);
		this.setHeaderText(this.getAttrs().getString(R.styleable.form,R.styleable.form_form_section_header,""));
		this.setFooterText(this.getAttrs().getString(R.styleable.form,R.styleable.form_form_section_footer,""));


		Drawable divider = this.getAttrs().getDrawable(R.styleable.form,R.styleable.form_form_row_divider);
		int dividerPadding = this.getAttrs().getInt(R.styleable.form,R.styleable.form_form_row_divider,Integer.MIN_VALUE);
		int showDivider = this.getAttrs().getInt(R.styleable.form,R.styleable.form_form_row_show_divider,Integer.MIN_VALUE);

		if(divider != null) {
			containerView.setDividerDrawable(divider);
		}
		if(dividerPadding != Integer.MIN_VALUE) {
			containerView.setDividerPadding(dividerPadding);
		}
		if(showDivider != Integer.MIN_VALUE) {
			containerView.setShowDividers(showDivider);
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void addViewItem(View item,int index,ViewGroup.LayoutParams params) {
		if(item instanceof SectionHeader){
			headerView.addView(item);
		}else if(item instanceof SectionFooter){
			footerView.addView(item);
		}else {
			containerView.addView(item, containerView.getChildCount() - 1, params);
		}
	}


	public void setHeaderText(CharSequence text){
		headerTextView.setText(text);
	}
	public CharSequence getHeaderText(){
		return headerTextView.getText();
	}

	public void setFooterText(CharSequence text){
		footerTextView.setText(text);
	}
	public CharSequence getFooterText(){
		return footerTextView.getText();
	}
	@Override
	protected void genAttrs() {
		super.genAttrs();
		this.addAttr(R.styleable.form,R.styleable.form_form_section_header, AttrType.String);
		this.addAttr(R.styleable.form,R.styleable.form_form_section_footer,AttrType.String);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_divider,AttrType.Drawable);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_divider,AttrType.Drawable);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_divider_padding,AttrType.Int);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_show_divider,AttrType.Int);

	}
}
