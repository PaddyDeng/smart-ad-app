package school.lg.overseas.school.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.ui.other.DialogWait;

/**
 * Created by Administrator on 2018/1/23.
 */

public class WaitUtils {
    private static Map<String,DialogWait> dialogs =new HashMap<>();
    public static void show(Context context,String tag){
        if(dialogs.containsKey(tag)){
            dialogs.get(tag).dismiss();
            dialogs.remove(tag);
        }
        DialogWait dialog =new DialogWait(context);
        dialogs.put(tag,dialog);
        dialog.show();
    }

    public static void setHint(String tag,String hint){
        DialogWait dialogWait = dialogs.get(tag);
        if(null!=dialogWait&&dialogWait.isShowing())dialogWait.setHint(hint);
    }
    public static void dismiss(String tag){
        DialogWait dialogWait = dialogs.get(tag);
        try {
            if(null!=dialogWait&&dialogWait.isShowing())dialogWait.dismiss();
            dialogs.remove(tag);
        }catch (Exception e){
            dialogs.remove(tag);
        }
    }
    public static boolean isRunning(String tag){
        if(dialogs.containsKey(tag))return true;
        else return false;
    }
}
