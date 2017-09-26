# easy-mvc
轻量级web框架


### web.xml中增加Listener和Servlet
``` java
<listener>
    <listener-class>easy.framework.mvc.listener.ContainerListener</listener-class>
</listener>
<servlet>
    <servlet-name>easyMvc</servlet-name>
    <servlet-class>easy.framework.mvc.dispatcher.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:easy-mvc.properties</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
</servlet>
<servlet-mapping>
    <servlet-name>easyMvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

### demo
``` java
@Controller("userController")
@Action(value = "/user")
@Aspect(value = { MethodExecuteTimeAspect.class, SayHelloAspect.class })
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Inject
	private UserService userService;
	@Autowired
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
		View view = new View("/user/del/100", params);
		return view;
	}
	@Action(value = { "/view/{userId}" }, method = RequestMethod.GET)
	public Result userInfo(@PathVariable Integer userId) {
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
		return new View("user/delete.jsp", param);
	}
	@Action(value = "/upload", method = RequestMethod.POST)
	public void uploadFile(@FileBody FileModel fileModel) {
		logger.debug("upload......");
	}
	@Action(value = "/uploads", method = RequestMethod.POST)
	public void uploadFiles(@FileBody List<FileModel> fileModel) {
		logger.debug("upload......");
	}
}
