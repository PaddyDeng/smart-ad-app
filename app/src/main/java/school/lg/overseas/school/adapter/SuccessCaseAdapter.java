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
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * 案例适配器
 */

public class SuccessCaseAdapter extends RecyclerView.Adapter<SuccessCaseAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;
    public SuccessCaseAdapter(Context context, List<LittleData> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public SuccessCaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_hotcase,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SuccessCaseAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        if(position==datas.size()-1)holder.v1.setVisibility(View.GONE);
        else holder.v1.setVisibility(View.VISIBLE);
        new GlideUtils().load(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(data.getCnName());
        holder.school.setText(data.getProblemComplement());
        holder.finish_school.setText(data.getNumbering());
        holder.condition.setText(data.getSentenceNumber());
        holder.major.setText(data.getArticle());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.start(context,data.getId(),0,"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView name,school,finish_school,condition,major;
        private View v1,rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            school = (TextView) v.findViewById(R.id.school);
            finish_school = (TextView) v.findViewById(R.id.finish_school);
            condition = (TextView) v.findViewById(R.id.condition);
            major = (TextView) v.findViewById(R.id.major);
            v1 = v.findViewById(R.id.v);
        }
    }
}
