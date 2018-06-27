package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import school.lg.overseas.school.bean.InformationData;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.MyWebViewClient;

/**
 * 资讯详情
 */

public class InformationActivity extends BaseActivity{
    private TextView title_tv;
    private ImageView back;
    private String id;
    private int tag;
    private String catId;
    private WebView webview;
    private SwipeRefreshLayout refresh;

    public static void start(Context context,String id,int tag,String catId){
        Intent intent =new Intent(context,InformationActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("tag",tag);
        intent.putExtra("catId",catId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if(getIntent()!=null){
            id = getIntent().getStringExtra("id");
            tag = getIntent().getIntExtra("tag",0);
            catId =getIntent().getStringExtra("catId");
        }
        findView();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        initData();
    }

    private void initData() {
        showLoadDialog();
        Request<String> req;
        if(tag==0) {
            req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.INFORMATION, RequestMethod.POST);
        }else{
            req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.INTERNSHIPDETAIL, RequestMethod.POST);
            req.set("catId",catId);
        }
        req.set("id",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                LogUtils.log(response.get());
                if(response.isSucceed()){
                    try {
                        InformationData informationData = JSON.parseObject(response.get(), InformationData.class);
                        title_tv.setText(TextUtils.isEmpty(informationData.getData().get(0).getTitle())?"加载中...":informationData.getData().get(0).getTitle());
                        String content="";
                        if(tag==0&&!TextUtils.isEmpty(informationData.getData().get(0).getAlternatives())){
                            content = informationData.getData().get(0).getAlternatives();
                        }else if(tag==1&&!TextUtils.isEmpty(informationData.getData().get(0).getAnswer())){
                            content=informationData.getData().get(0).getAnswer();
                        }
                        String s = HtmlUtil.repairContent(content, NetworkTitle.DomainSmartApplyResourceNormal);
                        String html = HtmlUtil.getHtml(s,0);
                        WebSettings settings = webview.getSettings();
                        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        // 是否支持js  说了用js，这句是必须加上的
                        settings.setJavaScriptEnabled(true);
                        //  重写 WebViewClient
                        webview.setWebViewClient(new MyWebViewClient());
                        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
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
        refresh= (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv= (TextView) findViewById(R.id.title_tv);
//        collection = (ImageView) findViewById(R.id.collection);
        webview = (WebView) findViewById(R.id.webview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
