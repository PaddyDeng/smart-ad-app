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
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.callback.SelectListener;

/**
 * 商品筛选
 */

public class GoodsSelectAdapter extends RecyclerView.Adapter<GoodsSelectAdapter.ViewHolder>{
    private Context context;
    private List<Country> datas;
    private int selectPos=-1;
    private SelectListener listener;
    public GoodsSelectAdapter(Context context, List<Country> datas, SelectListener listener) {
        this.context=context;
        this.datas=datas;
        this.listener =listener;
    }

    public void setSelectPos(int i){
        selectPos=i;
    }

    @Override
    public GoodsSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_screen_country,null,false);
        ViewHolder holder= new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GoodsSelectAdapter.ViewHolder holder, final int position) {
        Country screenLittleData = datas.get(position);
        holder.tv.setText(screenLittleData.getName());
        if(position==selectPos){
            holder.tv.setTextColor(context.getResources().getColor(R.color.mainGreen));
            holder.iv.setVisibility(View.VISIBLE);
        }else{
            holder.tv.setTextColor(context.getResources().getColor(R.color.line_black));
            holder.iv.setVisibility(View.GONE);
        }
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.select(position);
                selectPos=position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
