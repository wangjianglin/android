package lin.client.http1.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date Jun 26, 2015 11:46:37 AM
 *
 */
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpFileInfo {
	String name();
	String mimeType();
}
