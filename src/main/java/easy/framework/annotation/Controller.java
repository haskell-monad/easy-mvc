package easy.framework.annotation;

import java.lang.annotation.*;

/**
 * @author limengyu
 * @create 2017/9/13
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
	public String value() default "";
}
