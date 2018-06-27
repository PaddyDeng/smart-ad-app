package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.UrlUtils;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>{
    private Context context;
    private List<ReplyBean> datas;
    private SelectListener listener;

    public ReplyAdapter(Context context, List<ReplyBean> datas,SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener =listener;
    }

    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itme_reply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.ViewHolder holder, final int position) {
        final ReplyBean data = datas.get(position);
        String name = data.getuName();
        String toReplyName = "";
        if (!TextUtils.isEmpty(data.getReplyUserName())) {
            toReplyName = data.getReplyUserName();
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = data.getContent();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));

        holder.tv.setText(builder);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击回复
                if(data.getUid().equals(SharedPreferencesUtils.getUserInfo(context).getUid())){
                    Toast.makeText(context,"不能回复自己",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.select(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        int num=0;
        if(null!=datas){
            num=datas.size()>5?5:datas.size();
        }
        return num;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_blue)), 0, subjectSpanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }
}
