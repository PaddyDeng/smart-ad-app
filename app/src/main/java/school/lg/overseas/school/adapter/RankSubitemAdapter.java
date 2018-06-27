package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.RankingSubItemData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RankSubitemAdapter extends RecyclerView.Adapter<RankSubitemAdapter.ViewHolder>{
    private Context context;
    private List<RankingSubItemData> datas;

    public RankSubitemAdapter(Context context, List<RankingSubItemData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RankSubitemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_rank_subitem,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankSubitemAdapter.ViewHolder holder, int position) {
        final RankingSubItemData data = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.DomainSchoolResourceNormal+data.getImage(),holder.iv);
        holder.cn_name.setText(data.getChineseName());
        holder.en_name.setText(data.getEnglishName());
        holder.rank_tv.setText(data.getRanking());
        holder.hot.setText(Integer.valueOf(data.getId())*10/3+5+"");
        holder.comment.setText(Integer.valueOf(data.getId())*3/2+10+"");
        if(Integer.valueOf(data.getRanking())<4){
            holder.rank_tv.setSelected(true);
            holder.rank_tv.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.rank_tv.setSelected(false);
            holder.rank_tv.setTextColor(context.getResources().getColor(R.color.dark_line));
        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, SchoolDetailsActivity.class);
                intent.putExtra("id",data.getRelationId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView cn_name,en_name,hot,comment,rank_tv;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            cn_name = (TextView) v.findViewById(R.id.cn_name);
            en_name = (TextView) v.findViewById(R.id.en_name);
            hot = (TextView) v.findViewById(R.id.hot);
            comment = (TextView) v.findViewById(R.id.comment);
            rank_tv = (TextView) v.findViewById(R.id.rank_tv);
        }
    }
}
