package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.SchoolTest;
import school.lg.overseas.school.ui.home.SchoolEvaluationResultActivity;
import school.lg.overseas.school.utils.TimeUtils;

/**
 * Created by Administrator on 2018/1/13.
 */

public class SchoolTestAdapter extends RecyclerView.Adapter<SchoolTestAdapter.ViewHolder>{
    private Context context;
    private List<SchoolTest> datas;

    public SchoolTestAdapter(Context context, List<SchoolTest> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public SchoolTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_school_test,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SchoolTestAdapter.ViewHolder holder, int position) {
        final SchoolTest data = datas.get(position);
        holder.score.setText(data.getScore()+"分");
        holder.time.setText(TimeUtils.longToString(Long.valueOf(data.getCreateTime())*1000,"yyyy.MM.dd HH:mm"));
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchoolEvaluationResultActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView score,time;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            score = (TextView) v.findViewById(R.id.score);
            time = (TextView) v.findViewById(R.id.time);
            rl = v.findViewById(R.id.rl);
        }
    }
}
