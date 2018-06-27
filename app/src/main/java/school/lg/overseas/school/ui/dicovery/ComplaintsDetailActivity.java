package school.lg.overseas.school.ui.dicovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.ComplaintsReplyAdapter;
import school.lg.overseas.school.adapter.ReplyAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.RemarkData;
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.ui.other.ImgActivity;
import school.lg.overseas.school.ui.other.ReplyDialog;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.SpField;
import school.lg.overseas.school.utils.TimeUtils;
import school.lg.overseas.school.utils.Utils;
import school.lg.overseas.school.view.MultiImage.MultiImageView;
import school.lg.overseas.school.view.MultiImage.PhotoInfo;

/**
 * Created by Administrator on 2018/1/17.
 */

public class ComplaintsDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView remarkTitle,remarkContent,name,time,write,praise,title_tv;
    private RelativeLayout delete;
    private MultiImageView multiImg;
    private ImageView head,back;
    private  SwipeRefreshLayout refresh;
    private View line;
    private RecyclerView list;
    private boolean b;
    private String id;
    private RemarkData data;
    private PersonalDetail personalDetail;
    private String replyUser="",replyUserName="",type="";

    public static void start(Context context,String id){
        Intent intent =new Intent(context,ComplaintsDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintsdetail);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        head.setOnClickListener(this);
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
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            personalDetail = SharedPreferencesUtils.getPersonalDetail(ComplaintsDetailActivity.this);
            showLoadDialog();
            final Request<String> req = NoHttp.createStringRequest(NetworkTitle.GOSSIPURL + NetworkChildren.GOSSIPDETAIL, RequestMethod.POST);
            req.set("gossipId",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                        data = JSON.parseObject(response.get(), RemarkData.class);
                        b=data.isLikeId();
                        new GlideUtils().loadCircle(ComplaintsDetailActivity.this, NetworkTitle.DomainSmartApplyResourceNormal+data.getIcon(),head);
                        name.setText(data.getPublisher());
                        time.setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
                        if(!TextUtils.isEmpty(data.getUid())&&TextUtils.equals(data.getUid(), SharedPreferencesUtils.getUserInfo(ComplaintsDetailActivity.this).getUid())){
                            delete.setVisibility(View.VISIBLE);
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {//删帖
                                    String session = SharedPreferencesUtils.getSession(ComplaintsDetailActivity.this, 1);
                                    Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.DELETEMAKEPOST, RequestMethod.GET);
                                    req.set("gossipId",data.getId());
                                    req.setHeader("Cookie","PHPSESSID="+session);
                                    request(0, req, new SimpleResponseListener<String>() {
                                        @Override
                                        public void onSucceed(int what, Response<String> response) {
                                            if(response.isSucceed()){
                                                try {
                                                    PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                                    if(praiseBack.getCode()==1){
                                                        SharedPreferencesUtils.setDelete(ComplaintsDetailActivity.this, SpField.complaints,true);
                                                        Toast.makeText(ComplaintsDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }catch (JSONException e){

                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailed(int what, Response<String> response) {
                                            super.onFailed(what, response);
                                        }
                                    });
                                }
                            });
                        }else {
                            delete.setVisibility(View.GONE);
                        }
                        if(TextUtils.isEmpty(data.getTitle())){
                            remarkTitle.setVisibility(View.GONE);
                        }else{
                            title_tv.setText(data.getTitle());
                            remarkTitle.setVisibility(View.VISIBLE);
                            remarkTitle.setText(data.getTitle());
                            remarkTitle.setMaxLines(1);
                            remarkTitle.setEllipsize(TextUtils.TruncateAt.END);
                        }
                        if (TextUtils.isEmpty(data.getContent())) {
                            remarkContent.setVisibility(View.GONE);
                        } else {
                            remarkContent.setText(data.getContent().replace("&nbsp;"," "));
                            remarkContent.setMaxLines(3);
                            remarkContent.setEllipsize(TextUtils.TruncateAt.END);
                            remarkContent.setVisibility(View.VISIBLE);
                        }
                        write.setText(String.valueOf(data.getReply().size()));
                        write.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//弹出输入框
                                type="1";
                                replyUser="";
                                replyUserName="";
                                ReplyDialog dialog =new ReplyDialog(ComplaintsDetailActivity.this);
                                dialog.show();
                                dialog.setListener(new ReplyListener() {
                                    @Override
                                    public void setListener(String s) {
                                        send(s);
                                    }
                                });
                            }
                        });
                        praise.setText(data.getLikeNum());
                        if(b) {
                            praise.setSelected(true);
                            praise.setTextColor(ComplaintsDetailActivity.this.getResources().getColor(R.color.red));
                        }else{
                            praise.setSelected(false);
                            praise.setTextColor(ComplaintsDetailActivity.this.getResources().getColor(R.color.mainTextColor));
                        }
                        praise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String session = SharedPreferencesUtils.getSession(ComplaintsDetailActivity.this, 1);
                                if(TextUtils.isEmpty(session)){
                                    LoginHelper.needLogin(ComplaintsDetailActivity.this,"需要登录才可以继续哦");
                                    return;
                                }
                                showLoadDialog();
                                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.COMPLAINTSPRAISE, RequestMethod.POST);
                                req.set("gossipId",data.getId()).set("belong","14");
                                req.setHeader("Cookie","PHPSESSID="+session);
                                request(0, req, new SimpleResponseListener<String>() {
                                    @Override
                                    public void onSucceed(int what, Response<String> response) {
                                        dismissLoadDialog();
                                        if(response.isSucceed()){
                                            try {
                                                PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                                if(praiseBack.getCode()==1){
                                                    praise.setText(praiseBack.getLikeNum());
                                                    if(b) {
                                                        b=false;
                                                        praise.setTextColor(ComplaintsDetailActivity.this.getResources().getColor(R.color.mainTextColor));
                                                    }else{
                                                        b=true;
                                                        praise.setTextColor(ComplaintsDetailActivity.this.getResources().getColor(R.color.red));
                                                    }
                                                    praise.setSelected(b);
                                                    data.setLikeId(b);
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
                        });
                        Object obj = data.getImage();
                        List<String> images = new ArrayList<>();
                        if (obj != null) {
                            if (obj instanceof String) {
                                images.add((String) obj);
                            } else if (obj instanceof List) {
                                images.addAll((List<String>) obj);
                            }
                        }
                        if(null!=images&&!images.isEmpty()){
                            multiImg.setVisibility(View.VISIBLE);
                            final List<PhotoInfo> photos =new ArrayList<>();
                            for (String url : images) {
                                PhotoInfo pInfo = new PhotoInfo();
                                pInfo.url = NetworkTitle.GOSSIPURL + url;
                                photos.add(pInfo);
                            }
                            multiImg.setList(photos);
                            multiImg.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {//放大图片
                                    ImgActivity.start(ComplaintsDetailActivity.this,photos.get(position).getUrl());
                                }
                            });
                        }else{
                            multiImg.setVisibility(View.GONE);
                        }
                        if(null==data.getReply()){
                            line.setVisibility(View.GONE);
                        }else {
                            line.setVisibility(View.VISIBLE);
                            LinearLayoutManager manager = new LinearLayoutManager(ComplaintsDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                            list.setLayoutManager(manager);
                            ComplaintsReplyAdapter replyAdapter =new ComplaintsReplyAdapter(ComplaintsDetailActivity.this, data.getReply(), new SelectListener() {
                                @Override
                                public void select(int position) {
                                    ReplyBean replyBean = data.getReply().get(position);
                                    type="2";
                                    replyUser=replyBean.getUid();
                                    replyUserName=replyBean.getuName();
                                    ReplyDialog dialog =new ReplyDialog(ComplaintsDetailActivity.this);
                                    dialog.show();
                                    dialog.setListener(new ReplyListener() {
                                        @Override
                                        public void setListener(String s) {
                                            send(s);
                                        }
                                    });
                                    dialog.setHint("@"+replyUserName+":");
                                }
                            });
                            list.setAdapter(replyAdapter);
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
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        remarkTitle = (TextView) findViewById(R.id.remark_title);
        remarkContent = (TextView) findViewById(R.id.remark_content);
        head = (ImageView) findViewById(R.id.remark_head_img);
        name = (TextView) findViewById(R.id.remark_user_name);
        time = (TextView) findViewById(R.id.remark_time);
        delete = (RelativeLayout) findViewById(R.id.delete);
        write = (TextView) findViewById(R.id.remark_comm_write);
        praise = (TextView) findViewById(R.id.remark_comm_praise);
        multiImg = (MultiImageView) findViewById(R.id.remark_multiImagView);
        list = (RecyclerView) findViewById(R.id.list);
        line = findViewById(R.id.remark_conn_line);
    }

    private void send(String s){
            String uName;
            if(TextUtils.isEmpty(personalDetail.getNickname())){
                uName=personalDetail.getUserName();
            }else{
                uName= personalDetail.getNickname();
            }
            String session = SharedPreferencesUtils.getSession(ComplaintsDetailActivity.this, 1);
            if(TextUtils.isEmpty(session)){
                LoginHelper.needLogin(ComplaintsDetailActivity.this,"需要先登录哦");
                return;
            }
            showLoadDialog();
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.REPLY, RequestMethod.POST);
            req.set("content",s).set("type",type).set("id",id).set("gossipUser",data.getUid())
                    .set("uName",uName).set("userImage",personalDetail.getImage())
                    .set("replyUser",replyUser).set("replyUserName",replyUserName)
                    .set("belong","1");
            req.setHeader("Cookie","PHPSESSID="+session);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                            PraiseBack praiseBack =JSON.parseObject(response.get(),PraiseBack.class);
                            if(praiseBack.getCode()==1) {
                                initData();
                            }else{
                                LoginHelper.needLogin(ComplaintsDetailActivity.this,"登录已过期，请重新登录");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.remark_head_img:
                FansDetailActivity.start(ComplaintsDetailActivity.this,data.getUid());
                break;
        }
    }

}
