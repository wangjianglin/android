package io.cess.core.annotation;

import android.os.Build;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 31/12/2016.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attrs {
    Attr[] value();
}


//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//@ProcessorClass(CheckedProcessor.class)
//public @interface Checked {
//    int[] value() default 0;
//    String[] id() default "";
//}