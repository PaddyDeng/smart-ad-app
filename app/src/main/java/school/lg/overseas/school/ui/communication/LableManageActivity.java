package school.lg.overseas.school.ui.communication;

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
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.LableManageAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 标签管理
 */

public class LableManageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView list_view;
    private List<TitleTag> titleTags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_manage);
        findView();
        initView();
        initData();
        setClick();
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.QUESTIONTAG);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        titleTags = JSON.parseArray(response.get(), TitleTag.class);
                        String questionTag = SharedPreferencesUtils.getQuestionTag(LableManageActivity.this);
                        List<String> tags = JSON.parseArray(questionTag, String.class);
                        if(null!=tags){
                            for (int i = 0; i < titleTags.size(); i++) {
                                if(tags.contains(titleTags.get(i).getId())){
                                    titleTags.get(i).setClose(true);
                                }
                            }
                        }
                        LableManageAdapter adapter =new LableManageAdapter(LableManageActivity.this,titleTags);
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

    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(LableManageActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
    }


    private void setClick() {
        back.setOnClickListener(this);
    }

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("标签管理");
        back = (ImageView) title.findViewById(R.id.back);
        list_view = (RecyclerView) findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                List<String> closeTag =new ArrayList<>();
                for (int i = 0; i <titleTags.size(); i++) {
                    if(titleTags.get(i).isClose()==true)closeTag.add(titleTags.get(i).getId());
                }
                SharedPreferencesUtils.setQuestionTag(LableManageActivity.this,JSON.toJSONString(closeTag));
                setResult(102);
                finish();
                break;
        }
    }

}
