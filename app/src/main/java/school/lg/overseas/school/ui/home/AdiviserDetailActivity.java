package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import school.lg.overseas.school.adapter.AdiviserQuestionAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.AdiviserDetail;
import school.lg.overseas.school.bean.Answer;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.OnlineActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

/**
 * 顾问详情
 *
 */

public class AdiviserDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_tv,name,summary,lable,num,rate,score,introduce_t,answer_t,consultation,again;
    private ImageView back,iv;
    private ViewPager viewPager;
    private SwipeRefreshLayout refresh;
    private int oldTag;
    private RecyclerView item_list;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,AdiviserDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiviser_detail);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        introduce_t.setOnClickListener(this);
        answer_t.setOnClickListener(this);
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
            Intent intent = getIntent();
            String id = intent.getStringExtra("id");
            showLoadDialog();
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ADIVISERDETAIL, RequestMethod.POST);
            req.set("contentid",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                            AdiviserDetail adiviserDetail = JSON.parseObject(response.get(), AdiviserDetail.class);
                            LittleData data = adiviserDetail.getData().get(0);
                            List<Answer> answer = adiviserDetail.getAnswer();
                            title_tv.setText(data.getName());
                            new GlideUtils().loadCircle(AdiviserDetailActivity.this, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),iv);
                            name.setText(data.getName());
                            summary.setText(data.getAlternatives()+",从业"+data.getArticle()+"年");
                            lable.setText(data.getA());
                            num.setText(data.getListeningFile()+"份");
                            rate.setText(data.getCnName()+"%");
                            score.setText(data.getNumbering()+"分");
                            List<View> views =new ArrayList<>();
                            View tv = LayoutInflater.from(AdiviserDetailActivity.this).inflate(R.layout.tv_layout,null);
                            TextView content = (TextView) tv.findViewById(R.id.content);
                            content.setText(HtmlReplaceUtils.replaceAllToLable(data.getAnswer()));
                            View quesitionView = LayoutInflater.from(AdiviserDetailActivity.this).inflate(R.layout.fragment_recycler,null);
                            again = (TextView) quesitionView.findViewById(R.id.again);
                            item_list = (RecyclerView) quesitionView.findViewById(R.id.item_list);
                            views.add(tv);
                            views.add(quesitionView);
                            if(null!=answer&&answer.size()!=0) {
                                again.setVisibility(View.GONE);
                                LinearLayoutManager manager = new LinearLayoutManager(AdiviserDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                                item_list.setLayoutManager(manager);
                                AdiviserQuestionAdapter questionAdapter = new AdiviserQuestionAdapter(AdiviserDetailActivity.this, answer);
                                item_list.setAdapter(questionAdapter);
                            }else{
                                again.setText("这里什么都没有哦");
                                again.setVisibility(View.VISIBLE);
                            }
                            viewPager.setAdapter(new MyViewPageAdapter(views));
                            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    setTitle(position%2);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            });
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
        setTitle(0);
    }


    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        View title = findViewById(R.id.title);
        title_tv = (TextView) title.findViewById(R.id.title_tv);
        back = (ImageView) title.findViewById(R.id.back);
        iv = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById(R.id.name);
        summary = (TextView) findViewById(R.id.summary);
        lable = (TextView) findViewById(R.id.lable);
        num = (TextView) findViewById(R.id.num);
        rate = (TextView) findViewById(R.id.rate);
        score = (TextView) findViewById(R.id.score);
        introduce_t = (TextView) findViewById(R.id.introduce_t);
        answer_t = (TextView) findViewById(R.id.answer_t);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        consultation = (TextView) findViewById(R.id.consultation);
    }

    public void setTitle(int i){
        if(i==0){
            introduce_t.setSelected(true);
            introduce_t.setTextColor(getResources().getColor(R.color.mainGreen));
            answer_t.setSelected(false);
            answer_t.setTextColor(getResources().getColor(R.color.black));
        }else{
            introduce_t.setSelected(false);
            introduce_t.setTextColor(getResources().getColor(R.color.black));
            answer_t.setSelected(true);
            answer_t.setTextColor(getResources().getColor(R.color.mainGreen));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.introduce_t:
                setTitle(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.answer_t:
                setTitle(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.consultation:
                Intent intent =new Intent(AdiviserDetailActivity.this, OnlineActivity.class);
                startActivity(intent);
                break;
        }
    }

}
