package lin.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lin.core.AttrType;


/**
 * Created by lin on 31/12/2016.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attr{
    int value() default 0;
    int[] attrs() default 0;
    String id() default "";
    AttrType type();
    Class<?> R() default Object.class;
}