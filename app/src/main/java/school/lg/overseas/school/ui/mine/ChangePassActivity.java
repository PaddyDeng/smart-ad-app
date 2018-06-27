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
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/15.
 */

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {
    private EditText old_et,new_et;
    private TextView tv;
    private ImageView back;
    public static void start(Context context){
        Intent intent =new Intent(context,ChangePassActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        findView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        tv.setOnClickListener(this);
    }

    private void findView() {
        old_et = (EditText) findViewById(R.id.old_et);
        new_et = (EditText) findViewById(R.id.new_et);
        tv = (TextView) findViewById(R.id.tv);
        back = (ImageView) findViewById(R.id.back);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv:
                if(TextUtils.isEmpty(old_et.getText())){
                    Toast.makeText(ChangePassActivity.this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(new_et.getText())){
                    Toast.makeText(ChangePassActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String oldP = old_et.getText().toString();
                String newP = new_et.getText().toString();
                if(oldP.equals(newP)){
                    Toast.makeText(ChangePassActivity.this,"新密码不能与旧密码相同",Toast.LENGTH_SHORT).show();
                    return;
                }
                Login userInfo = SharedPreferencesUtils.getUserInfo(ChangePassActivity.this);
//                String password = userInfo.getPassword();
//                if(!oldP.equals(password)){
//                    Toast.makeText(ChangePassActivity.this,"旧密码输入错误",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                changePass(userInfo.getUid(),oldP,newP);
                break;
        }
    }

    private void changePass(String uid, String oldP, final String newP) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.PHONEANDMAILCHANGE, RequestMethod.POST);
        req.set("uid",uid).set("oldPass",oldP).set("newPass",newP);
        String session = SharedPreferencesUtils.getSession(ChangePassActivity.this,1);
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
                            SharedPreferencesUtils.setPassword(ChangePassActivity.this,"",newP);
                            Toast.makeText(ChangePassActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ChangePassActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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

}
