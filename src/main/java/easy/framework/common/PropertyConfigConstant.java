package easy.framework.common;

/**
 * Created by limengyu on 2017/8/21.
 */
public class PropertyConfigConstant {

    public static final String CONFIG_FILE_NAME = "easy-mvc.properties";

    public static final String ROOT_PACKAGE = "easy/mvc";

    public static final String BASE_PACKAGE_CONFIG = "easy.mvc.base.package";
    public static final String JSP_DIR_CONFIG = "easy.mvc.web.jsp.dir";
    public static final String STATIC_DIR_CONFIG = "easy.mvc.web.static.dir";
    public static final String JDBC_TYPE = "easy.mvc.jdbc.type";
    public static final String JDBC_URL = "easy.mvc.jdbc.url";
    public static final String JDBC_USERNAME = "easy.mvc.jdbc.username";
    public static final String JDBC_PASSWORD = "easy.mvc.jdbc.password";

    public static final String BASE_PACKAGE_CONFIG_DEFAULT = "/base";
    public static final String JSP_DIR_CONFIG_DEFAULT = "/WEB-INF/views/";
    public static final String STATIC_DIR_CONFIG_DEFAULT = "/static/";

    //自定义实现
    public static final String HANDLER_MAPPING_KEY = "easy.framework.custom.handler-mapping";
    public static final String HANDLER_INVOKE_KEY = "easy.framework.custom.handler-invoke";
    public static final String HANDLER_VIEW_RESOLVER_KEY = "easy.framework.custom.handler-view-resolver";


    //文件上传
    public static final String FILE_UPLOAD_MAX_SIZE = "easy.framework.custom.fileUpload.maxSize";
    public static final String FILE_UPLOAD_SINGLE_FILE_SIZE = "easy.framework.custom.fileUpload.singleFileSize";
    public static final String FILE_UPLOAD_DIR_NAME = "easy.framework.custom.fileUpload.directory";

    public static final long FILE_UPLOAD_MAX_SIZE_DEFAULT = 1024*1024*20;
    public static final long FILE_UPLOAD_SINGLE_FILE_SIZE_DEFAULT = 1024*1024*2;
    public static final String FILE_UPLOAD_DIR_NAME_DEFAULT = "/fileUploadTempDir";
}
