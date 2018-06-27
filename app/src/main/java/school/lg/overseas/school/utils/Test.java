package school.lg.overseas.school.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/1/15.
 */

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s=" Body: schoolName=%E4%B9%94%E6%B2%BB%E6%95%A6%E5%A4%A7%E5%AD%A6&majorName=%E4%BA%BA%E5%8A%9B%E8%B5%84%E6%BA%90%E7%AE%A1%E7%90%86&gpa=3&toefl=7.5&education=%E6%9C%AC%E7%A7%91&school=2&attendSchool=%E6%B5%8B%E8%AF%95&major=%E7%AE%A1%E7%90%86&gamt=&bigFour=&foreignCompany=&enterprises=&privateEnterprise=1&project=1&study=&publicBenefit=&awards=";
        String decode = URLDecoder.decode(s, "utf-8");
        System.out.print(decode);
//        String s ="1516097816";
//        String s1 = TimeUtils.longToString(Long.valueOf(s), "yyyy-MM-dd");
//        System.out.println(s1);
//        System.out.println(System.currentTimeMillis());
    }
}
