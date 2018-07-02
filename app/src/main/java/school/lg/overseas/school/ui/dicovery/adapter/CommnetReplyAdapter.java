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

public class CommnetReplyAdapter extends RecyclerView.Adapter {

    private final int NORMAL_REPLY = 1;  //  正常回复
    private final int MORE_REPLY = 2;    //  更多回复
    private Context context;
    private List<AbroadReplyBean> abroadReplyBeanList;

    public CommnetReplyAdapter(Context context, List<AbroadReplyBean> abroadReplyBeanList) {
        this.context = context;
        this.abroadReplyBeanList = abroadReplyBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_REPLY) {
            return new CommentReplyHolder(LayoutInflater.from(context).inflate(R.layout.item_user_comment, parent, false));
        } else {
            return new MoreHolder(LayoutInflater.from(context).inflate(R.layout.item_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbroadReplyBean abroadReplyBean = abroadReplyBeanList.get(position);
        if (abroadReplyBean != null) {
            if (holder instanceof CommentReplyHolder) {
                CommentReplyHolder commentReplyHolder = (CommentReplyHolder) holder;
                commentReplyHolder.name.setText(abroadReplyBean.getReplyName() + "：");
                commentReplyHolder.content.setText(abroadReplyBean.getContent());
            }else if (holder instanceof MoreHolder){
                MoreHolder moreHolder = (MoreHolder) holder;
                moreHolder.showMoreReply.setText(abroadReplyBean.getContent());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (abroadReplyBeanList != null && abroadReplyBeanList.size() > 0) {
            AbroadReplyBean abroadReplyBean = abroadReplyBeanList.get(position % abroadReplyBeanList.size());
            if (abroadReplyBean.getViewType() == NORMAL_REPLY) {
                return NORMAL_REPLY;
            } else if (abroadReplyBean.getViewType() == MORE_REPLY) {
                return MORE_REPLY;
            } else {
                return MORE_REPLY;
            }
        } else {
            return NORMAL_REPLY;
        }
    }

    @Override
    public int getItemCount() {
        return abroadReplyBeanList == null ? 0 : abroadReplyBeanList.size();
    }

    class CommentReplyHolder extends RecyclerView.ViewHolder {
        private TextView name, content;

        public CommentReplyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    class MoreHolder extends RecyclerView.ViewHolder {
        private TextView showMoreReply;

        public MoreHolder(View itemView) {
            super(itemView);
            showMoreReply = (TextView) itemView.findViewById(R.id.show_more_reply);
        }
    }
}
