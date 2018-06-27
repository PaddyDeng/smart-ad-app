package school.lg.overseas.school.ui.casePage;

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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.SuccessCaseAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.HotCase;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.DialogWait;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 成功案例
 */

public class SuccessCaseActivity extends BaseActivity{
    private ImageView back;
    private RecyclerView list_view;
    private List<LittleData> datas;
    private SwipeRefreshLayout refresh;
    private int pages;

    public static void start(Context context){
        Intent intent  =new Intent(context,SuccessCaseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_case);
        findView();
        initView();
        initData(false,1);
    }


    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(SuccessCaseActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData(true,pages+1);
            }
        });
    }

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("成功案例");
        back = (ImageView) title.findViewById(R.id.back);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(false,1);
                refresh.setRefreshing(false);
            }
        });
    }

    private void initData(final boolean isMore, final int page) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SUCCESSCASE, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        HotCase hotCase = JSON.parseObject(response.get(), HotCase.class);
                        List<LittleData> data = hotCase.getData();
                        if(!isMore){
                            SuccessCaseAdapter successAdapter = new SuccessCaseAdapter(SuccessCaseActivity.this, data);
                            list_view.setAdapter(successAdapter);
                            datas=data;
                        }else{
                            datas.addAll(data);
                            list_view.getAdapter().notifyDataSetChanged();
                        }
                        pages=page;
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
