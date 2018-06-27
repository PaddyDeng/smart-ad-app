package school.lg.overseas.school.adapter;

import android.content.Context;
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
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.ActDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/3.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;

    public ActivityAdapter(Context context, List<LittleData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_activity,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.OPEN+data.getImage(),holder.iv);
        holder.name.setText(data.getName());
        holder.time.setText("开课时间:"+data.getCnName());
        holder.time_length.setText(data.getProblemComplement());
        holder.fabulous_num.setText(data.getViewCount());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View rl;
        private ImageView iv;
        private TextView name,time_length,fabulous_num,time;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            time_length = (TextView) v.findViewById(R.id.time_length);
            fabulous_num = (TextView) v.findViewById(R.id.fabulous_num);
            time = (TextView) v.findViewById(R.id.time);
        }
    }
}
