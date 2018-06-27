package school.lg.overseas.school.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/1/24.
 */

public class KeyboardUtils {
    //强制显示或者关闭系统键盘
    public static void hideOrShowKeyBoard(final EditText et,final String status){

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run()
            {
                InputMethodManager m = (InputMethodManager)
                        et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(status.equals("o"))
                {
                    m.showSoftInput(et,InputMethodManager.SHOW_FORCED);
                }
                else
                {
                    m.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }

                previousKeyboardHeight = height;

            }
        });
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }
}
