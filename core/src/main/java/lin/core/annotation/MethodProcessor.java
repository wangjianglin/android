package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:57 PM
 *
 */
public interface MethodProcessor {
	void process(View view, Method method, Annotation annotation, Class<?> idClass);
}
