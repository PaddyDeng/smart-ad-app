package school.lg.overseas.school.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2017/10/13.
 */

public class LogUtils {
    public static void log(String s) {
        if (!TextUtils.isEmpty(s)) {
            if (s.length() > 3000) {
                for (int i = 0; i < s.length(); i += 3000) {
                    if (i + 3000 < s.length())
                        Log.i("rescounter" + i, s.substring(i, i + 3000));
                    else
                        Log.i("rescounter" + i, s.substring(i, s.length()));
                }
            } else
                Log.i("resinfo", s);
        }else {
            Log.i("字符串为空","");
        }
    }
}
