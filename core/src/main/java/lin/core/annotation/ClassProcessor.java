package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;

/**
 * Created by lin on 7/6/16.
 */
public interface ClassProcessor<T extends Annotation> {
//    void process(View view,Annotation annotation, Class<?> idClass);
    void process(Object target, View view, T annot, Package pack);
}