package lin.client.tcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * @author lin
 * @date 2014年12月16日 上午12:18:03
 *
 * 便于类扫描
 * 
 */
@Target(value = { ElementType.TYPE })
public @interface Command {
	public int commaand();
}
