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
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/1/11.
 */

public class IntentionalStateAdapter extends RecyclerView.Adapter<IntentionalStateAdapter.ViewHolder>{
    private Context context;
    private List<Country> datas;
    private int tag;
    private SelectListener listener;

    public IntentionalStateAdapter(Context context, List<Country> datas, int tag, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.tag=tag;
        this.listener =listener;
    }

    @Override
    public IntentionalStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_intentional_state,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(IntentionalStateAdapter.ViewHolder holder, final int position) {
        Country data = datas.get(position);
        if(position==tag)holder.iv.setVisibility(View.VISIBLE);
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

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView iv;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            tv = (TextView) v.findViewById(R.id.tv);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
