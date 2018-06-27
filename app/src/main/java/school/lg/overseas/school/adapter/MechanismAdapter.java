package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.MechanismDetailActivity;
import school.lg.overseas.school.ui.other.OnlineActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlReplaceUtils;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * Created by Administrator on 2017/12/29.
 */

public class MechanismAdapter extends RecyclerView.Adapter<MechanismAdapter.ViewHolder>{
    private Context context;
    private List<LittleData> datas;

    public MechanismAdapter(Context context, List<LittleData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MechanismAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_mechanism, parent, false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MechanismAdapter.ViewHolder holder, int position) {
        final LittleData data = datas.get(position);
        new GlideUtils().load(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.name.setText(data.getName());
        //好评度

        holder.content.setText(HtmlReplaceUtils.replaceAllToCharacter(data.getDescription()));
        holder.num.setText(HtmlUtil.fromHtml("已有<font color=\"#6f9701\">"+data.getCnName()+"</font>人咨询"));

        holder.appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //咨询
                Intent intent =new Intent(context, OnlineActivity.class);
                context.startActivity(intent);
            }
        });
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MechanismDetailActivity.start(context,data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView name,content,num;
        private LinearLayout star_ll;
        private RelativeLayout appointment;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            iv = (ImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            star_ll = (LinearLayout) v.findViewById(R.id.star_ll);
            content = (TextView) v.findViewById(R.id.content);
            appointment = (RelativeLayout) v.findViewById(R.id.appointment);
            num = (TextView) v.findViewById(R.id.num);
        }
    }
}
