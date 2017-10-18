package easy.framework.mvc.annotation;

import easy.framework.mvc.common.RequestMethod;

import java.lang.annotation.*;

/**
 * @author limengyu
 * @create 2017/09/18
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
	String[] value() default {};
	RequestMethod[] method() default {};
}
