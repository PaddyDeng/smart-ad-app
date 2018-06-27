package school.lg.overseas.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import school.lg.overseas.school.adapter.ChooseMajorAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ChooseMajor;
import school.lg.overseas.school.bean.MajorSelect;
import school.lg.overseas.school.bean.ScreenLittleData;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 */

public class ChooseMajorActivity extends BaseActivity {
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private RecyclerView title_list,content_list;
    private int titleTag=-1,contentTag=-1;
    private List<ScreenLittleData> select;
    private MajorSelect majorSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_major);
        findView();
        initView();
        initData();
        setClick();
    }

    private void setClick() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content_list.getVisibility()==View.VISIBLE){
                    title_list.setVisibility(View.VISIBLE);
                    content_list.setVisibility(View.GONE);
                }else{
                    finish();
                }
            }
        });
    }

    private void initView() {
        if(null!=getIntent()){
            titleTag = getIntent().getIntExtra("titleTag", -1);
            contentTag = getIntent().getIntExtra("contentTag", -1);
        }
        select = new ArrayList<>();
        majorSelect =new MajorSelect();
        LinearLayoutManager titleManger =new LinearLayoutManager(ChooseMajorActivity.this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager contentManager =new LinearLayoutManager(ChooseMajorActivity.this,LinearLayoutManager.VERTICAL,false);
        title_list.setLayoutManager(titleManger);
        content_list.setLayoutManager(contentManager);
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.CHOOSEMAJOR, RequestMethod.POST);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        ChooseMajor chooseMajor = JSON.parseObject(response.get(), ChooseMajor.class);
                        final List<ScreenLittleData> major = chooseMajor.getMajor();
                        ChooseMajorAdapter titleAdapter =new ChooseMajorAdapter(ChooseMajorActivity.this, major,titleTag, new SelectListener() {
                            @Override
                            public void select(int position) {
                                majorSelect.setTitleP(position);
                                majorSelect.setTitleId(major.get(position).getId());
                                majorSelect.setTitleName(major.get(position).getName());
                                int contentTags=-1;
                                if(position==titleTag)contentTags=contentTag;
                                ChooseMajorAdapter contentAdapter =new ChooseMajorAdapter(ChooseMajorActivity.this, major.get(position).getChild(),contentTags, new SelectListener() {
                                    @Override
                                    public void select(int position) {
                                        majorSelect.setContentP(position);
                                        majorSelect.setContentId(major.get(majorSelect.getTitleP()).getChild().get(position).getId());
                                        majorSelect.setContentName(major.get(majorSelect.getTitleP()).getChild().get(position).getName());
                                        Intent intent =new Intent();
                                        intent.putExtra("majorSelect",JSON.toJSONString(majorSelect));
                                        setResult(122,intent);
                                        finish();
                                    }
                                });
                                content_list.setAdapter(contentAdapter);
                                title_list.setVisibility(View.GONE);
                                content_list.setVisibility(View.VISIBLE);
                            }
                        });
                        title_list.setAdapter(titleAdapter);
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
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("选择专业");
        back = (ImageView) title.findViewById(R.id.back);
        title_list = (RecyclerView) findViewById(R.id.title_list);
        content_list = (RecyclerView) findViewById(R.id.content_list);
    }

    @Override
    protected boolean preBackExitPage() {
        if(content_list.getVisibility()==View.VISIBLE){
            content_list.setVisibility(View.GONE);
            title_list.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

}
