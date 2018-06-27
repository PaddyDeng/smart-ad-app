package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

/**
 * 专栏适配器
 */

public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;
    public ColumnAdapter(Context context, List<LittleData> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public ColumnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_column,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ColumnAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.content.setText(HtmlReplaceUtils.replaceAllToLable(data.getAnswer()));
        holder.time.setText(data.getCreateTime());
        holder.rl.setOnClickListener(new View.OnClickListener() {
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
        private TextView title,content,time;
        private RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            time = (TextView) v.findViewById(R.id.time);
        }
    }
}
