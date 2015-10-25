package lin.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 9, 2015 5:03:21 PM
 *
 */
@ResourceClass(R.class)
public class TabBarItem extends ResourceView{

//	public TabBarItem(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		
//	}
//
//	public TabBarItem(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}

	private TabBar tabBar;
	public TabBarItem(TabBar tabBar,Context context) {
		super(context);
		this.tabBar = tabBar;
	}
	
	private View contentView;
	void setBarItemResourceId(int id,View contentView){
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		this.setLayoutParams(layoutParams);
		
		this.contentView = contentView;
		this.init(id);
	}

	@ViewById(id="tabbar_tiem_bar_text_id")
	private TextView text;// = (TextView) barView.findViewById(R.id.tabbar_tiem_bar_text_id);
	
	@ViewById(id="tabbar_tiem_bar_overlay_view_id")
	private View overlayView;
	
	@ViewById(id="tabbar_tiem_bar_img_id")
	private ImageView image;// = (ImageView) barView.findViewById(R.id.tabbar_tiem_bar_img_id);
	
	private Drawable activateBackground = null;
	private Drawable activateIcon = null;
	private Drawable background = null;
	private Drawable icon = null;
	private Drawable overlayDrawable = null;

	private boolean isInited = false;
	@SuppressLint("NewApi")
	@Override
	protected void onInited() {
		if(!(this.contentView instanceof LinView)){
			return;
		}
		LinView rv = (LinView) contentView;
		text.setText(rv.getAttrs().getString(R.styleable.lin_tab_name));
		
		Attrs attrs = rv.getAttrs();
		
		icon = attrs.getDrawable(R.styleable.lin_tab_icon);
		activateIcon = attrs.getDrawable(R.styleable.lin_tab_activate_icon);
		overlayDrawable = attrs.getDrawable(R.styleable.lin_tab_overlay);
		
		if(overlayDrawable == null){
			overlayDrawable = tabBar.getOverlayDrawable();
		}
		
		background = attrs.getDrawable(R.styleable.lin_tab_background);
		if(background == null){
			background = tabBar.getBackground();
		}
		if(background == null){
			background = super.getBackground();
		}
		activateBackground = attrs.getDrawable(R.styleable.lin_tab_activate_background);
		if(activateBackground == null){
			activateBackground = tabBar.getActivateBackground();
		}
		
		if(image != null){
			if(icon != null){
				image.setImageDrawable(icon);
			}else{
				icon = image.getDrawable();
			}
		}
		if(background != null){
			this.setBackground(background);
		}
		isInited = true;
		this.setActivate(this.activate);
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

	private boolean activate;

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
				super.setBackground(activateBackground);
			}
			if(this.activateIcon != null && image != null){
				image.setImageDrawable(activateIcon);
			}
			if(this.overlayView != null){
				overlayView.setBackgroundDrawable(null);
			}
		}else{
			//if(this.background != null){
				super.setBackground(background);
			//}
			if(image != null){
				image.setImageDrawable(icon);
			}
			if(this.overlayView != null){
				overlayView.setBackgroundDrawable(overlayDrawable);
			}
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
	}

	public Drawable getOverlayDrawable() {
		return overlayDrawable;
	}

	public void setOverlayDrawable(Drawable overlayDrawable) {
		this.overlayDrawable = overlayDrawable;
	}

}
