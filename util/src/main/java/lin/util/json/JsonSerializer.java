package lin.util.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author 王江林
 * @date 2012-7-11 下午7:48:29
 *
 */
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerializer {

	public Class<? extends ValueSerializer> serializer();
	public String[] parameter() default "";
}
