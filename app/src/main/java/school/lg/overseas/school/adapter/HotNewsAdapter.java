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
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.view.RoundCornerImageView;

/**
 * Created by Administrator on 2017/12/29.
 */

public class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;
    private int[] imgs ={R.mipmap.hot_news_1,R.mipmap.hot_news_2,R.mipmap.hot_news_3};
    public HotNewsAdapter(Context context, List<LittleData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public HotNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_hot_news,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(HotNewsAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        holder.tv.setText(data.getName());
        holder.background_iv.setBackgroundResource(imgs[position%3]);
        holder.background_iv.setOnClickListener(new View.OnClickListener() {
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
        private TextView tv;
        private RoundCornerImageView background_iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.cn_name);
            background_iv = (RoundCornerImageView) v.findViewById(R.id.background_iv);
        }
    }
}
