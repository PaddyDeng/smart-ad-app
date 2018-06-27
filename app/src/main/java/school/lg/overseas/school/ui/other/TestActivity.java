package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;


import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;

/**
 * Created by Administrator on 2018/1/13.
 */

public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();
    }

    private void initData() {

    }
}
