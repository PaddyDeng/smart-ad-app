package school.lg.overseas.school.ui.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.QuestionReplyAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Answer;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.QuestionData;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.manager.AutoLinearLayoutManager;
import school.lg.overseas.school.ui.dicovery.ComplaintsDetailActivity;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.ui.other.ReplyDialog;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 问答详情
 */

public class QuestionDetailActivity extends BaseActivity{
    private SwipeRefreshLayout refresh;
    private ImageView portrait,back,collection,reply_top;
    private TextView name,time,title,content,follow_num,answer_num,title_tv;
    private View main_content;
    private LinearLayout lable_ll;
    private RecyclerView item_list;
    private String id;
    private boolean isCollection;
    private QuestionData data;
    private String replyUser="",type="2",replyUid="";
    private String qid;
    public static void start(Context context,String id){
        Intent intent =new Intent(context,QuestionDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        getArgs();
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansDetailActivity.start(QuestionDetailActivity.this,data.getUserId());
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCollection){
                    PracticeManager.getInstance().deleteOne(1,2,data.getId());
                    collection.setSelected(false);
                    isCollection=false;
                }else{
                    Reading reading =new Reading(1,2,data.getId(),data.getQuestion());
                    PracticeManager.getInstance().insert(reading);
                    isCollection=true;
                    collection.setSelected(true);
                }
            }
        });
        reply_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyUser="";type="2";replyUid="";
                qid=id;
                ReplyDialog dialog = new ReplyDialog(QuestionDetailActivity.this);
                dialog.show();
                dialog.setListener(new ReplyListener() {
                    @Override
                    public void setListener(String s) {
                        toReply(s);
                    }
                });
                dialog.setHint("我来回答");
            }
        });
        main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyUser="";type="2";replyUid="";
                qid=id;
                ReplyDialog dialog = new ReplyDialog(QuestionDetailActivity.this);
                dialog.show();
                dialog.setListener(new ReplyListener() {
                    @Override
                    public void setListener(String s) {
                        toReply(s);
                    }
                });
                dialog.setHint("我来回答");
            }
        });
    }

    private void getArgs() {
        if(null!=getIntent()){
            id=getIntent().getStringExtra("id");
            qid=id;
        }
    }

    private void initData() {
        showLoadDialog();
        String session = SharedPreferencesUtils.getSession(QuestionDetailActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.QUESTIONDETAIL, RequestMethod.POST);
        req.setHeader("Cookie","PHPSESSID="+session);
        req.set("id",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        data = JSON.parseObject(response.get(), QuestionData.class);
                        List<Reading> readings = PracticeManager.getInstance().queryForId(1, 2, data.getId());
                        if(null!=readings&&readings.size()!=0){
                            collection.setSelected(true);
                            isCollection=true;
                        }else{
                            collection.setSelected(false);
                            isCollection=false;
                        }
                        Reading reading =new Reading();
                        reading.setId(data.getId());
                        reading.setTag(0);
                        reading.setType(2);
                        reading.setTitle(data.getQuestion());
                        PracticeManager.getInstance().insert(reading);
                        new GlideUtils().loadCircle(QuestionDetailActivity.this,NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),portrait);
                        name.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
                        time.setText(data.getAddTime());
                        title.setText(data.getQuestion());
                        title_tv.setText(data.getQuestion());
                        content.setText(HtmlReplaceUtils.replaceAllToLable(data.getContent()));
                        lable_ll.removeAllViews();
                        for (int i = 0; i < data.getTags().size(); i++) {
                            TextView tv =new TextView(QuestionDetailActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            params.rightMargin=15;
                            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.lable_back_false));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(10);
                            tv.setText("  "+data.getTags().get(i).getName()+"  ");
                            lable_ll.addView(tv,params);
                        }
                        follow_num.setText(data.getBrowse());
                        answer_num.setText(data.getAnswer().size()+"");
                        AutoLinearLayoutManager manager =new AutoLinearLayoutManager(QuestionDetailActivity.this,LinearLayoutManager.VERTICAL,false);
                        item_list.setNestedScrollingEnabled(false);
                        item_list.setLayoutManager(manager);
                        QuestionReplyAdapter adapter =new QuestionReplyAdapter(QuestionDetailActivity.this,data.getAnswer(),new SelectListener(){

                            @Override
                            public void select(int position) {
                                Answer answer = data.getAnswer().get(position);
                                replyUid=answer.getUserId();
                                replyUser=(TextUtils.isEmpty(answer.getNickname())?answer.getUsername():answer.getNickname());
                                type="3";
                                qid=answer.getId();
                                ReplyDialog dialog =new ReplyDialog(QuestionDetailActivity.this);
                                dialog.show();
                                dialog.setListener(new ReplyListener() {
                                    @Override
                                    public void setListener(String s) {
                                        toReply(s);
                                    }
                                });
                                dialog.setHint("回复 "+(TextUtils.isEmpty(answer.getNickname())?answer.getUsername():answer.getNickname())+" 的回答");
                            }
                        });
                        item_list.setAdapter(adapter);
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

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        main_content = findViewById(R.id.main_content);
        portrait = (ImageView) main_content.findViewById(R.id.portrait);
        name = (TextView) main_content.findViewById(R.id.name);
        time = (TextView) main_content.findViewById(R.id.time);
        title = (TextView) main_content.findViewById(R.id.title);
        content = (TextView) main_content.findViewById(R.id.content);
        lable_ll = (LinearLayout) main_content.findViewById(R.id.lable_ll);
        follow_num = (TextView) main_content.findViewById(R.id.follow_num);
        answer_num = (TextView) main_content.findViewById(R.id.answer_num);
        View v1 = main_content.findViewById(R.id.v1);
        v1.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.back);
        title_tv = (TextView) findViewById(R.id.title_tv);
        collection = (ImageView) findViewById(R.id.collection);
        reply_top = (ImageView) findViewById(R.id.reply_top);
        item_list = (RecyclerView) findViewById(R.id.item_list);
    }

    private void toReply(String contents){
        Login userInfo = SharedPreferencesUtils.getUserInfo(QuestionDetailActivity.this);
        if(null==userInfo||TextUtils.isEmpty(userInfo.getUid())){
            LoginHelper.needLogin(QuestionDetailActivity.this,"需要登录才能回复哦");
            return;
        }
        String session = SharedPreferencesUtils.getSession(QuestionDetailActivity.this, 1);
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.QUESTIONREPLY, RequestMethod.POST);
        req.setHeader("Cookie","PHPSESSID="+session);
        req.set("questionId",qid).set("contents",contents).set("replyUser",replyUser).set("type",type).set("replyUid",replyUid);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            initData();
                        }else{

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
