package school.lg.overseas.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.ui.communication.QuestionDetailActivity;
import school.lg.overseas.school.ui.home.ActDetailActivity;
import school.lg.overseas.school.ui.home.MajorDetailsActivity;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;
import school.lg.overseas.school.ui.other.KnowledgeDetailActivity;
import school.lg.overseas.school.ui.other.LatelyDeleteDialog;
import school.lg.overseas.school.ui.other.MajorDialog;
import school.lg.overseas.school.utils.TimeUtils;

/**
 * Created by Administrator on 2018/1/16.
 */

public class LatelyAdapter extends RecyclerView.Adapter<LatelyAdapter.ViewHolder>{
    private Context context;
    private List<Reading> datas;

    public LatelyAdapter(Context context, List<Reading> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public LatelyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_lately,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(LatelyAdapter.ViewHolder holder, int position) {
        final Reading data = datas.get(position);
        holder.name.setText(data.getTitle());
        String s = TimeUtils.longToString(Long.valueOf(data.getTime()), "yyyy-MM-dd HH:mm");
        holder.time.setText(s);
        holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LatelyDeleteDialog dialog =new LatelyDeleteDialog(context,R.style.AlphaDialogAct);
                dialog.setContext("确定删除该记录？",data.getId(),data.getType());
                dialog.show();
                return true;
            }
        });
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data.getType()){
                    case 0:
                        Intent schoolIntent =new Intent(context,SchoolDetailsActivity.class);
                        schoolIntent.putExtra("id",data.getId());
                        context.startActivity(schoolIntent);
                        break;
                    case 1:
//                        Intent majorIntent =new Intent(context, MajorDetailsActivity.class);
//                        majorIntent.putExtra("id",data.getId());
//                        context.startActivity(majorIntent);
                        MajorDialog dialog =new MajorDialog(context,R.style.AlphaDialogAct);
                        dialog.show();
                        dialog.setData(data);
                        break;
                    case 2:
                        QuestionDetailActivity.start(context,data.getId());
                        break;
                    case 3:
                        ActDetailActivity.start(context,data.getId());
                        break;
                    case 4:
                        KnowledgeDetailActivity.start(context,data.getId());
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,time;
        private View rl;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl=v.findViewById(R.id.rl);
            name= (TextView) v.findViewById(R.id.name);
            time = (TextView) v.findViewById(R.id.time);
        }
    }
}
