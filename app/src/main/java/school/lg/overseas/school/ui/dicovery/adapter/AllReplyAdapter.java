package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.HolderSelectListener;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.bean.AbroadReplyBean;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/7/2.
 */

public class AllReplyAdapter extends RecyclerView.Adapter<AllReplyAdapter.AllReplyHolder> {
    private Context context;
    private List<AbroadReplyBean> abroadReplyBeanList;

    private HolderSelectListener holderSelectListener;

    public AllReplyAdapter(Context context, List<AbroadReplyBean> abroadReplyBeanList) {
        this.context = context;
        this.abroadReplyBeanList = abroadReplyBeanList;
    }

    public void setSelectListener(HolderSelectListener holderSelectListener) {
        this.holderSelectListener = holderSelectListener;
    }

    @Override
    public AllReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllReplyHolder(LayoutInflater.from(context).inflate(R.layout.item_all_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(final AllReplyHolder holder, final int position) {
        AbroadReplyBean abroadReplyBean = abroadReplyBeanList.get(position % abroadReplyBeanList.size());
        holder.name.setText(abroadReplyBean.getNickname());
        holder.time.setText(abroadReplyBean.getCreateTime());
        holder.num.setText(abroadReplyBean.getFane());
        new GlideUtils().loadCircle(context, NetworkTitle.DomainSchoolResourceNormal + abroadReplyBean.getImage(), holder.head);
        String content = "回复" + abroadReplyBean.getReplyName() + "：" + abroadReplyBean.getContent() ;
        setTextColor(holder.content ,content ,abroadReplyBean.getReplyName());
        holder.num_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holderSelectListener != null) holderSelectListener.itemSelect(holder, position);
            }
        });
    }

    private void setTextColor(TextView textView , String content , String replyName){
        SpannableString style = new SpannableString(content);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text)),0 , 2 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.mainGreen)) ,2 , 2 + replyName.length() ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)),3+ replyName.length() ,content.length() ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);

    }


    @Override
    public int getItemCount() {
        return abroadReplyBeanList == null ? 0 : abroadReplyBeanList.size();
    }

   public  class AllReplyHolder extends RecyclerView.ViewHolder {
        private CircleImageView head;
        public TextView name, time, num, content;
        private ImageView num_img;

        public AllReplyHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.head);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            num = (TextView) itemView.findViewById(R.id.num);
            content = (TextView) itemView.findViewById(R.id.reply_name);
            num_img = (ImageView) itemView.findViewById(R.id.num_img);
        }
    }
}
