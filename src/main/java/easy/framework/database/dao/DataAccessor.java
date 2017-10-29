package easy.framework.database.dao;

import java.sql.Connection;
import java.util.List;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public interface DataAccessor {
	/**
	 * select count(*) from users 返回数据总数
	 * @param sql
	 * @param params
	 * @return
	 */
	int selectCount(String sql, Object... params);
	/**
	 * 返回单个数据
	 * @param sql sql语句
	 * @param clazz 实体类
	 * @param params 参数列表
	 * @param <T>
	 * @return
	 */
	<T> T select(String sql, Class<T> clazz, Object... params);
	/**
	 * 返回所有数据列表
	 * @param sql sql语句
	 * @param clazz 实体类
	 * @param params 参数列表
	 * @param <T>
	 * @return
	 */
	<T> List<T> selectList(String sql, Class<T> clazz, Object... params);
	/**
	 * 新增数据，返回自增主键值
	 * @param generateKey 自增主键字段名称,默认值是PropertyConfigConstant.GENERATE_KEY_NAME
	 * @param sql
	 * @param params
	 * @return
	 */
	int insertGeneratedKeys(String generateKey, String sql, Object... params);
	/**
	 * 插入、删除、更新数据
	 * @param sql
	 * @param params 参数
	 * @return
	 */
	int update(String sql, Object... params);
}
