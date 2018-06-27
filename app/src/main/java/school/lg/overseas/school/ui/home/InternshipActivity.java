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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.InternshipAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 找实习
 */

public class InternshipActivity extends BaseActivity {
    private ImageView back;
    private RecyclerView list_view;

    public static void start(Context context){
        Intent intent =new Intent(context, InternshipActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship);
        findView();
        initView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(InternshipActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.INTERNSHIPLIST);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        List<Apply> datas = JSON.parseArray(response.get(), Apply.class);
                        InternshipAdapter adapter =new InternshipAdapter(InternshipActivity.this,datas);
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
        View title = findViewById(R.id.title);
        back = (ImageView) title.findViewById(R.id.back);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("找实习");
        list_view = (RecyclerView) findViewById(R.id.list_view);
    }

}
