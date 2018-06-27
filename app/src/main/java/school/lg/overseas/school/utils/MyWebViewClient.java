package school.lg.overseas.school.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MyWebViewClient  extends WebViewClient {
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //  html加载完成之后，调用js的方法
        imgReset(view);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    private void imgReset(WebView view) {
        view.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName('img'); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "var img = objs[i];   "
                + "    img.style.width = '100%';   "
                + "    img.style.height = 'auto';   "
                + "}" + "})()");
    }

}
