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
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/2/1.
 */

public class NewsTagAdapter extends RecyclerView.Adapter<NewsTagAdapter.ViewHolder>{
    private Context context;
    private List<TitleTag> datas;
    private SelectListener listener;
    private int selectPos=-1;
    private String tag;

    public NewsTagAdapter(Context context,String tag, List<TitleTag> datas,SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
        this.tag=tag;
    }

    @Override
    public NewsTagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tag,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsTagAdapter.ViewHolder holder, final int position) {
        final TitleTag tags = datas.get(position);
        holder.tv.setText(tags.getName());
        if(tag.equals(tags.getId()))selectPos=position;
        if(selectPos==position){
            holder.tv.setSelected(true);
            holder.tv.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.tv.setSelected(false);
            holder.tv.setTextColor(context.getResources().getColor(R.color.line_black));
        }
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag=tags.getId();
                selectPos=position;
                listener.select(selectPos);
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
