package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.KnowApplyAdapter;
import school.lg.overseas.school.adapter.KnowApplyLittleAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Know;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 * Mentor课程
 */

public class KnowledgeBaseActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ViewPager list_view;
    private TextView apply,plan,again;
    private SwipeRefreshLayout refresh;
    private List<TextView> titles;
    private int old=-1;
    private List<View> views;
    private RecyclerView plan_list,apply_list;

    public static void start(Context context){
        Intent intent =new Intent(context,KnowledgeBaseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_base);
        findView();
        initView();
        initData(false);
        setClick();
    }

    private void initView() {
        views=new ArrayList<>();
        final View apply = LayoutInflater.from(KnowledgeBaseActivity.this).inflate(R.layout.fragment_apply_analysis, null);
        apply_list = (RecyclerView) apply.findViewById(R.id.apply_list);
        View plan = LayoutInflater.from(KnowledgeBaseActivity.this).inflate(R.layout.item_apply, null);
        View ll = plan.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        plan_list = (RecyclerView) plan.findViewById(R.id.plan_itme_list);
        views.add(apply);
        views.add(plan);
        list_view.setAdapter(new MyViewPageAdapter(views));

        LinearLayoutManager applyManager =new LinearLayoutManager(KnowledgeBaseActivity.this,LinearLayoutManager.VERTICAL,false);
        apply_list.setLayoutManager(applyManager);
        GridLayoutManager planManager =new GridLayoutManager(KnowledgeBaseActivity.this,2);
        plan_list.setLayoutManager(planManager);

        list_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setChoose(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
                refresh.setRefreshing(false);
            }
        });
    }

    private void setClick() {
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
        plan.setOnClickListener(this);
        again.setOnClickListener(this);
    }

    private void initData(final boolean isRefresh) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.KONW);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        Know know = JSON.parseObject(response.get(), Know.class);
                        if(!isRefresh) {
                            KnowApplyAdapter applyAdapter = new KnowApplyAdapter(KnowledgeBaseActivity.this, know.getApplyVideo());
                            apply_list.setAdapter(applyAdapter);
                            KnowApplyLittleAdapter planAdapter = new KnowApplyLittleAdapter(KnowledgeBaseActivity.this, know.getVideoAnalysis());
                            plan_list.setAdapter(planAdapter);
                        }else{
                            apply_list.getAdapter().notifyDataSetChanged();
                            plan_list.getAdapter().notifyDataSetChanged();
                        }
                    }catch (JSONException e){

                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }

            @Override
            public void onFinish(int what) {
                again.setVisibility(View.VISIBLE);
            }
        });
    }

    private void findView() {
        again = (TextView) findViewById(R.id.again);
        back = (ImageView) findViewById(R.id.back);
        list_view = (ViewPager) findViewById(R.id.list_view);
        apply = (TextView) findViewById(R.id.apply);
        plan = (TextView) findViewById(R.id.plan);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        titles = new ArrayList<>();
        titles.add(apply);
        titles.add(plan);
        setChoose(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.apply:
                setChoose(0);
                list_view.setCurrentItem(0);
                break;
            case R.id.plan:
                setChoose(1);
                list_view.setCurrentItem(1);
                break;
            case R.id.again:
                initData(false);
                again.setVisibility(View.GONE);
                break;
        }
    }

    private void setChoose(int i){
        if(old!=i){
            if(old==-1)old=0;
            titles.get(old).setSelected(false);
            titles.get(old).setTextColor(getResources().getColor(R.color.white));
            titles.get(i).setSelected(true);
            titles.get(i).setTextColor(getResources().getColor(R.color.mainGreen));
            old=i;
        }
    }

}
