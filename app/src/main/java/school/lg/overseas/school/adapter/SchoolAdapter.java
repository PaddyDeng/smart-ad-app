package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.callback.RecyclerSelectListener01;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * 院校列表item
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder>{
    private Context context;
    private List<SearchSchool> datas;
    private boolean isSelect;
    private RecyclerSelectListener01 listener;
    public SchoolAdapter(Context context, List<SearchSchool>datas,boolean isSelect,RecyclerSelectListener01 listener) {
        this.context=context;
        this.datas=datas;
        this.isSelect=isSelect;
        this.listener =listener;
    }

    @Override
    public SchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_school,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SchoolAdapter.ViewHolder holder, final int position) {
        final SearchSchool data = datas.get(position);
        holder.cn_name.setText(data.getName());
        holder.ranking.setText("排名："+data.getRank());
        holder.address.setText("地址："+data.getSeat());
        holder.en_name.setText(data.getTitle());
        new GlideUtils().load(context, NetworkTitle.DomainSchoolResourceNormal+data.getImage(),holder.iv);
        if(isSelect) {
            holder.major_list.setVisibility(View.VISIBLE);
            LinearLayoutManager manager =new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            holder.major_list.setLayoutManager(manager);
            SearchSchoolMajorAdapter majorAdapter = new SearchSchoolMajorAdapter(context, data.getMajor(), new SelectListener() {
                @Override
                public void select(int contentId) {
                    listener.setListener(position,contentId);
                }
            });
            holder.major_list.setAdapter(majorAdapter);
        }else{
            holder.major_list.setVisibility(View.GONE);
        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, SchoolDetailsActivity.class);
                intent.putExtra("id",data.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView cn_name,ranking,address,en_name;
        private RelativeLayout rl;
        private RecyclerView major_list;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl= (RelativeLayout) v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            cn_name = (TextView) v.findViewById(R.id.cn_name);
            ranking = (TextView) v.findViewById(R.id.ranking);
            address = (TextView) v.findViewById(R.id.address);
            en_name = (TextView) v.findViewById(R.id.en_name);
            major_list = (RecyclerView) v.findViewById(R.id.major_list);
        }
    }
}
