package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ConcernAdapter extends RecyclerView.Adapter<ConcernAdapter.ViewHolder>{
    private Context context;
    private String[] datas;
    private List<String> selects=new ArrayList<>();
    public ConcernAdapter(Context context, String[] datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_concern,viewGroup,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    public List<String> getSelects() {
        return selects;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final int u =i;
        holder.tv.setText(datas[i]);
        if(selects.contains(u+""))holder.iv.setSelected(true);
        else holder.iv.setSelected(false);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selects.contains(u+"")){
                    selects.remove(u+"");
                }else{
                    selects.add(u+"");
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView tv;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            iv = (ImageView) v.findViewById(R.id.iv);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
