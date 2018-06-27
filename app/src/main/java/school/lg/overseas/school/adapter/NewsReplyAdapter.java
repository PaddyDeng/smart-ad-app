package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import school.lg.overseas.school.utils.TimeUtils;

/**
 * Created by Administrator on 2018/1/30.
 */

public class NewsReplyAdapter extends RecyclerView.Adapter<NewsReplyAdapter.ViewHolder>{
    private Context context;
    private List<ReplyBean> datas;
    private SelectListener listener;

    public NewsReplyAdapter(Context context, List<ReplyBean> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public NewsReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_news_reply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsReplyAdapter.ViewHolder holder, final int position) {
        final ReplyBean data =datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSchoolResourceNormal+(TextUtils.isEmpty(data.getImage())?"":data.getImage()),holder.iv);
        holder.name.setText(TextUtils.isEmpty(data.getNickname())?data.getUsername():data.getNickname());
        holder.time.setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
        holder.content.setText(data.getContent());
        holder.iv.setOnClickListener(new View.OnClickListener() {
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

        private final ImageView iv;
        private final TextView name;
        private final TextView time;
        private final TextView content;
        private final View ll;

        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            content = (TextView) v.findViewById(R.id.content);
        }
    }
}
