package lin.client.http1.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Mar 8, 2015 10:24:20 PM
 *
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface HttpPackageMethod {

	lin.client.http1.HttpMethod value();
}
