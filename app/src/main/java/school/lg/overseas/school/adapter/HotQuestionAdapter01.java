package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.QuestionData;
import school.lg.overseas.school.bean.QuestionData01;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.communication.QuestionDetailActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

/**
 * 问答适配器
 */

public class HotQuestionAdapter01 extends RecyclerView.Adapter<HotQuestionAdapter01.ViewHolder>{
    private Context context;
    private List<QuestionData01> datas;
    public HotQuestionAdapter01(Context context, List<QuestionData01> datas) {
        this.context=context;
        this.datas=datas;
    }

    @Override
    public HotQuestionAdapter01.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(HotQuestionAdapter01.ViewHolder holder, int position) {
        final QuestionData01 data = datas.get(position);
        if(!TextUtils.isEmpty(data.getImage())){//加载头像
            new GlideUtils().loadCircle(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.portrait);
        }else{
            new GlideUtils().loadResCircle(context,R.mipmap.head_defult,holder.portrait);
        }
        holder.name.setText(data.getNickname());
        holder.time.setText(data.getAddTime());
        holder.title.setText(data.getQuestion());
        holder.content.setText(HtmlReplaceUtils.replaceAllToLable(data.getContent()));
        holder.follow_num.setText(data.getBrowse());
        holder.answer_num.setText(data.getAnswerNum());
        holder.lable_ll.removeAllViews();
        for (int i = 0; i < data.getTags().size(); i++) {
            TextView tv =new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin=15;
            tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lable_back_false));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(10);
            tv.setText("  "+data.getTags().get(i).getName()+"  ");
            holder.lable_ll.addView(tv,params);
        }
        if(position==datas.size()-1){
            holder.v1.setVisibility(View.GONE);
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView portrait;
        private TextView name,time,title,content,follow_num,answer_num;
        private LinearLayout lable_ll;
        private View v1,ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            portrait = (ImageView) v.findViewById(R.id.portrait);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            lable_ll = (LinearLayout) v.findViewById(R.id.lable_ll);
            follow_num = (TextView) v.findViewById(R.id.follow_num);
            answer_num = (TextView) v.findViewById(R.id.answer_num);
            v1 =v.findViewById(R.id.v1);
        }
    }
}
