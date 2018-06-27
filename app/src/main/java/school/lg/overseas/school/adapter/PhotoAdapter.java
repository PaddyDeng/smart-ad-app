package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.ui.other.ImgActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/18.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    private Context context;
    private List<String> datas;
    private SelectListener listener;

    public PhotoAdapter(Context context, List<String> datas,SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener=listener;
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.ViewHolder holder, final int position) {
        if(position==datas.size()){
            new GlideUtils().load(context,R.mipmap.add_photo,holder.iv);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.select(position);
                }
            });
        }else {
            final String s = datas.get(position);
            new GlideUtils().load(context,s,holder.iv);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position!=datas.size())
                    ImgActivity.start(context,s);
                }
            });
            holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(position!=datas.size()) {
                        listener.select(position);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==datas?1:datas.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
        }
    }
}
