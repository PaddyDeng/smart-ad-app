package school.lg.overseas.school.utils;


import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/12/26.
 */

public class Utils {
    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    public static void toggleInput(Context context){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     * 强制隐藏输入法键盘
     */
    public static void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *   获取EditText  String
     * @param et
     * @return
     */
    public static String getEditTextString(EditText et) {
        Editable text = et.getText();
        return text != null && text.toString().trim().length() != 0 ? text.toString().trim() : null;
    }


    public static void setVisible(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static void setGone(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public static void setInvisible(View... views) {
        if (views != null && views.length > 0) {
            View[] var4 = views;
            int var3 = views.length;

            for (int var2 = 0; var2 < var3; ++var2) {
                View view = var4[var2];
                if (view != null && view.getVisibility() != View.INVISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

}
