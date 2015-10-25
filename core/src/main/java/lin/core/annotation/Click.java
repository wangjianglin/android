package lin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author lin
 * @date May 13, 2015 4:55:09 PM
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(ClickProcessor.class)
public @interface Click {
	int[] value() default 0;
	String[] id() default "";
}
