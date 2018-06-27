package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/5.
 */

public class LableManageAdapter extends RecyclerView.Adapter<LableManageAdapter.ViewHolder>{
    private Context context;
    private List<TitleTag> datas;

    public LableManageAdapter(Context context, List<TitleTag> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public LableManageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_lable,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(LableManageAdapter.ViewHolder holder, final int position) {
        final TitleTag data = datas.get(position);
        holder.name.setText(data.getName());
        if(data.isClose())holder.iv.setBackgroundResource(R.mipmap.on_off_false);
        else holder.iv.setBackgroundResource(R.mipmap.on_off_true);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setClose(!data.isClose());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            name = (TextView) v.findViewById(R.id.name);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
