package lin.util.json;

import java.lang.reflect.Method;
import java.util.Locale;


/**
 * 
 * @author 王江林
 * @date 2012-7-11 下午7:52:32
 *
 */
public interface ValueSerializer {

	Object Serialize(Object value, Object parameter, Locale local);

	Method method();
	
	Object Deserialize(Object value, Object parameter, Locale local);
    
}
