package easy.framework.database.ds;

import java.util.Properties;

import javax.sql.DataSource;

import easy.framework.helper.ConfigHelper;

/**
 * @author limengyu
 * @create 2017/10/12
 */
public abstract class AbstractDataSourceFactory {
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

	/**
	 * 获取数据源
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	protected abstract DataSource getDataSource(Properties properties) throws Exception;
	protected Properties loadCustomProperties(Properties properties){
		return properties;
	}
}
