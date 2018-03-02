package io.cess.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author lin
 * @date May 18, 2015 3:12:35 AM
 *
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(ResIdProcessor.class)
@Inherited
public @interface ResId {
	int value() default 0;
	String id() default "";
}
