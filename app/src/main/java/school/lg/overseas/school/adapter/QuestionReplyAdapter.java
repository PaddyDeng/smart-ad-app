package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Answer;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/12/22.
 */

public class QuestionReplyAdapter extends RecyclerView.Adapter<QuestionReplyAdapter.ViewHolder>{
    private Context context;
    private List<Answer> datas;
    private List<Integer> replyNums;
    private List<Boolean> isReplys;
    private SelectListener listener;

    public QuestionReplyAdapter(Context context, List<Answer> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener=listener;
        replyNums =new ArrayList<>();
        isReplys =new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            replyNums.add(Integer.valueOf(datas.get(i).getPraise()));
            isReplys.add(datas.get(i).isFabulous());
        }
    }

    @Override
    public QuestionReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_question_reply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(QuestionReplyAdapter.ViewHolder holder, final int position) {
        final Answer data = datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.portrait);
        holder.name.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
        holder.time.setText(data.getAddTime());
        holder.num.setText(replyNums.get(position)+"");
        if(null==data.getReply()||data.getReply().size()==0){
            holder.v2.setVisibility(View.GONE);
            holder.reply_item.setVisibility(View.GONE);
        }else{
            holder.v2.setVisibility(View.VISIBLE);
            holder.reply_item.setVisibility(View.VISIBLE);
            LinearLayoutManager manager =new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            holder.reply_item.setLayoutManager(manager);
            QuestionReplyItemAdapter adapter =new QuestionReplyItemAdapter(context,data.getReply());
            holder.reply_item.setAdapter(adapter);
        }
        if(isReplys.get(position)){
            holder.praise_iv.setSelected(true);
            holder.num.setTextColor(context.getResources().getColor(R.color.praise_orange));
        }else{
            holder.praise_iv.setSelected(false);
            holder.num.setTextColor(context.getResources().getColor(R.color.mainTextColor));
        }
        holder.content.loadDataWithBaseURL(null, HtmlUtil.getHtml(HtmlReplaceUtils.replaceAllToLable(data.getContent()),0),"text/html","utf-8",null);
        if(position==datas.size()-1)holder.v1.setVisibility(View.GONE);
        holder.prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPraise(data.getId(),position);
            }
        });
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.select(position);
            }
        });
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansDetailActivity.start(context,data.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView portrait,praise_iv;
        private TextView name,time,num;
        private View v1,v2,ll;
        private LinearLayout prise;
        private RecyclerView reply_item;
        private WebView content;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            portrait = (ImageView) v.findViewById(R.id.portrait);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            num = (TextView) v.findViewById(R.id.num);
            content = (WebView) v.findViewById(R.id.content);
            v1 = v.findViewById(R.id.v1);
            prise = (LinearLayout) v.findViewById(R.id.prise);
            praise_iv = (ImageView) v.findViewById(R.id.praise_iv);
            ll = v.findViewById(R.id.ll);
            v2 = v.findViewById(R.id.v2);
            reply_item = (RecyclerView) v.findViewById(R.id.reply_item);
        }
    }

    private void toPraise(String id, final int position){
        Login userInfo = SharedPreferencesUtils.getUserInfo(context);
        if(null==userInfo||TextUtils.isEmpty(userInfo.getUid())){
            LoginHelper.needLogin(context,"需要登录才能继续哦");
            return;
        }
        String session = SharedPreferencesUtils.getSession(context, 1);
        StringBuffer sb =new StringBuffer();
        sb.append(NetworkTitle.DomainSmartApplyNormal);
        if(isReplys.get(position)){
            sb.append(NetworkChildren.QUESTIONUNPRAISE);
        }else{
            sb.append(NetworkChildren.QUESTIONPRAISE);
        }
        Request<String> req = NoHttp.createStringRequest(sb.toString(), RequestMethod.POST);
        req.setHeader("Cookie","PHPSESSID="+session);
        req.set("contentId",id);
        ((BaseActivity)context).showLoadDialog();
        ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)context).dismissLoadDialog();
                if (response.isSucceed()){
                    try {
                        PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            if(isReplys.get(position)){
                                replyNums.set(position,replyNums.get(position)-1);
                            }else{
                                replyNums.set(position,replyNums.get(position)+1);
                            }
                            isReplys.set(position,!isReplys.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context,praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                        }else if(praiseBack.getCode()==0){
                            LoginHelper.needLogin(context,"登录已过期，请重新登录");
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)context).dismissLoadDialog();
            }
        });
    }
}
