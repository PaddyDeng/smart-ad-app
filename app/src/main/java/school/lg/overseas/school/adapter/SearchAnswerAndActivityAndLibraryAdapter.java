package school.lg.overseas.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.AnswerAndActivityAndLibrary;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.ui.communication.QuestionDetailActivity;
import school.lg.overseas.school.ui.home.ActDetailActivity;
import school.lg.overseas.school.ui.home.MajorDetailsActivity;
import school.lg.overseas.school.ui.other.KnowledgeDetailActivity;
import school.lg.overseas.school.utils.HtmlReplaceUtils;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SearchAnswerAndActivityAndLibraryAdapter extends RecyclerView.Adapter<SearchAnswerAndActivityAndLibraryAdapter.ViewHolder>{
    private Context context;
    private List<AnswerAndActivityAndLibrary> datas;
    private int tag;
    public SearchAnswerAndActivityAndLibraryAdapter(Context context, List<AnswerAndActivityAndLibrary>datas,int tag) {
        this.context =context;
        this.datas =datas;
        this.tag =tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        ViewHolder holder =new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AnswerAndActivityAndLibrary data = datas.get(position);
        switch (tag){
            case 1:
                holder.cn_name.setText(data.getName());
                holder.en_name.setText(data.getAnswer());
                break;
            case 2:
                holder.cn_name.setText(data.getQuestion());
                holder.en_name.setText(HtmlReplaceUtils.replaceAllToLable(data.getContent()));
                break;
            case 3:
                holder.cn_name.setText(data.getName());
                holder.en_name.setText(data.getAnswer());
                break;
            case 4:
                holder.cn_name.setText(data.getName());
                holder.en_name.setText(data.getCreateTime());
                break;
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tag){
                    case 1:
                        MajorDetailsActivity.start(context,data.getId());
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
        private TextView cn_name,en_name;
        private View ll;
        public ViewHolder(View v) {
            super(v);
            AutoUtils.autoSize(v);
            cn_name = (TextView) v.findViewById(R.id.cn_name);
            en_name = (TextView) v.findViewById(R.id.en_name);
            ll = v.findViewById(R.id.ll);
        }
    }
}
