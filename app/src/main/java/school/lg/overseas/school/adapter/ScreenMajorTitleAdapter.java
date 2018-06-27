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

public class ScreenMajorTitleAdapter extends RecyclerView.Adapter<ScreenMajorTitleAdapter.ViewHolder> {
    private Context context;
    private List<ScreenLittleData> datas;
    private int selectPos =0;
    private SelectListener listener;
    public ScreenMajorTitleAdapter(Context context, List<ScreenLittleData> datas, SelectListener listener) {
        this.context =context;
        this.datas=datas;
        this.listener =listener;
    }

    @Override
    public ScreenMajorTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_screen_major_title,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScreenMajorTitleAdapter.ViewHolder holder, final int position) {
        ScreenLittleData data = datas.get(position);
        holder.tv.setText(data.getName());
        if(selectPos==position){
            holder.tv.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            holder.tv.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
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
