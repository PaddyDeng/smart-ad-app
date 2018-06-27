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
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/1/13.
 */

public class SchoolMajorAdapter extends RecyclerView.Adapter<SchoolMajorAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchoolLitle> datas;
    private SelectListener listener;
    private int tag;

    public SchoolMajorAdapter(Context context, List<SearchSchoolLitle> datas,int tag, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
        this.tag=tag;
    }

    @Override
    public SchoolMajorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_choose_major,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SchoolMajorAdapter.ViewHolder holder, final int position) {
        SearchSchoolLitle data = datas.get(position);
        if(tag==position)holder.iv.setVisibility(View.VISIBLE);
        else holder.iv.setVisibility(View.GONE);
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.select(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View rl,v1;
        private TextView tv;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            tv = (TextView) v.findViewById(R.id.tv);
            v1 = v.findViewById(R.id.v1);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
