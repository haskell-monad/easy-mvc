package easy.framework.utils;

/**
 * @author limengyu
 * @create 2017/10/22
 */
public class StringExtUtils {

	/**
	 * 构建左侧符号值
	 * @param symbol
	 * @param value
	 * @return
	 */
	public static String builderLeftValue(String symbol, String value) {
		return prettyValue("\"", symbol + value);
	}
	/**
	 * 构建右侧侧符号值
	 * @param symbol
	 * @param value
	 * @return
	 */
	public static String builderRightValue(String symbol, String value) {
		return prettyValue("\"", value + symbol);
	}
	/**
	 * 构建环绕符号值
	 * @param symbol
	 * @param value
	 * @return
	 */
	public static String builderLeftRightValue(String symbol, String value) {
		return prettyValue("\"", symbol + value + symbol);
	}
	/**
	 * 在值的左右两侧添加符号
	 * @param symbol
	 * @param value
	 * @return
	 */
	public static String prettyValue(String symbol, String value) {
		return symbol + value + symbol;
	}
	/**
	 * @param leftSymbol 左侧符号
	 * @param rightSymbol 右侧符号
	 * @param intervalSymbol 数据之间分隔符
	 * @param values 数据
	 * @return
	 */
	public static String prettyJoinValue(String leftSymbol, String rightSymbol, String intervalSymbol, CharSequence... values) {
		return leftSymbol + String.join(intervalSymbol, values) + rightSymbol;
	}
    /**
     * @param leftSymbol 左侧符号
     * @param rightSymbol 右侧符号
     * @param intervalSymbol 数据之间分隔符
     * @param elements 集合数据
     * @return
     */
    public static String prettyJoinValue(String leftSymbol, String rightSymbol, String intervalSymbol, Iterable<? extends CharSequence> elements) {
        return leftSymbol + String.join(intervalSymbol, elements) + rightSymbol;
    }
	/**
	 * 在值的左右两侧添加tab符号
	 * @param value
	 * @return
	 */
	public static String prettyValueWithTab(String value) {
		return "\t" + value + "\t";
	}
	/**
	 * 在值的左右两侧添加双引号
	 * @param value
	 * @return
	 */
	public static String prettyValueWithDoubleQuot(String value) {
		return "\"" + value + "\"";
	}
}
