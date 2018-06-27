package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Answer;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/1/2.
 */

public class AdiviserQuestionAdapter extends RecyclerView.Adapter<AdiviserQuestionAdapter.ViewHolder>{
    private Context context;
    private List<Answer> datas;

    public AdiviserQuestionAdapter(Context context, List<Answer> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public AdiviserQuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_adiviser_quesition,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdiviserQuestionAdapter.ViewHolder holder, int position) {
        Answer data = datas.get(position);
        holder.question.setText(data.getQuestion());
        holder.name.setText(data.getUsername());
        holder.time.setText(data.getAddTime());
        holder.content.setText(data.getContent());
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.main_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView question;
        private ImageView iv;
        private TextView name,time,content;
        private View main_ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            main_ll = v.findViewById(R.id.main_ll);
            question = (TextView) v.findViewById(R.id.question);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            content = (TextView) v.findViewById(R.id.content);
        }
    }
}
