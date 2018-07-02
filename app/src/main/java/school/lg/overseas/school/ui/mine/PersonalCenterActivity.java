package school.lg.overseas.school.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.jeanboy.cropview.cropper.CropperHandler;
import com.jeanboy.cropview.cropper.CropperManager;
import com.jeanboy.cropview.cropper.CropperParams;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.ByteArrayBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Personal;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.callback.PermissionCallback;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.communication.LableManageActivity;
import school.lg.overseas.school.ui.other.DialogWait;
import school.lg.overseas.school.ui.other.LoginActivity;
import school.lg.overseas.school.ui.other.PhotoDialog;
import school.lg.overseas.school.utils.CacheUtils;
import school.lg.overseas.school.utils.DataCleanManager;
import school.lg.overseas.school.utils.FileUtils;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.WaitUtils;

/**
 * 个人中心
 */

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener, CropperHandler {
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private RelativeLayout portrait_rl,name_rl,phone_rl,mailbox_rl,password_rl,lable_rl,feedback_rl,clean_rl;
    private ImageView portrait;
    private TextView name,phone,mailbox,password,vesion,clean;
    private Button exit;
    private PersonalDetail data;
    private DialogWait dialogWait;
    public static void start(Context context){
        Intent intent =new Intent(context,PersonalCenterActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        findView();
        CropperManager.getInstance().build(this);
        setClick();
    }

    private void setClick() {
        portrait_rl.setOnClickListener(this);
        name_rl.setOnClickListener(this);
        phone_rl.setOnClickListener(this);
        mailbox_rl.setOnClickListener(this);
        password_rl.setOnClickListener(this);
        lable_rl.setOnClickListener(this);
        feedback_rl.setOnClickListener(this);
        clean_rl.setOnClickListener(this);
        exit.setOnClickListener(this);
        back.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }

    private void initData() {
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(PersonalCenterActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.USERINFO, RequestMethod.POST);
        if (!TextUtils.isEmpty(session)) {
            req.setHeader("Cookie", "PHPSESSID=" + session);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if (response.isSucceed()) {
                        try {
                            Personal datas = JSON.parseObject(response.get(), Personal.class);
                            if(datas.getCode()==1) {
                                data = datas.getData();
                                SharedPreferencesUtils.setPersonal(PersonalCenterActivity.this, data);
                            }
                            initView();
                        }catch (JSONException e){

                        }

                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    dismissLoadDialog();
                }
            });
        }else{
            initView();
        }
    }

    private void initView() {
        if(SharedPreferencesUtils.isLogin(PersonalCenterActivity.this)){
            PersonalDetail personalDetail = SharedPreferencesUtils.getPersonalDetail(PersonalCenterActivity.this);
            new GlideUtils().loadCircle(PersonalCenterActivity.this, NetworkTitle.DomainSmartApplyResourceNormal+personalDetail.getImage(),portrait);
            name.setText(TextUtils.isEmpty(personalDetail.getNickname())?personalDetail.getUserName():personalDetail.getNickname());
            phone.setText(TextUtils.isEmpty(personalDetail.getPhone())?"":personalDetail.getPhone());
            mailbox.setText(TextUtils.isEmpty(personalDetail.getEmail())?"":personalDetail.getEmail());
            password.setText("*****");
            portrait_rl.setVisibility(View.VISIBLE);
            name_rl.setVisibility(View.VISIBLE);
            phone_rl.setVisibility(View.VISIBLE);
            mailbox_rl.setVisibility(View.VISIBLE);
            password_rl.setVisibility(View.VISIBLE);
            exit.setText("退出当前账户");
        }else{
            portrait_rl.setVisibility(View.GONE);
            name_rl.setVisibility(View.GONE);
            phone_rl.setVisibility(View.GONE);
            mailbox_rl.setVisibility(View.GONE);
            password_rl.setVisibility(View.GONE);
            exit.setText("马上登录");
        }
        try {
            String versionName = PersonalCenterActivity.this.getPackageManager().getPackageInfo(PersonalCenterActivity.this.getPackageName(), 0).versionName;
            vesion.setText(versionName);
        }catch (Exception e){
            vesion.setText("获取版本号失败");
        }
        try {
            String totalCacheSize = CacheUtils.getTotalCacheSize(PersonalCenterActivity.this);
            clean.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("个人中心");
        back = (ImageView) title.findViewById(R.id.back);
        portrait_rl = (RelativeLayout) findViewById(R.id.portrait_rl);
        name_rl = (RelativeLayout) findViewById(R.id.name_rl);
        phone_rl = (RelativeLayout) findViewById(R.id.phone_rl);
        mailbox_rl = (RelativeLayout) findViewById(R.id.mailbox_rl);
        password_rl = (RelativeLayout) findViewById(R.id.password_rl);
        lable_rl = (RelativeLayout) findViewById(R.id.lable_rl);
        feedback_rl = (RelativeLayout) findViewById(R.id.feedback_rl);
        clean_rl = (RelativeLayout) findViewById(R.id.clean_rl);
        portrait = (ImageView) findViewById(R.id.portrait);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        mailbox = (TextView) findViewById(R.id.mailbox);
        password = (TextView) findViewById(R.id.password);
        vesion = (TextView) findViewById(R.id.vesion);
        clean = (TextView) findViewById(R.id.clean);
        exit = (Button) findViewById(R.id.exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.portrait_rl:
                getPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 300, "需要赋予拍照和相册的权限，才能获取图片哦", 1, new PermissionCallback() {
                    @Override
                    public void onSuccessful() {//获取权限成功
                        PhotoDialog dialog =new PhotoDialog(PersonalCenterActivity.this,R.style.AlphaDialogAct);
                        dialog.setListener(new SelectListener() {
                            @Override
                            public void select(int position) {
                                if(position==0){//拍照
                                    CropperManager.getInstance().pickFromCamera();//拍照裁切
                                }else {//相册
                                    CropperManager.getInstance().pickFromGallery();//图库选择裁切
                                }
                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void onFailure() {//获取失败
                        Toast.makeText(PersonalCenterActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.name_rl:
                NameActivity.start(PersonalCenterActivity.this);
                break;
            case R.id.phone_rl:
                ChangePhoneActivity.start(PersonalCenterActivity.this,0);
                break;
            case R.id.mailbox_rl:
                ChangePhoneActivity.start(PersonalCenterActivity.this,1);
                break;
            case R.id.password_rl:
                ChangePassActivity.start(PersonalCenterActivity.this);
                break;
            case R.id.lable_rl:
                Intent intent1 =new Intent(PersonalCenterActivity.this,LableManageActivity.class);
                startActivity(intent1);
                break;
            case R.id.feedback_rl:
                FeedbackActivity.start(PersonalCenterActivity.this);
                break;
            case R.id.clean_rl:
                DataCleanManager.cleanApplicationData(PersonalCenterActivity.this);
                try {
                    String totalCacheSize = CacheUtils.getTotalCacheSize(PersonalCenterActivity.this);
                    clean.setText(totalCacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(PersonalCenterActivity.this,"清理完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                if(SharedPreferencesUtils.isLogin(PersonalCenterActivity.this)){
                    SharedPreferencesUtils.exitLogin(PersonalCenterActivity.this);
                    initView();
                }else {
                    Intent intent2 = new Intent(PersonalCenterActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferencesUtils.isLogin(PersonalCenterActivity.this)) {
            initData();
        }else{
            initView();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public CropperParams getParams() {
        return new CropperParams(0, 0);
        //配置裁切框比例
//        return new CropperParams(1, 1);
        //不约束裁切比例
        // return new CropperParams(0, 0);
    }

    @Override
    public void onCropped(Uri uri) {
        try {
            String realFilePath = GlideUtils.getRealFilePath(this, uri);
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Bitmap bitmap= FileUtils.comp(bm);
            String name = realFilePath.substring(realFilePath.lastIndexOf("/") + 1, realFilePath.length());
            byte[] bytes=FileUtils.getBitmapByte(bitmap);
            sendImg(new ByteArrayBinary(bytes,name));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCropCancel() {
    }

    @Override
    public void onCropFailed(String msg) {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropperManager.getInstance().handlerResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CropperManager.getInstance().unBind();
    }

    private void sendImg(ByteArrayBinary binarys){
        String session = SharedPreferencesUtils.getSession(PersonalCenterActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.PHOTOUP, RequestMethod.POST);
        req.set("upload",binarys);
        req.setHeader("Cookie","PHPSESSID="+session);
        dialogWait =new DialogWait(PersonalCenterActivity.this);
        dialogWait.show();
        dialogWait.setHint("上传头像中...");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dialogWait.dismiss();
                if(response.isSucceed()){
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            new GlideUtils().loadCircle(PersonalCenterActivity.this,NetworkTitle.DomainSmartApplyResourceNormal+praiseBack.getImage(),portrait);
                        }else{
                            Toast.makeText(PersonalCenterActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){

                    }

                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                dialogWait.dismiss();
            }
        });
    }

}

