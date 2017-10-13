package easy.framework.database.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import easy.framework.helper.ConfigHelper;

/**
 * Created by limengyu on 2017/10/12.
 */
public abstract class DataSourceFactory {
	final public DataSource createDataSource() {
		Properties properties = loadCommonProperties();
		properties = loadCustomProperties(properties);
		DataSource dataSource;
		try {
			dataSource = getDataSource(properties);
		} catch (Exception e) {
			throw new RuntimeException("创建数据源异常", e);
		}
		return dataSource;
	}
	private Properties loadCommonProperties() {
		Properties properties = new Properties();
		properties.put("driverClassName", ConfigHelper.getJdbcDriver());
		properties.put("url", ConfigHelper.getJdbcUrl());
		properties.put("username", ConfigHelper.getJdbcUserName());
		properties.put("password", ConfigHelper.getJdbcPassWord());
		return properties;
	}
	protected abstract DataSource getDataSource(Properties properties) throws Exception;
	protected Properties loadCustomProperties(Properties properties){
		return properties;
	}
}
