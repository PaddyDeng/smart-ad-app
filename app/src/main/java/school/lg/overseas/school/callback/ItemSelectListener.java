package school.lg.overseas.school.callback;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2018/6/29.
 */

public interface ItemSelectListener {
    void itemSelectListener(RecyclerView.ViewHolder holder , int poisition);

    void select(int poistion);
}
