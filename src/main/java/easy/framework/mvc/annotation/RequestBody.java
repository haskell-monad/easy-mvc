package easy.framework.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author limengyu
 * @create 2017/09/18
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {
    boolean required() default false;
}
