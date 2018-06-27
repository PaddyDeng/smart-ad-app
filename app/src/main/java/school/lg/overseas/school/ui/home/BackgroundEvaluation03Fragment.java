package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BackgroundEvaluation03Fragment extends BaseFragment {
    private TextView num;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_eavluation_03,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        num = (TextView) view.findViewById(R.id.num);

    }

}
