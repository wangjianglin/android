package lin.core.annotation;

import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date Aug 20, 2015 6:10:51 PM
 *
 */
public @interface Width {
	/**
	 * 以 dp 为单位
	 * @return
	 */
	int value() default ViewGroup.LayoutParams.WRAP_CONTENT;
}
