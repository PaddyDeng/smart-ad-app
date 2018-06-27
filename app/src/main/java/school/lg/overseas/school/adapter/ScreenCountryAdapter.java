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
import school.lg.overseas.school.bean.ScreenLittleData;
import school.lg.overseas.school.callback.SelectListener;

/**
 * 选择国家
 */

public class ScreenCountryAdapter extends RecyclerView.Adapter<ScreenCountryAdapter.ViewHolder>{
    private Context context;
    private List<ScreenLittleData> datas;
    private int selectPos=-1;
    private SelectListener listener;
    private int tag;
    private boolean isFirst;
    public ScreenCountryAdapter(Context context, List<ScreenLittleData> datas,int tag,SelectListener listener) {
        this.context=context;
        this.datas=datas;
        this.listener =listener;
        this.tag =tag;
    }

    @Override
    public ScreenCountryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_screen_country,null,false);
        ViewHolder holder= new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScreenCountryAdapter.ViewHolder holder, final int position) {
        if(tag==0&&!isFirst){
            isFirst=true;
            selectPos=0;
        }
        ScreenLittleData screenLittleData = datas.get(position);
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
