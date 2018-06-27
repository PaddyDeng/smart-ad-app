package school.lg.overseas.school.ui.dicovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HomeBanner;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.callback.NewsSelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.view.carouselFigure.ImageSlideshow;

/**
 * 留学社区
 */

public class DiscoveryFragment extends BaseFragment implements View.OnClickListener {
    private TextView complaints_title,app_guide_title , abroad_info;  //discover_title
    private ImageView release_iv;
    private List<TextView> titles;
    private List<BaseFragment> fragments;
    private int oldTitle=2;
    private String catId;
    private NewsFragment newsFragment;
    private AbroadInfoFragment abroadInfoFragment ;
//    private  ComplaintsFragment complaintsFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initData();
        initView();
        setClick();
    }

    private void setClick() {
        complaints_title.setOnClickListener(this);
//        discover_title.setOnClickListener(this);
        app_guide_title.setOnClickListener(this);
        release_iv.setOnClickListener(this);
        abroad_info.setOnClickListener(this);
    }

    private void initView() {
        titles= new ArrayList<>();
        titles.add(complaints_title);
//        titles.add(discover_title);
        titles.add(app_guide_title);
        titles.add(abroad_info);
        setTitle(oldTitle);
    }

    private void initData() {
        fragments =new ArrayList<>();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        newsFragment =new NewsFragment();
//        complaintsFragment = new ComplaintsFragment();
        ApplyFragment applyFragment = new ApplyFragment();
        abroadInfoFragment = new AbroadInfoFragment();
        newsFragment.setListener(new NewsSelectListener() {
            @Override
            public void setListener(String id) {
                catId=id;
            }
        });
        fragments.add(newsFragment);
//        fragments.add(complaintsFragment);
        fragments.add(applyFragment);
        fragments.add(abroadInfoFragment);
        ft.add(R.id.fl,newsFragment).add(R.id.fl,applyFragment).add(R.id.fl ,abroadInfoFragment);  //.add(R.id.fl,complaintsFragment)
        ft.hide(applyFragment);  //.hide(complaintsFragment)
        ft.hide(newsFragment);
        ft.commit();
    }

    private void findView(View v) {
        complaints_title = (TextView) v.findViewById(R.id.complaints_title);
//        discover_title = (TextView) v.findViewById(R.id.discover_title);
        app_guide_title = (TextView) v.findViewById(R.id.app_guide_title);
        release_iv = (ImageView) v.findViewById(R.id.release_iv);
        abroad_info = (TextView) v.findViewById(R.id.abroad_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complaints_title:
                setTitle(0);
                break;
//            case R.id.discover_title:
//                setTitle(1);
//                break;
            case R.id.app_guide_title:
                setTitle(1);
                break;
            case R.id.release_iv:
                if(!SharedPreferencesUtils.isLogin(getActivity())){
                    LoginHelper.needLogin(getActivity(),"需要登录才能发布哦");
                    return;
                }
                if(oldTitle==0){
                    Intent intent =new Intent(getActivity(),ReleasePostActivity.class);
                    intent.putExtra("id",catId);
                    startActivityForResult(intent,101);
                }else{
                    Intent intent = new Intent(getActivity(),ReleaseGossipActivity.class);
                    startActivityForResult(intent,103);
                }
                break;
            case R.id.abroad_info:
                setTitle(2);
                break;
        }
    }

    private void setTitle(int i){
        titles.get(oldTitle).setSelected(false);
        titles.get(oldTitle).setTextColor(getResources().getColor(R.color.white));
        titles.get(i).setSelected(true);
        titles.get(i).setTextColor(getResources().getColor(R.color.mainGreen));
        getChildFragmentManager().beginTransaction().hide(fragments.get(oldTitle)).show(fragments.get(i)).commit();
        oldTitle=i;
        if(i ==1 || i == 2){
            release_iv.setVisibility(View.GONE);
        }else{
            release_iv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==103){
            if(resultCode==104){
//                complaintsFragment.refresh();
            }
        }
        if(requestCode==101){
            if(resultCode==102){
                newsFragment.refresh();
            }
        }
    }

}
