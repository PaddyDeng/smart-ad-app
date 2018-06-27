package school.lg.overseas.school.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/11.
 */

public class PhoneAndEmailUtils {
    /**
     *验证手机号是否正确
     */
    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Pattern p = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
