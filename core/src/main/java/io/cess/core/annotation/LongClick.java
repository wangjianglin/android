package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Aug 11, 2015 11:13:54 PM
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(LongClickProcessor.class)
public @interface LongClick {
	int[] value() default 0;
	String[] id() default "";
}
