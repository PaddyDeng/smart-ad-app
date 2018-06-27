package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.ui.home.MajorDetailsActivity;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SearchSchoolAndMajorAdapter extends RecyclerView.Adapter<SearchSchoolAndMajorAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchool> datas;
    private int tag;
    private SelectListener listener;
    public SearchSchoolAndMajorAdapter(Context context, List<SearchSchool>datas,int tag) {
        this.context =context;
        this.datas =datas;
        this.tag =tag;
    }

    public void setListener(SelectListener listener){
        this.listener =listener;
    }

    @Override
    public SearchSchoolAndMajorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchSchoolAndMajorAdapter.ViewHolder holder, final int position) {
        final SearchSchool data = datas.get(position);
        holder.cn_name.setText(data.getName());
        holder.en_name.setText(data.getTitle());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tag){
                    case 0://学校详情
                        Intent intent =new Intent(context,SchoolDetailsActivity.class);
                        intent.putExtra("id",data.getId());
                        context.startActivity(intent);
                        break;
                    case 1://专业详情
                        Intent intent1 =new Intent(context, MajorDetailsActivity.class);
                        intent1.putExtra("id",data.getId());
                        context.startActivity(intent1);
                        break;
                    case 2://选择学校
                        listener.select(position);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView cn_name,en_name;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            cn_name = (TextView) v.findViewById(R.id.cn_name);
            en_name = (TextView) v.findViewById(R.id.en_name);
            ll = v.findViewById(R.id.ll);
        }
    }
}
