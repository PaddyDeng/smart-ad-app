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
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;
import school.lg.overseas.school.view.RoundCornerImageView;

/**
 * 学校适配器
 */

public class HomeSchoolAdapter extends RecyclerView.Adapter<HomeSchoolAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;
    private int[] imgs =new int[]{R.mipmap.school_1,R.mipmap.school_2,R.mipmap.school_3,R.mipmap.school_4,R.mipmap.school_5,R.mipmap.school_6,R.mipmap.school_7,R.mipmap.school_8,R.mipmap.school_9,R.mipmap.school_10,};
    public HomeSchoolAdapter(Context context, List<LittleData> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public HomeSchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.itme_home_school,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeSchoolAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        holder.background_iv.setBackgroundDrawable(context.getResources().getDrawable(imgs[position%10]));
        holder.cn_name.setText("["+data.getName()+"]");
        holder.en_name.setText(data.getTitle());
        holder.background_iv.setOnClickListener(new View.OnClickListener() {
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
        private RoundCornerImageView background_iv;
        private TextView cn_name,en_name;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            background_iv = (RoundCornerImageView) v.findViewById(R.id.background_iv);
            cn_name = (TextView) v.findViewById(R.id.cn_name);
            en_name = (TextView) v.findViewById(R.id.en_name);
        }
    }
}
