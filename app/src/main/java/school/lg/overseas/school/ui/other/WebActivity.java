package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;

/**
 * Created by Administrator on 2018/1/16.
 */

public class WebActivity extends BaseActivity {
    private TextView title_tv;
    private WebView web;
    private   SwipeRefreshLayout refresh;

    public static void start(Context context,String url){
        Intent intent =new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ImageView back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        web = (WebView) findViewById(R.id.web);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        if(null!=getIntent()){
            String url = getIntent().getStringExtra("url");
            WebChromeClient wcc =new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    title_tv.setText(title);
                }
            };
            web.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            web.setWebChromeClient(wcc);
            web.loadUrl(url);
        }
    }

}
