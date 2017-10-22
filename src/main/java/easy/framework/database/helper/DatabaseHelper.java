package easy.framework.database.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import easy.framework.InstanceFactory;
import easy.framework.database.dao.DataAccessor;
import easy.framework.database.ds.AbstractDataSourceFactory;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public class DatabaseHelper {
	private final static AbstractDataSourceFactory DATASOURCE_FACTORY = InstanceFactory.getDataSourceFactory();
	private final static ThreadLocal<Connection> THREAD_LOCAL = new ThreadLocal<>();
	private static final DataAccessor dataAccessor = InstanceFactory.getDataAccessor();

	public static DataSource getDataSource() {
		return DATASOURCE_FACTORY.createDataSource();
	}
	public static Connection getConnection() {
		Connection connection = THREAD_LOCAL.get();
		if (connection == null) {
			try {
				connection = getDataSource().getConnection();
				if (connection != null) {
					THREAD_LOCAL.set(connection);
				}
			} catch (SQLException e) {
				throw new RuntimeException("获取数据库连接异常", e);
			}
		}
		return connection;
	}
	public static void beginTransaction() {
		Connection connection = getConnection();
		try {
			if (connection != null) {
				connection.setAutoCommit(false);
			}
		} catch (SQLException e) {
			throw new RuntimeException("开启事务发生异常", e);
		} finally {
			THREAD_LOCAL.set(connection);
		}
	}
	public static void commitTransaction() {
		Connection connection = getConnection();
		try {
			if (connection != null) {
				connection.commit();
			}
		} catch (SQLException e) {
			throw new RuntimeException("提交事务发生异常", e);
		} finally {
			THREAD_LOCAL.remove();
		}
	}
	public static void rollbackTransaction() {
		Connection connection = getConnection();
		try {
			if (connection != null) {
				connection.rollback();
				connection.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("事务回滚发生异常", e);
		} finally {
			THREAD_LOCAL.remove();
		}
	}
	public static <T> T select(String sql,Class<T> clazz, Object... obj) {
		return dataAccessor.select(sql,clazz,obj);
	}
	public static <T> List<T> selectList(String sql, Class<T> clazz, Object... obj) {
		return dataAccessor.selectList(sql, clazz, obj);
	}
}
