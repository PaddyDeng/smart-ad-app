package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.ui.home.RankingSubitemActivity;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RankingTitleUnAdapter extends RecyclerView.Adapter<RankingTitleUnAdapter.ViewHolder>{
    private static final String TAG = RankingTitleUnAdapter.class.getSimpleName();
    private Context context;
    private List<Apply> datas;

    public RankingTitleUnAdapter(Context context, List<Apply> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RankingTitleUnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_apply_little,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingTitleUnAdapter.ViewHolder holder, int position) {
        final Apply data = datas.get(position);
        Log.e(TAG, "onBindViewHolder: " + data.getName() );
        holder.name.setText(data.getName());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingSubitemActivity.start(context,data.getId(),data.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private View ll,v1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            name = (TextView) v.findViewById(R.id.name);
            ll = v.findViewById(R.id.ll);
            v1 = v.findViewById(R.id.v1);
        }
    }
}
