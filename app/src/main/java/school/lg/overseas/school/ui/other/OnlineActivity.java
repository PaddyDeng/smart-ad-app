package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;

/**
 * 在线咨询
 */

public class OnlineActivity extends BaseActivity{
    private boolean isClose=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        View title = findViewById(R.id.title);
        ImageView back = (ImageView) title.findViewById(R.id.back);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        final WebView web = (WebView) findViewById(R.id.web);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        title_tv.setText("在线咨询");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClose) {
                    finish();
                }else {
                    web.loadUrl("");
                    //处理断开链接

                    isClose=true;
                }
            }
        });
        String url ="http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=";
        Map<String,String> header =new HashMap<>();
        header.put("Referer","http://www.gmatonline.cn/");
        web.loadUrl(url,header);
    }
}
