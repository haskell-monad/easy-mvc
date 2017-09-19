package easy.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by limengyu on 2017/9/13.
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
	public String value() default "";
}
