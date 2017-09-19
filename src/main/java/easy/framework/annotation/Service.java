package easy.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by limengyu on 2017/9/13.
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
	public String value() default "";
}
