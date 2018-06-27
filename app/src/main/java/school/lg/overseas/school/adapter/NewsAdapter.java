package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.NewsData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.NewsDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.view.MultiImage.MultiImageView;
import school.lg.overseas.school.view.MultiImage.PhotoInfo;

/**
 * Created by Administrator on 2018/1/8.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context context;
    private List<NewsData> datas;

    public NewsAdapter(Context context, List<NewsData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_news,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        final NewsData data = datas.get(position);
        if("1".equals(data.getHot()))holder.hot_iv.setVisibility(View.VISIBLE);
        else holder.hot_iv.setVisibility(View.GONE);
        holder.title.setText(data.getTitle());
        holder.content.setText(data.getCnContent());
        if(null!=data.getImageContent()&&data.getImageContent().size()!=0) {
            List<String> imageContent = data.getImageContent();
            LinearLayoutManager manager =new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            holder.img_list.setLayoutManager(manager);
            NewsImagAdapter adapter =new NewsImagAdapter(context,imageContent);
            holder.img_list.setAdapter(adapter);
        }
        holder.name.setText(null==data.getNickname()?data.getUsername():data.getNickname());
        holder.time.setText(data.getDateTime());
        holder.reply_num.setText("回复:"+data.getReplyNum());
        holder.see_num.setText("查看:"+data.getViewCount());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.start(context,data.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView hot_iv;
        private TextView title,name,time,reply_num,see_num,content;
//        private MultiImageView multiImagView;
        private RecyclerView img_list;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            hot_iv = (ImageView) v.findViewById(R.id.hot_iv);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
//            multiImagView = (MultiImageView) v.findViewById(R.id.multiImagView);
            img_list = (RecyclerView) v.findViewById(R.id.img_list);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            reply_num = (TextView) v.findViewById(R.id.reply_num);
            see_num = (TextView) v.findViewById(R.id.see_num);
        }
    }
}
