package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebSettings;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.MyWebViewClient;

/**
 * 知识库详情
 */

public class KnowledgeDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back,title_right;
    private TextView title_tv;
    private WebView web;
    private SwipeRefreshLayout refresh;
    private String id;
    private LittleData data;
    private boolean isCollection;
    private DialogWait dialogWait;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,KnowledgeDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_detail);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        title_right.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void initData() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            dialogWait = new DialogWait(KnowledgeDetailActivity.this);
            dialogWait.show();
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.KNOWLEDGEDETATIL, RequestMethod.POST);
            req.set("contentId",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dialogWait.dismiss();
                    if(response.isSucceed()){
                        try {
                            List<LittleData> littleDatas = JSON.parseArray(response.get(), LittleData.class);
                            data = littleDatas.get(0);
                            List<Reading> readings = PracticeManager.getInstance().queryForId(1, 4, data.getId());
                            if(null!=readings&&readings.size()!=0){
                                isCollection=true;
                                title_right.setSelected(true);
                            }else{
                                isCollection=false;
                                title_right.setSelected(false);
                            }
                            Reading reading =new Reading();
                            reading.setTag(0);
                            reading.setType(4);
                            reading.setTitle(data.getName());
                            reading.setId(data.getId());
                            PracticeManager.getInstance().insert(reading);
                            title_tv.setText(data.getName());
                            String answer = data.getAnswer();
                            String s = HtmlUtil.repairContent(answer, NetworkTitle.DomainSmartApplyResourceNormal);
//                        WebSettings webSettings= web.getSettings();
//                        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//                        webSettings.setSupportZoom(true);
//                        webSettings.setBuiltInZoomControls(true);
//                        webSettings.setUseWideViewPort(true);
//                        webSettings.setLoadWithOverviewMode(true);
//                        web.setInitialScale(150);
//                        WebSettings settings = web.getSettings();
//                        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//                        // 是否支持js  说了用js，这句是必须加上的
//                        settings.setJavaScriptEnabled(true);
//                        //  重写 WebViewClient
//                        web.setWebViewClient(new MyWebViewClient());
                            web.loadDataWithBaseURL("",HtmlUtil.getHtml(s,0),"text/html","utf-8",null);
                        }catch (JSONException e){

                        }

                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    dialogWait.dismiss();
                }
            });
        }
    }

    private void findView() {
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_right = (ImageView) findViewById(R.id.title_right);
        web = (WebView) findViewById(R.id.web);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.title_right:
                if(isCollection){
                    PracticeManager.getInstance().deleteOne(1,4,data.getId());
                    isCollection=false;
                    title_right.setSelected(false);
                }else{
                    Reading reading =new Reading(1,4,data.getId(),data.getName());
                    PracticeManager.getInstance().insert(reading);
                    isCollection=true;
                    title_right.setSelected(true);
                }
                break;
        }
    }

}
