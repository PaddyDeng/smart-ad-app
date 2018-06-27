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
import school.lg.overseas.school.bean.SchoolEvaluationRes;
import school.lg.overseas.school.bean.SchoolEvaluationResult;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/12.
 */

public class SchoolRecommendAdapter extends RecyclerView.Adapter<SchoolRecommendAdapter.ViewHolder>{
    private Context context;
    private List<SchoolEvaluationRes> datas;

    public SchoolRecommendAdapter(Context context, List<SchoolEvaluationRes> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public SchoolRecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_school_recommend,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SchoolRecommendAdapter.ViewHolder holder, int position) {
        final SchoolEvaluationRes data = datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSchoolResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(data.getName());
        holder.en_name.setText(data.getTitle());
        holder.address.setText(data.getPlace());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, SchoolDetailsActivity.class);
                intent.putExtra("id",data.getId());
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
        private TextView name,en_name,address;
        private  View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            en_name = (TextView) v.findViewById(R.id.en_name);
            address = (TextView) v.findViewById(R.id.address);
            rl = v.findViewById(R.id.rl);
        }
    }
}
