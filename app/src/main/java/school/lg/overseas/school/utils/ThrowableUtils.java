package school.lg.overseas.school.utils;


import android.util.Log;

/**
 * Created by Administrator on 2017/11/7.
 */

public class ThrowableUtils {
    public static void getEX(Throwable throwable){
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String e ="";
        for(StackTraceElement s:stackTrace){
            e+="\tat"+s+"\r\n";
        }
        Log.i("错误信息",e);
    }
}
