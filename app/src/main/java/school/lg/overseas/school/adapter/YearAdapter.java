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
import school.lg.overseas.school.bean.Year;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2017/12/21.
 */

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private Context context;
    private List<Year> datas;
    private SelectListener listener;
    private int selectPos = 0;

    public YearAdapter(Context context, List<Year> datas,SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener =listener;
    }

    public void setSelectPos(int i){
        selectPos =i;
    }

    @Override
    public YearAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_year,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(YearAdapter.ViewHolder holder, final int position) {
        Year data = datas.get(position);
        if(selectPos==position){
            holder.tv.setSelected(true);
            holder.tv.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.tv.setSelected(false);
            holder.tv.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.tv.setText("  "+data.getName()+"  ");
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
