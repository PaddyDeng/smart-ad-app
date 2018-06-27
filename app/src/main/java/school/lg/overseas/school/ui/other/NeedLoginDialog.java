package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.utils.LoginHelper;

/**
 * Created by Administrator on 2018/1/15.
 */

public class NeedLoginDialog extends Dialog{
    private Context context;
    private TextView n,y,content_t;
    private String content;
    public NeedLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context =context;
    }
    public void setContent(String s){
        this.content=s;
        if(null!=content_t)content_t.setText(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_need_login);
        n = (TextView) findViewById(R.id.n);
        y = (TextView) findViewById(R.id.y);
        content_t = (TextView) findViewById(R.id.content_t);
        if(!TextUtils.isEmpty(content))content_t.setText(content);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginHelper.toLogin(context);
                dismiss();
            }
        });
    }
}
