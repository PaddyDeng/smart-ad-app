package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import school.lg.overseas.school.R;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.bean.AbroadReplyBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/6/21.
 */

public class AbroadCommentAdapter extends RecyclerView.Adapter<AbroadCommentAdapter.AbroadCommentHolder> {

    private Context context ;
    private List<ArticalDetailBean.CommentBean.DataBeanX> dataBeanXList ;
    public AbroadCommentAdapter(Context context , List<ArticalDetailBean.CommentBean.DataBeanX> dataBeanXList){
        this.context = context ;
        this.dataBeanXList = dataBeanXList ;
    }
    @Override
    public AbroadCommentAdapter.AbroadCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbroadCommentAdapter.AbroadCommentHolder(LayoutInflater.from(context).inflate(R.layout.item_abroad_comment , parent ,false));
    }

    @Override
    public void onBindViewHolder(AbroadCommentAdapter.AbroadCommentHolder holder, int position) {
        ArticalDetailBean.CommentBean.DataBeanX dataBeanX = dataBeanXList.get(position % dataBeanXList.size());
        holder.name.setText(dataBeanX.getNickname());
        new GlideUtils().loadCircle(context , NetworkTitle.DomainSmartApplyResourceNormal + dataBeanX.getImage() , holder.head);
        holder.time.setText(dataBeanX.getCreateTime());
        holder.num.setText(dataBeanX.getFane());
        holder.content.setText(dataBeanX.getContent());
        if (dataBeanX.getReply() != null && dataBeanX.getReply().size() > 0) initAdapter(holder ,dataBeanX.getReply());
    }


    private void initAdapter(AbroadCommentHolder holder , List<AbroadReplyBean > abroadReplyBeanList){
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(context);
        holder.answer_list.setLayoutManager(linearLayoutManager);
        holder.answer_list.setAdapter(new CommnetReplyAdapter(context ,abroadReplyBeanList));
    }


    @Override
    public int getItemCount() {
        return dataBeanXList == null ? 0 : dataBeanXList.size();
    }

    class AbroadCommentHolder extends RecyclerView.ViewHolder{
        public CircleImageView head ;
        public TextView name , time ,num ,content ;
        public ImageView num_img ;
        public RecyclerView answer_list ;
        public AbroadCommentHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.head);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            num = (TextView) itemView.findViewById(R.id.num);
            content = (TextView) itemView.findViewById(R.id.content);
            num_img = (ImageView) itemView.findViewById(R.id.num_img);
            answer_list = (RecyclerView) itemView.findViewById(R.id.answer_list);
        }
    }
}
