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
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.GoodsDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.view.RoundCornerImageView;

/**
 * 文书适配器
 */

public class HomeDocumentAdapter extends RecyclerView.Adapter<HomeDocumentAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;
    public HomeDocumentAdapter(Context context, List<LittleData> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public HomeDocumentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_home_activity,parent,false);
        ViewHolder holder = new ViewHolder(v);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeDocumentAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        new GlideUtils().load(context,NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.back_iv);
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RoundCornerImageView back_iv;
        private TextView tv;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            back_iv = (RoundCornerImageView) v.findViewById(R.id.back_iv);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
