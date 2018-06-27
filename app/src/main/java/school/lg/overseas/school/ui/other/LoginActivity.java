package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.os.Bundle;
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

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText number_et,password_et;
    private Button login;
    private TextView rigister,forget_pass;

    private static LoginActivity sInstance;
    public static LoginActivity getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        return null;
    }
    public static void finishNow(){
        sInstance.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance=this;
        setContentView(R.layout.activity_login);
        findView();
        initData();
        setClick();
    }


    private void initData() {
        Map<String, String> map = SharedPreferencesUtils.getPassword(LoginActivity.this);
        Log.i("测试",map.get("phone")+":"+map.get("pass"));
        if(!TextUtils.isEmpty(map.get("phone"))){
            number_et.setText(map.get("phone"));
            password_et.setText(map.get("pass"));
        }
    }

    private void setClick() {
        back.setOnClickListener(this);
        login.setOnClickListener(this);
        rigister.setOnClickListener(this);
        forget_pass.setOnClickListener(this);
    }

    private void findView() {
        back = (ImageView) findViewById(R.id.back);
        number_et = (EditText) findViewById(R.id.number_et);
        password_et = (EditText) findViewById(R.id.password_et);
        login = (Button) findViewById(R.id.login);
        rigister = (TextView) findViewById(R.id.rigister);
        forget_pass = (TextView) findViewById(R.id.forget_pass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.login:
                String number,password;
                if(number_et.getText()!=null){
                    number = number_et.getText().toString();
                }else{
                    Toast.makeText(LoginActivity.this,"请填写手机号码或者邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_et.getText()!=null){
                    password=password_et.getText().toString();
                }else{
                    Toast.makeText(LoginActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferencesUtils.setPassword(LoginActivity.this,number,password);
                showLoadDialog();
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.LOGIN, RequestMethod.POST);
                req.set("userName",number).set("userPass",password);
                request(0, req, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        dismissLoadDialog();
                        if(response.isSucceed()){
                            try {
                                Login login = JSON.parseObject(response.get(), Login.class);
                                if(login.getCode()==1){//登录成功
                                    List<HttpCookie> cookies = response.getHeaders().getCookies();
//                                if(cookies.isEmpty()||TextUtils.isEmpty(cookies.get(0).getValue())) {
//                                    Toast.makeText(LoginActivity.this, "重置session失败，请重新登录", Toast.LENGTH_SHORT).show();
//                                }else {
                                    SharedPreferencesUtils.setSession(LoginActivity.this,1,cookies.get(0).getValue());
                                    SharedPreferencesUtils.setLogin(LoginActivity.this, login);
                                    LoginHelper.login(LoginActivity.this, login,0);
//                                }
                                }else{
                                    Toast.makeText(LoginActivity.this,login.getMessage(),Toast.LENGTH_SHORT).show();
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
            case R.id.rigister:
                RigisterActivity.start(LoginActivity.this);
                break;
            case R.id.forget_pass:
                ForgetPasswordActivity.start(LoginActivity.this);
                break;
        }
    }

}
