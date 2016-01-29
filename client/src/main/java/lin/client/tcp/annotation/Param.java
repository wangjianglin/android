package lin.client.tcp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 1/25/16.
 */

@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value() default "";
}
