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
import school.lg.overseas.school.bean.AdiviserItem;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.OnlineActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

/**
 * Created by Administrator on 2018/1/3.
 */

public class MechanismAdviserAdapter extends RecyclerView.Adapter<MechanismAdviserAdapter.ViewHolder>{
    private Context context;
    private List<AdiviserItem> datas;

    public MechanismAdviserAdapter(Context context, List<AdiviserItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MechanismAdviserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_mechanism_adviser,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MechanismAdviserAdapter.ViewHolder holder, int position) {
        AdiviserItem data = datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(data.getName());
        holder.content.setText(HtmlReplaceUtils.replaceAllToLable(data.getAnswer()));
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, OnlineActivity.class);
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
        private TextView name,content;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            content = (TextView) v.findViewById(R.id.content);
        }
    }
}
