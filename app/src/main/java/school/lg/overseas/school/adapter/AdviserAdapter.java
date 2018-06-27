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
import school.lg.overseas.school.ui.home.AdiviserDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;


/**
 * Created by Administrator on 2018/1/2.
 */

public class AdviserAdapter extends RecyclerView.Adapter<AdviserAdapter.ViewHolder>{
    private Context context;
    private List<AdiviserItem> datas;
    public AdviserAdapter(Context context, List<AdiviserItem> datas) {
        this.context = context;
        this.datas = datas;
    }
    public void setData(List<AdiviserItem> datas){
        this.datas =datas;
    }

    @Override
    public AdviserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_adviser,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdviserAdapter.ViewHolder holder, final int position) {
        final AdiviserItem data = datas.get(position);
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal + data.getImage(), holder.iv);
        holder.name.setText(data.getName());
        holder.identity.setText(data.getSource());
        holder.content.setText(HtmlReplaceUtils.replaceAllToLable(data.getAnswer()));
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdiviserDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{
        private View rl;
        private TextView name,identity,content;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            identity = (TextView) v.findViewById(R.id.identity);
            content = (TextView) v.findViewById(R.id.content);
        }
    }
}
