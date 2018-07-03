package school.lg.overseas.school.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import school.lg.overseas.school.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Administrator on 2018/6/27.
 */

public class PopHelper {
    private View mView ;
    private PopupWindow popupWindow ;
    private EditText edit_commend ;
    private TextView send_commend ;
    private PopListener popListener ;
    private String id ;
    private boolean isArtic ;  //是不是文章评论  true  是文章   false  不是文章
 //    private InputMethodManager imn  ;
//    private static PopHelper popHelper ;
    private static WeakReference<PopHelper> WeakReferenceInstance;  // 弱引用是防止内存泄漏
    public PopHelper(Context context){
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_commend ,null , false);
        initView(mView);
        popupWindow = new PopupWindow(mView ,MATCH_PARENT ,WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.update();
        popupWindow.setAnimationStyle(R.style.popup_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        imn = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imn.toggleSoftInputFromWindow( mView.getWindowToken(),0 , InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setId(String id , boolean isArtic){
        this.id = id ;
        this.isArtic = isArtic ;
    }

    public void setPopListener(PopListener popListener){
        this.popListener = popListener ;
    }

    private void initView(View view){
        edit_commend = (EditText) view.findViewById(R.id.edit_commend);
        send_commend = (TextView) view.findViewById(R.id.send_commend);
        edit_commend.setFocusable(true);
        edit_commend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    send_commend.setEnabled(true);
                }else{
                    send_commend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        send_commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popListener != null){
                    if (popListener.popListener(edit_commend.getText().toString().trim() , id , isArtic))  send_commend.setEnabled(false);
                }

            }
        });
    }

    public synchronized static PopHelper create(Context context ){
        if (WeakReferenceInstance == null || WeakReferenceInstance.get() == null){
            WeakReferenceInstance = new WeakReference<>(new PopHelper(context));
        }
        return WeakReferenceInstance.get() ;
    }


    public void show(View view){
        if (popupWindow != null) {
            popupWindow.showAtLocation(view ,  Gravity.BOTTOM ,0 ,0);
            if (edit_commend != null) edit_commend.setText("");
        }
    }

    public void hide(){
        if (popupWindow != null) popupWindow.dismiss();
    }

    public  interface PopListener{
        boolean popListener(String content , String id , boolean isArtic);
    }
}
