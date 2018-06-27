package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.sharesdk.onekeyshare.OnekeyShare;
import school.lg.overseas.school.R;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.ShareProxy;
import school.lg.overseas.school.utils.ShareProxy01;

/**
 * 分享
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private LinearLayout wechat_ll,wechatm_ll,qq_ll,qzone_ll,sina_ll;
    private String mImagePath;
    private String toQQimg;
    private View rl;
    private int tag=0;
    private String url,title;

    public ShareDialog(@NonNull Context context) {
        super(context,R.style.MyDialogStyle);
        this.context = context;
    }

    public void setTag(int i,String url,String title){
        this.tag=i;
        this.url=url;
        this.title=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shared);
        rl = findViewById(R.id.rl);
        wechat_ll = (LinearLayout) findViewById(R.id.wechat_ll);
        wechatm_ll = (LinearLayout) findViewById(R.id.wechatmoments_ll);
        qq_ll = (LinearLayout) findViewById(R.id.qq_ll);
        qzone_ll = (LinearLayout) findViewById(R.id.qzone_ll);
        sina_ll = (LinearLayout) findViewById(R.id.sina_ll);
        wechat_ll.setOnClickListener(this);
        wechatm_ll.setOnClickListener(this);
        qq_ll.setOnClickListener(this);
        qzone_ll.setOnClickListener(this);
        sina_ll.setOnClickListener(this);
        rl.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        if (tag == 0) {
            mImagePath = context.getFilesDir().getPath() + "share.png";
            toQQimg = context.getExternalCacheDir().getPath() + "share.png";
            switch (v.getId()) {
                case R.id.rl:
                    dismiss();
                    break;
                case R.id.wechat_ll:
                    ShareProxy.getInstance().shareToWechat(mImagePath);
                    dismiss();
                    break;
                case R.id.wechatmoments_ll:
                    ShareProxy.getInstance().shareWechatMoments(mImagePath);
                    dismiss();
                    break;
                case R.id.qq_ll:
                    ShareProxy.getInstance().shareToQQ(mImagePath);
                    dismiss();
                    break;
                case R.id.qzone_ll:
                    ShareProxy.getInstance().shareToQzone(mImagePath);
                    dismiss();
                    break;
                case R.id.sina_ll:

                    ShareProxy.getInstance().shareToSina(mImagePath);
                    dismiss();
                    break;
            }
        }else{
            switch (v.getId()){
                case R.id.rl:
                    dismiss();
                    break;
                case R.id.wechat_ll:
                    ShareProxy01.getInstance().shareToWechat(url,title);
                    dismiss();
                    break;
                case R.id.wechatmoments_ll:
                    ShareProxy01.getInstance().shareWechatMoments(url,title);
                    dismiss();
                    break;
                case R.id.qq_ll:
                    ShareProxy01.getInstance().shareToQQ(url,title);
                    dismiss();
                    break;
                case R.id.qzone_ll:
                    ShareProxy01.getInstance().shareToQzone(url,title);
                    dismiss();
                    break;
                case R.id.sina_ll:
                    ShareProxy01.getInstance().shareToSina(url,title);
                    dismiss();
                    break;
            }
        }
    }

    /**
     *  一键分享
     */
    private void showShare(String img) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("测评结果");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(NetworkTitle.DomainSmartApplyResourceNormal);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我在雷哥选校 的测评结果");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(img);
        // 启动分享GUI
        oks.show(context);
    }


}
