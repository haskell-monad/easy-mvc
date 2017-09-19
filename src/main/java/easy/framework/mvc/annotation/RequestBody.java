package easy.framework.mvc.annotation;

import java.lang.annotation.*;

/**
 * Created by limengyu on 2017/9/18.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {
    boolean required() default false;
}
