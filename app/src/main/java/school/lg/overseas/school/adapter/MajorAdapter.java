package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.MajorData;
import school.lg.overseas.school.ui.home.MajorDetailsActivity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class MajorAdapter extends RecyclerView.Adapter<MajorAdapter.ViewHolder>{
    private Context context;
    private List<MajorData> datas;
    public MajorAdapter(Context context, List<MajorData> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public MajorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_major,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MajorAdapter.ViewHolder holder, int position) {
        final MajorData data = datas.get(position);
        if(position==(datas.size()-1))holder.v1.setVisibility(View.GONE);
        else holder.v1.setVisibility(View.VISIBLE);
        holder.title.setText(data.getName());
        holder.content.setText(data.getIntroduce());
        holder.en_name.setText(TextUtils.isEmpty(data.getTitle())?"":"("+data.getTitle()+")");
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, MajorDetailsActivity.class);
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
        private LinearLayout ll;
        private TextView title,content,en_name;
        private View v1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            title = (TextView) v.findViewById(R.id.title);
            en_name= (TextView) v.findViewById(R.id.en_name);
            content = (TextView) v.findViewById(R.id.content);
            v1 = v.findViewById(R.id.v1);
        }
    }
}
