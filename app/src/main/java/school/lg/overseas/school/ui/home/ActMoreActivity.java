package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import school.lg.overseas.school.adapter.ActivityAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.HomeData;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 更多特色活动（留学活动）
 */

public class ActMoreActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView list_view;
    private List<LittleData> datas;
    private int pages;

    public static void start(Context context){
        Intent intent =new Intent(context,ActMoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_more);
        findView();
        initView();
        initData(1,false);
        setClick();
    }

    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(ActMoreActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        datas=new ArrayList<>();
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.i("测试",pages+"");
                initData(pages+1,true);
            }
        });
    }

    private void setClick() {
        back.setOnClickListener(this);
    }

    private void initData(final int page, final boolean isMore) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.OPEN + NetworkChildren.ACTIVITYLIST, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        HomeData homeData = JSON.parseObject(response.get(), HomeData.class);
                        List<LittleData> data = homeData.getData();
                        if(isMore){
                            datas.addAll(data);
                            list_view.getAdapter().notifyDataSetChanged();
                        }else {
                            datas.clear();
                            datas.addAll(data);
                            ActivityAdapter adapter = new ActivityAdapter(ActMoreActivity.this, datas);
                            list_view.setAdapter(adapter);
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

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("留学活动");
        back = (ImageView) title.findViewById(R.id.back);
        list_view = (RecyclerView) findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

}
