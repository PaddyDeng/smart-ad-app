package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.KeyboardUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 回复
 */

public class ReplyDialog extends Dialog{
    private Context context;
    private EditText et;
    private TextView send;
    private RelativeLayout rl;
    private ReplyListener listener;
    private ImageView head;

    public ReplyDialog(@NonNull Context context) {
        super(context,R.style.MyDialogStyle);
        this.context = context;
    }

    public void setListener(ReplyListener listener){
        this.listener =listener;
    }

    public void setHint(String s){
        et.setText("");
        et.setHint(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_reply);
        rl = (RelativeLayout) findViewById(R.id.rl);
        head = (ImageView) findViewById(R.id.my_portrait);
        et = (EditText) findViewById(R.id.et);
        send = (TextView) findViewById(R.id.send);

        PersonalDetail personalDetail=SharedPreferencesUtils.getPersonalDetail(context);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+(null==personalDetail?"":personalDetail.getImage()),head);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! SharedPreferencesUtils.isLogin(context)) {
                    LoginHelper.needLogin(context, "需要登陆才能回复哦");
                    return;
                }
                if(TextUtils.isEmpty(et.getText())){
                    Toast.makeText(context,"请填写回复信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                String s =et.getText().toString();
                listener.setListener(s);
                dismiss();
            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
