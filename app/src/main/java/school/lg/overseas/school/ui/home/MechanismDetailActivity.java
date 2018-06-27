package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.MechanismAdviserAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.MechanismDetail;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.manager.AutoLinearLayoutManager;
import school.lg.overseas.school.ui.other.OnlineActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;

/**
 * Created by Administrator on 2018/1/3.
 */

public class MechanismDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_tv,name,content,num,country_t,consultation;
    private ImageView back,iv;
    private SwipeRefreshLayout refresh;
    private RelativeLayout appointment;
    private View v2;
    private RecyclerView adiviser_list;
    private WebView introduce,case_list;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,MechanismDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanism_detail);
        findView();
        initView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        consultation.setOnClickListener(this);
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
            String id = getIntent().getStringExtra("id");
            showLoadDialog();
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.MECHANISMDETAIL, RequestMethod.POST);
            req.set("id",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                            List<MechanismDetail> mechanismDetails = JSON.parseArray(response.get(), MechanismDetail.class);
                            MechanismDetail data = mechanismDetails.get(0);
                            new GlideUtils().load(MechanismDetailActivity.this,NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),iv);
                            title_tv.setText(data.getName());
                            name.setText(data.getName());
                            content.setText(HtmlReplaceUtils.replaceAllToCharacter(data.getDescription()));
                            num.setText(HtmlUtil.fromHtml("已有<font color=\"#6f9701\">"+data.getCnName()+"</font>人咨询"));
                            country_t.setText(data.getAnswer().replaceAll(",","\t\t\t").replaceAll("、","\t\t\t").replaceAll("\r\n",""));
                            String alternatives = data.getAlternatives();
                            String s = HtmlUtil.repairContent(alternatives, NetworkTitle.DomainSmartApplyResourceNormal);
                            introduce.loadDataWithBaseURL(null,HtmlUtil.getHtml(s,0),"text/html","utf-8",null);
                            MechanismAdviserAdapter adviserAdapter =new MechanismAdviserAdapter(MechanismDetailActivity.this,data.getAdviser());
                            adiviser_list.setAdapter(adviserAdapter);
                            case_list.loadDataWithBaseURL(null,HtmlUtil.getHtml(HtmlUtil.repairContent(data.getListeningFile(),NetworkTitle.DomainSmartApplyResourceNormal),0),"text/html","utf-8",null);
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

    private void initView() {
        appointment.setVisibility(View.GONE);
        v2.setVisibility(View.VISIBLE);
        AutoLinearLayoutManager adviserManage=new AutoLinearLayoutManager(MechanismDetailActivity.this, LinearLayoutManager.VERTICAL,false);
        adiviser_list.setLayoutManager(adviserManage);
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back = (ImageView) findViewById(R.id.back);
        iv = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById(R.id.name);
        content = (TextView) findViewById(R.id.content);
        appointment = (RelativeLayout) findViewById(R.id.appointment);
        num = (TextView) findViewById(R.id.num);
        v2 = findViewById(R.id.v2);
        country_t = (TextView) findViewById(R.id.country_t);
        introduce = (WebView) findViewById(R.id.introduce);
        adiviser_list = (RecyclerView) findViewById(R.id.adiviser_list);
        case_list = (WebView) findViewById(R.id.case_list);
        consultation = (TextView) findViewById(R.id.consultation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.consultation:
                Intent intent =new Intent(MechanismDetailActivity.this, OnlineActivity.class);
                startActivity(intent);
                break;
        }
    }

}
