package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/2/26.
 */

public class QuestionLableAdapter extends RecyclerView.Adapter<QuestionLableAdapter.ViewHolder>{
    private static final String TAG = QuestionLableAdapter.class.getSimpleName();
    private Context context;
    private List<TitleTag> datas;
    private SelectListener listener;
    private int selectP=0;
    private int[] bgs={R.mipmap.question_lable_bg_1,R.mipmap.question_lable_bg_2,R.mipmap.question_lable_bg_3,R.mipmap.question_lable_bg_4
            ,R.mipmap.question_lable_bg_5,R.mipmap.question_lable_bg_6,R.mipmap.question_lable_bg_7,R.mipmap.question_lable_bg_8};
    private int[] ivs={R.mipmap.question_lable_7,R.mipmap.question_lable_1,R.mipmap.question_lable_2,R.mipmap.question_lable_3,R.mipmap.question_lable_4,
            R.mipmap.question_lable_5,R.mipmap.question_lable_6,R.mipmap.question_lable_8};
    public QuestionLableAdapter(Context context, List<TitleTag> datas, SelectListener listener) {
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    public void setSelectP(int i){
        this.selectP=i;
        notifyDataSetChanged();
    }



    @Override
    public QuestionLableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_question_lable,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final QuestionLableAdapter.ViewHolder holder, final int position) {
        TitleTag data=datas.get(position);
        if (position == selectP){
            holder.side_iv.setVisibility(View.VISIBLE);
        }else{
            holder.side_iv.setVisibility(View.GONE);
        }
        if(position==datas.size()-1){
            holder.bg_iv.setBackgroundResource(bgs[bgs.length-1]);
            holder.iv.setBackgroundResource(ivs[ivs.length-1]);
        }else {
            holder.bg_iv.setBackgroundResource(bgs[position]);
            holder.iv.setBackgroundResource(ivs[position]);

        }
        holder.tv.setText(data.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectP=position;
//                holder.select_bg.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
//                if(position==datas.size()-1) listener.select(7);
//                else listener.select(position);
                listener.select(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return null==datas?0:datas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView bg_iv,side_iv,iv ;
        private final TextView tv;
        private final View rl;

        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            rl = v.findViewById(R.id.rl);
            bg_iv = (ImageView) v.findViewById(R.id.bg_iv);
            side_iv = (ImageView) v.findViewById(R.id.side_iv);
            iv = (ImageView) v.findViewById(R.id.iv);
            tv = (TextView) v.findViewById(R.id.tv);
        }
    }
}
