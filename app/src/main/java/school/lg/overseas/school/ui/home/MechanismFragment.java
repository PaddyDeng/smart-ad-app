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
import school.lg.overseas.school.adapter.MechanismAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HomeBanner;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.manager.AutoLinearLayoutManager;

/**
 * Created by Administrator on 2017/12/29.
 */

public class MechanismFragment extends BaseFragment implements View.OnClickListener {
    private TextView all,praise;
    private View v1,v2;
    private ViewPager viewPager;
    private int oldTag;
    private List<View> vs;
    private List<TextView> tvs;
    private List<View> views;
    private List<RecyclerView> recyclerViews;
    private List<TextView> agains;
    private List<Integer> pages;
    private List<Integer> tags;
    private Map<String,List<LittleData>> datas;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mechanism,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void setClick() {
        all.setOnClickListener(this);
        praise.setOnClickListener(this);
    }

    private void initView() {
        vs = new ArrayList<>();
        tvs =new ArrayList<>();
        views= new ArrayList<>();
        recyclerViews =new ArrayList<>();
        agains =new ArrayList<>();
        pages =new ArrayList<>();
        tags =new ArrayList<>();
        datas =new HashMap<>();
        vs.add(v1);
        vs.add(v2);
        tvs.add(all);tvs.add(praise);
        for (int i = 0; i < tvs.size(); i++) {
            final int u =i;
            pages.add(1);
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler, null);
            TextView again = (TextView) v.findViewById(R.id.again);
            RecyclerView item_list = (RecyclerView) v.findViewById(R.id.item_list);
            final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
            AutoLinearLayoutManager manage = new AutoLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            item_list.setLayoutManager(manage);
            agains.add(again);
            recyclerViews.add(item_list);
            views.add(v);
            item_list.addOnScrollListener(new EndLessOnScrollListener(manage) {
                @Override
                public void onLoadMore(int currentPage) {
                    initData(pages.get(u)+1,u,true);
                }
            });
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initData(1,oldTag,false);
                    refresh.setRefreshing(false);
                }
            });
        }
        setTitle(0);
        initData(1,0,false);
        viewPager.setAdapter(new MyViewPageAdapter(views));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                setTitle(position);
                if(!tags.contains(position)){
                    initData(1,position,false);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void initData( final int page, final int type, final boolean isMore) {
        ((BaseActivity)getActivity()).showLoadDialog("MechanismFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.MECHANISMLIST, RequestMethod.POST);
        req.set("sort",type+"").set("pageSize","10").set("page",page+"");
        ((MechanismActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("MechanismFragment");
                if(response.isSucceed()){
                    try {
                        HomeBanner homeBanner = JSON.parseObject(response.get(), HomeBanner.class);
                        List<LittleData> data = homeBanner.getData().getData();
                        if(isMore){
                            datas.get(type+"").addAll(data);
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                        }else{
                            tags.add(type);
                            datas.put(""+type,data);
                            MechanismAdapter adapter =new MechanismAdapter(getActivity(),datas.get(type+""));
                            recyclerViews.get(type).setAdapter(adapter);
                        }
                        pages.set(type,page);
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("MechanismFragment");
            }
        });
    }

    private void findView(View v) {
        all = (TextView) v.findViewById(R.id.all);
        praise = (TextView) v.findViewById(R.id.praise);
        v1 = v.findViewById(R.id.v1);
        v2 = v.findViewById(R.id.v2);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
    }
    private void setTitle(int i){
        vs.get(oldTag).setBackgroundColor(getContext().getResources().getColor(R.color.white));
        vs.get(i).setBackgroundColor(getContext().getResources().getColor(R.color.mainGreen));
        tvs.get(oldTag).setTextColor(getContext().getResources().getColor(R.color.black));
        tvs.get(i).setTextColor(getContext().getResources().getColor(R.color.mainGreen));
        oldTag=i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all:
                viewPager.setCurrentItem(0);
                setTitle(0);
                break;
            case R.id.praise:
                viewPager.setCurrentItem(1);
                setTitle(1);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("MechanismFragment");
    }
}
