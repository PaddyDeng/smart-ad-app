package school.lg.overseas.school.ui.other;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;

/**
 * Created by Administrator on 2018/1/16.
 */

public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private List<BaseFragment> fragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SharedPreferences guide = getSharedPreferences("guide", MODE_PRIVATE);
        SharedPreferences.Editor edit = guide.edit();
        edit.putBoolean("isFirst",true);
        edit.commit();
        fragments =new ArrayList<>();
        fragments.add(new Guide01Fragment());
        fragments.add(new Guide02Fragment());
        fragments.add(new Guide03Fragment());
        fragments.add(new Guide04Fragment());
        fragments.add(new Guide05Fragment());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
