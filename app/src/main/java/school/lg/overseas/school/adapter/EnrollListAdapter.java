package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Enroll;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.utils.TimeUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class EnrollListAdapter extends RecyclerView.Adapter<EnrollListAdapter.ViewHolder>{
    private Context context;
    private List<Enroll> datas;
    private SelectListener listener;

    public EnrollListAdapter(Context context, List<Enroll> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public EnrollListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_enroll,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EnrollListAdapter.ViewHolder holder, final int position) {
        Enroll data = datas.get(position);
        holder.school.setText("大学:"+data.getSchool());
        holder.major.setText("专业:"+data.getMajor());
        holder.proportion.setText(data.getPercent()+"%");
        holder.time.setText(TimeUtils.longToString(Long.valueOf(data.getCreateTime())*1000,"yyyy.MM.dd HH:mm"));
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
        private TextView school,major,proportion,time;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            school = (TextView) v.findViewById(R.id.school);
            major = (TextView) v.findViewById(R.id.major);
            proportion = (TextView) v.findViewById(R.id.proportion);
            time = (TextView) v.findViewById(R.id.time);
            ll = v.findViewById(R.id.ll);
        }
    }
}
