package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.boredream.bdvideoplayer.BDVideoView;
import com.boredream.bdvideoplayer.listener.SimpleOnVideoControlListener;
import com.boredream.bdvideoplayer.utils.DisplayUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;


import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Conf;
import school.lg.overseas.school.bean.Goods;
import school.lg.overseas.school.bean.VideoDetailInfo;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.WaitUtils;

/**
 * Created by Administrator on 2017/10/24.
 */

public class VideoActivity extends AppCompatActivity {
    private VideoDetailInfo info;
    private ImageView ivs;
    private BDVideoView vv;
    private WebView t_data;
    private TextView consult;
    private ArrayList<String> pngs;
    private ArrayList<Integer> times;
    private String data,urlStart,id;
    private int tag=-1,search;
    private boolean isVisibity =true;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mRequestQueue = NoHttp.newRequestQueue(6);
        findView();
        initData();
        initView();
    }

    private void initView() {
        try {
            WaitUtils.show(VideoActivity.this,"VideoActivity");
        }catch (Exception e){
        }
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.GOODSDETAIL, RequestMethod.POST);
        req.set("id",id);
        mRequestQueue.add(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                WaitUtils.dismiss("VideoActivity");
                if(response.isSucceed()){
                    try{
                        Goods goods = JSON.parseObject(response.get(), Goods.class);
                        String detailed = goods.getData().getDetailed();
                        String s = HtmlUtil.repairContent(detailed, NetworkTitle.DomainSmartApplyResourceNormal);
                        String html = HtmlUtil.getHtml(s,0);
                        t_data.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                WaitUtils.dismiss("VideoActivity");
            }
        });
        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//咨询
                Intent intent =new Intent(VideoActivity.this,OnlineActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(WaitUtils.isRunning("VideoActivity")){
            WaitUtils.dismiss("VideoActivity");
        }
    }

    private void findView() {
        vv = (BDVideoView) findViewById(R.id.video);
        t_data = (WebView) findViewById(R.id.t_date);
        consult = (TextView) findViewById(R.id.consult);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            urlStart=getIntent().getStringExtra("urlStart");
            pngs =getIntent().getStringArrayListExtra("pngs");
            times= getIntent().getIntegerArrayListExtra("times");
            info= (VideoDetailInfo) getIntent().getSerializableExtra("info");
            id = getIntent().getStringExtra("id");
//            teacherData = HtmlReplaceUtils.replaceAllToCharacter(teacherData);

        }
        vv.setOnVideoControlListener(new SimpleOnVideoControlListener() {

            @Override
            public void onRetry(int errorStatus) {
                // TODO: 2017/6/20 调用业务接口重新获取数据
                // get info and call method "videoView.startPlayVideo(info);"
            }

            @Override
            public void onBack() {
                onBackPressed();
            }

            @Override
            public void onFullScreen() {
                DisplayUtils.toggleScreenOrientation(VideoActivity.this,consult);
            }

            @Override
            public void showAndHide() {
                vv.setClose();
            }
        });

        vv.startPlayVideo(info);
        ivs = vv.getIv();
        setImg();
    }

    private void setImg() {
        MyThread myThread =new MyThread();
        new Thread(myThread).start();
    }

    public static int search(List<Integer> arr, int key) {
        int start = 0;
        int end = arr.size() - 1;
        int min = 0;
        while (start <= end) {
            int middle = (start + end) / 2;
            if (key < arr.get(middle)) {
                end = middle - 1;
            } else if (key > arr.get(middle)) {
                min=middle;
                start = middle + 1;
            } else {
                return middle;
            }
        }
        return min;
    }
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int time = msg.arg1;
            search = search(times, time);
            if(search!=tag){
                if(!TextUtils.isEmpty(pngs.get(search))) {
                    new GlideUtils().load(getApplicationContext(),urlStart + pngs.get(search),ivs);
                }
                tag=search;
            }
            setImg();
        }
    };


    private class MyThread implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int cu = vv.getCu();
            Message msg =new Message();
            msg.arg1=cu;
            handler.sendMessage(msg);
        }
    }

    public static void start(Context context, String title, Conf conf,String id) {
        Intent intent = new Intent(context, VideoActivity.class);
        VideoDetailInfo info =new VideoDetailInfo();
        info.setTitle(title);
        info.setVideoPath(conf.getUrlStart()+conf.getHls());

        intent.putExtra("urlStart",conf.getUrlStart());
        intent.putStringArrayListExtra("pngs", (ArrayList<String>) conf.getPngs());
        intent.putIntegerArrayListExtra("times", (ArrayList<Integer>) conf.getTimes());
        intent.putExtra("info",info);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        vv.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
        vv.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vv.onDestroy();
        handler.removeCallbacksAndMessages(null);
        mRequestQueue.cancelAll(); // 退出页面时时取消所有请求。
        mRequestQueue.stop(); // 退出时销毁队列，回收资源。
    }

    @Override
    public void onBackPressed() {
        if (!DisplayUtils.isPortrait(this)) {//横屏
            if(!vv.isLock()) {
                DisplayUtils.toggleScreenOrientation(this,consult);
            }
        } else {
            super.onBackPressed();
        }
    }

}
