package io.cess.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;

/**
 * @author lin
 * @date 7/6/16.
 */
public interface ClassProcessor<T extends Annotation> {
//    void process(View view,Annotation annotation, Class<?> idClass);
    void process(Object target, View view, T annot, Package pack);
}