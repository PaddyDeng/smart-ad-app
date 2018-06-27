package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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

import java.util.Timer;
import java.util.TimerTask;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.PhoneAndEmailUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/15.
 */

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone_et,code_et;
    private TextView send_code,tv,title_tv;
    private ImageView back;
    private String oldPhone;
    private PersonalDetail personalDetail;
    private int tag;
    private boolean isSendCode;
    private int recLen = 60;
    private Timer timer;

    public static void start(Context context,int i){
        Intent intent =new Intent(context,ChangePhoneActivity.class);
        intent.putExtra("tag",i);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        findView();
        initView();
        setClick();
    }

    private void initView() {
        if (null!=getIntent()){
            tag = getIntent().getIntExtra("tag", -1);
        }
        if(tag==1){
            title_tv.setText("邮箱");
        }else{
            title_tv.setText("电话");
        }
        personalDetail = SharedPreferencesUtils.getPersonalDetail(ChangePhoneActivity.this);
        if(null!=personalDetail){
            if(tag==0) {
                oldPhone = TextUtils.isEmpty(personalDetail.getPhone()) ? "" : personalDetail.getPhone();
            }else{
                oldPhone =TextUtils.isEmpty(personalDetail.getEmail())?"":personalDetail.getEmail();
            }
            phone_et.setText(oldPhone);
        }
        LoginHelper.initMessage(ChangePhoneActivity.this);
    }

    private void setClick() {
        back.setOnClickListener(this);
        send_code.setOnClickListener(this);
        tv.setOnClickListener(this);
    }

    private void findView() {
        phone_et = (EditText) findViewById(R.id.phone_et);
        code_et = (EditText) findViewById(R.id.code_et);
        send_code = (TextView) findViewById(R.id.send_code);
        tv = (TextView) findViewById(R.id.tv);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send_code:
                if(!isSendCode) {
                    if (TextUtils.isEmpty(phone_et.getText())) {
                        Toast.makeText(ChangePhoneActivity.this, "账号未填写", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (phone_et.getText().toString().equals(oldPhone)) {
                        Toast.makeText(ChangePhoneActivity.this, "号码并未改变", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String phones = phone_et.getText().toString();
                    if (tag == 0 && PhoneAndEmailUtils.isMobileNO(phones)) {
                        sendCode(phones);
                    } else if (tag == 1 && PhoneAndEmailUtils.isEmail(phones)) {
                        sendCode(phones);
                    } else {
                        Toast.makeText(ChangePhoneActivity.this, "请填写正确的账号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            case R.id.tv:
                if(TextUtils.isEmpty(phone_et.getText())){
                    Toast.makeText(ChangePhoneActivity.this,"账号未填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone_et.getText().toString().equals(oldPhone)){
                    Toast.makeText(ChangePhoneActivity.this,"号码并未改变",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(code_et.getText())){
                    Toast.makeText(ChangePhoneActivity.this,"请填写验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = phone_et.getText().toString();
                String code = code_et.getText().toString();
                if(tag==0&&PhoneAndEmailUtils.isMobileNO(phone)){
                    changePhone(phone,code);
                }else if(tag==1&&PhoneAndEmailUtils.isEmail(phone)){
                    changePhone(phone,code);
                }else{
                    Toast.makeText(ChangePhoneActivity.this,"请填写正确的账号",Toast.LENGTH_SHORT).show();
                    return;
                }
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
        req.set("type","3");
        String session = SharedPreferencesUtils.getSession(ChangePhoneActivity.this,1);
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
                            finish();
                        }else {
                            Toast.makeText(ChangePhoneActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
    private void changePhone(final String num, String code){
        showLoadDialog();
        String url=NetworkTitle.DomainLoginNormal+NetworkChildren.PHONEANDMAILCHANGE;
        Request<String> req = NoHttp.createStringRequest(url, RequestMethod.POST);
        if(tag==0){
            req.set("phone",num);
        }else{
            req.set("email",num);
        }
        req.set("code",code);
        String session = SharedPreferencesUtils.getSession(ChangePhoneActivity.this,1);
        if(!TextUtils.isEmpty(session)){
            req.setHeader("Cookie","PHPSESSID="+session);
        }
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    SharedPreferencesUtils.setPassword(ChangePhoneActivity.this,num,"");
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            finish();
                        }else{
                            Toast.makeText(ChangePhoneActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
