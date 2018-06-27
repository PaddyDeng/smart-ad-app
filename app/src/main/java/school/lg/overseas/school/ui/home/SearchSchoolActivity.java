package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.SchoolAdapter;
import school.lg.overseas.school.adapter.ScreenCountryAdapter;
import school.lg.overseas.school.adapter.ScreenMajorContentAdapter;
import school.lg.overseas.school.adapter.ScreenMajorTitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ScreenData;
import school.lg.overseas.school.bean.ScreenLittleData;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.bean.SearchSchoolList;
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.callback.RecyclerSelectListener01;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.MajorDialog;
import school.lg.overseas.school.ui.other.WebActivity;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 查院校
 */

public class SearchSchoolActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView country_tv,ranking_tv,major_tv,nothing;
    private RelativeLayout country_rl,ranking_rl,major_rl,title;
    private SwipeRefreshLayout refresh;
    private View country_v,ranking_v,major_v,v1;
    private RecyclerView list_view,country_list,ranking_list,major_title,major_content;
    private LinearLayout major_list;
    private int tag=-1;
    private List<View> titleViews;
    private PopupWindow popupWindow;
    private List<ScreenLittleData> country,rank,major;
    private int countryTemp=0,rankTemp=-1,majorTitleTemp=-1,majorContentTemp=-1,countryNow=-1,rankNow=-1,majorTitleNow=0,majorContentNow=-1;
    private boolean dataOk,popOk;
    private String schoolName;
    private Reading reading;
    private boolean isColle=false;
    private List<ScreenLittleData> child;
    private int pages;
    private List<SearchSchool> schoolDatas;
    private LinearLayoutManager schoolManager;
    private EndLessOnScrollListener endLessOnScrollListener;

    public static void start(Context context){
        Intent intent =new Intent(context,SearchSchoolActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        findView();
        schoolManager =new LinearLayoutManager(SearchSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(schoolManager);
        schoolDatas =new ArrayList<>();
        initPop();
        initData(1,false,true);
        setClick();
    }


    private void initPop() {
        View popView = getLayoutInflater().inflate(R.layout.pop_search_school, null);
        country_list = (RecyclerView) popView.findViewById(R.id.country_list);
        ranking_list = (RecyclerView) popView.findViewById(R.id.ranking_list);
        major_list = (LinearLayout) popView.findViewById(R.id.major_list);
        major_title = (RecyclerView) popView.findViewById(R.id.major_title);
        major_content = (RecyclerView) popView.findViewById(R.id.major_content);
        TextView n = (TextView) popView.findViewById(R.id.n);
        TextView y = (TextView) popView.findViewById(R.id.y);
        LinearLayoutManager countryManager =new LinearLayoutManager(SearchSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        country_list.setLayoutManager(countryManager);
        LinearLayoutManager rankingManager =new LinearLayoutManager(SearchSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        ranking_list.setLayoutManager(rankingManager);
        LinearLayoutManager majorTitleManager =new LinearLayoutManager(SearchSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        major_title.setLayoutManager(majorTitleManager);
        LinearLayoutManager majorContentManager = new LinearLayoutManager(SearchSchoolActivity.this,LinearLayoutManager.VERTICAL,false);
        major_content.setLayoutManager(majorContentManager);
        initScreen();
        popupWindow = new PopupWindow(popView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSelect(tag);
                setTitleSelect(tag);
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                countryNow=countryTemp;
                rankNow=rankTemp;
                majorContentNow=majorContentTemp;
                if(countryNow!=-1)country_tv.setText(country.get(countryNow).getName());
                if(rankNow!=-1)ranking_tv.setText(rank.get(rankNow).getName());
                if(majorContentNow!=-1)major_tv.setText(major.get(majorTitleNow==-1?0:majorTitleNow).getChild().get(majorContentNow).getName());
                setScreenSelect(tag);
                setTitleSelect(tag);
                initData(1,false,false);
            }
        });
    }

    //获取筛选列表数据
    private void initScreen() {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLSCREEN);
        request(0, req, new SimpleResponseListener<String>() {

            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                popOk=true;
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        ScreenData screenData = JSON.parseObject(response.get(), ScreenData.class);
                        country = screenData.getCountry();
                        rank = screenData.getRank();
                        major = screenData.getMajor();
                        ScreenCountryAdapter countryAdapter = new ScreenCountryAdapter(SearchSchoolActivity.this, country,0, new SelectListener() {
                            @Override
                            public void select(int position) {
                                countryTemp=position;
                            }
                        });
                        country_list.setAdapter(countryAdapter);
                        ScreenCountryAdapter rankAdapter = new ScreenCountryAdapter(SearchSchoolActivity.this, rank,1, new SelectListener() {
                            @Override
                            public void select(int position) {
                                rankTemp=position;
                            }
                        });
                        ranking_list.setAdapter(rankAdapter);
                        child = major.get(0).getChild();
                        ScreenMajorContentAdapter majorContentAdapter =new ScreenMajorContentAdapter(SearchSchoolActivity.this, child, new SelectListener() {
                            @Override
                            public void select(int position) {
                                majorTitleNow=majorTitleTemp;
                                majorContentTemp=position;
                            }
                        });
                        major_content.setAdapter(majorContentAdapter);
                        ScreenMajorTitleAdapter majorTitleAdapter =new ScreenMajorTitleAdapter(SearchSchoolActivity.this, major, new SelectListener() {
                            @Override
                            public void select(int position) {
                                majorTitleTemp=position;
                                child=major.get(position).getChild();
                                ScreenMajorContentAdapter majorContentAdapter =new ScreenMajorContentAdapter(SearchSchoolActivity.this, child, new SelectListener() {
                                    @Override
                                    public void select(int position) {
                                        majorTitleNow=majorTitleTemp;
                                        majorContentTemp=position;
                                    }
                                });
                                major_content.setAdapter(majorContentAdapter);
                            }
                        });
                        major_title.setAdapter(majorTitleAdapter);
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

    private void setClick() {
        back.setOnClickListener(this);
        country_v.setOnClickListener(this);
        ranking_v.setOnClickListener(this);
        major_v.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1,false,false);
                refresh.setRefreshing(false);
            }
        });
    }

    /**
     *
     * @param page 页数
     * @param isMore 加载更多
     * @param isFirst 是否第一次加载
     */
    private void initData(final int page, final boolean isMore, final boolean isFirst) {
        dataOk=false;
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLLIST, RequestMethod.POST);
        req.set("country",countryNow==-1?"155":country.get(countryNow).getId())
                .set("rank",rankNow==-1?"":rank.get(rankNow).getId())
                .set("major",majorContentNow==-1?"":major.get(majorTitleNow==-1?0:majorTitleNow).getChild().get(majorContentNow).getId())
                .set("page",page+"")
                .set("pageSize","10");
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.isSucceed()) {
                    try {
                        final SearchSchoolList schoolList = JSON.parseObject(response.get(), SearchSchoolList.class);
                        if (null == schoolList.getData() || schoolList.getData().size() == 0) {
                            nothing.setVisibility(View.VISIBLE);
                        } else {
                            nothing.setVisibility(View.GONE);
                        }
                        boolean b;
                        if (majorContentNow != -1) {
                            b = true;
                        } else {
                            b = false;
                        }
                        List<SearchSchool> schoolData = schoolList.getData();
                        if (isMore) {
                            schoolDatas.addAll(schoolData);
                            list_view.getAdapter().notifyDataSetChanged();
                        } else if(isFirst){
                            schoolDatas=schoolData;
                            if(null!=endLessOnScrollListener)
                                list_view.removeOnScrollListener(endLessOnScrollListener);
                            endLessOnScrollListener = new EndLessOnScrollListener(schoolManager) {
                                @Override
                                public void onLoadMore(int currentPage) {
                                    Log.i("第一次的more",pages+1+"");
                                    initData(pages+1,true,false);
                                }
                            };
                            list_view.addOnScrollListener(endLessOnScrollListener);
                            SchoolAdapter schoolAdapter = new SchoolAdapter(SearchSchoolActivity.this, schoolDatas, b, new RecyclerSelectListener01() {
                                @Override
                                public void setListener(int titleId, int contentId) {
                                    popupWindow.dismiss();
                                    schoolName = schoolList.getData().get(titleId).getName();
                                    SearchSchoolLitle major = schoolList.getData().get(titleId).getMajor().get(contentId);
                                    reading = new Reading(1, 1, major.getId(), major.getName());
                                    reading.setS(schoolList.getData().get(titleId).getListeningFile());//官网地址
                                    reading.setName(schoolList.getData().get(titleId).getName());//学校名
                                    reading.setEnMajprName(schoolList.getData().get(titleId).getMajor().get(contentId).getTitle());
                                    MajorDialog dialog = new MajorDialog(SearchSchoolActivity.this, R.style.AlphaDialogAct);
                                    dialog.show();
                                    dialog.setData(reading);
                                }
                            });
                            list_view.setAdapter(schoolAdapter);
                        }else{
                            schoolDatas=schoolData;
                            if(null!=endLessOnScrollListener)
                                list_view.removeOnScrollListener(endLessOnScrollListener);
                            endLessOnScrollListener = new EndLessOnScrollListener(schoolManager) {
                                @Override
                                public void onLoadMore(int currentPage) {
                                    Log.i("不是第一次的more",pages+1+"");
                                    initData(pages+1,true,false);
                                }
                            };
                            list_view.addOnScrollListener(endLessOnScrollListener);
                            SchoolAdapter schoolAdapter = new SchoolAdapter(SearchSchoolActivity.this, schoolDatas, b, new RecyclerSelectListener01() {
                                @Override
                                public void setListener(int titleId, int contentId) {
                                    popupWindow.dismiss();
                                    schoolName = schoolList.getData().get(titleId).getName();
                                    SearchSchoolLitle major = schoolList.getData().get(titleId).getMajor().get(contentId);
                                    reading = new Reading(1, 1, major.getId(), major.getName());
                                    reading.setS(schoolList.getData().get(titleId).getListeningFile());
                                    reading.setName(schoolList.getData().get(titleId).getName());
                                    reading.setEnMajprName(schoolList.getData().get(titleId).getMajor().get(contentId).getTitle());
                                    MajorDialog dialog = new MajorDialog(SearchSchoolActivity.this, R.style.AlphaDialogAct);
                                    dialog.show();
                                    dialog.setData(reading);
                                }
                            });
                            list_view.setAdapter(schoolAdapter);
                        }
                        pages=page;
                    }catch (JSONException e){

                    }
                }
                dataOk=true;
                if(dataOk&&popOk)dismissLoadDialog();
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dataOk=true;
                if(dataOk&&popOk)dismissLoadDialog();
            }
        });

    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        v1 =findViewById(R.id.v1);
        back = (ImageView) findViewById(R.id.back);
        title = (RelativeLayout) findViewById(R.id.title);
        country_rl = (RelativeLayout) findViewById(R.id.country_rl);
        ranking_rl = (RelativeLayout) findViewById(R.id.ranking_rl);
        major_rl = (RelativeLayout) findViewById(R.id.major_rl);
        country_tv = (TextView) findViewById(R.id.country_tv);
        ranking_tv = (TextView) findViewById(R.id.ranking_tv);
        major_tv = (TextView) findViewById(R.id.major_tv);
        country_v = findViewById(R.id.country_v);
        ranking_v = findViewById(R.id.ranking_v);
        major_v = findViewById(R.id.major_v);
        nothing = (TextView) findViewById(R.id.nothing);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        titleViews =new ArrayList<>();
        titleViews.add(country_v);
        titleViews.add(ranking_v);
        titleViews.add(major_v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.country_v:
                setScreenSelect(0);
                setTitleSelect(0);
                break;
            case R.id.ranking_v:
                setScreenSelect(1);
                setTitleSelect(1);
                break;
            case R.id.major_v:
                setScreenSelect(2);
                setTitleSelect(2);
                break;
        }
    }
    //设置顶部选项点击效果
    private void setTitleSelect(int i){
        if(tag!=i) {
            if(tag!=-1)titleViews.get(tag).setSelected(false);
            titleViews.get(i).setSelected(true);
            tag = i;
        }else {
            titleViews.get(tag).setSelected(false);
            tag=-1;
        }
    }
    //设置选项列表显示
    private void setScreenSelect(int i){
        popupWindow.showAsDropDown(v1);
        switch (i){
            case 0:
                country_list.setVisibility(View.VISIBLE);
                break;
            case 1:
                ranking_list.setVisibility(View.VISIBLE);
                break;
            case 2:
                major_list.setVisibility(View.VISIBLE);
                break;
        }
        if(tag!=-1){
            switch (tag){
                case 0:
                    country_list.setVisibility(View.GONE);
                    break;
                case 1:
                    ranking_list.setVisibility(View.GONE);
                    break;
                case 2:
                    major_list.setVisibility(View.GONE);
                    break;
            }
        }
        if(tag==i)popupWindow.dismiss();
    }

}
