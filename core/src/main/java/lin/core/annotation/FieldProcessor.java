package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:31 PM
 *
 */
public interface FieldProcessor {

//	void process(View view, Field field, Annotation annotation, Class<?> idClass);
//	void process(View view, Field field, Annotation annotation, Package pack);
	void process(Object target, View view, Field field, Annotation annotation, Package pack);
}
