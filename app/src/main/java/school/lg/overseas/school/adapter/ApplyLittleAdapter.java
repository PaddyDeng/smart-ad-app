package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.ApplyChildData;
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.ui.other.KnowledgeDetailActivity;

/**
 * Created by Administrator on 2018/1/9.
 */

public class ApplyLittleAdapter extends RecyclerView.Adapter<ApplyLittleAdapter.ViewHolder>{
    private Context context;
    private List<ApplyChildData> datas;

    public ApplyLittleAdapter(Context context, List<ApplyChildData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ApplyLittleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_apply_little,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ApplyLittleAdapter.ViewHolder holder, int position) {
        if(position==datas.size()-1) holder.v1.setVisibility(View.GONE);
        final ApplyChildData data = datas.get(position);
        holder.name.setText(data.getName());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KnowledgeDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private View ll,v1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            name = (TextView) v.findViewById(R.id.name);
            ll = v.findViewById(R.id.ll);
            v1 = v.findViewById(R.id.v1);
        }
    }
}
