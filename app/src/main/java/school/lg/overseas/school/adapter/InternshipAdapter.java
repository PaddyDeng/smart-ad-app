package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Apply;

/**
 * Created by Administrator on 2017/12/22.
 */

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder>{
    private Context context;
    private List<Apply> datas;

    public InternshipAdapter(Context context, List<Apply> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public InternshipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_internship,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(InternshipAdapter.ViewHolder holder, int position) {
        Apply data = datas.get(position);
        holder.tv.setText(data.getName());
        GridLayoutManager manager =new GridLayoutManager(context,2);
        holder.content_list.setLayoutManager(manager);
        InternshipItemAdapter adapter =new InternshipItemAdapter(context,data.getData());
        holder.content_list.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private RecyclerView content_list;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.title_t);
            content_list = (RecyclerView) v.findViewById(R.id.content_list);
        }
    }
}
