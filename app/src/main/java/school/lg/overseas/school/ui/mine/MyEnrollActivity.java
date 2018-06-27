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
import school.lg.overseas.school.adapter.EnrollListAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Enroll;
import school.lg.overseas.school.bean.SchoolTestList;
import school.lg.overseas.school.callback.EnrollCloseListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.EnrollResultDialog;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 录取测评列表
 */

public class MyEnrollActivity extends BaseActivity {
    private ImageView back;
    private RecyclerView list_view;
    private TextView nothing;

    public static void start(Context context){
        Intent intent =new Intent(context,MyEnrollActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enroll);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        nothing = (TextView) findViewById(R.id.nothing);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("录取报告");
        list_view = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager manager =new LinearLayoutManager(MyEnrollActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        initData();
    }
    private void initData(){
        String session = SharedPreferencesUtils.getSession(MyEnrollActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SCHOOLEVALUATIONLIST, RequestMethod.POST);
        req.setHeader("Cookie","PHPSESSID="+session);
        showLoadDialog();
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        SchoolTestList schoolTestList = JSON.parseObject(response.get(), SchoolTestList.class);
                        final List<Enroll> enrolls = schoolTestList.getProbabilityTest();
                        if(null!=enrolls&&enrolls.size()!=0) {
                            nothing.setVisibility(View.GONE);
                            EnrollListAdapter adapter = new EnrollListAdapter(MyEnrollActivity.this, enrolls, new SelectListener() {
                                @Override
                                public void select(int position) {
                                    EnrollResultDialog dialog = new EnrollResultDialog(MyEnrollActivity.this, R.style.AlphaDialogAct);
                                    dialog.show();
                                    dialog.setData(enrolls.get(position), new EnrollCloseListener() {
                                        @Override
                                        public void setLinstener(boolean isClose) {

                                        }
                                    });
                                }
                            });
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

}
