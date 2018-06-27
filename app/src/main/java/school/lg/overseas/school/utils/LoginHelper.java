package school.lg.overseas.school.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.AdvActivity;
import school.lg.overseas.school.ui.home.EnrollActivity;
import school.lg.overseas.school.ui.mine.NameActivity;
import school.lg.overseas.school.ui.other.ForgetPasswordActivity;
import school.lg.overseas.school.ui.other.LoginActivity;
import school.lg.overseas.school.ui.other.NeedLoginDialog;

/**
 * 登录过程
 */
public class LoginHelper {

    /**
     *
     * @param context
     * @param tag  0改名字 1打开app重置\注册  2 忘记密码
     */
    public static void againLogin(final Context context, final int tag){
        Map<String,String> map =SharedPreferencesUtils.getPassword(context);
        if(!TextUtils.isEmpty(map.get("phone"))) {
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.LOGIN, RequestMethod.POST);
            req.set("userName", map.get("phone")).set("userPass", map.get("pass"));
            ((BaseActivity) context).request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    if (response.isSucceed()) {
                        try {
                            Login login = JSON.parseObject(response.get(), Login.class);
                            if (login.getCode() == 1) {//登录成功
                                List<HttpCookie> cookies = response.getHeaders().getCookies();
                                if (cookies.isEmpty() || TextUtils.isEmpty(cookies.get(0).getValue())) {
                                    Toast.makeText(context, "重置session失败，需重新登录", Toast.LENGTH_SHORT).show();
                                } else {
                                    SharedPreferencesUtils.setPassword(context, TextUtils.isEmpty(login.getPhone()) ? login.getEmail() : login.getPhone(), login.getPassword());
                                    SharedPreferencesUtils.setSession(context, 1, cookies.get(0).getValue());
                                    SharedPreferencesUtils.setLogin(context, login);
                                    if (tag == 0) {
                                        login(context, login, 2);
                                    } else if(tag==1){
                                        login(context, login,1);
                                    }else if(tag==2){
                                        login(context,login,3);
                                    }
                                }
                            } else {

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
        }
    }

    /**
     *
     * @param context
     * @param login
     * @param tag 0登录界面 1   2改名字 3改密码
     */
    public static void login(final Context context, Login login, final int tag){
        List<String> urlList =new ArrayList<>();
        urlList.add(NetworkTitle.TOEFL+ NetworkChildren.TOEFL);
        urlList.add(NetworkTitle.DomainSmartApplyNormal+NetworkChildren.TOEFL);
        urlList.add(NetworkTitle.GMAT+NetworkChildren.GMAT);
        urlList.add(NetworkTitle.DomainGossipNormal+NetworkChildren.TOEFL);

        for (int i = 0; i < urlList.size(); i++) {
            final int u =i;
            final Request<String> req = NoHttp.createStringRequest(urlList.get(i), RequestMethod.GET);
            req.set("uid",login.getUid())
                    .set("nickname",login.getNickname())
                    .set("username",login.getUsername())
                    .set("password",login.getPassword())
                    .set("email",login.getEmail())
                    .set("phone",login.getPhone());
//            if(u==1){
//                String session = SharedPreferencesUtils.getSession(context, 1);
//                if(!TextUtils.isEmpty(session))req.setHeader("Cookie","PHPSESSID="+session);
//            }

            ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    Headers headers = response.getHeaders();
                    String cookie;
                    List<HttpCookie> cookies = headers.getCookies();
                    if(!cookies.isEmpty()){
                        cookie=cookies.get(0).getValue();
                        SharedPreferencesUtils.setSession(context,u,cookie);
                    }
                    if(tag==0) {
                        ((LoginActivity) context).setResult(10);
                        ((LoginActivity) context).finish();
                    }
                    if(tag==2){
                        ((NameActivity)context).finish();
                    }
                    if(tag==3){
                        ((ForgetPasswordActivity)context).finish();
                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    Toast.makeText(context,"重置session失败,需重新登录",Toast.LENGTH_SHORT).show();
//                    if(tag==0){
//                        ((LoginActivity)context).setResult(10);
//                        ((LoginActivity)context).finish();
//                    }
                }
            });
        }
    }
    public static void toLogin(Context context){
        Intent intent =new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    public static void needLogin(Context context,String s){
        NeedLoginDialog dialog =new NeedLoginDialog(context, R.style.AlphaDialogAct);
        dialog.setContent(s);
        dialog.show();
    }

    public static void initMessage(final Context context){
        String session=SharedPreferencesUtils.getSession(context,1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.MESSAGEINIT, RequestMethod.GET);
        if(!TextUtils.isEmpty(session)){
            req.setHeader("Cookie","PHPSESSID="+session);
        }
        ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.isSucceed()){
                    List<HttpCookie> cookies = response.getHeaders().getCookies();
                    if(null!=cookies&&cookies.size()!=0) {
                        SharedPreferencesUtils.setSession(context, 1, cookies.get(0).getValue());
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }
        });
    }
}
