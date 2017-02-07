package lin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 20/01/2017.
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(EditorActionProcessor.class)
public @interface EditorAction {
    int[] value() default 0;
    String[] id() default "";
    int action() default Integer.MIN_VALUE;
}