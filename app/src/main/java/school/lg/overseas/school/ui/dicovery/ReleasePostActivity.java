package school.lg.overseas.school.ui.dicovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.NewsTagAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 发布发现帖子
 */

public class ReleasePostActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_right;
    private ImageView back;
    private EditText title_et,content_et;
    private String id="20";
    private RecyclerView tag_list;
    private  List<TitleTag> titleTags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_post);
        findView();
        initView();
        setClick();
    }

    private void initView() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            if(TextUtils.isEmpty(id))id="20";
        }
        GridLayoutManager manager =new GridLayoutManager(ReleasePostActivity.this,3);
        tag_list.setLayoutManager(manager);
        String tags =SharedPreferencesUtils.getNewsTags(ReleasePostActivity.this);
        titleTags =JSON.parseArray(tags, TitleTag.class);
        NewsTagAdapter adapter =new NewsTagAdapter(ReleasePostActivity.this, id, titleTags, new SelectListener() {
            @Override
            public void select(int position) {
                TitleTag titleTag =titleTags.get(position);
                if(titleTag.getId().equals("14"))id="20";
                else id=titleTag.getId();
            }
        });
        tag_list.setAdapter(adapter);
    }

    private void setClick() {
        back.setOnClickListener(this);
        title_right.setOnClickListener(this);
    }

    private void findView() {
        tag_list = (RecyclerView) findViewById(R.id.tag_list);
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("发表帖子");
        title_right = (TextView) title.findViewById(R.id.title_right);
        title_right.setVisibility(View.VISIBLE);
        title_right.setText("发表");
        back = (ImageView) title.findViewById(R.id.back);
        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.title_right:
                if(!SharedPreferencesUtils.isLogin(ReleasePostActivity.this)){
                    LoginHelper.needLogin(ReleasePostActivity.this,"需要登录才能继续哦");
                    return;
                }
                if(TextUtils.isEmpty(title_et.getText())){
                    Toast.makeText(ReleasePostActivity.this,"未填写标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(content_et.getText())){
                    Toast.makeText(ReleasePostActivity.this,"未填写内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                String titles = title_et.getText().toString();
                String contents = content_et.getText().toString();
                String session = SharedPreferencesUtils.getSession(ReleasePostActivity.this, 1);
                if(TextUtils.isEmpty(session)){
                    LoginHelper.needLogin(ReleasePostActivity.this,"需要登录才能发帖哦");
                    return;
                }
                showLoadDialog();
                if(TextUtils.isEmpty(id))id="20";
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.ADDPOST, RequestMethod.POST);
                req.set("title",titles).set("content",contents).set("catId",id);
                req.setHeader("Cookie","PHPSESSID="+session);
                request(0, req, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        dismissLoadDialog();
                        if(response.isSucceed()){
                            try {
                                PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                int code = praiseBack.getCode();
                                if(code==1){
                                    SharedPreferencesUtils.setPost(ReleasePostActivity.this,"News",true);
                                    Toast.makeText(ReleasePostActivity.this,"发帖成功",Toast.LENGTH_SHORT).show();
                                    setResult(102);
                                    finish();
                                }else if(code==0){
                                    LoginHelper.needLogin(ReleasePostActivity.this,"登录已过期，请重新登录");
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
}
