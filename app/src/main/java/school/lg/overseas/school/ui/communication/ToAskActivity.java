package school.lg.overseas.school.ui.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.TagAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.Tags;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 发表问答
 */

public class ToAskActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title_right;
    private RecyclerView lable_list;
    private EditText title_et,content_et;
    private List<Tags> tagses;
    private int selectPos=-1;

    public static void start(Context context){
        Intent intent =new Intent(context,ToAskActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_ask);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        title_right.setOnClickListener(this);
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.COMMUNICATION, RequestMethod.GET);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        tagses = JSON.parseArray(response.get(), Tags.class);
                        TagAdapter adapter =new TagAdapter(ToAskActivity.this, tagses, new SelectListener() {
                            @Override
                            public void select(int position) {
                                selectPos=position;
                            }
                        });
                        lable_list.setAdapter(adapter);
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

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("发表问答");
        back = (ImageView) title.findViewById(R.id.back);
        title_right = (TextView) title.findViewById(R.id.title_right);
        title_right.setText("发表");
        title_right.setVisibility(View.VISIBLE);
        lable_list = (RecyclerView) findViewById(R.id.lable_list);
        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
        GridLayoutManager manager =new GridLayoutManager(ToAskActivity.this,3);
        lable_list.setLayoutManager(manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.title_right:
                boolean login = SharedPreferencesUtils.isLogin(ToAskActivity.this);
                if(!login){
                    LoginHelper.needLogin(ToAskActivity.this,"需要登录才能继续哦");
                    return;
                }
                if(TextUtils.isEmpty(title_et.getText())){
                    Toast.makeText(ToAskActivity.this,"请填写问题标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(content_et.getText())){
                    Toast.makeText(ToAskActivity.this,"请填写问题详情",Toast.LENGTH_SHORT).show();
                    return;
                }
                String titles = title_et.getText().toString();
                String contents = content_et.getText().toString();
                String tag="";
                if(selectPos!=-1){
                    tag=tagses.get(selectPos).getId();
                }else{
                    Toast.makeText(ToAskActivity.this,"请选择问题标签",Toast.LENGTH_SHORT).show();
                    return;
                }
                toSend(titles,contents,tag);
                break;
        }
    }

    private void toSend(String titles, String contents, String tag) {
        String session = SharedPreferencesUtils.getSession(ToAskActivity.this, 1);
        if(TextUtils.isEmpty(session)){
            //登录
            LoginHelper.needLogin(ToAskActivity.this,"需要登录才能继续哦");
            return;
        }
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.NEWQUESTION, RequestMethod.POST);
        req.set("tag",tag).set("question",titles).set("contents",contents);
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        Login login = JSON.parseObject(response.get(), Login.class);
                        if(login.getCode()==1){
                            Toast.makeText(ToAskActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                            setResult(106);
                            finish();
                        }else{//提示发表失败
                            Toast.makeText(ToAskActivity.this,login.getMessage(),Toast.LENGTH_SHORT).show();
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
