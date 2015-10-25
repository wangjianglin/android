package lin.core.annotation;

import java.lang.annotation.ElementType;
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
@ProcessorClass(ResourceIdProcessor.class)
public @interface ResourceId {
	int value() default 0;
	String id() default "";
}
