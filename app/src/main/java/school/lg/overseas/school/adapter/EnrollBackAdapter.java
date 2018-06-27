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
 * Created by Administrator on 2018/1/15.
 */

public class EnrollBackAdapter extends RecyclerView.Adapter<EnrollBackAdapter.ViewHolder>{
    private Context context;
    private List<String> selects =new ArrayList<>();
    private List<String> datas;

    public EnrollBackAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public List<String> getSelects(){
        return selects;
    }

    @Override
    public EnrollBackAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_enroll_back,viewGroup,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EnrollBackAdapter.ViewHolder holder, final int i) {
        String data = datas.get(i);
        if(selects.contains(i+""))holder.iv.setSelected(true);//设置选中图片
        else holder.iv.setSelected(false);//设置未未选中图片
        holder.tv.setText(data);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selects.contains(i+"")){
                    selects.remove(i+"");
                }else{
                    selects.add(i+"");
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView iv;
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
