package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.ui.mine.LatelyActivity;

/**
 * Created by Administrator on 2018/1/16.
 */

public class LatelyDeleteAllDialog extends Dialog{
    private Context context;
    private TextView n,y,content_t;
    private String content;
    public LatelyDeleteAllDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context =context;
    }
    public void setContext(String s){
        this.content=s;
        if(null!=content_t)content_t.setText(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_need_login);
        y = (TextView) findViewById(R.id.y);
        n = (TextView) findViewById(R.id.n);
        content_t = (TextView) findViewById(R.id.content_t);
        if(!TextUtils.isEmpty(content))content_t.setText(content);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeManager.getInstance().deleteAllForTag(0);
                ((LatelyActivity)context).initView();
                dismiss();
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
