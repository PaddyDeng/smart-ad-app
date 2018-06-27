package com.boredream.bdvideoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boredream.bdvideoplayer.bean.IVideoInfo;
import com.boredream.bdvideoplayer.listener.OnVideoControlListener;
import com.boredream.bdvideoplayer.listener.SimplePlayerCallback;
import com.boredream.bdvideoplayer.utils.NetworkUtils;
import com.boredream.bdvideoplayer.view.VideoBehaviorView;
import com.boredream.bdvideoplayer.view.VideoControllerView;
import com.boredream.bdvideoplayer.view.VideoProgressOverlay;
import com.boredream.bdvideoplayer.view.VideoSystemOverlay;

/**
 * 视频播放器View
 */
public class BDVideoView extends VideoBehaviorView {

    private SurfaceView mSurfaceView;
    private View mLoading;
    private VideoControllerView mediaController;
    private VideoSystemOverlay videoSystemOverlay;
    private VideoProgressOverlay videoProgressOverlay;
    private BDVideoPlayer mMediaPlayer;
    private ImageView iv;

    private int initWidth;//视频窗体
    private int initHeight;
    private int vvHeight;
    private int vvWidth;//播放窗体
    private int d_x,d_y;
    private int t_x,t_y;
    private int m_x,m_y;
    private int left,right,top,bottom;
    private int s_width,s_height;
    private boolean isClose=false;//是否关闭视频

    //获取当前播放时间
    public int getCu(){
        return mMediaPlayer.getCurrentPosition();
    }

    //获取背后图片
    public ImageView getIv(){
        return iv;
    }

    public boolean isLock() {
        return mediaController.isLock();
    }

    public BDVideoView(Context context) {
        super(context);
        init();
    }

    public BDVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BDVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //测量播放器宽高
    private void getWH(){
        //获取整体宽高
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                vvHeight = getMeasuredHeight();
                vvWidth = getMeasuredWidth();
                return true;
            }
        });
        mSurfaceView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSurfaceView.getViewTreeObserver().removeOnPreDrawListener(this);
                s_width = mSurfaceView.getMeasuredWidth();
                s_height = mSurfaceView.getMeasuredHeight();
                return true;
            }
        });

    }

    public void setClose(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
        if(isClose){
            params.setMargins(vvWidth - s_width, vvHeight - s_height, 0, 0);
            isClose=false;
            mediaController.getCloseBtn().setText("关闭\n视频");
        }else{
            params.setMargins(vvWidth, vvHeight, 0, 0);
            isClose=true;
            mediaController.getCloseBtn().setText("打开\n视频");
        }
        mSurfaceView.setLayoutParams(params);
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.video_view, this);

        iv = (ImageView) findViewById(R.id.iv);
        mSurfaceView = (SurfaceView) findViewById(R.id.video_surface);
        mLoading = findViewById(R.id.video_loading);
        mediaController = (VideoControllerView) findViewById(R.id.video_controller);
        videoSystemOverlay = (VideoSystemOverlay) findViewById(R.id.video_system_overlay);
        videoProgressOverlay = (VideoProgressOverlay) findViewById(R.id.video_progress_overlay);

        initPlayer();

        getWH();

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initWidth = getWidth();
                initHeight = getHeight();

                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(holder);
                    mMediaPlayer.openVideo();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case  MotionEvent.ACTION_DOWN:
                        d_x= (int) event.getRawX();
                        d_y= (int) event.getRawY();
                        left =v.getLeft();
                        top =v.getTop();
                        right =v.getRight();
                        bottom =v.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        m_x= (int) event.getRawX();
                        m_y= (int) event.getRawY();
                        if(left+m_x-d_x<=0){
                            t_x=0;
                        }else if(right+m_x-d_x>=vvWidth){
                            t_x=vvWidth-(right-left);
                        }else{
                            t_x=left+m_x-d_x;
                        }
                        if(top+m_y-d_y<=0){
                            t_y=0;
                        }else if(bottom+m_y-d_y>=vvHeight){
                            t_y=vvHeight-(bottom-top);
                        }else{
                            t_y=top+m_y-d_y;
                        }
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
                        params.setMargins(t_x,t_y,vvWidth-(right-left+t_x),vvHeight-(bottom-top+t_y));
                        mSurfaceView.setLayoutParams(params);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
        registerNetChangedReceiver();
    }

    private void initPlayer() {
        mMediaPlayer = new BDVideoPlayer();
        mMediaPlayer.setCallback(new SimplePlayerCallback() {

            @Override
            public void onStateChanged(int curState) {
                switch (curState) {
                    case BDVideoPlayer.STATE_IDLE:
                        am.abandonAudioFocus(null);
                        break;
                    case BDVideoPlayer.STATE_PREPARING:
                        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                        break;
                }
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaController.updatePausePlay();
            }

            @Override
            public void onError(MediaPlayer mp, int what, int extra) {
                mediaController.checkShowError(false);
            }

            @Override
            public void onLoadingChanged(boolean isShow) {
                if (isShow) showLoading();
                else hideLoading();
            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                mediaController.show();
                mediaController.hideErrorView();
            }
        });
        mediaController.setMediaPlayer(mMediaPlayer);
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    private boolean isBackgroundPause;

    public void onStop() {
        if (mMediaPlayer.isPlaying()) {
            // 如果已经开始且在播放，则暂停同时记录状态
            isBackgroundPause = true;
            mMediaPlayer.pause();
        }
    }

    public void onStart() {
        if (isBackgroundPause) {
            // 如果切换到后台暂停，后又切回来，则继续播放
            isBackgroundPause = false;
            mMediaPlayer.start();
        }
    }

    public void onDestroy() {
        mMediaPlayer.stop();
        mediaController.release();
        unRegisterNetChangedReceiver();
    }

    /**
     * 开始播放
     */
    public void startPlayVideo(final IVideoInfo video) {
        if (video == null) {
            return;
        }

        mMediaPlayer.reset();

        String videoPath = video.getVideoPath();
        mediaController.setVideoInfo(video);
        mMediaPlayer.setVideoPath(videoPath);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mediaController.toggleDisplay();
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isLock()) {
            return false;
        }
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isLock()) {
            return false;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    protected void endGesture(int behaviorType) {
        switch (behaviorType) {
            case VideoBehaviorView.FINGER_BEHAVIOR_BRIGHTNESS:
            case VideoBehaviorView.FINGER_BEHAVIOR_VOLUME:
                videoSystemOverlay.hide();
                break;
            case VideoBehaviorView.FINGER_BEHAVIOR_PROGRESS:
                mMediaPlayer.seekTo(videoProgressOverlay.getTargetProgress());
                videoProgressOverlay.hide();
                break;
        }
    }

    @Override
    protected void updateSeekUI(int delProgress) {
        videoProgressOverlay.show(delProgress, mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
    }

    @Override
    protected void updateVolumeUI(int max, int progress) {
        videoSystemOverlay.show(VideoSystemOverlay.SystemType.VOLUME, max, progress);
    }

    @Override
    protected void updateLightUI(int max, int progress) {
        videoSystemOverlay.show(VideoSystemOverlay.SystemType.BRIGHTNESS, max, progress);
    }

    public void setOnVideoControlListener(OnVideoControlListener onVideoControlListener) {
        mediaController.setOnVideoControlListener(onVideoControlListener);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                getLayoutParams().width = initWidth;
                getLayoutParams().height = initHeight;
                vvWidth = initWidth;
                vvHeight = initHeight;
            } else {
                getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
                getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                vvWidth = wm.getDefaultDisplay().getWidth();
                vvHeight = wm.getDefaultDisplay().getHeight();
            }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
        if(!isClose) {
            params.setMargins(vvWidth - s_width, vvHeight - s_height, 0, 0);
        }else{
            params.setMargins(vvWidth, vvHeight, 0, 0);
        }
        mSurfaceView.setLayoutParams(params);
    }

    private NetChangedReceiver netChangedReceiver;

    public void registerNetChangedReceiver() {
        if (netChangedReceiver == null) {
            netChangedReceiver = new NetChangedReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            activity.registerReceiver(netChangedReceiver, filter);
        }
    }

    public void unRegisterNetChangedReceiver() {
        if (netChangedReceiver != null) {
            activity.unregisterReceiver(netChangedReceiver);
        }
    }

    private class NetChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable extra = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (extra != null && extra instanceof NetworkInfo) {
                NetworkInfo netInfo = (NetworkInfo) extra;

                if (NetworkUtils.isNetworkConnected(context) && netInfo.getState() != NetworkInfo.State.CONNECTED) {
                    // 网络连接的情况下只处理连接完成状态
                    return;
                }

                mediaController.checkShowError(true);
            }
        }
    }
}
