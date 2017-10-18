package easy.framework.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认表字段名称为大写，下划线分隔
 * 如：实体变量userName会被映射到表字段USER_NAME
 * 如果不想使用默认表字段名称(USER_NAME)，需要增加此注解来标识
 * @author limengyu
 * @create 2017/10/17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {
	String name();
}
