package lin.core.form;


import android.content.Context;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lin.core.AttrType;
import lin.core.Attrs;
import lin.core.R;
import lin.core.ResView;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date May 5, 2015 3:55:37 PM
 *
 */
@ResCls(R.class)
@ResId(id="lin_core_form_row")
@BindingMethods({
		@BindingMethod(type = Row.class, attribute = "form_row_text", method = "setText"),//定义绑定的方法
		@BindingMethod(type = Row.class, attribute = "form_row_title", method = "setTitle"),
@BindingMethod(type = Row.class, attribute = "form_row_accessory", method = "setAccessory")//定义绑定的方法
})
public class Row extends ResView {

	public Row(Context context) {
		super(context);
	}

	public Row(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public Row(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@ViewById(id="lin_core_form_row_accessory")
	private View accessoryView;
	@ViewById(id="lin_core_form_row_text")
	private TextView textView;
	@ViewById(id="lin_core_form_row_title")
	private TextView titleView;

	@ViewById(id="lin_core_form_row_layout")
	private ViewGroup layout;
	
	@Override
	protected void onCreate(){
		Attrs attrs = this.getAttrs();

		this.setText(attrs.getString(R.styleable.form,R.styleable.form_form_row_text));
		this.setTitle(attrs.getString(R.styleable.form,R.styleable.form_form_row_title));

		this.setAccessory(attrs.getBoolean(R.styleable.form,R.styleable.form_form_row_accessory,true));

		if(this.getBackground() == null){
			this.setBackgroundColor(0xffffffff);
		}
	}

	@Override
	protected void addViewItem(View item, int index, ViewGroup.LayoutParams params) {
		titleView.setVisibility(View.GONE);
		textView.setVisibility(View.GONE);
		layout.addView(item,index,params);
	}

	protected void addTextView(View item){
		titleView.setVisibility(View.VISIBLE);
		textView.setVisibility(View.GONE);
		layout.addView(item);
	}

	private CharSequence title;
	private CharSequence text;
	public boolean accessory;
	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(CharSequence title) {
		this.title = title;
		if(titleView != null) {
			titleView.setText(title);
		}
	}

	public CharSequence getText() {
		return text;
	}

	public void setText(CharSequence text) {
		this.text = text;
		if(textView != null) {
			this.textView.setText(text);
		}
	}

	public boolean isAccessory() {
		return accessory;
	}

	public void setAccessory(boolean accessory) {
		this.accessory = accessory;
		if(accessoryView != null) {
			accessoryView.setVisibility(accessory?View.VISIBLE:View.GONE);
		}
	}

	@Override
	protected void genAttrs() {
		this.addAttr(R.styleable.form,R.styleable.form_form_row_text, AttrType.String);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_title,AttrType.String);
		this.addAttr(R.styleable.form,R.styleable.form_form_row_accessory,AttrType.Boolean);
	}

	@Override
	public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new RelativeLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	}


	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {

		if (lp instanceof RelativeLayout.LayoutParams) {
			return new LayoutParams((RelativeLayout.LayoutParams) lp);
		}

		return new LayoutParams(lp);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof RelativeLayout.LayoutParams;
	}
}
