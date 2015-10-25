package lin.client.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 8:40:47 PM
 *
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpPackageReturnType {

	public Class<?> value();
	Class<?>[] parameterizedType() default {};
}
