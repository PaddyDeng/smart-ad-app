package school.lg.overseas.school.ui.dicovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import school.lg.overseas.school.adapter.HotNewsAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.NewsAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.adapter.TitleBgAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HomeBanner;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.NewsData;
import school.lg.overseas.school.bean.NewsDatas;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.NewsSelectListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 发现
 */

public class NewsFragment extends BaseFragment{
    private static final String TAG = NewsFragment.class.getSimpleName() ;
    private RecyclerView hot_list,title_list;
    private ViewPager viewPager;
    private  List<TitleTag> titletags;
    private List<View> views;
    private List<Integer> pages;
    private List<RecyclerView> recyclerViews;
    private List<TextView> agains;
    private Map<String,List<NewsData>> datas;
    private NewsSelectListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initHot();
        initTitle(0,1,false,true);
    }
    public void setListener(NewsSelectListener listener){
        this.listener=listener;
    }

    public void refresh(){
        initTitle(0,1,false,false);
    }

    private void initTitle(final int type, final int page, final boolean isMore, final boolean isFirst) {
        ((BaseActivity)getActivity()).showLoadDialog("NewsFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.GOSSIPURL + NetworkChildren.POSTLIST, RequestMethod.POST);
        req.set("selectId",type==0?"14":titletags.get(type).getId()).set("page",page+"").set("pageSize","10");
        ((BaseActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("NewsFragment");
                if (response.isSucceed()) {
                    try {
                        NewsDatas newsDatas = JSON.parseObject(response.get(), NewsDatas.class);
                        List<NewsData> data = newsDatas.getData();
                        if (isFirst) {
                            datas.put(type+"", data);
                            titletags = newsDatas.getCatChild();
                            String titletagList=JSON.toJSONString(titletags);
//                            TitleTag titleTag = new TitleTag();
//                            titleTag.setId("14");
//                            titleTag.setName("全部");
//                            titletags.add(0, titleTag);
                            SharedPreferencesUtils.setNewsTags(getActivity(),titletagList);
                            final TitleBgAdapter titleAdapter = new TitleBgAdapter(getActivity(), titletags, new SelectListener() {
                                @Override
                                public void select(int position) {
                                    listener.setListener(titletags.get(position).getId());
                                    viewPager.setCurrentItem(position);
                                }
                            });
                            title_list.setAdapter(titleAdapter);
                            for (int i = 0; i < titletags.size(); i++) {
                                pages.add(1);
                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler, null);
                                final SwipeRefreshLayout refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
                                RecyclerView item_list = (RecyclerView) view.findViewById(R.id.item_list);
                                TextView again = (TextView) view.findViewById(R.id.again);
                                final int finalI = i;
                                refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        initTitle(finalI,1,false,false);
                                        refresh.setRefreshing(false);
                                    }
                                });
                                LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                item_list.setLayoutManager(manager);
                                item_list.addOnScrollListener(new EndLessOnScrollListener(manager) {
                                    @Override
                                    public void onLoadMore(int currentPage) {
                                        int currentItem = viewPager.getCurrentItem();
                                        initTitle(currentItem,pages.get(currentItem)+1,true,false);
                                    }
                                });
                                recyclerViews.add(item_list);
                                agains.add(again);
                                views.add(view);
                            }
                            NewsAdapter allAdapter = new NewsAdapter(getActivity(), datas.get(type+""));
                            recyclerViews.get(0).setAdapter(allAdapter);
                            viewPager.setAdapter(new MyViewPageAdapter(views));
                            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    titleAdapter.setSelectPos(position);
                                    if (!datas.containsKey(position + "")) {
                                        initTitle(position, pages.get(position), false, false);
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            });
                        }else if(isMore){
                            datas.get(type+"").addAll(data);
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                        }else{
                            datas.put(type+"",data);
                            NewsAdapter adapter =new NewsAdapter(getActivity(),datas.get(type+""));
                            recyclerViews.get(type).setAdapter(adapter);
                        }
                        pages.set(type,page);
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("NewsFragment");
            }
        });
    }

    private void initView() {
        views =new ArrayList<>();
        pages =new ArrayList<>();
        recyclerViews= new ArrayList<>();
        agains =new ArrayList<>();
        datas =new HashMap<>();
        LinearLayoutManager hotManage =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager titleManage =new GridLayoutManager(getActivity(),2);
        hot_list.setLayoutManager(hotManage);
        title_list.setLayoutManager(titleManage);
    }

    private void initHot() {
        ((BaseActivity)getActivity()).showLoadDialog("NewsFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyResourceNormal+ NetworkChildren.HOMEBANNER, RequestMethod.POST);
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("NewsFragment");
                if(response.isSucceed()) {
                    try {
                        final HomeBanner homeBanner = JSON.parseObject(response.get(), HomeBanner.class);
                        List<LittleData> data = homeBanner.getData().getData();
                        HotNewsAdapter adapter =new HotNewsAdapter(getActivity(),data);
                        hot_list.setAdapter(adapter);
                    }catch (JSONException e){

                    }

                }
            }
            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("NewsFragment");
            }
        });
    }


    private void findView(View v) {
        hot_list = (RecyclerView) v.findViewById(R.id.hot_list);
        title_list = (RecyclerView) v.findViewById(R.id.title_list);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isPost=SharedPreferencesUtils.getPost(getActivity(),"News");
        if(isPost){
            SharedPreferencesUtils.setPost(getActivity(),"News",false);
            initTitle(0,1,false,true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("NewsFragment");
    }
}
