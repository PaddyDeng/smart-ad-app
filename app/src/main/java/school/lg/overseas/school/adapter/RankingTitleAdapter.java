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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.ui.home.RankingSubitemActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RankingTitleAdapter extends RecyclerView.Adapter<RankingTitleAdapter.ViewHolder>{
    private Context context;
    private List<Apply> datas;

    public RankingTitleAdapter(Context context, List<Apply> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RankingTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_ranking_title,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingTitleAdapter.ViewHolder holder, int position) {
        final Apply data = datas.get(position);
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingSubitemActivity.start(context,data.getId(),data.getName());
            }
        });
        switch (data.getId()){
            case "296":
                new GlideUtils().load(context,R.mipmap.rank_us,holder.iv);
                break;
            case "293":
                new GlideUtils().load(context,R.mipmap.rank_qs,holder.iv);
                break;
            case "330":
                new GlideUtils().load(context,R.mipmap.rank_arwu,holder.iv);
                break;
            case "295":
                new GlideUtils().load(context,R.mipmap.rank_times,holder.iv);
                break;
            case "297":
                new GlideUtils().load(context,R.mipmap.rank_times,holder.iv);
                break;
            case "294":
                new GlideUtils().load(context,R.mipmap.rank_macleans,holder.iv);
                break;
            case "312":
                new GlideUtils().load(context,R.mipmap.rank_macleans,holder.iv);
                break;
            default:
                new GlideUtils().load(context,R.mipmap.rank_default,holder.iv);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View rl;
        private ImageView iv;
        private TextView tv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
