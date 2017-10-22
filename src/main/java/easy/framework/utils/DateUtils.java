package easy.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author limengyu
 * @create 2017/10/19
 */
public class DateUtils {

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
