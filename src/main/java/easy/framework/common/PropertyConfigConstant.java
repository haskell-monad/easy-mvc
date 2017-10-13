package easy.framework.common;

/**
 * Created by limengyu on 2017/8/21.
 */
public class PropertyConfigConstant {
	public static final String CONFIG_FILE_NAME = "easy-mvc.properties";
	public static final String ROOT_PACKAGE = "easy/mvc";
	public static final String BASE_PACKAGE_CONFIG_DEFAULT = "/base";
	// jsp文件目录、静态文件目录配置项
	public static final String BASE_PACKAGE_CONFIG = "easy.mvc.base.package";
	public static final String JSP_DIR_CONFIG = "easy.mvc.web.jsp.dir";
	public static final String STATIC_DIR_CONFIG = "easy.mvc.web.static.dir";
	// jsp文件目录、静态文件目录默认值
	public static final String JSP_DIR_CONFIG_DEFAULT = "/WEB-INF/views/";
	public static final String STATIC_DIR_CONFIG_DEFAULT = "/static/";
	// 数据库配置项
	public static final String JDBC_TYPE = "easy.mvc.jdbc.type";
	public static final String JDBC_DRIVER = "easy.mvc.jdbc.driver";
	public static final String JDBC_URL = "easy.mvc.jdbc.url";
	public static final String JDBC_USERNAME = "easy.mvc.jdbc.username";
	public static final String JDBC_PASSWORD = "easy.mvc.jdbc.password";
	// 数据库配置项默认值
	public static final String JDBC_TYPE_DEFAULT = "MYSQL";
	public static final String JDBC_DRIVER_DEFAULT = "com.mysql.jdbc.Driver";
	public static final String JDBC_URL_DEFAULT = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
	public static final String JDBC_USERNAME_DEFAULT = "root";
	public static final String JDBC_PASSWORD_DEFAULT = "root";
	// 自定义mvc配置项
	public static final String HANDLER_MAPPING_KEY = "easy.framework.custom.handler-mapping";
	public static final String HANDLER_INVOKE_KEY = "easy.framework.custom.handler-invoke";
	public static final String HANDLER_VIEW_RESOLVER_KEY = "easy.framework.custom.handler-view-resolver";
	// 文件上传配置项
	public static final String FILE_UPLOAD_MAX_SIZE = "easy.framework.custom.fileUpload.maxSize";
	public static final String FILE_UPLOAD_SINGLE_FILE_SIZE = "easy.framework.custom.fileUpload.singleFileSize";
	public static final String FILE_UPLOAD_DIR_NAME = "easy.framework.custom.fileUpload.directory";
	// 文件上传配置默认值
	public static final long FILE_UPLOAD_MAX_SIZE_DEFAULT = 1024 * 1024 * 20;
	public static final long FILE_UPLOAD_SINGLE_FILE_SIZE_DEFAULT = 1024 * 1024 * 2;
	public static final String FILE_UPLOAD_DIR_NAME_DEFAULT = "/fileUploadTempDir";
	// 自定义数据源配置项
	public static final String DATASOURCE_KEY = "easy.framework.custom.dataSource";
}
