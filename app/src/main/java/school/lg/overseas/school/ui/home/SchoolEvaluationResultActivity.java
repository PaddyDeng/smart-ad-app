package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.AnalysisAdapter;
import school.lg.overseas.school.adapter.SchoolRecommendAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.SchoolEvaluationData;
import school.lg.overseas.school.bean.SchoolEvaluationResult;
import school.lg.overseas.school.bean.SchoolEvaluationScore;
import school.lg.overseas.school.bean.SchoolEvaluationUser;
import school.lg.overseas.school.bean.SchoolEvaluationWork;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.ShareDialog;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.ScreenShotUtil;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SchoolEvaluationResultActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title_tv,name,score,school_num,again;
    private LinearLayout share,school_ll;
    private RecyclerView list_view,analysis_list;
    private SwipeRefreshLayout refresh;
    private boolean isShare;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,SchoolEvaluationResultActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_evaluation_result);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        again.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("选校测评");
        name = (TextView) findViewById(R.id.name);
        score = (TextView) findViewById(R.id.score);
        school_num = (TextView) findViewById(R.id.school_num);
        share = (LinearLayout) findViewById(R.id.share);
        school_ll = (LinearLayout) findViewById(R.id.school_ll);
        again = (TextView) findViewById(R.id.again);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        analysis_list = (RecyclerView) findViewById(R.id.analysis_list);
        LinearLayoutManager analysisManager =new LinearLayoutManager(SchoolEvaluationResultActivity.this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager listManager =new LinearLayoutManager(SchoolEvaluationResultActivity.this,LinearLayoutManager.VERTICAL,false);
        analysis_list.setLayoutManager(analysisManager);
        list_view.setLayoutManager(listManager);
    }

    private void initData() {
        String session = SharedPreferencesUtils.getSession(SchoolEvaluationResultActivity.this,1);
        if(TextUtils.isEmpty(session)){
            LoginHelper.needLogin(SchoolEvaluationResultActivity.this,"需要登录才能继续哦");
            return;
        }
        String id="";
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
        }
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SCHOOLEVALUATIONRESULT, RequestMethod.POST);
        req.set("uid", SharedPreferencesUtils.getUserInfo(SchoolEvaluationResultActivity.this).getUid());
        if(!TextUtils.isEmpty(id))req.set("id",id);
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        SchoolEvaluationResult datas = JSON.parseObject(response.get(), SchoolEvaluationResult.class);
                        if(datas.getCode()==1) {
                            SchoolEvaluationUser user = datas.getUser();
                            SchoolEvaluationData data = datas.getData();
                            SchoolEvaluationScore scores = datas.getScore();
                            name.setText(TextUtils.isEmpty(user.getNickname())?user.getUserName():user.getNickname());
                            score.setText(data.getScore()+"");
                            List<SchoolEvaluationWork> works =new ArrayList<>();
                            if(null!=scores.getGpa()) {
                                scores.getGpa().setTag(0);
                                works.add(0,scores.getGpa());
                            }
                            if(null!=scores.getGmat()){
                                scores.getGmat().setTag(1);
                                works.add(scores.getGmat());
                            }
                            if(null!=scores.getToefl()){
                                scores.getToefl().setTag(2);
                                works.add(scores.getToefl());
                            }
                            if(null!=scores.getSchool()){
                                scores.getSchool().setTag(3);
                                works.add(scores.getSchool());
                            }
                            if(null!=scores.getWork()){
                                scores.getWork().setTag(4);
                                works.add(scores.getWork());
                            }
                            AnalysisAdapter analysisAdapter =new AnalysisAdapter(SchoolEvaluationResultActivity.this,works);
                            analysis_list.setAdapter(analysisAdapter);
                            if(null==data.getRes()||data.getRes().size()==0){
                                school_num.setText("0+");
                                school_ll.setVisibility(View.GONE);
                            }else {
                                school_ll.setVisibility(View.VISIBLE);
                                school_num.setText(data.getRes().size()+"+");
                                SchoolRecommendAdapter adapter =new SchoolRecommendAdapter(SchoolEvaluationResultActivity.this,data.getRes());
                                list_view.setAdapter(adapter);
                            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.share:
                if(!isShare) {
                    isShare=true;
                    ScreenShotUtil.shoot(SchoolEvaluationResultActivity.this);
                    ShareDialog dialog = new ShareDialog(SchoolEvaluationResultActivity.this);
                    dialog.show();
                    isShare=false;
                }
                break;
            case R.id.again:
                SchoolEvaluationActivity.start(SchoolEvaluationResultActivity.this);
                finish();
                break;
        }
    }

}
