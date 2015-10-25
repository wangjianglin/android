package lin.core;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 
 * @author lin
 * @date May 6, 2015 5:29:58 PM
 *
 */
public class NavigationButton {

	private String title;
	private Drawable icon;
	private View.OnClickListener onClickListener;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public View.OnClickListener getOnClickListener() {
		return onClickListener;
	}
	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}
