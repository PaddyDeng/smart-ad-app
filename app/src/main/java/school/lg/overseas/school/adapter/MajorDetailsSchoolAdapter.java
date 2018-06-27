package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import school.lg.overseas.school.R;

/**
 * Created by Administrator on 2017/12/19.
 */

public class MajorDetailsSchoolAdapter extends RecyclerView.Adapter<MajorDetailsSchoolAdapter.ViewHolder>{
    private Context context;
    private String[] datas;
    public MajorDetailsSchoolAdapter(Context context,String[] datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public MajorDetailsSchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_major_details_school,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MajorDetailsSchoolAdapter.ViewHolder holder, int position) {
        if(position==0)holder.iv.setVisibility(View.VISIBLE);
        else holder.iv.setVisibility(View.GONE);
        holder.tv.setText(datas[position]);
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
