package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.ApplyChildData;
import school.lg.overseas.school.bean.Conf;
import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.VideoActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.WaitUtils;
import school.lg.overseas.school.utils.XmlUtils;
import school.lg.overseas.school.view.TopRoundImageView;

/**
 * Created by Administrator on 2017/12/20.
 */

public class KnowApplyLittleAdapter extends RecyclerView.Adapter<KnowApplyLittleAdapter.ViewHolder>{
    private Context context;
    private List<ApplyChildData> datas;
    private boolean canClick=true;
    public KnowApplyLittleAdapter(Context context, List<ApplyChildData> datas) {
        this.context =context;
        this.datas =datas;
    }

    @Override
    public KnowApplyLittleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_know_apply_little,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(KnowApplyLittleAdapter.ViewHolder holder, final int position) {
        final ApplyChildData data = datas.get(position);
        holder.name.setText(data.getName());
        holder.time.setText(data.getDuration());
        holder.num.setText(data.getViewCount());
        new GlideUtils().load(context, NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),holder.iv);
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login userInfo = SharedPreferencesUtils.getUserInfo(context);
                if(null==userInfo||TextUtils.isEmpty(userInfo.getUid())){
                    LoginHelper.needLogin(context,"需要先登录才能继续哦");
                    return;
                }
                if(canClick) {
                    canClick = false;
                    final String url = data.getUrl();
                    WaitUtils.show(context,"KnowApplyLittleAdapter");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Conf conf = XmlUtils.main(url);
                            canClick=true;
                            if(WaitUtils.isRunning("KnowApplyLittleAdapter")){
                                WaitUtils.dismiss("KnowApplyLittleAdapter");
                            }
                            VideoActivity.start(context, data.getTitle(), conf, data.getId());
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TopRoundImageView iv;
        private TextView name,time,num;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            iv = (TopRoundImageView) v.findViewById(R.id.iv);
            name = (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
            num = (TextView) v.findViewById(R.id.num);
            rl = v.findViewById(R.id.rl);
        }
    }
}
