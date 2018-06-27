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
 * Created by Administrator on 2017/12/20.
 */

public class KnowApplyAdapter extends RecyclerView.Adapter<KnowApplyAdapter.ViewHolder>{
    private Context context;
    private List<Apply> datas;
    public KnowApplyAdapter(Context context, List<Apply> datas) {
        this.context =context;
        this.datas =datas;
    }

    @Override
    public KnowApplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_apply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(KnowApplyAdapter.ViewHolder holder, int position) {
        Apply data = datas.get(position);
        holder.title.setText(data.getName());
        if(0!=data.getData().size()) {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            holder.list_view.setLayoutManager(manager);
            KnowApplyLittleAdapter littleAdapter = new KnowApplyLittleAdapter(context, data.getData());
            holder.list_view.setAdapter(littleAdapter);
        }else{
            holder.list_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private RecyclerView list_view;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            title = (TextView) v.findViewById(R.id.title);
            list_view = (RecyclerView) v.findViewById(R.id.plan_itme_list);
        }
    }
}
