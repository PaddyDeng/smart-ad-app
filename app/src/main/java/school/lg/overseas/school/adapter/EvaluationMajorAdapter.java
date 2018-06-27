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
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/1/14.
 */

public class EvaluationMajorAdapter extends RecyclerView.Adapter<EvaluationMajorAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchoolLitle> datas;
    private SelectListener listener;

    public EvaluationMajorAdapter(Context context, List<SearchSchoolLitle> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public EvaluationMajorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_choose_major,viewGroup,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EvaluationMajorAdapter.ViewHolder holder, final int i) {
        SearchSchoolLitle data = datas.get(i);
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.select(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View rl,v1;
        private TextView tv;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            tv = (TextView) v.findViewById(R.id.tv);
            v1 = v.findViewById(R.id.v1);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
