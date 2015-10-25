package lin.util.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date 2013-7-19 上午1:23:27
 *
 */
@Target(value={ElementType.METHOD , ElementType.FIELD})
public @interface PropertyType {

	Class<?> type();// default null;
}
