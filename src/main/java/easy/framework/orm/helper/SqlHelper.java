package easy.framework.orm.helper;

import easy.framework.orm.Condition;

/**
 * @author limengyu
 * @create 2017/10/19
 */
public class SqlHelper {
	public static <T> Condition builder(Class<T> clazz) {
		return new Condition(clazz);
	}
}
