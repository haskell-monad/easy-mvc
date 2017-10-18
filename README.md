# easy-mvc
轻量级web框架

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
