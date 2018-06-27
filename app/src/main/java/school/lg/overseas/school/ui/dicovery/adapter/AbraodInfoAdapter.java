package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;
import school.lg.overseas.school.ui.dicovery.bean.AbroadBean;
import school.lg.overseas.school.utils.C;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2018/6/19.
 */

public class AbraodInfoAdapter  extends RecyclerView.Adapter<AbraodInfoAdapter.AbraodInfoHolder> {

    private Context context ;
    private List<AbroadBean> abroadBeanList;
    private SelectListener selectListener ;
    public AbraodInfoAdapter(Context context , List<AbroadBean> abroadBeanList){
        this.context = context ;
        this.abroadBeanList = abroadBeanList ;
    }

    public void setSelectListener(SelectListener selectListener){
        this.selectListener = selectListener ;
        notifyDataSetChanged();
    }
    @Override
    public AbraodInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbraodInfoHolder(LayoutInflater.from(context).inflate(R.layout.item_abroad_info ,parent ,false));
    }

    @Override
    public void onBindViewHolder(AbraodInfoHolder holder, final int position) {
        AbroadBean abroadBean = abroadBeanList.get(position % abroadBeanList.size());
        AbroadBean.EditUserBean editUserBean = abroadBean.getEditUser() ;
        if (editUserBean.getFollow() == C.LGAttention){
            holder.attention.setVisibility(View.VISIBLE);
        }else{
            holder.attention.setVisibility(View.GONE);
        }

        if (position % abroadBeanList.size() == abroadBeanList.size() -1){
            holder.inter_line.setVisibility(View.GONE);
        }else{
            holder.inter_line.setVisibility(View.VISIBLE);
        }
        holder.name.setText(editUserBean.getNickname());
        new GlideUtils().load(context , NetworkTitle.DomainSmartApplyResourceNormal + editUserBean.getImage() ,holder.image);
        holder.title.setText(abroadBean.getTitle());
        holder.num.setText(abroadBean.getViewCount());
        holder.time.setText(abroadBean.getArticle());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListener.select(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return abroadBeanList == null ? 0:abroadBeanList.size();
    }

    static class AbraodInfoHolder extends RecyclerView.ViewHolder{
        private ImageView image ;
        private TextView time , name ,title , attention , num ;
        private RelativeLayout rl ;
        private View inter_line ;
        public AbraodInfoHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            time = (TextView) itemView.findViewById(R.id.time);
            name = (TextView) itemView.findViewById(R.id.name);
            title = (TextView) itemView.findViewById(R.id.title);
            attention = (TextView) itemView.findViewById(R.id.attention);
            num = (TextView) itemView.findViewById(R.id.num);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            inter_line = itemView.findViewById(R.id.inter_line);
        }
    }
}
