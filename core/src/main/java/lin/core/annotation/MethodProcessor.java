package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:57 PM
 *
 */
public interface MethodProcessor {
//	void process(View view, Method method, Annotation annotation, Class<?> idClass);
//	void process(View view, Method method, Annotation annotation, Package pack);
	void process(Object target, View view, Method method, Annotation annotation, Package pack);
}
