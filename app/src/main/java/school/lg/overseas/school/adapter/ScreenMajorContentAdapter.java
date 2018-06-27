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
import school.lg.overseas.school.bean.ScreenLittleData;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2017/12/13.
 */

public class ScreenMajorContentAdapter extends RecyclerView.Adapter<ScreenMajorContentAdapter.ViewHolder>{
    private Context context;
    private List<ScreenLittleData> datas;
    private SelectListener listener;
    private int selectPos=-1;
    public ScreenMajorContentAdapter(Context context, List<ScreenLittleData> datas, SelectListener listener) {
        this.context=context;
        this.datas=datas;
        this.listener =listener;
    }

    public void setDatas(List<ScreenLittleData> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }

    @Override
    public ScreenMajorContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_screen_major_content,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScreenMajorContentAdapter.ViewHolder holder, final int position) {
        ScreenLittleData data = datas.get(position);
        holder.tv.setText("    "+data.getName()+"    ");
        if(selectPos==position){
            holder.tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.screen_major_content_true));
        }else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.mainTextColor));
            holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.screen_major_content_false));
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
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
