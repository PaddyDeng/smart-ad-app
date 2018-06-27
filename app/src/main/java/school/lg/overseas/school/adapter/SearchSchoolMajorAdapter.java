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
 * Created by Administrator on 2018/1/14.
 */

public class SearchSchoolMajorAdapter extends RecyclerView.Adapter<SearchSchoolMajorAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchoolLitle> datas;
    private SelectListener listener;

    public SearchSchoolMajorAdapter(Context context, List<SearchSchoolLitle> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public SearchSchoolMajorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_little_school_major,viewGroup,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchSchoolMajorAdapter.ViewHolder holder, final int i) {
        SearchSchoolLitle data = datas.get(i);
        holder.tv.setText(data.getTitle());
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.select(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView tv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
