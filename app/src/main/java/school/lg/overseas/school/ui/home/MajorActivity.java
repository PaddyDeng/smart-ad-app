package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.adapter.MajorAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.MajorData;
import school.lg.overseas.school.bean.MajorLable;
import school.lg.overseas.school.bean.MajorLables;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 * 专业库
 */

public class MajorActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView title_list;
    private ViewPager list_view;
    private SwipeRefreshLayout refresh;
    private TitleAdapter titleAdapter;
    private List<RecyclerView> recyclerViews;
    private List<Integer> pages;//保存每页页数
    private Map<String,List<MajorData>> datas;//保存每页数据
    private List<Integer> tags;//记录每个专业的编号，用于viewpager添加fragment
    private List<TitleTag> titleTags;

    public static void start(Context context){
        Intent intent =new Intent(context,MajorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        findView();
        initTitle();
        initView();
        setClick();
    }

    private void initView() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int currentItem = list_view.getCurrentItem();
                pages.set(currentItem,1);
                initData(titleTags.get(currentItem).getId(),1,currentItem,false);
                refresh.setRefreshing(false);
            }
        });
        list_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                titleAdapter.setSelectPos(position);
                title_list.scrollToPosition(position);
                if(!tags.contains(position)){//不包含，说明未加载过该数据
                    initData(titleTags.get(position).getId(),1,position,false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setClick() {
        back.setOnClickListener(this);
    }

    private void initTitle() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SPECIALTYLIST, RequestMethod.POST);
        req.set("page","1").set("pageSize","10");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {


                    MajorLables majorLables = JSON.parseObject(response.get(), MajorLables.class);
                    List<MajorLable> titleData = majorLables.getCategory();
                    datas =new HashMap<>();
                    pages = new ArrayList<>();
                    recyclerViews =new ArrayList<>();
                    List<View> views =new ArrayList<>();
                    tags =new ArrayList<>();
                    titleTags = new ArrayList<>();
                    for (int i = 0; i <titleData.size() ; i++) {
                        final int u =i;
                        TitleTag titleTag = new TitleTag();
                        titleTag.setId(titleData.get(i).getId());
                        titleTag.setName(titleData.get(i).getName());
                        titleTags.add(titleTag);
                        pages.add(1);
                        View v = LayoutInflater.from(MajorActivity.this).inflate(R.layout.fragment_recycler, null);
                        LinearLayoutManager manager =new LinearLayoutManager(MajorActivity.this,LinearLayoutManager.VERTICAL,false);
                        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.item_list);
                        recyclerView.setLayoutManager(manager);
                        recyclerViews.add(recyclerView);
                        views.add(v);
                        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
                            @Override
                            public void onLoadMore(int currentPage) {
                                initData(titleTags.get(u).getId(),pages.get(u)+1,u,true);
                            }
                        });
                    }

                    LinearLayoutManager manager = new LinearLayoutManager(MajorActivity.this,LinearLayoutManager.HORIZONTAL,false);
                    title_list.setLayoutManager(manager);
                    titleAdapter =new TitleAdapter(MajorActivity.this, titleTags, new SelectListener() {
                        @Override
                        public void select(int position) {
                            list_view.setCurrentItem(position);
                        }
                    });
                    title_list.setAdapter(titleAdapter);


                    tags.add(0);
                    list_view.setAdapter(new MyViewPageAdapter(views));
                    List<MajorData> data = majorLables.getData().getData();
                    datas.put("0",data);
                    MajorAdapter majorAdapter = new MajorAdapter(MajorActivity.this, data);
                    recyclerViews.get(0).setAdapter(majorAdapter);
                    }catch (JSONException e){
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });

    }

    private void initData(String catId, final int page, final int type, final boolean isMore) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SPECIALTYLIST, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set("catId",catId);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                    MajorLables majorLables = JSON.parseObject(response.get(), MajorLables.class);
                    List<MajorData> data = majorLables.getData().getData();
                    if(isMore){
                        pages.set(type,page);
                        datas.get(type+"").addAll(data);
                        recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                    }else {
                        tags.add(type);
                        datas.put(type+"",data);
                        MajorAdapter majorAdapter = new MajorAdapter(MajorActivity.this, datas.get(type+""));
                        recyclerViews.get(type).setAdapter(majorAdapter);
                    }
                    }catch (JSONException e){

                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }

    private void findView() {
        RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
        back = (ImageView) title.findViewById(R.id.back);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("专业解析");
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        title_list = (RecyclerView) findViewById(R.id.title_list);
        list_view = (ViewPager) findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

}
