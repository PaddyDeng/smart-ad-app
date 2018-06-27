package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.bean.RankingTitles;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.RankingActivity;

/**
 * Created by Administrator on 2018/1/9.
 */

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder>{
    private Context context;
    private List<Apply> datas;
    private List<Apply> classes ;

    public ApplyAdapter(Context context, List<Apply> datas ,List<Apply> classes) {
        this.context = context;
        this.datas = datas;
        this.classes = classes ;
    }

    @Override
    public ApplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_apply,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ApplyAdapter.ViewHolder holder, int position) {
        Apply data = datas.get(position);
        holder.v1.setVisibility(View.GONE);
        holder.ll.setBackgroundColor(context.getResources().getColor(R.color.gray_white));
        holder.title.setText(data.getName());
        LinearLayoutManager manager =new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.list_view.setLayoutManager(manager);
        if (classes!= null && "大学排名".equals(data.getName())){
            RankingTitleUnAdapter littleAdapter =new RankingTitleUnAdapter(context,classes);
            holder.list_view.setAdapter(littleAdapter);
        }else {
            ApplyLittleAdapter littleAdapter  = new ApplyLittleAdapter(context, data.getData());
            holder.list_view.setAdapter(littleAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private RecyclerView list_view;
        private View ll,v1;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            ll = v.findViewById(R.id.ll);
            title = (TextView) v.findViewById(R.id.title);
            list_view = (RecyclerView) v.findViewById(R.id.plan_itme_list);
            v1 = v.findViewById(R.id.v1);
        }
    }
}
