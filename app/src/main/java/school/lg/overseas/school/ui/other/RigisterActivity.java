package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.mine.NameActivity;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.PhoneAndEmailUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 注册
 */

public class RigisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText number_et,code_et,password_et,password_again_et;
    private TextView send_code,to_login;
    private Button rigister_btn;
    private int tag;
    private ImageView check_iv;
    private TextView protocol_btn;
    private boolean isCheck, isSendCode;
    private int recLen = 60;
    private Timer timer;

    public static void start(Context context){
        Intent intent =new Intent(context,RigisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);
        findView();
        LoginHelper.initMessage(RigisterActivity.this);
        setClick();
    }


    private void setClick() {
        back.setOnClickListener(this);
        send_code.setOnClickListener(this);
        to_login.setOnClickListener(this);
        rigister_btn.setOnClickListener(this);
        check_iv.setOnClickListener(this);
        protocol_btn.setOnClickListener(this);
    }

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("注册");
        back = (ImageView) title.findViewById(R.id.back);
        number_et = (EditText) findViewById(R.id.number_et);
        send_code = (TextView) findViewById(R.id.send_code);
        code_et = (EditText) findViewById(R.id.code_et);
        password_et = (EditText) findViewById(R.id.password_et);
        password_again_et = (EditText) findViewById(R.id.password_again_et);
        rigister_btn = (Button) findViewById(R.id.rigister_btn);
        to_login = (TextView) findViewById(R.id.to_login);
        check_iv = (ImageView) findViewById(R.id.check_iv);
        protocol_btn = (TextView) findViewById(R.id.protocol_btn);
        isCheck=true;
        check_iv.setSelected(isCheck);
        to_login.setText(Html.fromHtml("<font color=\"#000\">*已经注册雷哥网在线账号？</font><font color =\"#98bc36\">登录</font>"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_iv:
                isCheck=!isCheck;
                check_iv.setSelected(isCheck);
                break;
            case R.id.protocol_btn:
                UserProtocolActivity.start(RigisterActivity.this);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.send_code:
                if(!isSendCode) {
                    if (number_et.getText() != null) {
                        String number = number_et.getText().toString();
                        if (PhoneAndEmailUtils.isMobileNO(number)) {//手机号码格式正确
                            tag = 0;
                            sendCode(number);
                        } else if (PhoneAndEmailUtils.isEmail(number)) {//邮箱格式正确
                            tag = 1;
                            sendCode(number);
                        } else {
                            Toast.makeText(RigisterActivity.this, "请输入正确的手机号码或邮箱", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RigisterActivity.this, "请填写手机号码或者邮箱", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.to_login:
                finish();
                break;
            case R.id.rigister_btn:
                int type=1;
                if(!isCheck){
                    Toast.makeText(RigisterActivity.this,"请阅读并同意《用户协议》",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String number,code,password;
                if(number_et.getText()!=null){
                    number = number_et.getText().toString();
                    if(PhoneAndEmailUtils.isMobileNO(number)){//手机号码格式正确
                        type=1;
                    }else if(PhoneAndEmailUtils.isEmail(number)) {//邮箱格式正确
                        type=2;
                    }else{
                        Toast.makeText(RigisterActivity.this,"请输入正确的手机号码或邮箱",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    Toast.makeText(RigisterActivity.this,"请填写手机号码或者邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(code_et.getText())){
                    code=code_et.getText().toString();
                }else{
                    Toast.makeText(RigisterActivity.this,"请填写验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_et.getText()!=null){
                    password=password_et.getText().toString();
                }else{
                    Toast.makeText(RigisterActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_again_et.getText()!=null){
                    password_again_et.getText().toString();
                }else{
                    Toast.makeText(RigisterActivity.this,"请重新填写密码",Toast.LENGTH_SHORT).show();
                    return;
                }


                showLoadDialog();
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.REGISTER, RequestMethod.POST);
                req.set("registerStr",number).set("pass",password).set("code",code).set("userName","").set("source","3").set("belong","2").set("type",type+"");
                String session = SharedPreferencesUtils.getSession(RigisterActivity.this,1);
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
                                    Toast.makeText(RigisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    LoginActivity.getInstance().finishNow();
                                    SharedPreferencesUtils.setPassword(RigisterActivity.this,number,password);
                                    LoginHelper.againLogin(RigisterActivity.this,1);
                                    Intent intent =new Intent(RigisterActivity.this, NameActivity.class);
                                    intent.putExtra("tag",1);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(RigisterActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
        isSendCode =true;
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
        req.set("type","1");
        String session = SharedPreferencesUtils.getSession(RigisterActivity.this,1);
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
                            Toast.makeText(RigisterActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RigisterActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
