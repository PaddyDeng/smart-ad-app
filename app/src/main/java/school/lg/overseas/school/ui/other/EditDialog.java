package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.utils.KeyboardUtils;

/**
 * Created by Administrator on 2018/1/30.
 */

public class EditDialog extends Dialog {
    private Context context;
    private LinearLayout bottom_ll;
    private EditText et;
    private ReplyListener listener;
    private String hint;
    private View rl;

    public EditDialog(@NonNull Context context) {
        super(context, R.style.MyDialogStyle);
        this.context=context;
    }

    public void setHint(String s, ReplyListener listener){
        this.hint=s;
        this.listener=listener;
        if(null!=et)et.setHint(hint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        rl = findViewById(R.id.rl);
        bottom_ll = (LinearLayout) findViewById(R.id.bottom_ll);
        et = (EditText) findViewById(R.id.et);
        if(!TextUtils.isEmpty(hint))et.setHint(hint);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("测试",charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dismiss();
            }
        });
    }
    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        KeyboardUtils.hideOrShowKeyBoard(et,"o");
    }
}
