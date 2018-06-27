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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.IntentionalStateAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ChooseMajor;
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 * Created by Administrator on 2018/1/11.
 */

public class IntentionalStateActivity extends BaseActivity {
    private ImageView back;
    private RecyclerView list_view;
    private SwipeRefreshLayout refresh;
    private int tag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentional_state);
        if(null!=getIntent())tag=getIntent().getIntExtra("position",-1);
        back = (ImageView) findViewById(R.id.back);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("意向国家");
        list_view = (RecyclerView) findViewById(R.id.list_view);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(IntentionalStateActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       initData();
    }
    private void initData(){
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.CHOOSEMAJOR, RequestMethod.POST);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        ChooseMajor chooseMajor = JSON.parseObject(response.get(), ChooseMajor.class);
                        final List<Country> country = chooseMajor.getCountry();
                        IntentionalStateAdapter adapter =new IntentionalStateAdapter(IntentionalStateActivity.this, country, tag, new SelectListener() {
                            @Override
                            public void select(int position) {
                                Intent intent =new Intent();
                                intent.putExtra("select",JSON.toJSONString(country.get(position)));
                                setResult(124,intent);
                                finish();
                            }
                        });
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

}
