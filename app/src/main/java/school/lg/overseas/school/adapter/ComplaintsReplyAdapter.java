package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.TimeUtils;

/**
 * Created by Administrator on 2018/1/24.
 */

public class ComplaintsReplyAdapter extends RecyclerView.Adapter<ComplaintsReplyAdapter.ViewHolder>{
    private Context context;
    private List<ReplyBean> datas;
    private SelectListener listener;

    public ComplaintsReplyAdapter(Context context, List<ReplyBean> datas,SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener=listener;
    }

    @Override
    public ComplaintsReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_complaints_reply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ComplaintsReplyAdapter.ViewHolder holder, final int position) {
        final ReplyBean data =datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getUserImage(),holder.portrait);
        StringBuilder sb =new StringBuilder();
        sb.append("<font color=\"#6f9701\">");
        sb.append(data.getuName());
        sb.append("</font>");
        if(!TextUtils.isEmpty(data.getReplyUserName())) {
            sb.append(" 回复 ");
            sb.append("<font color=\"#6f9701\">");
            sb.append(data.getReplyUserName());
            sb.append("</font>");
        }
        sb.append(":");
        sb.append(data.getContent());
        Log.i("测试",sb.toString());
        holder.name.setText(HtmlUtil.fromHtml(sb.toString()));
        holder.time.setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FansDetailActivity.start(context,data.getUid());
            }
        });
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.select(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final View ll;
        private ImageView portrait;
        private TextView name,time;

        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            portrait = (ImageView) v.findViewById(R.id.portrait);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            ll = v.findViewById(R.id.ll);
        }
    }
}
