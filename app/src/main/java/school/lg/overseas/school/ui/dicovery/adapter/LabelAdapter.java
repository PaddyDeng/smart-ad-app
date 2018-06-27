package school.lg.overseas.school.ui.dicovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import school.lg.overseas.school.R;

/**
 * Created by Administrator on 2018/6/20.
 */

public class LabelAdapter  extends RecyclerView.Adapter<LabelAdapter.LabelHolder>{

    private Context context ;
    private List<String> labels ;

    public LabelAdapter(Context context , List<String> labels){
        this.context = context ;
        this.labels = labels ;
    }

    @Override
    public LabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new LabelHolder(LayoutInflater.from(context).inflate(R.layout.item_label ,parent , false));
    }

    @Override
    public void onBindViewHolder(LabelHolder holder, int position) {
        String label  = labels.get(position % labels.size());
        holder.label.setText(label);
    }

    @Override
    public int getItemCount() {
        return labels == null ? 0 : labels.size();
    }

    static class LabelHolder extends RecyclerView.ViewHolder{
        private TextView label ;
        public LabelHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
