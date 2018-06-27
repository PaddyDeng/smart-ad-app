package school.lg.overseas.school.ui.dicovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.NewsReplyAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.NewsDetail;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.ReplyDialog;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.ShareUtils01;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

import static school.lg.overseas.school.http.NetworkTitle.GOSSIPURL;

/**
 * 发现
 */

public class NewsDetailActivity extends BaseActivity {
    private ImageView back,title_right,portrait;
    private TextView title_tv,head,see_num,reply_num,time;
    private WebView web;
//    private  SwipeRefreshLayout refresh;
    private String id;
    private  NewsDetail data;
    private LinearLayout bottom_ll;
    private RecyclerView reply_list;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,NewsDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findView();
        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager manager =new LinearLayoutManager(NewsDetailActivity.this,LinearLayoutManager.VERTICAL,false);
        reply_list.setLayoutManager(manager);
        PersonalDetail personalDetail = SharedPreferencesUtils.getPersonalDetail(NewsDetailActivity.this);
        new GlideUtils().loadCircle(NewsDetailActivity.this, NetworkTitle.DomainSmartApplyResourceNormal+(null==personalDetail?"":personalDetail.getImage()),portrait);
    }

    private void initData() {
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
            showLoadDialog();
            Request<String> req = NoHttp.createStringRequest(GOSSIPURL + NetworkChildren.NEWSDETAIL, RequestMethod.POST);
            req.set("postId",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                            data = JSON.parseObject(response.get(), NewsDetail.class);
                            bottom_ll.setVisibility(View.VISIBLE);
                            title_tv.setText(data.getTitle());
                            head.setText(data.getTitle());
                            see_num.setText(data.getViewCount());
                            reply_num.setText(null==data.getReply()?"0":data.getReply().size()+"");
                            time.setText(data.getDateTime());
                            String content = data.getContent();
                            String s = HtmlUtil.repairContent(content, GOSSIPURL);
                            web.loadDataWithBaseURL(null,HtmlUtil.getHtml(s,0),"text/html","utf-8",null);
                            NewsReplyAdapter adapter =new NewsReplyAdapter(NewsDetailActivity.this, data.getReply(), new SelectListener() {
                                @Override
                                public void select(int position) {//回复二级？

                                }
                            });
                            reply_list.setAdapter(adapter);
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
//        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_right = (ImageView) findViewById(R.id.title_right);
        head = (TextView) findViewById(R.id.head);
        see_num = (TextView) findViewById(R.id.see_num);
        reply_num = (TextView) findViewById(R.id.reply_num);
        time = (TextView) findViewById(R.id.time);
        web = (WebView) findViewById(R.id.web);
        portrait = (ImageView) findViewById(R.id.portrait);
        reply_list = (RecyclerView) findViewById(R.id.reply_list);
        bottom_ll = (LinearLayout) findViewById(R.id.bottom_ll);
        bottom_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyDialog dialog =new ReplyDialog(NewsDetailActivity.this);
                dialog.show();
                dialog.setListener(new ReplyListener() {
                    @Override
                    public void setListener(String s) {
                        String sessionId=SharedPreferencesUtils.getSession(NewsDetailActivity.this,1);
                        if(TextUtils.isEmpty(sessionId)){
                            LoginHelper.needLogin(NewsDetailActivity.this,"需要登录才能回复哦");
                            return;
                        }
                        showLoadDialog();
                        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.NEWSREPLY, RequestMethod.POST);
                        req.set("content",s).set("postId",id);
                        req.setHeader("Cookie","PHPSESSID="+sessionId);
                        request(0, req, new SimpleResponseListener<String>() {
                                @Override
                                public void onSucceed(int what, Response<String> response) {
                                    dismissLoadDialog();
                                    if(response.isSucceed()){
                                        try {
                                            PraiseBack praiseBack =JSON.parseObject(response.get(), PraiseBack.class);
                                            if(praiseBack.getCode()==1){
                                                Toast.makeText(NewsDetailActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                                                initData();
                                            }else{
                                                Toast.makeText(NewsDetailActivity.this,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
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
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData();
//                refresh.setRefreshing(false);
//            }
//        });

        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ScreenShotUtil.shoot(NewsDetailActivity.this);
//                ShareDialog dialog =new ShareDialog(NewsDetailActivity.this);
//                dialog.show();
//                dialog.setTag(1,data.getTitle(),"http://bbs.viplgw.cn/post/details/"+data.getId()+".html");
                ShareUtils01.toShare(NewsDetailActivity.this,"http://bbs.viplgw.cn/post/details/"+data.getId()+".html",data.getTitle());
            }
        });
    }

}
