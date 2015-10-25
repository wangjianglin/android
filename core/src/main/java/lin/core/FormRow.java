package lin.core;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date May 5, 2015 3:55:37 PM
 *
 */
@ResourceClass(R.class)
@ResourceId(id="lin_core_form_row")
public class FormRow extends ResourceView{

	public FormRow(Context context) {
		super(context);
	}

	public FormRow(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public FormRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@ViewById(id="form_row_accessory")
	private View accessoryView;
	@ViewById(id="form_row_text")
	private TextView textView;
	@ViewById(id="form_row_title")
	private TextView titleView;
	
	@Override
	protected void onViewCreate() {
		this.setMinimumHeight((int)(44 * this.getContext().getResources().getDisplayMetrics().density + 0.5));
//		textView = (TextView) this.findViewById(R.id.form_row_text);
//		titleView = (TextView) this.findViewById(R.id.form_row_title);
//		accessoryView = this.findViewById(R.id.form_row_accessory);
//		this.setAccessory(this.getAttrs().getString(id));
		this.setText(this.getAttrs().getString(R.styleable.lin_form_row_text));
		this.setTitle(this.getAttrs().getString(R.styleable.lin_form_row_title));
		
		accessoryView.setVisibility(this.getAttrs().getBoolean(R.styleable.lin_form_row_accessory,true)?View.VISIBLE:View.INVISIBLE);
	}
	
	private String title;
	private String text;
	public boolean accessory;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		titleView.setText(title);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.textView.setText(text);
	}

	public boolean isAccessory() {
		return accessory;
	}

	public void setAccessory(boolean accessory) {
		this.accessory = accessory;
	}
	
}
