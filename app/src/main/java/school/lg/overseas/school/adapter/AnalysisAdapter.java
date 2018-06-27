package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.SchoolEvaluationResult;
import school.lg.overseas.school.bean.SchoolEvaluationWork;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;

/**
 * Created by Administrator on 2018/1/12.
 */

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.ViewHolder>{
    private Context context;
    private List<SchoolEvaluationWork> datas;

    public AnalysisAdapter(Context context, List<SchoolEvaluationWork> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public AnalysisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_analysis,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AnalysisAdapter.ViewHolder holder, int position) {
        final SchoolEvaluationWork data = datas.get(position);
        if(data.getType()==1){
            holder.iv.setBackgroundResource(R.drawable.green_side);
            holder.iv.setTextColor(context.getResources().getColor(R.color.mainGreen));
            holder.iv.setText("优势");
            holder.num.setTextColor(context.getResources().getColor(R.color.mainGreen));
        }else{
            holder.iv.setBackgroundResource(R.drawable.red_side);
            holder.iv.setTextColor(context.getResources().getColor(R.color.shallow_red));
            holder.iv.setText("劣势");
            holder.num.setTextColor(context.getResources().getColor(R.color.shallow_red));
        }
        String titles="";
        String scores ="";
        switch (data.getTag()){
            case 0:
                titles="GPA:";
                scores=data.getScore();
                break;
            case 1:
                titles="GMAT/GRE:";
                scores=data.getScore();
                break;
            case 2:
                titles="TOEFL/IELTS:";
                scores=data.getScore();
                break;
            case 3:
                titles="院校背景:";
                scores=data.getName();
                break;
            case 4:
                titles="个人经历:";
                scores=data.getName();
                break;
        }
        holder.title.setText(titles);
        holder.score.setText(scores);
        holder.num.setText(data.getNum()+"");
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView iv,title,score,num;
        private RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (TextView) v.findViewById(R.id.iv);
            title = (TextView) v.findViewById(R.id.title);
            score = (TextView) v.findViewById(R.id.score);
            num = (TextView) v.findViewById(R.id.num_t);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
        }
    }
}
