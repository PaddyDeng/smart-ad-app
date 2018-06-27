package school.lg.overseas.school.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2018/3/15.
 */

public class UpdataUtil {

    /**
     * 获取系统版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        int  currentVersionCode = 0 ;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName() ,0);
            currentVersionCode = packageInfo.versionCode ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }


}
