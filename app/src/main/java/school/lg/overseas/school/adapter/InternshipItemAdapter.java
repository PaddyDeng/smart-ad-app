package school.lg.overseas.school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;
import java.util.Random;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.ApplyChildData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.RandomColorUtils;

/**
 * Created by Administrator on 2017/12/22.
 */

public class InternshipItemAdapter extends RecyclerView.Adapter<InternshipItemAdapter.ViewHolder>{
    private Context context;
    private List<ApplyChildData> datas;

    public InternshipItemAdapter(Context context, List<ApplyChildData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public InternshipItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_internship_item,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(InternshipItemAdapter.ViewHolder holder, int position) {
        final ApplyChildData data = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.tv.setText(data.getName());
        holder.ll_1.setBackgroundColor(RandomColorUtils.getRandomColor());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationActivity.start(context,data.getId(),1,data.getCatId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView tv;
        private LinearLayout ll,ll_1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
            tv = (TextView) v.findViewById(R.id.tv);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            ll_1 = (LinearLayout) v.findViewById(R.id.ll_1);
        }
    }
}
