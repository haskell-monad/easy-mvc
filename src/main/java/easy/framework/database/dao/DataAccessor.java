package easy.framework.database.dao;

import java.util.List;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public interface DataAccessor {
	/**
	 * 新增数据
	 * @param sql
	 * @param params 参数
	 * @return
	 */
	int insert(String sql, Object... params);
	/**
	 * 批量插入数据
	 * @param sql
	 * @param params 参数
	 * @return
	 */
	int insertBatch(String sql, Object[][]... params);
	int update(String sql, Object... params);
	/**
	 * 删除数据
	 * @param sql
	 * @param params 参数
	 * @return
	 */
	int delete(String sql, Object... params);
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
}
