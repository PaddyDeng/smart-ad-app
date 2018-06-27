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
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NewsImagAdapter extends RecyclerView.Adapter<NewsImagAdapter.ViewHolder>{
    private Context context;
    private List<String> datas;

    public NewsImagAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public NewsImagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_news_img,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsImagAdapter.ViewHolder holder, int position) {
        String s = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.GOSSIPURL+s,holder.iv);
    }

    @Override
    public int getItemCount() {
        int num=0;
        if(null!=datas){
            if(datas.size()<=3){
                num=datas.size();
            }else{
                num=3;
            }
        }
        return num;
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
