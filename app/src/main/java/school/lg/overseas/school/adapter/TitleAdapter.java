package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;

/**
 * 问答标签适配器
 */

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>{
    private Context context;
    private List<TitleTag> datas;
    private int selectPos =0;
    private SelectListener listener;

    public TitleAdapter(Context context, List<TitleTag>datas, SelectListener listener) {
        this.context=context;
        this.datas=datas;
        this.listener=listener;
    }



    public void setSelectPos(int i){
        selectPos=i;
        notifyDataSetChanged();
    }

    @Override
    public TitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_title,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TitleAdapter.ViewHolder holder, final int position) {
        TitleTag data = datas.get(position);
        if (position == selectPos){
             holder.title_cursor.setVisibility(View.VISIBLE);
            holder.tv.setTextColor(context.getResources().getColor(R.color.mainGreen));
        }else{
            holder.title_cursor.setVisibility(View.GONE);
            holder.tv.setTextColor(context.getResources().getColor(R.color.mainTextColor));
        }
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos=position;
                notifyDataSetChanged();
                listener.select(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv , title_cursor;
        private final View rl;

        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            tv = (TextView) v.findViewById(R.id.tv);
            title_cursor = (TextView) v.findViewById(R.id.title_cursor);
        }
    }
}
