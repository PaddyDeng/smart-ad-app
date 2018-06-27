package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.AdviserAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.AdiviserItem;
import school.lg.overseas.school.bean.AdiviserList;
import school.lg.overseas.school.bean.AdiviserTitles;
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 * Created by Administrator on 2017/12/29.
 */

public class AdviserFragment extends BaseFragment{
    private RecyclerView title_list;
    private ViewPager viewPager;
    private List<TitleTag> titles;
    private List<View> views;
    private List<TextView> agains;
    private List<RecyclerView> recyclerViews;
    private List<Integer> pages;
    private Map<String,List<AdiviserItem>> datas;
    private TitleAdapter titleAdapter;
    private int oldTag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adviser,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initTitle();
    }

    private void initView() {
        LinearLayoutManager titleManager =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(titleManager);
    }

    private void initTitle() {
        ((BaseActivity)getActivity()).showLoadDialog("AdviserFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ADVISERTITLE, RequestMethod.POST);
        ((MechanismActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("AdviserFragment");
                if(response.isSucceed()){
                    try {
                        AdiviserTitles adiviserTitles = JSON.parseObject(response.get(), AdiviserTitles.class);
                        List<Country> country = adiviserTitles.getCountry();
                        titles =new ArrayList<>();
                        views= new ArrayList<>();
                        agains =new ArrayList<>();
                        recyclerViews =new ArrayList<>();
                        pages =new ArrayList<>();
                        datas =new HashMap<>();
                        for (int i = 0; i < country.size()+1; i++) {
                            final int u =i;
                            pages.add(1);
                            TitleTag titleTag =new TitleTag();
                            if(i==0){
                                titleTag.setId("0");
                                titleTag.setName("全部");
                            }else {
                                titleTag.setId(country.get(i - 1).getId());
                                titleTag.setName(country.get(i - 1).getName());
                            }
                            titles.add(titleTag);

                            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler, null);
                            TextView again = (TextView) v.findViewById(R.id.again);
                            RecyclerView item_list = (RecyclerView) v.findViewById(R.id.item_list);
                            final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
                            LinearLayoutManager manager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            item_list.setLayoutManager(manager);
                            item_list.addOnScrollListener(new EndLessOnScrollListener(manager) {
                                @Override
                                public void onLoadMore(int currentPage) {
                                    initData(pages.get(u)+1,u,1);
                                }
                            });
                            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    initData(1,oldTag,2);
                                    refresh.setRefreshing(false);
                                }
                            });
                            agains.add(again);
                            recyclerViews.add(item_list);
                            views.add(v);
                        }
                        viewPager.setAdapter(new MyViewPageAdapter(views));
                        initData(1,0,0);

                        titleAdapter =new TitleAdapter(getActivity(), titles, new SelectListener() {
                            @Override
                            public void select(int position) {
                                oldTag =position;
                                viewPager.setCurrentItem(position);
                            }
                        });
                        title_list.setAdapter(titleAdapter);
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("AdviserFragment");
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                titleAdapter.setSelectPos(position);
                title_list.scrollToPosition(position);
                oldTag=position;
                if(!datas.containsKey(position+"")){
                    initData(1,position,0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //style 0初始化，1加载更多，2刷新
    private void initData(final int page, final int type, final int style){
        ((BaseActivity)getActivity()).showLoadDialog("AdviserFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ADVISERLIST, RequestMethod.POST);
        req.set("country",titles.get(type).getId()).set("page",page+"").set("pageSize","10");
        ((MechanismActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("AdviserFragment");
                if(response.isSucceed()){
                    try {
                        AdiviserList adiviserList = JSON.parseObject(response.get(), AdiviserList.class);
                        List<AdiviserItem> data = adiviserList.getData();
                        if(style==0){
                            datas.put(type+"",data);
                            AdviserAdapter adapter =new AdviserAdapter(getActivity(), datas.get(type+""));
                            recyclerViews.get(type).setAdapter(adapter);
                        }else{
                            if(style==1) {
                                datas.get(type + "").addAll(data);
                            }else{
                                datas.put(type+"",data);
                            }
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                        }
                        pages.set(type,page);
                    }catch (JSONException e){

                    }


                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("AdviserFragment");
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
        ((BaseActivity)getActivity()).dismissLoadDialog("AdviserFragment");
    }
}
