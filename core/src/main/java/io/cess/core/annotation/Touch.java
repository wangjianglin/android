package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Jul 28, 2015 6:18:37 PM
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(TouchProcessor.class)
public @interface Touch {
	int[] value() default 0;
	String[] id() default "";
	int action() default Integer.MIN_VALUE;
}
