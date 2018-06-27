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
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.RankingTitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.bean.RankingTitles;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 看排名
 */

public class RankingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = RankingActivity.class.getSimpleName();
    private ImageView back;
    private RecyclerView list_view;

    public static void start(Context context){
        Intent intent =new Intent(context,RankingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        findView();
        initView();
        initData();
        setClick();
    }

    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(RankingActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
    }

    private void setClick() {
        back.setOnClickListener(this);
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.RANKINGTITLE);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        RankingTitles rankingTitles = JSON.parseObject(response.get(), RankingTitles.class);
                        RankingTitleAdapter adapter =new RankingTitleAdapter(RankingActivity.this,rankingTitles.getClasses());
                        list_view.setAdapter(adapter);
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
        back = (ImageView) findViewById(R.id.back);
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
