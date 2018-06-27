package school.lg.overseas.school.ui.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import school.lg.overseas.school.adapter.SearchAnswerAndActivityAndLibraryAdapter;
import school.lg.overseas.school.adapter.SearchSchoolAndMajorAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.AnswerAndActivityAndLibrary;
import school.lg.overseas.school.bean.AnswerAndActivityAndLibrarys;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.bean.SearchSchoolList;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.KeyboardUtils;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 搜索关心的问题
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private EditText et;
    private ImageView delete_iv;
    private TextView back;
    private  RecyclerView title_list;
    private ViewPager viewpager;
    private List<TitleTag> titleTags;
    private List<View> views;
    private List<RecyclerView> recyclerViews;
    private List<TextView> agains;
    private List<Integer> pages;
    private List<SearchSchool> schoolDatas;
    private Map<String,List<AnswerAndActivityAndLibrary>> answerAndActivityAndLibraryDatas;
    private int oldPage=0;
    private String keyWord="";
    private int tag;

    public static void start(Context context,int tag){
        Intent intent =new Intent(context,SearchActivity.class);
        intent.putExtra("tag",tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getArgs();
        findView();
        initTitle();
        setClick();
    }

    private void getArgs() {
        if(null!=getIntent()){
            tag = getIntent().getIntExtra("tag", 0);
        }
    }

    private void setClick() {
        back.setOnClickListener(this);
        delete_iv.setOnClickListener(this);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    delete_iv.setVisibility(View.VISIBLE);
                }else{
                    delete_iv.setVisibility(View.GONE);
                }
                keyWord=s.toString();
                setSelect();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setSelect(){
        if(TextUtils.isEmpty(keyWord)){
            agains.get(oldPage).setText("请填写搜索关键字");
        }else{
            agains.get(oldPage).setText("网络问题，或该条目没有资料");
        }
        switch (oldPage){
            case 0:
                initData01(0,1,false);
                break;
            case 1:
                initData02(1,1,false);
                break;
            case 2:
                initData02(2,1,false);
                break;
            case 3:
                initData02(3,1,false);
                break;
            case 4:
                initData02(4,1,false);
                break;
        }

    }

    private void initTitle() {
        String[] titles ={"学校","专业","问答","活动","知识库"};
        String[] titleIds ={"school","majorWord","questionWord","activityWord","knowWord"};
        titleTags =new ArrayList<>();
        views = new ArrayList<>();
        recyclerViews =new ArrayList<>();
        agains =new ArrayList<>();
        pages =new ArrayList<>();
        schoolDatas =new ArrayList<>();
        answerAndActivityAndLibraryDatas =new HashMap<>();
        for (int i = 0; i < titles.length; i++) {
            final int u =i;
            pages.add(1);
            TitleTag titleTag =new TitleTag();
            titleTag.setName(titles[i]);
            titleTag.setId(titleIds[i]);
            titleTags.add(titleTag);
            View v = LayoutInflater.from(SearchActivity.this).inflate(R.layout.fragment_recycler, null);
            LinearLayoutManager manager =new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false);
            RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.item_list);
            TextView again = (TextView) v.findViewById(R.id.again);
            final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setSelect();
                    refresh.setRefreshing(false);
                }
            });
            recyclerView.setLayoutManager(manager);
            recyclerViews.add(recyclerView);
            agains.add(again);
            views.add(v);
            recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
                @Override
                public void onLoadMore(int currentPage) {
                    if(u==0){
                        initData01(u,pages.get(u)+1,true);
                    }else{
                        initData02(u,pages.get(u)+1,true);
                    }
                }
            });
        }
        viewpager.setAdapter(new MyViewPageAdapter(views));
        LinearLayoutManager manager=new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(manager);
        final TitleAdapter titleAdapter =new TitleAdapter(SearchActivity.this, titleTags, new SelectListener() {
            @Override
            public void select(int position) {
                oldPage=position;
                viewpager.setCurrentItem(position);
                setSelect();
            }
        });
        title_list.setAdapter(titleAdapter);
        if(tag==1) {
            titleAdapter.setSelectPos(2);
            oldPage=2;
            viewpager.setCurrentItem(2);
        }
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                oldPage =position;
                titleAdapter.setSelectPos(position);
                setSelect();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    //学校和专业
    private void initData01(final int type, final int page, final boolean isMore) {
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLANDMAJOR, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set(titleTags.get(type).getId(),keyWord);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
                if(response.isSucceed()){
                    try {
                        SearchSchoolList searchSchoolList = JSON.parseObject(response.get(), SearchSchoolList.class);
                        List<SearchSchool> data = searchSchoolList.getData();
                        if(isMore){
                            pages.set(type,page);
                            schoolDatas.addAll(data);
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                        }else{
                            //加入到数据集合中
                            if(null==data||data.size()==0)agains.get(type).setVisibility(View.VISIBLE);
                            else agains.get(type).setVisibility(View.GONE);
                            schoolDatas=data;
                            SearchSchoolAndMajorAdapter searchAdapter =new SearchSchoolAndMajorAdapter(SearchActivity.this, schoolDatas,type);
                            recyclerViews.get(type).setAdapter(searchAdapter);
                            pages.set(type,1);
                        }
                    }catch (JSONException e){

                    }


                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
            }
        });
    }


    //问答、活动、知识库
    private void initData02(final int type, final int page, final boolean isMore){
        Log.i("搜索",titleTags.get(type).getId()+":"+keyWord);
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ANSWERANDACTIVITYANDLIBRARY, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set(titleTags.get(type).getId(),keyWord);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
                if(response.isSucceed()){
                    try {
                        AnswerAndActivityAndLibrarys answerAndActivityAndLibrarys = JSON.parseObject(response.get(), AnswerAndActivityAndLibrarys.class);
                        List<AnswerAndActivityAndLibrary> data = answerAndActivityAndLibrarys.getData();
                        if(isMore){
                            answerAndActivityAndLibraryDatas.get(type+"").addAll(data);
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                            pages.set(type,page);
                        }else{
                            if(null==data||data.size()==0)agains.get(type).setVisibility(View.VISIBLE);
                            else agains.get(type).setVisibility(View.GONE);
                            answerAndActivityAndLibraryDatas.put(type+"",data);
                            SearchAnswerAndActivityAndLibraryAdapter adapter =new SearchAnswerAndActivityAndLibraryAdapter(SearchActivity.this,answerAndActivityAndLibraryDatas.get(type+""),type);
                            recyclerViews.get(type).setAdapter(adapter);
                            pages.set(type,1);
                        }
                    }catch (JSONException e){

                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                KeyboardUtils.hideOrShowKeyBoard(et,"o");
            }
        });
    }


    private void findView() {
        et = (EditText) findViewById(R.id.et);
        delete_iv = (ImageView) findViewById(R.id.delete_iv);
        back = (TextView) findViewById(R.id.back);
        title_list = (RecyclerView) findViewById(R.id.title_list);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        KeyboardUtils.hideOrShowKeyBoard(et,"o");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.delete_iv:
                et.setText("");
                delete_iv.setVisibility(View.GONE);
                break;
        }
    }

}
