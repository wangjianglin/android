package lin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 7/6/16.
 */
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ProcessorClass(ViewModelProcessor.class)
@OpportunityAnnotation(Opportunity.OnAttached)
public @interface ViewModel {
    String value() default "";
}
