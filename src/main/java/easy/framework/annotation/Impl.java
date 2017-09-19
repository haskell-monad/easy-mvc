package easy.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by limengyu on 2017/9/13.
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Impl {
	Class<?> value();
}
