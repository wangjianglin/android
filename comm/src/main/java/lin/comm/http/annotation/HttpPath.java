package lin.comm.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 18/12/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface HttpPath {
    public String value() default "";
}
