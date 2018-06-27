package school.lg.overseas.school.ui;

import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.DealActivity;
import school.lg.overseas.school.utils.RxHelper;

public class AdvertisingActivity extends BaseActivity implements View.OnClickListener {
    protected String TAG = AdvertisingActivity.this.getClass().getSimpleName();
    private static final String IMG_URL = "img_url";
    private static final String TIMES = "time";
    private static final String JUMP_URL = "jump_url";
    private static final String IMAGE_FILE = "img_file";
    private ImageView adImg;
    private TextView counterDownTv;
    private String imgUrl;
    private String jumpUrl;
    private int time;
    private String imgFile;
    protected ConcurrentMap<String, CompositeDisposable> mConcurrentMap = new ConcurrentHashMap<>();

    public static void setAdvert(Context context, String imgUrl, int time, String jumpUrl) {
        Intent intent = new Intent(context, AdvertisingActivity.class);
        intent.putExtra(IMG_URL, imgUrl);
        intent.putExtra(TIMES, time);
        intent.putExtra(JUMP_URL, jumpUrl);
        context.startActivity(intent);
    }

    public static void setAdvert(Context context, String imgUrl, int time, String jumpUrl, String imageFile) {
        Intent intent = new Intent(context, AdvertisingActivity.class);
        intent.putExtra(IMG_URL, imgUrl);
        intent.putExtra(TIMES, time);
        intent.putExtra(JUMP_URL, jumpUrl);
        intent.putExtra(IMAGE_FILE, imageFile);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverty);
        getArgs();
        findView();
        initView();
    }

    public void findView() {
        adImg = (ImageView) findViewById(R.id.advertising_img);
        counterDownTv = (TextView) findViewById(R.id.advertising_counter_down);
        counterDownTv.setOnClickListener(this);
        adImg.setOnClickListener(this);
    }


    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        imgUrl = intent.getStringExtra(IMG_URL);
        jumpUrl = intent.getStringExtra(JUMP_URL);
        time = intent.getIntExtra(TIMES, 5);//默认5秒
        imgFile = intent.getStringExtra(IMAGE_FILE);
    }


    protected void initView() {
        if (!TextUtils.isEmpty(imgFile)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile);
            if (bitmap != null) {
                adImg.setImageBitmap(bitmap);
            }
        } else {
            if (!TextUtils.isEmpty(imgUrl)) {
                imgUrl = NetworkTitle.DomainSmartApplyResourceNormal + imgUrl;
                Glide.with(AdvertisingActivity.this).load(imgUrl).into(adImg);
            }
        }
        counterDownTv.setText(getString(R.string.str_counter_down, time));
        addToCompositeDis(RxHelper
                .countDown(time)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        counterDownTv.setText(getString(R.string.str_counter_down, integer));
                        if (integer.intValue() <= 0) {
                            goMain();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        goMain();
                    }
                }));
    }


    //网络请求
    protected void addToCompositeDis(Disposable disposable) {
        CompositeDisposable compositeDisposable = mConcurrentMap.get(TAG);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            mConcurrentMap.put(TAG, compositeDisposable);
        }
        compositeDisposable.add(disposable);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advertising_counter_down:
                goMain();
                break;
            case R.id.advertising_img:
                goDealActivity();
                break;
            default:
                break;
        }
    }

    private void goDealActivity() {
        if (!TextUtils.isEmpty(jumpUrl)) {
            DealActivity.startDealActivity(mContext, "", jumpUrl, 2);
            finishWithAnim();
        }
    }

    private void goMain() {
        forword(MainActivity.class);
        finish();
    }

    public void forword(Class<?> c) {
        startActivity(new Intent(this, c));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CompositeDisposable disposable = mConcurrentMap.get(TAG);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
