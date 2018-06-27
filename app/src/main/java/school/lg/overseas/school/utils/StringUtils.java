package school.lg.overseas.school.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 * String工具类
 */

public class StringUtils {
    public static  String getImageFileFromUrl(String url) {
        String fileName = "";
        if (!TextUtils.isEmpty(url)) {
            String[] strings = url.split("/");
            fileName = strings[strings.length -1];
        }
        return fileName ;
    }


    /**
     * 将字符串分割成数组
     * @param spilt  分割符号
     * @param content 内容
     * @return
     */
    public static List<String> ArrayFromString(String spilt , String content){
        List<String> strings = new ArrayList<>();
        String[] contents        = content.split(spilt);
        for (int i = 0 ;i < contents.length ; i ++){
            strings.add(contents[i]);
        }
        return strings ;
    }
}
