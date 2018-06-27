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
import school.lg.overseas.school.bean.QuestionData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.communication.QuestionDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

/**
 * Created by Administrator on 2017/12/23.
 */

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder>{
    private Context context;
    private List<QuestionData> datas;

    public QuestionListAdapter(Context context, List<QuestionData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public QuestionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_question_list,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(QuestionListAdapter.ViewHolder holder, int position) {
        final QuestionData data = datas.get(position);
        holder.title.setText(data.getQuestion());
        holder.content.setText(HtmlReplaceUtils.replaceAllToLable(data.getContent()));
        holder.tag.setText(data.getTags().get(0).getName());
//        if(!TextUtils.isEmpty(data.getImage())){//加载头像
            new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.portrait);
//        }else{
//            new GlideUtils().loadResCircle(context,R.mipmap.ic_launcher,holder.portrait);
//        }
        holder.name.setText(null==data.getNickname()?data.getUserName():data.getNickname());
        holder.time.setText(data.getAddTime());
        holder.look_num.setText(data.getBrowse());
        holder.asker_num.setText(data.getAnswer().size()+"");
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,content,tag,name,time,look_num,asker_num;
        private ImageView portrait;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            tag = (TextView) v.findViewById(R.id.tag);
            portrait = (ImageView) v.findViewById(R.id.portrait);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            look_num = (TextView) v.findViewById(R.id.look_num);
            asker_num = (TextView) v.findViewById(R.id.asker_num);
        }
    }
}
