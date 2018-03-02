package io.cess.util.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午8:53:33
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSON {
    String name() default "";

    boolean serialize() default true;

    boolean deserialize() default true;

    String format() default "";
}