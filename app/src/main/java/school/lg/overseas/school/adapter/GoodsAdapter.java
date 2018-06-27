package school.lg.overseas.school.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.GoodsDetail;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.GoodsDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private Context context;
    private List<GoodsDetail> datas;

    public GoodsAdapter(Context context, List<GoodsDetail> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_goods,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GoodsAdapter.ViewHolder holder, int position) {
        final GoodsDetail data = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(data.getName());
        holder.now_price.setText("￥"+data.getPrice());
        holder.old_price.setText("原价:"+data.getOriginalPrice());
        holder.old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
        private ImageView iv;
        private TextView name,now_price,old_price;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            now_price = (TextView) v.findViewById(R.id.now_price);
            old_price = (TextView) v.findViewById(R.id.old_price);
            rl = v.findViewById(R.id.rl);
        }
    }
}
