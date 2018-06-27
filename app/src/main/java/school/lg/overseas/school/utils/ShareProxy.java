package school.lg.overseas.school.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import school.lg.overseas.school.MyApplication;
import school.lg.overseas.school.http.NetworkTitle;

public class ShareProxy {

    private static ShareProxy shareProxy;
    private static Context mContext;

    public static ShareProxy getInstance() {
        if (shareProxy == null) {
            synchronized (ShareProxy.class) {
                if (shareProxy == null) {
                    shareProxy = new ShareProxy(MyApplication.getInstance());
                }
            }
        }
        return shareProxy;
    }

    private ShareProxy(Context context) {
        mContext = context;
    }

    private static PlatformActionListener platformActionListener = new PlatformActionListener() {
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
        }

        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        }

        public void onCancel(Platform arg0, int arg1) {
        }
    };

    private void share(Platform.ShareParams params, String platName) {
        Platform weibo = ShareSDK.getPlatform(platName);
        weibo.setPlatformActionListener(platformActionListener);
        weibo.share(params);
    }

    public void shareWechatMoments(String imagePath) {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("测试结果");
        sp.setText("我在 雷哥选校 的测试结果");
        sp.setImagePath(imagePath);
        share(sp, WechatMoments.NAME);
    }

    public void shareToWechat(String imagePath) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("测试结果");
        sp.setText("我在 雷哥选校 的测试结果");
        sp.setImagePath(imagePath);
        share(sp, Wechat.NAME);
    }

    public void shareToSina(String imgPath) {
//        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setText("我在 雷哥选校 的测试结果");
//        sp.setImagePath(imgPath);
//        share(sp, SinaWeibo.NAME);
    }

    public void shareToQzone(String imagePath) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle("测试结果");
        sp.setText("我在 雷哥选校 的测试结果");
        sp.setImagePath(imagePath);
        sp.setTitleUrl(NetworkTitle.DomainSmartApplyResourceNormal); // 标题的超链接
        sp.setSite(NetworkTitle.DomainSmartApplyResourceNormal);//发布分享的网站名称
        sp.setSiteUrl(NetworkTitle.DomainSmartApplyResourceNormal);//发布分享网站的地址
        share(sp, QZone.NAME);
    }

    public void shareToQQ(String imagePath) {
        QQ.ShareParams sp = new QQ.ShareParams();

        sp.setTitle("我在 雷哥选校 的测评结果");
        sp.setTitleUrl(NetworkTitle.DomainSmartApplyResourceNormal);
        sp.setImagePath(imagePath);
        Platform qq = ShareSDK.getPlatform (QQ.NAME);

        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("ShareSDK", "onComplete ---->  分享成功");
                /*platform.isClientValid();*/
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("ShareSDK", "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Log.d("ShareSDK", "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("ShareSDK", "onCancel ---->  分享取消");
            }
        });
        qq.share(sp);
    }
}
