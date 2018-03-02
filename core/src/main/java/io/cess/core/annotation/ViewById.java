package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date May 13, 2015 4:48:18 PM
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(ViewByIdProcessor.class)
public @interface ViewById{
	int value() default 0;
	String id() default "";
}
