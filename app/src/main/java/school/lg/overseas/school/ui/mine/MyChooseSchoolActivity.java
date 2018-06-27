package school.lg.overseas.school.ui.mine;

import android.content.Context;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.SchoolTestAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.SchoolTest;
import school.lg.overseas.school.bean.SchoolTestList;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 我的选校报告
 */

public class MyChooseSchoolActivity extends BaseActivity {
    private ImageView back;
    private TextView title_tv;
    private RecyclerView list_view;
    private SwipeRefreshLayout refresh;
    private TextView nothing;

    public static void start(Context context){
        Intent intent =new Intent(context,MyChooseSchoolActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_choose_school);
        findView();
        initData();
    }

    private void initData() {
        String session = SharedPreferencesUtils.getSession(MyChooseSchoolActivity.this, 1);
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SCHOOLEVALUATIONLIST, RequestMethod.POST);
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        SchoolTestList schoolTestList = JSON.parseObject(response.get(), SchoolTestList.class);
                        List<SchoolTest> schoolTest = schoolTestList.getSchoolTest();
                        if(null!=schoolTest&&schoolTest.size()!=0) {
                            nothing.setVisibility(View.GONE);
                            SchoolTestAdapter adapter = new SchoolTestAdapter(MyChooseSchoolActivity.this, schoolTest);
                            list_view.setAdapter(adapter);
                        }else{
                            nothing.setVisibility(View.VISIBLE);
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

    private void findView() {
        nothing = (TextView) findViewById(R.id.nothing);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("我的选校报告");
        list_view = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager manager =new LinearLayoutManager(MyChooseSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

}
