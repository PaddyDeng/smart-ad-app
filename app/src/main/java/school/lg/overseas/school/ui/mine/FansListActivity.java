package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import school.lg.overseas.school.adapter.FansAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Fans;
import school.lg.overseas.school.bean.FansBean;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.FansBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 粉丝列表、关注列表
 */

public class FansListActivity extends BaseActivity {
    private int tag;
    private String id;
    private RecyclerView list_view;
    private SwipeRefreshLayout refresh;
    private List<Fans> datas;
    private int pages;
    private List<Fans> data;

    public static void start(Context context,int tag,String id){
        Intent intent =new Intent(context,FansListActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        if(null!=getIntent()){
            tag = getIntent().getIntExtra("tag", 0);
            id=getIntent().getStringExtra("id");
        }
        ImageView back = (ImageView) findViewById(R.id.back);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        if(tag==0)title_tv.setText("粉丝列表");
        else title_tv.setText("关注列表");
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
        LinearLayoutManager manager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                if(data.size()>=10) {
                    initData(pages + 1, true);
                }
            }
        });
    }

    private void initData(final int page, final boolean isMore) {
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(FansListActivity.this, 1);
        StringBuilder sb =new StringBuilder();
        sb.append(NetworkTitle.DomainSmartApplyNormal);
        if(tag==0)sb.append(NetworkChildren.FANSLIST);
        else sb.append( NetworkChildren.FOLLOWlIST);
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        String uid;
        if(TextUtils.isEmpty(id)){
            uid = SharedPreferencesUtils.getUserInfo(FansListActivity.this).getUid();
        }else{
            uid=id;
        }
        req.set("uid",uid).set("page",page+"").set("pageSize","10");
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if (response.isSucceed()) {
                    try {
                        FansBean fansBean = JSON.parseObject(response.get(), FansBean.class);
                        data = fansBean.getData().getData();
                        if (isMore) {
                            datas.addAll(data);
                            list_view.getAdapter().notifyDataSetChanged();
                        } else{
                            datas=data;
                            FansAdapter adapter = new FansAdapter(FansListActivity.this, tag, datas, new FansBack() {
                                @Override
                                public void setListener(int position, boolean isBoolean) {
                                    toFollow(data.get(position).getUid(), isBoolean);
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
                dismissLoadDialog();
            }
        });
    }

    private void toFollow(String id,boolean b) {
        Log.i("关取关",b+"");
        StringBuilder sb =new StringBuilder();
        sb.append(NetworkTitle.DomainSmartApplyNormal);
        if(b) {
            sb.append(NetworkChildren.FOLLOW);
        }else{
            sb.append(NetworkChildren.UNFOLLOW);
        }
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(FansListActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        req.set("followUser",id);
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(1,false);
    }

}
