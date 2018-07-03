package school.lg.overseas.school.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by Administrator on 2018/1/30.
 */

public class ShareUtils01 {
    public static void toShare(Context context, final String url , final String title){
        Log.e("share", "toShare>>>>>>>>>>>: " + url );
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if("SinaWeibo".equals(platform.getName())){
                    paramsToShare.setText(title);
                            paramsToShare.setImageUrl("http://www.smartapply.cn/cn/images/index3-img/index3-logo.png");
                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setTitle(title);
                    paramsToShare.setUrl(url);
                            paramsToShare.setText(title);
                    paramsToShare.setImageUrl("http://www.smartapply.cn/cn/images/index3-img/index3-logo.png");
                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setTitle(title);
                    paramsToShare.setUrl("url");
                            paramsToShare.setText(title);
                    paramsToShare.setImageUrl("http://www.smartapply.cn/cn/images/index3-img/index3-logo.png");
                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("QQ".equals(platform.getName())) {
                    paramsToShare.setTitle(title);
                    paramsToShare.setTitleUrl(url);
                            paramsToShare.setText(title);
                    paramsToShare.setImageUrl("http://www.smartapply.cn/cn/images/index3-img/index3-logo.png");
                }
                if("QZone".equals(platform.getName())){
                    //QQ空间您自己写了
                    paramsToShare.setTitle(title);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setText(title);
                    paramsToShare.setImageUrl("http://www.smartapply.cn/cn/images/index3-img/index3-logo.png");
                }
            }
        });

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("ShareLogin", "onComplete ---->  分享成功");
                platform.getName();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace().toString());
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("ShareLogin", "onCancel ---->  分享取消");
            }
        });

// 启动分享GUI
        oks.show(context);
    }


}
