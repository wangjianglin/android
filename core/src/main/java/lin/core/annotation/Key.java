package lin.core.annotation;

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
@ProcessorClass(KeyProcessor.class)
public @interface Key {
	int[] value();
	String[] id() default "";
	
}
