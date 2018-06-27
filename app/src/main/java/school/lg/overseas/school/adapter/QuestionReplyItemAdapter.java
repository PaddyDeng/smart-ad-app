package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.Reply;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/20.
 */

public class QuestionReplyItemAdapter extends RecyclerView.Adapter<QuestionReplyItemAdapter.ViewHolder>{
    private Context context;
    private List<Reply> datas;

    public QuestionReplyItemAdapter(Context context, List<Reply> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public QuestionReplyItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_question_reply_item,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(QuestionReplyItemAdapter.ViewHolder holder, int position) {
        Reply data = datas.get(position);
        if(position==datas.size()-1){
            holder.v1.setVisibility(View.GONE);
        }else{
            holder.v1.setVisibility(View.VISIBLE);
        }
        Login userInfo = SharedPreferencesUtils.getUserInfo(context);
        if(!TextUtils.isEmpty(userInfo.getUid())&&userInfo.getUid().equals(data.getUserId()))holder.name.setTextColor(context.getResources().getColor(R.color.mainGreen));
        else holder.name.setTextColor(context.getResources().getColor(R.color.black));
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
        holder.time.setText(data.getAddTime());
        holder.content.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView name,time,content;
        private View v1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            content = (TextView) v.findViewById(R.id.content);
            v1 = v.findViewById(R.id.v1);
        }
    }
}
