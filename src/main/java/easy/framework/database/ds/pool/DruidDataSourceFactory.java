package easy.framework.database.ds.pool;

import java.util.Properties;

import javax.sql.DataSource;

import easy.framework.database.ds.AbstractDataSourceFactory;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public class DruidDataSourceFactory extends AbstractDataSourceFactory {
	@Override
	protected DataSource getDataSource(Properties properties) throws Exception {
		DataSource dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(properties);
		return dataSource;
	}
	@Override
	protected Properties loadCustomProperties(Properties properties) {
		// 配置初始化大小、最小、最大
		properties.put("initialSize", "5");
		properties.put("minIdle", "5");
		properties.put("maxActive", "20");
		// 配置获取连接等待超时的时间
		properties.put("maxWait", "60000");
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		properties.put("timeBetweenEvictionRunsMillis", "60000");
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		properties.put("minEvictableIdleTimeMillis", "300000");
		properties.put("validationQuery", "SELECT 'x' FROM DUAL");
		properties.put("testWhileIdle", "true");
		properties.put("testOnBorrow", "false");
		properties.put("testOnReturn", "false");
		return properties;
	}
}
