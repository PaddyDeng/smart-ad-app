package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;

/**
 * Created by Administrator on 2018/6/19.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotHolder> {

    private Context context ;
    private List<AbroadHomeBean.ToutiaoBean>  toutiaoBeanList ;
    private SelectListener selectListener ;
    public HotAdapter(Context context , List<AbroadHomeBean.ToutiaoBean> toutiaoBeanList){
        this.context = context ;
        this.toutiaoBeanList = toutiaoBeanList ;
    }

    public void setSelectListener(SelectListener selectListener){
        this.selectListener = selectListener ;
    }
    @Override
    public HotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotHolder(LayoutInflater.from(context).inflate(R.layout.item_abroad_hot_info ,parent , false));
    }

    @Override
    public void onBindViewHolder(HotHolder holder, final int position) {
        AbroadHomeBean.ToutiaoBean toutiaoBean = toutiaoBeanList.get(position % toutiaoBeanList.size());
        holder.title.setText(toutiaoBean.getTitle());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListener.select(position % toutiaoBeanList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return toutiaoBeanList == null ? 0 : toutiaoBeanList.size();
    }

    static class HotHolder extends RecyclerView.ViewHolder{
        private TextView title ;
        private AutoLinearLayout ll ;
        public HotHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            ll = (AutoLinearLayout) itemView.findViewById(R.id.ll);
        }
    }
}
