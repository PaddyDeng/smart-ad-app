package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 修改昵称
 */

public class NameActivity extends BaseActivity {
    private ImageView back;
    private TextView tv;
    private EditText name_et;
    private String oldName="";
    private int tag;
    public static void start(Context context){
        Intent intent =new Intent(context,NameActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        if(null!=getIntent()){
            tag=getIntent().getIntExtra("tag",0);
        }
        back = (ImageView) findViewById(R.id.back);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("昵称");
        name_et = (EditText) findViewById(R.id.name_et);
        tv = (TextView) findViewById(R.id.tv);
        PersonalDetail personalDetail = SharedPreferencesUtils.getPersonalDetail(NameActivity.this);
        if(null!=personalDetail){
            oldName=TextUtils.isEmpty(personalDetail.getNickname())?personalDetail.getUserName():personalDetail.getNickname();
            name_et.setText(TextUtils.isEmpty(personalDetail.getNickname())?personalDetail.getUserName():personalDetail.getNickname());
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name_et.getText())){
                    Toast.makeText(NameActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name_et.getText().toString().equals(oldName)){
                    Toast.makeText(NameActivity.this,"昵称并为改变",Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadDialog();
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainLoginNormal + NetworkChildren.NICECHANGE, RequestMethod.POST);
                req.set("nickname",name_et.getText().toString());
                String session = SharedPreferencesUtils.getSession(NameActivity.this,1);
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
                                    LoginHelper.againLogin(NameActivity.this,0);
                                    MainActivity.getInstance().setChoose(4);
                                    Toast.makeText(NameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(NameActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
