/**
 * Created by limengyu on 2017/9/19.
 */
public class TestMain {
	public static Object convertValue(Object paramValue, Class<?> paramType) {
		if (paramValue == null) {
			return paramValue;
		}
		if (paramType.isInstance(paramValue)) {
			System.out.println("======0======");
			return paramValue;
		}
		System.out.println("=============================:" + (paramValue instanceof String));
		System.out.println("=============================:" + (paramValue instanceof Integer));
		String param = null;
		if (paramValue instanceof String) {
			param = (String) paramValue;
		}
		if (paramType.isAssignableFrom(Byte.class)) {
			System.out.println("======1======");
			return Byte.valueOf(param);
		} else if (paramType.isAssignableFrom(Short.class)) {
			System.out.println("======2======");
			return Short.valueOf(param);
		} else if (paramType.isAssignableFrom(Integer.class)) {
			System.out.println("======3======");
			return Integer.valueOf(param);
		} else if (paramType.isAssignableFrom(Long.class)) {
			System.out.println("======4======");
			return Long.valueOf(param);
		} else if (paramType.isAssignableFrom(Float.class)) {
			System.out.println("======5======");
			return Float.valueOf(param);
		} else if (paramType.isAssignableFrom(Double.class)) {
			System.out.println("======6======");
			return Double.valueOf(param);
		} else {
			System.out.println("======7======");
			return paramType.cast(paramValue);
		}
	}
	public static void main(String[] args) {
		// String path = "/user/add/{userName}/{passWord}";
		// String sub = "/user/add/username/password";
		//
		// String regex = path.replaceAll("\\{[^\\}]+\\}", "([a-zA-Z_\\$]+[0-9]*)");
		// System.out.println("pattern: " + regex);
		// System.out.println("sub: " + sub);
		// Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher(sub);
		// System.out.println(matcher.matches());
		// int n = matcher.groupCount();
		// for (int i = 1; i <= n; i++) {
		// System.out.println(matcher.group(i));
		// }
		// System.out.println("===================================");
		// System.out.println(matcher.matches());
		// n = matcher.groupCount();
		// for (int i = 1; i <= n; i++) {
		// System.out.println(matcher.group(i));
		// }
		// Object str = "12";
		// Object result = new Result();
		// System.out.println(int.class.getName());
		// System.out.println(int.class.getCanonicalName());
		// System.out.println(int.class.getSimpleName());
		// System.out.println(int.class.getTypeName());
		// List<FileModel> list = new ArrayList<>();
		// System.out.println(List.class.isAssignableFrom(list.getClass()));
		// System.out.println(list.getClass().isAssignableFrom(List.class));
		// System.out.println(list.getClass().isAssignableFrom(FileModel.class));
	}

}
