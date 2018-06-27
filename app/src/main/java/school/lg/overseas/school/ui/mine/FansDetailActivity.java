package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import school.lg.overseas.school.bean.FansDetail;
import school.lg.overseas.school.bean.FansDetailBean;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class FansDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back,iv;
    private TextView title_tv,name,follow,question_num,ask_num,fans_num,follow_num;
    private RelativeLayout question_rl,ask_rl,fans_rl,follow_rl;
    private SwipeRefreshLayout refresh;
    private String id;
    private boolean b;
    private FansDetail data;
    public static void start(Context context,String id){
        Intent intent =new Intent(context,FansDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_detail);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        follow.setOnClickListener(this);
        question_rl.setOnClickListener(this);
        ask_rl.setOnClickListener(this);
        fans_rl.setOnClickListener(this);
        follow_rl.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void initData() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            String session = SharedPreferencesUtils.getSession(FansDetailActivity.this, 1);
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.UserDetail, RequestMethod.POST);
            req.set("uid",id);
            req.setHeader("Cookie","PHPSESSID="+session);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    if(response.isSucceed()){
                        try {
                            FansDetailBean fansDetailBean = JSON.parseObject(response.get(), FansDetailBean.class);
                            data = fansDetailBean.getData();
                            title_tv.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
                            new GlideUtils().loadCircle(FansDetailActivity.this,NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),iv);
                            name.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
                            b=data.isBoolean();
                            if(b){
                                follow.setSelected(true);
                                follow.setText("已关注");
                            }else{
                                follow.setSelected(false);
                                follow.setText("关注");
                            }
                            question_num.setText(data.getQuestionNum());
                            ask_num.setText(data.getAnswerNum());
                            fans_num.setText(data.getFans());
                            follow_num.setText(data.getFollow());
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

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        iv = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById(R.id.name);
        follow = (TextView) findViewById(R.id.follow);
        question_rl = (RelativeLayout) findViewById(R.id.question_rl);
        question_num = (TextView) findViewById(R.id.question_num);
        ask_rl = (RelativeLayout) findViewById(R.id.ask_rl);
        ask_num = (TextView) findViewById(R.id.ask_num);
        fans_rl = (RelativeLayout) findViewById(R.id.fans_rl);
        fans_num = (TextView) findViewById(R.id.fans_num);
        follow_rl = (RelativeLayout) findViewById(R.id.follow_rl);
        follow_num = (TextView) findViewById(R.id.follow_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.follow:
                b=!b;
                toFollow(b);
                break;
            case R.id.question_rl:
                MyQuestionActivity.start(FansDetailActivity.this,0,id);
                break;
            case R.id.ask_rl:
                MyQuestionActivity.start(FansDetailActivity.this,1,id);
                break;
            case R.id.fans_rl:
                FansListActivity.start(FansDetailActivity.this,0,id);
                break;
            case R.id.follow_rl:
                FansListActivity.start(FansDetailActivity.this,1,id);
                break;
        }
    }
    private void toFollow(final boolean b) {
        PersonalDetail personalDetail =SharedPreferencesUtils.getPersonalDetail(FansDetailActivity.this);
        if(null!=personalDetail){
            if(id.equals(personalDetail.getUid())){
                Toast.makeText(FansDetailActivity.this,"不能关注自己哦",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!SharedPreferencesUtils.isLogin(FansDetailActivity.this)){
            LoginHelper.needLogin(FansDetailActivity.this,"需要登录才能关注哦");
            return;
        }
        StringBuilder sb =new StringBuilder();
        sb.append(NetworkTitle.DomainSmartApplyNormal);
        if(b) {
            sb.append(NetworkChildren.FOLLOW);
        }else{
            sb.append(NetworkChildren.UNFOLLOW);
        }
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(FansDetailActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        req.set("followUser",id);
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(b){
                    follow.setText("已关注");
                    follow.setSelected(true);
                }else{
                    follow.setText("关注");
                    follow.setSelected(false);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }

}
