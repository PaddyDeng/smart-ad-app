package school.lg.overseas.school.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.io.File;
import java.io.FileOutputStream;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.AdverData;
import school.lg.overseas.school.bean.Advertising;
import school.lg.overseas.school.callback.PermissionCallback;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.GuideActivity;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/12.
 */

public class AdvActivity extends BaseActivity {

    private String jumpUrl;
    private String imgUrl;
    private static final String TAG = AdvActivity.class.getSimpleName();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                SharedPreferences guide = getSharedPreferences("guide", MODE_PRIVATE);
                boolean isFirst = guide.getBoolean("isFirst", false);
                if (isFirst) {
                    getGdie();
                } else {
                    Intent intent = new Intent(AdvActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            return false;
        }
    });

    /**
     * 获取广告页
     */
    public void getGdie() {
        final Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.OPEN_MAP);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Advertising advertising = JSON.parseObject(response.get(), Advertising.class);
                AdverData data = advertising.getData();
                imgUrl = data.getImage();
                jumpUrl = data.getAnswer();
                getBitmap(NetworkTitle.DomainSmartApplyResourceNormal + imgUrl);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Intent intent = new Intent(AdvActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFinish(int what) {
            }
        });

    }

    public void getBitmap(final String url) {
        final String ImageFile = Environment.getExternalStorageDirectory() + "/school/" + StringUtils.getImageFileFromUrl(url);
        String perfs_url = SharedPreferencesUtils.getImageFileName(this);
        if (TextUtils.equals(ImageFile, perfs_url)) {
            AdvertisingActivity.setAdvert(this, imgUrl, 5, jumpUrl, ImageFile);
            finish();
        } else {
            final Request<Bitmap> req = NoHttp.createImageRequest(url);
            request(4, req, new OnResponseListener<Bitmap>() {
                @Override
                public void onStart(int what) {

                }

                @Override
                public void onSucceed(int what,final Response<Bitmap> response) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveBitmap(response.get(), url);
                        }
                    }).start();
                    SharedPreferencesUtils.setPrefsImageFileName(AdvActivity.this, ImageFile);
                    AdvertisingActivity.setAdvert(AdvActivity.this, imgUrl, 5, jumpUrl, ImageFile);
                    finish();
                }

                @Override
                public void onFailed(int what, Response<Bitmap> response) {
                    AdvertisingActivity.setAdvert(AdvActivity.this, imgUrl, 5, jumpUrl);
                    finish();
                }

                @Override
                public void onFinish(int what) {

                }
            });
        }
    }

    /**
     * 本地存储广告页
     *
     * @param bitmap
     * @param fileName
     */
    public void saveBitmap(Bitmap bitmap, String fileName) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "school");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, StringUtils.getImageFileFromUrl(fileName));
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            AdvertisingActivity.setAdvert(AdvActivity.this, imgUrl, 5, jumpUrl);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setBackgroundDrawable(null);
        setContentView(R.layout.activity_advertising);
        getPermission(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 300, "需要读写文件权限", 1, new PermissionCallback() {
            @Override
            public void onSuccessful() {//获取权限成功
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure() {//获取失败
                Toast.makeText(AdvActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                AdvActivity.this.finish();
            }
        });

    }


}
