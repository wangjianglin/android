package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 4:48:20 PM
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(CheckedProcessor.class)
public @interface Checked {
	int[] value() default 0;
	String[] id() default "";
}
