package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Fans;
import school.lg.overseas.school.callback.FansBack;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.mine.FansDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class FansAdapter extends RecyclerView.Adapter<FansAdapter.ViewHolder> {
    private Context context;
    private List<Fans> datas;
    private FansBack listener;
    private List<Boolean> bs;
    private int tag;
    public FansAdapter(Context context,int tag, List<Fans> datas,FansBack listener) {
        this.context = context;
        this.datas = datas;
        this.listener =listener;
        this.tag=tag;
        bs=new ArrayList<>();
    }

    @Override
    public FansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_fans,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FansAdapter.ViewHolder holder, final int position) {
        final Fans data = datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(null==data.getNickname()?data.getUsername():data.getNickname());
        if(tag!=0){
            holder.follow.setVisibility(View.GONE);
        } else {
            holder.follow.setVisibility(View.VISIBLE);
            boolean b = data.isBoolean();
            bs.add(b);
            Log.i("是否关注",data.getNickname()+":"+b);
            if (b) {
                holder.follow.setSelected(true);
                holder.follow.setText("已关注");
            } else {
                holder.follow.setSelected(false);
                holder.follow.setText("关注");
            }
            holder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bs.set(position,!bs.get(position));
                    listener.setListener(position, bs.get(position));
                    data.setBoolean(!data.isBoolean());
                    notifyDataSetChanged();
                }
            });
        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag==0)
                FansDetailActivity.start(context, data.getUid());
                else  FansDetailActivity.start(context, data.getFollowUser());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView name,follow;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            follow = (TextView) v.findViewById(R.id.follow);
            rl = v.findViewById(R.id.rl_all);
        }
    }
}
