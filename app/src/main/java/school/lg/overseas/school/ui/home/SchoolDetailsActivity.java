package school.lg.overseas.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import school.lg.overseas.school.adapter.SchoolMajorContentAdapter;
import school.lg.overseas.school.adapter.ScreenMajorTitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.SchoolDetails;
import school.lg.overseas.school.bean.ScreenLittleData;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.MajorDialog;
import school.lg.overseas.school.ui.other.WebActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 院校详情
 */

public class SchoolDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back,title_right,title_iv;
    private ImageView school_logo;
    private TextView title_tv,address,position,school_ranking,website,ranking,expenses;
    private RelativeLayout evaluation;
    private SwipeRefreshLayout refresh;
    private RecyclerView major_title,major_content;
    private String id;
    private SchoolDetails schoolDetails;
    private WebView synopsis;
    private LittleData data;
    private boolean isCollection,isLoad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);
        getArgs();
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        evaluation.setOnClickListener(this);
        title_right.setOnClickListener(this);
        website.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLDETAILS, RequestMethod.POST);
        req.set("schoolId",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                try {
                    schoolDetails = JSON.parseObject(response.get(), SchoolDetails.class);
                    if(schoolDetails.getCode()==1){
                        isLoad=true;
                        data = schoolDetails.getData();
                        List<Reading> readings = PracticeManager.getInstance().queryForId(1, 0, data.getId());
                        if(null!=readings&&readings.size()!=0){
                            title_right.setSelected(true);
                            isCollection=true;
                        }else{
                            title_right.setSelected(false);
                            isCollection=false;
                        }
                        Reading reading = new Reading();
                        reading.setId(data.getId());
                        reading.setTag(0);
                        reading.setTitle(data.getName());
                        reading.setType(0);
                        PracticeManager.getInstance().insert(reading);
                        title_tv.setText(data.getName());
                        new GlideUtils().load(SchoolDetailsActivity.this,NetworkTitle.DomainSchoolResourceNormal+data.getDuration(),title_iv);
                        new GlideUtils().loadCircle(SchoolDetailsActivity.this,NetworkTitle.DomainSchoolResourceNormal+data.getImage(),school_logo);

                        address.setText(data.getAnswer());
                        position.setText(data.getAlternatives());
                        school_ranking.setText(data.getArticle());
                        website.setText(data.getListeningFile());
                        ranking.setText(data.getArticle());
                        expenses.setText(TextUtils.isEmpty(data.getSentenceNumber())?"暂无详情内容":data.getSentenceNumber());
                        synopsis.loadDataWithBaseURL(null,HtmlUtil.getHtml(data.getCnName()),"text/html","utf-8",null);
                        LinearLayoutManager titleManager =new LinearLayoutManager(SchoolDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
                        LinearLayoutManager contentManager =new LinearLayoutManager(SchoolDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
                        major_title.setLayoutManager(titleManager);
                        major_content.setLayoutManager(contentManager);
                        List<ScreenLittleData> major = schoolDetails.getMajor();
                        int len=major.size();
                        for (int i = len-1; i >=0; i--) {
                            if(major.get(i).getContent().size()==0)major.remove(i);
                        }
                        if(null!=schoolDetails.getMajor()&&schoolDetails.getMajor().size()!=0&&null!=schoolDetails.getMajor().get(0).getContent()&&schoolDetails.getMajor().get(0).getContent().size()!=0) {
                            SchoolMajorContentAdapter contentAdapter = new SchoolMajorContentAdapter(SchoolDetailsActivity.this, schoolDetails.getMajor().get(0).getContent(), new SelectListener() {
                                @Override
                                public void select(int position) {
                                    Reading reading1 = new Reading(1, 1, schoolDetails.getMajor().get(0).getContent().get(position).getId(), schoolDetails.getMajor().get(0).getContent().get(position).getName());
                                    reading1.setName(data.getName());
                                    reading1.setS(data.getListeningFile());
                                    reading1.setEnMajprName(schoolDetails.getMajor().get(0).getContent().get(position).getTitle());
                                    MajorDialog dialog = new MajorDialog(SchoolDetailsActivity.this, R.style.AlphaDialogAct);
                                    dialog.show();
                                    dialog.setData(reading1);
                                }
                            });
                            major_content.setAdapter(contentAdapter);
                        }
                        if(null!=schoolDetails.getMajor()&&schoolDetails.getMajor().size()!=0) {
                            ScreenMajorTitleAdapter titleAdapter = new ScreenMajorTitleAdapter(SchoolDetailsActivity.this, schoolDetails.getMajor(), new SelectListener() {
                                @Override
                                public void select(int position) {
                                    final int titleId = position;
                                    if(null!=schoolDetails.getMajor()&&schoolDetails.getMajor().size()!=0&&null!=schoolDetails.getMajor().get(0).getContent()&&schoolDetails.getMajor().get(0).getContent().size()!=0) {
                                        SchoolMajorContentAdapter contentAdapter = new SchoolMajorContentAdapter(SchoolDetailsActivity.this, schoolDetails.getMajor().get(position).getContent(), new SelectListener() {
                                            @Override
                                            public void select(int position) {
                                                Reading reading1 = new Reading(1, 1, schoolDetails.getMajor().get(titleId).getContent().get(position).getId(), schoolDetails.getMajor().get(titleId).getContent().get(position).getName());
                                                reading1.setName(data.getName());
                                                reading1.setS(data.getListeningFile());
                                                reading1.setEnMajprName(schoolDetails.getMajor().get(0).getContent().get(position).getTitle());
                                                MajorDialog dialog = new MajorDialog(SchoolDetailsActivity.this, R.style.AlphaDialogAct);
                                                dialog.show();
                                                dialog.setData(reading1);
                                            }
                                        });
                                        major_content.setAdapter(contentAdapter);
                                    }
                                }
                            });
                            major_title.setAdapter(titleAdapter);
                        }
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

    private void getArgs(){
        if(getIntent()!=null){
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.i("学校id",id);
        }
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_right = (ImageView) findViewById(R.id.title_right);
        title_iv = (ImageView) findViewById(R.id.title_iv);
        school_logo = (ImageView) findViewById(R.id.school_logo);
        evaluation = (RelativeLayout) findViewById(R.id.evaluation);
        address = (TextView) findViewById(R.id.address);
        position = (TextView) findViewById(R.id.position);
        school_ranking = (TextView) findViewById(R.id.school_ranking);
        website = (TextView) findViewById(R.id.website);
        synopsis = (WebView) findViewById(R.id.synopsis);
        ranking = (TextView) findViewById(R.id.ranking);
        expenses = (TextView) findViewById(R.id.expenses);
        major_title = (RecyclerView) findViewById(R.id.major_title);
        major_content = (RecyclerView) findViewById(R.id.major_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.evaluation:
                EnrollActivity.start01(SchoolDetailsActivity.this,data.getName(),"");
                break;
            case R.id.title_right:
                if(isLoad) {
                    if (isCollection) {
                        PracticeManager.getInstance().deleteOne(1, 0, data.getId());
                        title_right.setSelected(false);
                        isCollection = false;
                    } else {
                        Reading reading = new Reading(1, 0, data.getId(), data.getName());
                        PracticeManager.getInstance().insert(reading);
                        title_right.setSelected(true);
                        isCollection = true;
                    }
                }
                break;
            case R.id.website:
                WebActivity.start(SchoolDetailsActivity.this,website.getText().toString());
                break;
        }
    }

}
