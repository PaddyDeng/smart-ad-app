package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.PhoneAndEmailUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;


/**
 * 找回密码
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText number_et,code_et,password_et;
    private TextView send_code;
    private Button btn;
    private int tag;
    private String phone,code,pass;
    private boolean  isSendCode;
    private int recLen = 60;
    private Timer timer;

    public static void start(Context context){
        Intent intent =new Intent(context,ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        findView();
        LoginHelper.initMessage(ForgetPasswordActivity.this);
        initData();
        setClick();
    }

    private void initData() {
        Map<String, String> map = SharedPreferencesUtils.getPassword(ForgetPasswordActivity.this);
        if(!TextUtils.isEmpty(map.get("phone"))){
            number_et.setText(map.get("phone"));
        }
    }

    private void setClick() {
        back.setOnClickListener(this);
        send_code.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("找回密码");
        back = (ImageView) title.findViewById(R.id.back);
        number_et = (EditText) findViewById(R.id.number_et);
        code_et = (EditText) findViewById(R.id.code_et);
        password_et = (EditText) findViewById(R.id.password_et);
        send_code = (TextView) findViewById(R.id.send_code);
        btn = (Button) findViewById(R.id.btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send_code:
                if(!isSendCode) {
                    if (!TextUtils.isEmpty(number_et.getText())) {
                        String phone = number_et.getText().toString();
                        if (PhoneAndEmailUtils.isMobileNO(phone)) {
                            tag = 0;
                            sendCode(phone);
                        } else if (PhoneAndEmailUtils.isEmail(phone)) {
                            tag = 1;
                            sendCode(phone);
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "请填写正确的账号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                break;
            case R.id.btn:
                if(!TextUtils.isEmpty(number_et.getText())) {
                    phone = number_et.getText().toString();
                    if (PhoneAndEmailUtils.isMobileNO(phone)) {
                        tag=0;
                    } else if (PhoneAndEmailUtils.isEmail(phone)) {
                        tag=1;
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "请填写正确的账号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(TextUtils.isEmpty(code_et.getText())){
                    Toast.makeText(ForgetPasswordActivity.this,"请填写验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                code =code_et.getText().toString();
                if(TextUtils.isEmpty(password_et.getText())){
                    Toast.makeText(ForgetPasswordActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                pass=password_et.getText().toString();
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.FindPass, RequestMethod.POST);
                req.set("type",tag==0?"1":"2").set("registerStr",phone).set("pass",pass).set("code",code);
                String session = SharedPreferencesUtils.getSession(ForgetPasswordActivity.this,1);
                if(!TextUtils.isEmpty(session)){
                    req.setHeader("Cookie","PHPSESSID="+session);
                }
                request(0, req, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        if(response.isSucceed()){
                            try {
                                PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                if(praiseBack.getCode()==1){
                                    SharedPreferencesUtils.setPassword(ForgetPasswordActivity.this,phone,pass);
                                    LoginHelper.againLogin(ForgetPasswordActivity.this,2);
                                    LoginActivity.getInstance().finishNow();
                                    Toast.makeText(ForgetPasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ForgetPasswordActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){

                            }

                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        super.onFailed(what, response);
                    }
                });
                break;
        }
    }
    private void sendCode(String num){
        StringBuffer sb =new StringBuffer();
        sb.append(NetworkTitle.DomainLoginNormal);
        boolean isPhone;
        if(tag==0){
            sb.append(NetworkChildren.PHONECODE);
            isPhone=true;
        }else{
            sb.append(NetworkChildren.MAILCODE);
            isPhone=false;
        }
        showLoadDialog();
        isSendCode=true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                recLen--;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 1000, 1000);
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        if(!isPhone)req.set("email",num);
        else if(isPhone)req.set("phoneNum",num);
        req.set("type","2");
        String session = SharedPreferencesUtils.getSession(ForgetPasswordActivity.this,1);
        if(!TextUtils.isEmpty(session)){
            req.setHeader("Cookie","PHPSESSID="+session);
        }
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            Toast.makeText(ForgetPasswordActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ForgetPasswordActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }


    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    send_code.setText(recLen+"s");
                    if(recLen < 0){
                        timer.cancel();
                        isSendCode =false;
                        recLen=60;
                        send_code.setText("验证码");
                    }
            }
        }
    };

}
