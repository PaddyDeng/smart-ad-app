package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.MajorDetailsSchoolAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.OnlineActivity;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 专业详情
 */

public class MajorDetailsActivity extends BaseActivity implements View.OnClickListener {
    private String id="";
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private RecyclerView school_list;
    private TextView title_tv,cn_name,en_name,praise,degree,employment,vocation,diploma,ranking,explain,condition,curriculum,direction;
    private RelativeLayout praise_rl,contact_rl;
    private boolean isPrise;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,MajorDetailsActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_details);
        getArgs();
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        praise_rl.setOnClickListener(this);
        contact_rl.setOnClickListener(this);
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
        View title = findViewById(R.id.title);
        back = (ImageView) title.findViewById(R.id.back);
        title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("专业详情");
        cn_name = (TextView) findViewById(R.id.cn_name);
        en_name = (TextView) findViewById(R.id.en_name);
        praise = (TextView) findViewById(R.id.praise);
        praise_rl = (RelativeLayout) findViewById(R.id.praise_rl);
        contact_rl = (RelativeLayout) findViewById(R.id.contact_rl);
        degree = (TextView) findViewById(R.id.degree);
        employment = (TextView) findViewById(R.id.employment);
        vocation = (TextView) findViewById(R.id.vocation);
        diploma = (TextView) findViewById(R.id.diploma);
        ranking = (TextView) findViewById(R.id.ranking);
        explain = (TextView) findViewById(R.id.explain);
        condition = (TextView) findViewById(R.id.condition);
        school_list = (RecyclerView) findViewById(R.id.school_list);
        curriculum = (TextView) findViewById(R.id.curriculum);
        direction = (TextView) findViewById(R.id.direction);
    }

    private void getArgs() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
        }
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.MAJORDETAILS, RequestMethod.POST);
        req.set("id",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try{
                        LittleData data = JSON.parseObject(response.get(), LittleData.class);
                        Reading reading =new Reading();
                        reading.setId(data.getId());
                        reading.setType(1);
                        reading.setTitle(data.getName());
                        reading.setTag(0);
                        PracticeManager.getInstance().insert(reading);
                        initView(data);
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

    private void initView(LittleData data) {
        title_tv.setText(data.getName());
        cn_name.setText(data.getName());
        en_name.setText("("+data.getTitle()+")");
        praise.setText("点赞("+data.getFabulous()+")");
        degree.setText(data.getAnswer());
        employment.setText(data.getAlternatives());
        vocation.setText(data.getArticle());
        diploma.setText(data.getListeningFile());
        ranking.setText(data.getCnName());
        explain.setText(data.getNumbering());
        condition.setText(data.getSentenceNumber());
        curriculum.setText(data.getProblemComplement());
        direction.setText(data.getTrainer().replaceAll(",","     "));
        LinearLayoutManager manager =new LinearLayoutManager(MajorDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
        school_list.setLayoutManager(manager);
        String[] schools = data.getDuration().split(",");
        MajorDetailsSchoolAdapter schoolAdapter =new MajorDetailsSchoolAdapter(MajorDetailsActivity.this,schools);
        school_list.setAdapter(schoolAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.praise_rl:
                if(!isPrise)toPraise();
                break;
            case R.id.contact_rl:
                Intent intent =new Intent(MajorDetailsActivity.this,OnlineActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void toPraise(){
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.MAJORPRAISE, RequestMethod.POST);
        req.set("contentId",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                try {
                    PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                    if(1==praiseBack.getCode()){
                        isPrise=true;
                        Toast.makeText(MajorDetailsActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                        praise.setText("点赞("+praiseBack.getNum()+")");
                    }
                }catch (JSONException e){

                }
            }
            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }

}
