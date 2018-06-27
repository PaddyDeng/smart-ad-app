package school.lg.overseas.school.ui.dicovery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.PhotoAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.callback.PermissionCallback;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.PhotoDialog;
import school.lg.overseas.school.utils.FileUtils;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.WaitUtils;

/**
 * 发布八卦
 */

public class ReleaseGossipActivity extends BaseActivity implements View.OnClickListener,CropperHandler {
    private ImageView back;
    private TextView title_right;
    private EditText title_et,content_et;
    private RecyclerView photo_list;
    private List<String> photoUrls;
    private List<String> imgUrl,video,audio;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_gossip);
        findView();
        CropperManager.getInstance().build(this);
        initView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        title_right.setOnClickListener(this);
    }

    private void initView() {
        GridLayoutManager manager =new GridLayoutManager(ReleaseGossipActivity.this,5);
        photo_list.setLayoutManager(manager);
        photoUrls =new ArrayList<>();
        imgUrl= new ArrayList<>();
        video=new ArrayList<>();
        audio =new ArrayList<>();
        PhotoAdapter adapter =new PhotoAdapter(ReleaseGossipActivity.this, photoUrls, new SelectListener() {
            @Override
            public void select(int position) {
                if(position==photoUrls.size()){//获取图片、拍照权限
                    getPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 300, "需要赋予拍照和相册的权限，才能获取图片哦", 1, new PermissionCallback() {
                        @Override
                        public void onSuccessful() {//获取权限成功
                            PhotoDialog dialog =new PhotoDialog(ReleaseGossipActivity.this,R.style.AlphaDialogAct);
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
                            Log.i("权限","失败");
                        }
                    });
                }else{//删除图片
                    photoUrls.remove(position);
                    photo_list.getAdapter().notifyDataSetChanged();
                    imgUrl.remove(position);
                }
            }
        });
        photo_list.setAdapter(adapter);
    }

    private void findView() {
        back = (ImageView) findViewById(R.id.back);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("发布帖子");
        title_right = (TextView) findViewById(R.id.title_right);
        title_right.setText("发布");
        title_right.setVisibility(View.VISIBLE);
        title_et = (EditText) findViewById(R.id.title_et);
        content_et = (EditText) findViewById(R.id.content_et);
        photo_list = (RecyclerView) findViewById(R.id.photo_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.title_right:
              if(!SharedPreferencesUtils.isLogin(ReleaseGossipActivity.this)){
                  LoginHelper.needLogin(ReleaseGossipActivity.this,"需要登录才能继续哦");
                  return;
                }
                if(TextUtils.isEmpty(title_et.getText())){
                    Toast.makeText(ReleaseGossipActivity.this,"请填写帖子标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(content_et.getText())){
                    Toast.makeText(ReleaseGossipActivity.this,"请填写帖子内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = title_et.getText().toString();
                String content = content_et.getText().toString();
                sendPost(title,content);
                break;
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
            Bitmap bitmap=FileUtils.comp(bm);
            String name = realFilePath.substring(realFilePath.lastIndexOf("/") + 1, realFilePath.length());
            byte[] bytes=FileUtils.getBitmapByte(bitmap);
            sendImage(new ByteArrayBinary(bytes,name),realFilePath);
        }catch (IOException e){
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

    private void sendPost(String title,String content){
        if(imgUrl.size()==0){
            Log.i("图片","没有");
        }else{
            for (String url:imgUrl) {
                Log.i("图片",url);
            }
        }
        PersonalDetail personalDetail = SharedPreferencesUtils.getPersonalDetail(ReleaseGossipActivity.this);
        String session = SharedPreferencesUtils.getSession(ReleaseGossipActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.ADDMAKEPOST, RequestMethod.POST);
        req.set("title",title).set("content",content)
                .set("video",video).set("audio",audio).set("icon",personalDetail.getImage())
                .set("publisher",null==personalDetail.getNickname()?personalDetail.getUserName():personalDetail.getNickname())
                .set("belong","3");
        for (int i = 0; i < imgUrl.size(); i++) {
            req.set("image["+i+"]",imgUrl.get(i));
        }
        req.setHeader("Cookie","PHPSESSID="+session);
        showLoadDialog();
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            Toast.makeText(ReleaseGossipActivity.this,"发帖成功",Toast.LENGTH_SHORT).show();
                            setResult(104);
                            finish();
                        }else{
                            Toast.makeText(ReleaseGossipActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });

    }

    private void sendImage(ByteArrayBinary binarys, final String path ){
            String session = SharedPreferencesUtils.getSession(ReleaseGossipActivity.this, 1);
            Request<String> req =new StringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.PHOTOUP, RequestMethod.POST);
            req.add("upload",binarys);
            req.setHeader("Cookie","PHPSESSID="+session);
            WaitUtils.show(mContext,"ReleaseGossipActivity");
            WaitUtils.setHint("ReleaseGossipActivity","上传图片中...");
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    WaitUtils.dismiss("ReleaseGossipActivity");
                    if(response.isSucceed()){
                        try {
                            PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                            if(praiseBack.getCode()==1){
                                photoUrls.add(path);
                                photo_list.getAdapter().notifyDataSetChanged();
                                imgUrl.add(praiseBack.getImage());
                            }else{
                                Toast.makeText(ReleaseGossipActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){

                        }

                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    WaitUtils.dismiss("ReleaseGossipActivity");
                }
            });
        }

}
