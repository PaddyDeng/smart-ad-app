package school.lg.overseas.school.ui.mine;

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
import school.lg.overseas.school.adapter.HotQuestionAdapter;
import school.lg.overseas.school.adapter.HotQuestionAdapter01;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Question;
import school.lg.overseas.school.bean.Question01;
import school.lg.overseas.school.bean.QuestionData;
import school.lg.overseas.school.bean.QuestionData01;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/10.
 */

public class MyQuestionActivity extends BaseActivity {
    private RecyclerView list_view;
    private SwipeRefreshLayout refresh;
    private TextView title_tv;
    private String id;
    private int tag;
    private int pages;
    private List<QuestionData> datas;
    private List<QuestionData01> datas01;
    public static void start(Context context,int tag,String id){
        Intent intent =new Intent(context,MyQuestionActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("tag",tag);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);
        ImageView back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1,false);
                refresh.setRefreshing(false);
            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(MyQuestionActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData(pages+1,true);
            }
        });
        initData(1,false);
    }

    private void initData(final int page, final boolean isMore) {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            tag = getIntent().getIntExtra("tag", 0);
            if(tag==0)title_tv.setText("提出的问题");
            else title_tv.setText("回答的问题");
            if(TextUtils.isEmpty(id))id=SharedPreferencesUtils.getUserInfo(MyQuestionActivity.this).getUid();
        }
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(MyQuestionActivity.this, 1);
        StringBuffer sb =new StringBuffer();
        sb.append(NetworkTitle.DomainSmartApplyNormal );
        if(tag==0)sb.append(NetworkChildren.MYQUESTION);
        else sb.append(NetworkChildren.ANSWERLIST);
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        req.set("uid",id).set("page",page+"").set("pageSize","10");
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    if(tag==0){
                        try {
                            Question question = JSON.parseObject(response.get(), Question.class);
                            List<QuestionData> data = question.getData().getData();
                            if(isMore){
                                datas.addAll(data);
                                list_view.getAdapter().notifyDataSetChanged();
                            }else {
                                datas=data;
                                HotQuestionAdapter adapter = new HotQuestionAdapter(MyQuestionActivity.this, datas);
                                list_view.setAdapter(adapter);
                            }
                        }catch (JSONException e){

                        }

                    }else {
                        try {
                            Question01 question = JSON.parseObject(response.get(), Question01.class);
                            List<QuestionData01> data = question.getData().getData();
                            if(isMore){
                                datas01.addAll(data);
                                list_view.getAdapter().notifyDataSetChanged();
                            }else {
                                datas01=data;
                                HotQuestionAdapter01 adapter = new HotQuestionAdapter01(MyQuestionActivity.this, datas01);
                                list_view.setAdapter(adapter);
                            }
                        }catch (JSONException e){

                        }

                    }
                    pages=page;
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }

}
