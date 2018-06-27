package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;

/**
 * Created by Administrator on 2018/2/2.
 */

public class Guide03Fragment extends BaseFragment{
    private ImageView iv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_guide03,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       iv= (ImageView) view.findViewById(R.id.iv);
        Glide.with(getActivity()).load(R.mipmap.guide_3).into(iv);
    }
}
