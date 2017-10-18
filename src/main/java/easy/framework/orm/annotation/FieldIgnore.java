package easy.framework.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认所有的private变量都应该有相对应的表字段
 * 如果不想作为表字段，需要增加这个注解进行忽略
 * @author limengyu
 * @create 2017/10/17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldIgnore {
}
