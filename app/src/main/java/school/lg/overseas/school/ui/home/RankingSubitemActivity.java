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
import school.lg.overseas.school.adapter.RankSubitemAdapter;
import school.lg.overseas.school.adapter.YearAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.HotCase;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.RankingSubItemBean;
import school.lg.overseas.school.bean.RankingSubItemData;
import school.lg.overseas.school.bean.RankingSubItemDatas;
import school.lg.overseas.school.bean.Year;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 大学排名详情
 */

public class RankingSubitemActivity extends BaseActivity {
    private ImageView back;
    private RecyclerView title_list,content_list;
    private SwipeRefreshLayout refresh;
    private String titleT;
    private String id;
    private List<Year> years;
    private List<RankingSubItemData> data;
    private int oldPage=0;
    private int pages=1;

    public static void start(Context content,String id,String title){
        Intent intent = new Intent(content,RankingSubitemActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        content.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_subitem);
        getArgs();
        findView();
        initData(0,0,1,false);
        setClick();
    }

    /**
     *
     * @param tag 貌似是第一次？
     * @param type
     * @param page
     * @param isMore
     */
    private void initData(final int tag, final int type, final int page, final boolean isMore) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.RANKINGITEM, RequestMethod.GET);
        req.set("classId",id).set("page",page+"").set("pageSize","10");
        if(tag!=0)req.set("yearId",years.get(type).getId());
        else req.set("yearId","");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        RankingSubItemBean rankingSubItemBean = JSON.parseObject(response.get(), RankingSubItemBean.class);
                        if(tag==0){//加载年份
                            years=rankingSubItemBean.getYears();
                            initTitle();
                        }else{
                            if(isMore){
                                data.addAll(rankingSubItemBean.getData().getData());
                                content_list.getAdapter().notifyDataSetChanged();
                            }else{
                                data = rankingSubItemBean.getData().getData();
                                RankSubitemAdapter adapter =new RankSubitemAdapter(RankingSubitemActivity.this,data);
                                content_list.setAdapter(adapter);
                            }
                            pages=page;
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

    private void setClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1,oldPage,1,false);
                refresh.setRefreshing(false);
            }
        });
    }

    private void initTitle() {
        LinearLayoutManager titleManager =new LinearLayoutManager(RankingSubitemActivity.this,LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(titleManager);
        LinearLayoutManager contentManager =new LinearLayoutManager(RankingSubitemActivity.this,LinearLayoutManager.VERTICAL,false);
        content_list.setLayoutManager(contentManager);
        YearAdapter yearAdapter =new YearAdapter(RankingSubitemActivity.this,years,new SelectListener(){
            @Override
            public void select(int position) {
                oldPage =position;
                initData(1,position,1,false);
            }
        });
        title_list.setAdapter(yearAdapter);
        initData(1,1,1,false);
        content_list.addOnScrollListener(new EndLessOnScrollListener(contentManager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData(1,oldPage,pages+1,true);
            }
        });
    }

    private void getArgs() {
        if(null!=getIntent()){
            titleT = getIntent().getStringExtra("title");
            id = getIntent().getStringExtra("id");
            Log.i("排名",id);
        }
    }


    private void findView() {
        View title = findViewById(R.id.title);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) title.findViewById(R.id.back);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText(titleT);
        title_list = (RecyclerView) findViewById(R.id.title_list);
        content_list = (RecyclerView) findViewById(R.id.content_list);
    }

}
