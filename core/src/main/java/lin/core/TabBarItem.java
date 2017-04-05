package lin.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lin.core.annotation.ResCls;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 9, 2015 5:03:21 PM
 *
 */

//<attr name="tabbar_icon" format="reference"/>
//		<attr name="tabbar_activate_icon" format="reference"/>
//		<attr name="tabbar_background" format="reference|color"/>
//		<attr name="tabbar_text_color" format="reference|color"/>
//		<attr name="tabbar_text_activate_color" format="reference|color"/>
//		<attr name="tabbar_activate_background" format="reference|color"/>
//		<attr name="tabbar_name" format="reference|string"/>
//		<!-- <attr name="tab_theme" format="reference|integer"/> -->
//		<attr name="tabbar_item_theme" format="reference"/>
//		<attr name="tabbar_overlay" format="reference|color"/>
@ResCls(R.class)
public class TabBarItem extends ResView {

	private Drawable activateBackground = null;
	private Drawable activateIcon = null;
	private Drawable background = null;
	private Drawable icon = null;
	private Drawable tintIcon = null;
	private Drawable tintActivateIcon = null;
	private Drawable overlayDrawable = null;

	private int textColor = 0xffffff;
	private int activateTextColor = 0;
	private CharSequence title;

	private TabBar tabBar;
	public TabBarItem(TabBar tabBar,Context context) {
		super(context);
		this.tabBar = tabBar;
	}

	
	private View contentView;
	void setBarItemResource(int id, View contentView, Attrs attrs){

		this.contentView = contentView;
		this.init(id);
		this.initDefValue(attrs);
	}


	private void initDefValue(Attrs attrs){
//		if(!(this.contentView.getLayoutParams() instanceof ContentView.LayoutParams)){
//			return;
//		}
//		Attrs attrs = ((LayoutParams) this.contentView.getLayoutParams()).getAttrs();

		if(attrs == null){
			return;
		}

		title = attrs.getString(R.styleable.tabbar,R.styleable.tabbar_tabbar_name);

		icon = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_icon);
		activateIcon = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_icon);
		overlayDrawable = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_overlay);

		if(overlayDrawable == null){
			overlayDrawable = tabBar.getOverlayDrawable();
		}

		background = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_background);
		if(background == null){
			//background = tabBar.getBackground();
		}
		if(background == null){
			//background = super.getBackground();
		}
		activateBackground = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_background);
		if(activateBackground == null){
			activateBackground = tabBar.getActivateBackground();
		}

		textColor = attrs.getColor(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_color,tabBar.getTextColor());

		activateTextColor = attrs.getColor(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_activate_color,tabBar.getActivateTextColor());

		setIconColor();
	}

	private void setIconColor(){
		if(activateIcon == null && icon != null) {
			tintIcon = icon.getConstantState().newDrawable().mutate();
			tintIcon = DrawableCompat.wrap(tintIcon);
			DrawableCompat.setTint(tintIcon, textColor);

			tintActivateIcon = icon.getConstantState().newDrawable().mutate();
			tintActivateIcon = DrawableCompat.wrap(tintActivateIcon);
			DrawableCompat.setTint(tintActivateIcon,activateTextColor);
		}else{
			tintIcon = icon;
			tintActivateIcon = activateIcon;
		}
	}

	@ViewById(id="lin_core_tabbar_tiem_bar_text_id")
	private TextView text;// = (TextView) barView.findViewById(R.id.tabbar_tiem_bar_text_id);
	
	@ViewById(id="lin_core_tabbar_tiem_bar_overlay_view_id")
	private View overlayView;
	
	@ViewById(id="lin_core_tabbar_tiem_bar_img_id")
	private ImageView image;// = (ImageView) barView.findViewById(R.id.tabbar_tiem_bar_img_id);
	


	private boolean isInited = false;
	@SuppressLint("NewApi")
	@Override
	protected void onInited() {

		if(text != null) {
			text.setText(title);
		}
		if(image != null){
			if(icon != null){
				image.setImageDrawable(icon);
			}else{
				icon = image.getDrawable();
			}
		}
		if(background != null){
			//this.setBackground(background);
		}
		isInited = true;
		this.getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				setActivate(activate);
			}
		},10);
//		this.setActivate(this.activate);
//		this.requestLayout();
	}
//	public Drawable getIcon() {
//		return icon;
//	}
//
//	public String getName() {
//		return name;
//	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		this.setMeasuredDimension(300, 400);
//	}

//	@Override
//	public void setBackgroundColor(int color) {
//		super.setBackgroundColor(color);
//	}

	private boolean activate = false;

	public boolean isActivate() {
		return activate;
	}

//	private ColorDrawable nullDrabable = new ColorDrawable();
	@SuppressLint("NewApi")
	public void setActivate(boolean activate) {
		this.activate = activate;
		if(!isInited){
			return;
		}
		if(activate){
			if(this.activateBackground != null){
				super.setBackgroundDrawable(activateBackground);
			}
			if(this.tintActivateIcon != null && image != null){
				image.setImageDrawable(tintActivateIcon);
			}
			if(this.overlayView != null){
				overlayView.setBackgroundDrawable(null);
			}
			text.setTextColor(activateTextColor);
		}else{
			//if(this.background != null){
				super.setBackgroundDrawable(background);
			//}
			if(image != null){
				image.setImageDrawable(tintIcon);
			}
			if(this.overlayView != null){
				overlayView.setBackgroundDrawable(overlayDrawable);
			}
			text.setTextColor(textColor);
		}
	}

	public TextView getText() {
		return text;
	}

	public void setText(TextView text) {
		this.text = text;
	}

	public Drawable getActivateBackground() {
		return activateBackground;
	}

	public void setActivateBackground(Drawable activateBackground) {
		this.activateBackground = activateBackground;
	}

	public Drawable getActivateIcon() {
		return activateIcon;
	}

	public void setActivateIcon(Drawable activateIcon) {
		this.activateIcon = activateIcon;
		setIconColor();
	}

	public Drawable getBackground() {
		return background;
	}

	public void setBackground(Drawable background) {
		this.background = background;
		super.setBackground(background);
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
		setIconColor();
	}

	public Drawable getOverlayDrawable() {
		return overlayDrawable;
	}

	public void setOverlayDrawable(Drawable overlayDrawable) {
		this.overlayDrawable = overlayDrawable;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		setIconColor();
	}

	public int getActivateTextColor() {
		return activateTextColor;
	}

	public void setActivateTextColor(int activateTextColor) {
		this.activateTextColor = activateTextColor;
		setIconColor();
	}
}
