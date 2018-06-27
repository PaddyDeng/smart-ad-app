package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.RemarkData;
import school.lg.overseas.school.callback.RecyclerSelectListener01;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.ComplaintsDetailActivity;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.ui.other.ImgActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.TimeUtils;
import school.lg.overseas.school.view.MultiImage.MultiImageView;
import school.lg.overseas.school.view.MultiImage.PhotoInfo;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder>{
    private Context context;
    private List<RemarkData> datas;
    private int type;
    private boolean b;
    private RecyclerSelectListener01 listener;

    public ComplaintsAdapter(Context context, List<RemarkData> datas,RecyclerSelectListener01 listener) {
        this.context = context;
        this.datas = datas;
        this.listener =listener;
    }

    @Override
    public ComplaintsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==1){
            v = LayoutInflater.from(context).inflate(R.layout.remark_replay_item_layout,parent,false);
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.item_complaints,parent,false);
        }
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ComplaintsAdapter.ViewHolder holder, final int position) {
        final int titleP=position;
        final RemarkData data = datas.get(position);
        b=data.isLikeId();
        if(type==1){
            holder.msgNum.setText(context.getString(R.string.str_remark_reply, data.getRemarkNum()));
            holder.msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去新消息页

                }
            });
        }else{
            new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getIcon(),holder.head);
            holder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FansDetailActivity.start(context,data.getUid());
                }
            });

            holder.name.setText(data.getPublisher());
            holder.time.setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
            if(!TextUtils.isEmpty(data.getUid())&&TextUtils.equals(data.getUid(), SharedPreferencesUtils.getUserInfo(context).getUid())){
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//删帖
                            String session = SharedPreferencesUtils.getSession(context, 1);
                            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.DELETEMAKEPOST, RequestMethod.GET);
                            req.set("gossipId",data.getId());
                            req.setHeader("Cookie","PHPSESSID="+session);
                            ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
                                @Override
                                public void onSucceed(int what, Response<String> response) {
                                    if(response.isSucceed()){
                                        try {
                                            PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                            if(praiseBack.getCode()==1){
                                                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                                datas.remove(position);
                                                notifyDataSetChanged();
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
                holder.delete.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(data.getTitle())){
                holder.remarkTitle.setVisibility(View.GONE);
            }else{
                holder.remarkTitle.setVisibility(View.VISIBLE);
                holder.remarkTitle.setText(data.getTitle());
                holder.remarkTitle.setMaxLines(1);
                holder.remarkTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
            if (TextUtils.isEmpty(data.getContent())) {
                holder.remarkContent.setVisibility(View.GONE);
            } else {
                holder.remarkContent.setText(data.getContent().replace("&nbsp;"," "));
                holder.remarkContent.setMaxLines(3);
                holder.remarkContent.setEllipsize(TextUtils.TruncateAt.END);
                holder.remarkContent.setVisibility(View.VISIBLE);
            }
            holder.write.setText(String.valueOf(data.getReply().size()));
            holder.write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//弹出输入框
                    listener.setListener(position,-1);
                }
            });
            holder.praise.setText(data.getLikeNum());
            if(b) {
                holder.praise.setSelected(true);
                holder.praise.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                holder.praise.setSelected(false);
                holder.praise.setTextColor(context.getResources().getColor(R.color.mainTextColor));
            }
            holder.praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String session = SharedPreferencesUtils.getSession(context, 1);
                    if(TextUtils.isEmpty(session)){
                        LoginHelper.needLogin(context,"需要登录才可以继续哦");
                        return;
                    }
                    ((BaseActivity)context).showLoadDialog();
                    Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.COMPLAINTSPRAISE, RequestMethod.POST);
                    req.set("gossipId",data.getId()).set("belong","14");
                    req.setHeader("Cookie","PHPSESSID="+session);
                    ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            ((BaseActivity)context).dismissLoadDialog();
                            if(response.isSucceed()){
                                try {
                                    PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                                    if(praiseBack.getCode()==1){
                                        holder.praise.setText(praiseBack.getLikeNum());
                                        if(data.isLikeId()) {
                                            b=false;
                                            holder.praise.setTextColor(context.getResources().getColor(R.color.mainTextColor));
                                        }else{
                                            b=true;
                                            holder.praise.setTextColor(context.getResources().getColor(R.color.red));
                                        }
                                        holder.praise.setSelected(b);
                                        data.setLikeId(b);
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
                holder.multiImg.setVisibility(View.VISIBLE);
                final List<PhotoInfo> photos =new ArrayList<>();
                for (String url : images) {
                    PhotoInfo pInfo = new PhotoInfo();
                    pInfo.url = NetworkTitle.GOSSIPURL + url;
                    photos.add(pInfo);
                }
                holder.multiImg.setList(photos);
                holder.multiImg.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {//放大图片
                        ImgActivity.start(context,photos.get(position).getUrl());
                    }
                });
            }else{
                holder.multiImg.setVisibility(View.GONE);
            }
            if(null==data.getReply()){
                holder.line.setVisibility(View.GONE);
            }else{
                holder.line.setVisibility(View.VISIBLE);
                LinearLayoutManager manager =new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                holder.list.setLayoutManager(manager);
                ReplyAdapter replyAdapter =new ReplyAdapter(context, data.getReply(), new SelectListener() {
                    @Override
                    public void select(int position) {//回复
                        listener.setListener(titleP,position);
                    }
                });
                holder.list.setAdapter(replyAdapter);
                if(data.getReply().size()>5){
                    holder.more.setText("查看剩余"+(data.getReply().size()-5)+"条评论");
                    holder.more.setVisibility(View.VISIBLE);
                }else{
                    holder.more.setVisibility(View.GONE);
                }
                holder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//跳转详情
                        ComplaintsDetailActivity.start(context,data.getId());
                    }
                });
//                holder.list.setVisibility(View.VISIBLE);
//                holder.list.setDatas(data.getReply());
//                holder.list.setOnItemClickListener(new CommentListView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int position) {
//                        if(TextUtils.equals(data.getUid(), SharedPreferencesUtils.getUserInfo(context).getUid())){
//                            Toast.makeText(context,"不能回复自己",Toast.LENGTH_SHORT).show();
//                        }else{//弹出输入框
//
//                        }
//                    }
//                });
            }
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComplaintsDetailActivity.start(context,data.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView msgNum,remarkTitle,remarkContent,name,time,write,praise,more;
        private RelativeLayout delete;
        private MultiImageView multiImg;
        private ImageView head;
        private View msg,line;
        private RecyclerView list;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            if (type==1){
                msgNum = (TextView) v.findViewById(R.id.remakr_list_item_reply);
                msg = v.findViewById(R.id.remark_new_msg_container);
            }else{
                remarkTitle = (TextView) v.findViewById(R.id.remark_title);
                remarkContent = (TextView) v.findViewById(R.id.remark_content);
                head = (ImageView) v.findViewById(R.id.remark_head_img);
                name = (TextView) v.findViewById(R.id.remark_user_name);
                time = (TextView) v.findViewById(R.id.remark_time);
                delete = (RelativeLayout) v.findViewById(R.id.delete);
                write = (TextView) v.findViewById(R.id.remark_comm_write);
                praise = (TextView) v.findViewById(R.id.remark_comm_praise);
                multiImg = (MultiImageView) v.findViewById(R.id.remark_multiImagView);
                list = (RecyclerView) v.findViewById(R.id.list);
                line = v.findViewById(R.id.remark_conn_line);
                more = (TextView) v.findViewById(R.id.more);
                ll = v.findViewById(R.id.ll);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && !TextUtils.isEmpty(datas.get(position).getRemarkNum())) {
            type=1;
            return 1;
        }else{
            type=2;
            return 2;
        }
    }
}
