package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.ui.dicovery.bean.AbroadReplyBean;

/**
 * Created by Administrator on 2018/6/25.
 */

public class CommnetReplyAdapter extends RecyclerView.Adapter<CommnetReplyAdapter.CommentReplyHolder> {

    private Context context ;
    private List<AbroadReplyBean>  abroadReplyBeanList ;

    public CommnetReplyAdapter(Context context ,List<AbroadReplyBean> abroadReplyBeanList){
        this.context = context ;
        this.abroadReplyBeanList = abroadReplyBeanList ;
    }
    @Override
    public CommentReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentReplyHolder(LayoutInflater.from(context).inflate(R.layout.item_user_comment ,parent ,false));
    }

    @Override
    public void onBindViewHolder(CommentReplyHolder holder, int position) {
        AbroadReplyBean abroadReplyBean =  abroadReplyBeanList.get(position);
        if (abroadReplyBean != null){
            holder.name.setText(abroadReplyBean.getReplyName());
            holder.content.setText(abroadReplyBean.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return abroadReplyBeanList == null ? 0 : abroadReplyBeanList.size();
    }

    class CommentReplyHolder extends RecyclerView.ViewHolder{
        private TextView name ,content ;
        public CommentReplyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
