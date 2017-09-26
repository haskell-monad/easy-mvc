package easy.framework.aop.annotation;

import java.lang.annotation.*;

import easy.framework.aop.AbstractAspect;

/**
 * Created by limengyu on 2017/9/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
	Class<? extends AbstractAspect>[] value();
}
