package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.BackCode;
import school.lg.overseas.school.http.NetworkChildren;

import static school.lg.overseas.school.http.NetworkTitle.DomainSmartApplyNormal;

/**
 * 意见反馈
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title_right;
    private EditText content_et, phone_et;

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        title_right.setOnClickListener(this);
    }

    private void findView() {
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("意见反馈");
        title_right = (TextView) findViewById(R.id.title_right);
        title_right.setText("提交");
        title_right.setVisibility(View.VISIBLE);
        back = (ImageView) findViewById(R.id.back);
        content_et = (EditText) findViewById(R.id.content_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_right:
                if (TextUtils.isEmpty(content_et.getText())) {
                    Toast.makeText(FeedbackActivity.this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = content_et.getText().toString();
                String phone = "";
                if (!TextUtils.isEmpty(phone_et.getText())) {
                    phone = phone_et.getText().toString();
                }
                final Request<String> request = NoHttp.createStringRequest(DomainSmartApplyNormal + NetworkChildren.FEEDBACK, RequestMethod.POST);
                request.add("phone", phone);
                request.add("content", content);
                request(0, request, new SimpleResponseListener<String>() {
                    @Override
                    public void onStart(int what) {
                        super.onStart(what);
                    }

                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        super.onSucceed(what, response);
                        BackCode backCode = JSON.parseObject(response.get() ,BackCode.class);
                        if (backCode.getCode() == 1) {
                            Toast.makeText(FeedbackActivity.this, "反馈已收到，谢谢你的反馈", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(FeedbackActivity.this ,"反馈失败：" + backCode.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        super.onFailed(what, response);
                        Toast.makeText(FeedbackActivity.this, "出了点问题，反馈失败：" + response.getException(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish(int what) {
                        super.onFinish(what);
                    }
                });
                break;
        }
    }

}
