package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Goods;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;

/**
 * Created by Administrator on 2017/12/20.
 */

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title_tv,contact;
    private WebView web;
    private SwipeRefreshLayout refresh;
    private String id;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,GoodsDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        getArgs();
        findView();
        initData();
        setClick();
    }

    private void getArgs() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
        }
    }

    private void initData() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.GOODSDETAIL, RequestMethod.POST);
        req.set("id",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        Goods goods = JSON.parseObject(response.get(), Goods.class);
                        title_tv.setText(goods.getData().getName());
                        String detailed = goods.getData().getDetailed();
                        String s = HtmlUtil.repairContent(detailed, NetworkTitle.DomainSmartApplyResourceNormal);
                        String html = HtmlUtil.getHtml(s,0);
                        web.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
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

    private void setClick() {
        back.setOnClickListener(this);
        contact.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void findView() {
        View title = findViewById(R.id.title);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) title.findViewById(R.id.back);
        title_tv = (TextView) title.findViewById(R.id.title_tv);
        contact = (TextView) findViewById(R.id.contact);
        web = (WebView) findViewById(R.id.web);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.contact:
                Intent intent =new Intent(GoodsDetailActivity.this,OnlineActivity.class);
                startActivity(intent);
                break;
        }
    }

}
