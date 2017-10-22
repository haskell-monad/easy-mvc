# easy-mvc
轻量级web框架

### 注解使用说明
#### @Controller
spring-mvc中保留下来的字段,该注解标识当前类是一个控制器，类似的注解的注解还有@Service
***
#### @Inject/@Autowired
两个注解都标识了带有该注解的字段会被IOC容器自动注入
***
#### @Impl(UserServiceImpl.class)
配合@Inject/@Autowired一起使用,如果有多个实现的话，该注解指定具体某个实现类
***
#### @Action(value = { "/add/{userName}/{passWord}" }, method = RequestMethod.POST)
该注解标识该方法是一个请求处理器
value属性指示了路由地址，method属性指示了请求方法
***
#### @PathVariable/@Param/@RequestBody/@FileBody
(使用方法基本上和Spring-mvc一致)
@PathVariable用来指定path类型参数
@Param用来指定query类型参数
@RequestBody可以接收对象
@FileBody可以接收文件上传属性(multipart/form-data类型)	
***
### 两种类型返回值
#### View返回jsp或者redirect(params参数)
``` java
View view = new View("/user/list"); //以"/"开头的话将会重定向
View view = new View("user/info", params); //将会返回jsp页面
```
#### Result返回json数据或者文件上传(multipart/form-data类型)
``` java
	Result result = new Result()
	result.setData(new User());
	result.setCode(0);
	result.setMsg("查询成功");
```
***
#### 使用@Aspect实现方法执行前后进行拦截操作。当前只支持拦截所有方法，不支持切入点。
***
``` java
@Controller("userController")
@Action(value = "/user")
@Aspect(value = { MethodExecuteTimeAspect.class, SayHelloAspect.class })
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private UserService userService;
	
	@Autowired
	//指定具体实现类
	@Impl(UserServiceImpl.class)
	private UserService userServices;

	@Action(value = { "/add/{userName}/{passWord}" }, method = RequestMethod.POST)
	public View addUser(@PathVariable String userName, 
						@Param(required = false, defaultValue = "10") Integer age, 
						@Param(required = false, defaultValue = "2017") int year, 
						@PathVariable("passWord") String passWord, 
						@RequestBody(required = false) List<User> users, 
						@RequestBody(required = false) User user) {
		Map<String, Object> params = new HashMap<>();
		params.put("company", "iKang");
		params.put("area", "Beijing");
		params.put("users",users);
		//重定向以/开头
		View view = new View("/user/del/100", params);
		return view;
	}
	@Action(value = { "/view/{userId}" }, method = RequestMethod.GET)
	public Result userInfo(@PathVariable Integer userId) {
		//返回json
		Result result = new Result();
		result.setCode(100);
		result.setMsg("success");
		result.setData(new User());
		return result;
	}
	@Action(value = { "/del/{userId}" }, method = RequestMethod.GET)
	public View delUser(@PathVariable Integer userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", userId);
		//返回jsp页面
		return new View("user/delete.jsp", param);
	}
	
	//文件上传可以使用@FileBody注解和FileModel类
	//form表单设置enctype="multipart/form-data"
	@Action(value = "/upload", method = RequestMethod.POST)
	public void uploadFile(@FileBody FileModel fileModel) {
		logger.debug("upload......");
	}
	
	@Action(value = "/uploads", method = RequestMethod.POST)
	public void uploadFiles(@FileBody List<FileModel> fileModel) {
		logger.debug("upload......");
	}
}
```

## 默认数据访问器JdbcTemplate
可以通过在配置文件easy-mvc.properties配置：easy.framework.custom.dataAccessor选项覆盖默认访问器
``` java
        @Inject
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public User getUserByUserPhone(String userPhone) {
		String sql = "select * from user where USER_PHONE = ?";
		return jdbcTemplate.select(sql, User.class, userPhone);
	}
```

## ORM支持
1. 可使用注解：@Entity、@Table、@Field、@FieldIgnore
2. 数据库表名称：
	1. 默认标注了@Entity注解的实体类都会映射到相对应的数据库表
	2. 默认数据库表名会被映射成下划线分隔的小写实体类名称，如：UserRole实体类 -> user_role数据库表 可以使用@Table注解表名具体数据库表名称
3. 数据库表字段：
 	1. 默认所有private访问级别的字段会被映射到相对应的数据库表字段，如果某个字段不需要映射，可以通过@FieldIgnore注解标注忽略
	2. 默认实体类属性会被映射成下划线分隔的大写数据库表字段，如：userName -> USER_NAME,age -> AGE, firstUserName -> FIRST_USER_NAME
	3. 如果想指定表字段名称，可以使用@Field注解标注
	4. 实体类属性尽量使用Camel命名法(即驼峰命名法)，如userName
``` java
@Entity
@Table(name = "system_user")
public class User implements Serializable {
	@Field(name = "USER_NAME")
	private String userName;
	private String userPhone;
	private Integer userStatus;
	private String department;
	private Date createDate;
	private Date updateDate;
	@FieldIgnore
	private Integer ignoreField;
	
	...省略get...
	...省略set...
}
```
4. 使用
	1. 目前方法调用时需要传入字段字符串，如"userName",增加了手误操作的可能性，后续可能会参考Ebean实现更加友好的方式。
	2. Condition提供相关条件表达式操作，当前只提供了where/and/or/in/大于等于/orderby等关键字，后续会增加表连接等其他高级操作。
	3. field方法可以选择具体需要查询的字段，没有的话会查询表的所有字段。
``` java
	@Override
	public List<User> getAllUser() {
		List<User> userList = SqlHelper.builder(User.class)
				.and(Condition.notEquals("userName","Giggs"))
				.and(Condition.multiAnd(
						Condition.equals("gender","1"),
						Condition.before("createDate",new Date()),
						Condition.in("age","10","11","12","13"))
				)
				.asc("createDate")
				.desc("age")
				.groupBy("userPhone")
				.field("userName", "userPhone", "gender", "age", "createDate")
				.selectList();
		logger.debug("userList: {}", JsonUtils.toJson(userList));
		return userList;
	}
```
*demo代码会生成如下sql语句：*
``` sql
select USER_NAME,USER_PHONE,gender,age,CREATE_DATE
	from user 
	where USER_NAME != "Giggs" and (
		gender = "1" and CREATE_DATE <= "2017-10-22" and age in (10,11,12,13)
	) 
	group by USER_PHONE 
	order by CREATE_DATE asc,age desc;
```
## 事务支持
- spring支持7种事务传播行为
	- PROPAGATION_REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。
	- PROPAGATION_SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。
	- PROPAGATION_MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。
	- ROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。
	- ROPAGATION_NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
	- PROPAGATION_NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。
	- ROPAGATION_NESTED--如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。

***easy-mvc当前只支持第1种,通过使用@Transaction注解***
``` java
	@Override
	@Transaction
	public boolean addUser(User user){
		...
	}
``` 

## easy-mvc.properties 配置文件
``` java
#基础包路径（如（src/main/java/easy/mvc/xx1/xx1 || src/main/java/easy/mvc/xx2/xx2）则为easy.mvc）
easy.mvc.base.package=easy.mvc
#jsp文件目录
easy.mvc.web.jsp.dir=/WEB-INF/views
#静态文件映射目录
easy.mvc.web.static.dir=/static
#数据库配置(目前只支持mysql)
easy.mvc.jdbc.type=MYSQL
easy.mvc.jdbc.driver=com.mysql.cj.jdbc.Driver
easy.mvc.jdbc.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8
easy.mvc.jdbc.username=root
easy.mvc.jdbc.password=root
#自定义数据源（可选,默认commons-dbcp）
easy.framework.custom.dataSource=easy.framework.database.ds.pool.DruidDataSourceFactory
#自定义数据访问器(可选，默认commons-dbutils)
easy.framework.custom.dataAccessor=
#自定义映射/处理（可选）
easy.framework.custom.handler-mapping=
easy.framework.custom.handler-invoke=
easy.framework.custom.handler-view-resolver=
easy.framework.custom.handler-exception=
#文件上传相关配置（可选）
#默认总大小20MB
easy.framework.custom.fileUpload.maxSize=
#默认单个文件大小2MB
easy.framework.custom.fileUpload.singleFileSize=
#默认临时目录/fileUploadTempDir
easy.framework.custom.fileUpload.directory=

``` java

***

### To-do List
- [ ] orm-sql优化
- [ ] aop优化增加切入点
- [ ] cli命令行工具开发
- [ ] 事务优化方便集成spring事务
- [ ] 异常处理
- [ ] 插件支持
- [ ] redis/memcache/shiro插件开发
- [ ] evolution数据库同步插件
- [ ] docker支持
...

