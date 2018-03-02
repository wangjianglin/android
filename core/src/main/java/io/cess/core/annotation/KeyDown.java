package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:15:42 PM
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(KeyDownProcessor.class)
public @interface KeyDown {
	int[] value() default 0;
	String[] id() default "";
	int keyCode() default Integer.MIN_VALUE;
}
