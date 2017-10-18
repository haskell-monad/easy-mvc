package easy.framework.database.ds.pool;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import easy.framework.database.ds.AbstractDataSourceFactory;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public class DbcpDataSourceFactory extends AbstractDataSourceFactory {
	@Override
	protected DataSource getDataSource(Properties properties) throws Exception {
		return BasicDataSourceFactory.createDataSource(properties);
	}
	@Override
	protected Properties loadCustomProperties(Properties properties) {
		// 初试连接数、最大活跃数
		properties.put("initialSize", 5);
		properties.put("minIdle", 5);
		properties.put("maxIdle", 10);
		properties.put("maxTotal", 20);
		// 最长等待时间(毫秒)
		properties.put("maxWaitMillis", 5000);
		// 程序中的连接不使用后是否被连接池回收
		properties.put("removeAbandoned", true);
		// 连接在所指定的秒数内未使用才会被删除(秒)
		properties.put("removeAbandonedTimeout", 120);
		return properties;
	}
}
