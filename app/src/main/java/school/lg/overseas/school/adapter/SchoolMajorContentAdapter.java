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
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2017/12/13.
 */

public class SchoolMajorContentAdapter extends RecyclerView.Adapter<SchoolMajorContentAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchoolLitle> datas;
    private SelectListener listener;
    public SchoolMajorContentAdapter(Context context, List<SearchSchoolLitle> datas, SelectListener listener) {
        this.context=context;
        this.datas=datas;
        this.listener =listener;
    }

    @Override
    public SchoolMajorContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_school_major_content,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SchoolMajorContentAdapter.ViewHolder holder, final int position) {
        SearchSchoolLitle data = datas.get(position);
        holder.tv.setText("    "+data.getName()+"    ");
        holder.tv.setOnClickListener(new View.OnClickListener() {
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
        private TextView tv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
