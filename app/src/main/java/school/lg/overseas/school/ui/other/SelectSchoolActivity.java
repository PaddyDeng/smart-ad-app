package school.lg.overseas.school.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import school.lg.overseas.school.adapter.SearchSchoolAndMajorAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.bean.SearchSchoolList;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.KeyboardUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/8.
 */

public class SelectSchoolActivity extends BaseActivity implements View.OnClickListener {
    private TextView back;
    private ImageView delete_iv;
    private EditText et;
    private SwipeRefreshLayout refresh;
    private RecyclerView list_view;
    private String keyWord ="";
    private int pages;
    private List<SearchSchool> datas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        findView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        delete_iv.setOnClickListener(this);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    delete_iv.setVisibility(View.VISIBLE);
                }else{
                    delete_iv.setVisibility(View.GONE);
                }
                keyWord=s.toString();
                initData(1,false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1,false);
                refresh.setRefreshing(false);
            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(SelectSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData(pages+1,true);
            }
        });
    }

    private void initData(final int page, final boolean isMore) {
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLANDMAJOR, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set("school",keyWord);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
                if(response.isSucceed()){
                    try{
                        SearchSchoolList searchSchoolList = JSON.parseObject(response.get(), SearchSchoolList.class);
                        List<SearchSchool> data = searchSchoolList.getData();
                        if(isMore){
                            datas.addAll(data);
                            list_view.getAdapter().notifyDataSetChanged();
                        }else{
                            datas=data;
                            SearchSchoolAndMajorAdapter adapter =new SearchSchoolAndMajorAdapter(SelectSchoolActivity.this,datas,2);
                            adapter.setListener(new SelectListener() {
                                @Override
                                public void select(int position) {
                                    SearchSchool searchSchool = datas.get(position);
                                    Intent intent =new Intent();
                                    SharedPreferencesUtils.setCountry(SelectSchoolActivity.this,searchSchool.getCountry());
                                    intent.putExtra("select",JSON.toJSONString(searchSchool));
                                    intent.putExtra("country",searchSchool.getCountry());
                                    setResult(108,intent);
                                    finish();
                                }
                            });
                            list_view.setAdapter(adapter);
                        }
                        pages=page;
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
            }
        });
    }

    private void findView() {
        back = (TextView) findViewById(R.id.back);
        delete_iv = (ImageView) findViewById(R.id.delete_iv);
        et = (EditText) findViewById(R.id.et);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        KeyboardUtils.hideOrShowKeyBoard(et,"o");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.delete_iv:
                et.setText("");
                break;
        }
    }

}
