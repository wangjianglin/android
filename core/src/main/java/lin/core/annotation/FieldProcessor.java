package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:31 PM
 *
 */
public interface FieldProcessor {

	void process(View view, Field field, Annotation annotation, Class<?> idClass);
}
