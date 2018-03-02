package io.cess.core.annotation;

import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date Aug 20, 2015 6:10:42 PM
 *
 */
public @interface Height {
	/**
	 * 以 dp 为单位
	 * @return
	 */
	int value() default ViewGroup.LayoutParams.WRAP_CONTENT;
}
