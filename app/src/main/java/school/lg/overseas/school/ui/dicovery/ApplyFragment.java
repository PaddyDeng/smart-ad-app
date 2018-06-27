package school.lg.overseas.school.ui.dicovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.ApplyAdapter;
import school.lg.overseas.school.adapter.ApplyLittleAdapter;
import school.lg.overseas.school.adapter.ApplyUnAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.RankingTitleAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.bean.Know;
import school.lg.overseas.school.bean.RankingTitles;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.RankingActivity;

/**
 * 申请指南
 */

public class ApplyFragment extends BaseFragment{
    private static final String TAG = ApplyFragment.class.getSimpleName();
    private RecyclerView title_list;
    private ViewPager viewPager;
    private List<TitleTag> titleTags;
    private List<View> views;
    private List<RecyclerView> recyclerViews;
    private List<Apply> classes ;  // 大学排名数据
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apply,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initData();
        initData(true);
    }


    private void initView() {
        LinearLayoutManager titleManager =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(titleManager);
        titleTags = new ArrayList<>();
        views=new ArrayList<>();
        recyclerViews =new ArrayList<>();
    }

    private void initData(final boolean isFirst) {
        ((BaseActivity)getActivity()).showLoadDialog("ApplyFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.KONW, RequestMethod.POST);
        ((BaseActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("ApplyFragment");
                if(response.isSucceed()){
                    try {
                        Know know = JSON.parseObject(response.get(), Know.class);
                        List<Apply> data = know.getApply();
                        titleTags.clear();
                        if(null!=data&&data.size()!=0){
                            if(isFirst){
                                for(final Apply apply:data){
                                    ApplyAdapter applyAdapter = null ;
                                    TitleTag titleTag =new TitleTag();
                                    titleTag.setId(apply.getId());
                                    titleTag.setName(apply.getName());
                                    titleTags.add(titleTag);
                                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler, null);
                                    RecyclerView item_list = (RecyclerView) view.findViewById(R.id.item_list);
                                    final SwipeRefreshLayout refresh= (SwipeRefreshLayout) view.findViewById(R.id.refresh);
                                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    item_list.setLayoutManager(manager);
                                    refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            if ("申请前".equals(apply.getName())) {
                                                initData();
                                            }
                                            initData(true);
                                            refresh.setRefreshing(false);
                                        }
                                    });
                                    if ("申请前".equals(apply.getName())) {
                                        applyAdapter = new ApplyAdapter(getActivity(), apply.getChild(), classes);
                                    }else{
                                        applyAdapter = new ApplyAdapter(getActivity(), apply.getChild(), null);
                                    }
                                    item_list.setAdapter(applyAdapter);
                                    recyclerViews.add(item_list);
                                    views.add(view);
                                }
                                final TitleAdapter titleAdapter =new TitleAdapter(getActivity(), titleTags, new SelectListener() {
                                    @Override
                                    public void select(int position) {
                                        viewPager.setCurrentItem(position);
                                    }
                                });
                                title_list.setAdapter(titleAdapter);
                                viewPager.setAdapter(new MyViewPageAdapter(views));
                                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        titleAdapter.setSelectPos(position);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {
                                    }
                                });
                            }else{

                            }
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("ApplyFragment");
            }
        });
    }
    //  获取大学排名数据
    private void initData() {
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.RANKINGTITLE);
        ((BaseActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.isSucceed()){
                    try {
                        RankingTitles rankingTitles = JSON.parseObject(response.get(), RankingTitles.class);
                        classes = rankingTitles.getClasses();
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }
        });
    }

    private void findView(View v) {
        title_list = (RecyclerView) v.findViewById(R.id.title_list);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("ApplyFragment");
    }
}
