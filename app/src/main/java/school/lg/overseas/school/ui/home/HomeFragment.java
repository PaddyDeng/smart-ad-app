package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.HomeActivityAdapter;
import school.lg.overseas.school.adapter.HomeDocumentAdapter;
import school.lg.overseas.school.adapter.HomeSchoolAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HomeBanner;
import school.lg.overseas.school.bean.HomeContent;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.communication.SearchActivity;
import school.lg.overseas.school.ui.other.GoodsDetailActivity;
import school.lg.overseas.school.ui.other.InformationActivity;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.view.StrongRecycler;
import school.lg.overseas.school.view.carouselFigure.ImageSlideshow;

/**
 * 首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private RelativeLayout search_btn,school,ranking,specialty,knowledge_base,do_test,internship,mechanism,adviser;
    private TextView school_more,activity_more,document_more;
//    private RecyclerView hot_list,column_list;
    private StrongRecycler school_list,activity_list,document_list;
    private ImageSlideshow imgslideshow;
    private int hotPage=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initData();
        setClick();
    }

    private void setClick() {
//        hot_ask.setOnClickListener(this);
//        column.setOnClickListener(this);
        school.setOnClickListener(this);
        ranking.setOnClickListener(this);
        specialty.setOnClickListener(this);
        knowledge_base.setOnClickListener(this);
        do_test.setOnClickListener(this);
        internship.setOnClickListener(this);
        mechanism.setOnClickListener(this);
        adviser.setOnClickListener(this);
        school_more.setOnClickListener(this);
        activity_more.setOnClickListener(this);
        document_more.setOnClickListener(this);
        search_btn.setOnClickListener(this);
    }

    private void initView() {
        LinearLayoutManager schoolManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        school_list.setLayoutManager(schoolManager);
        LinearLayoutManager activityManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        activity_list.setLayoutManager(activityManager);
        LinearLayoutManager documentManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        document_list.setLayoutManager(documentManager);
//        AutoLinearLayoutManager hotManager = new AutoLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        hot_list.setNestedScrollingEnabled(false);
//        hot_list.setLayoutManager(hotManager);
//        AutoLinearLayoutManager columnManager = new AutoLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        column_list.setNestedScrollingEnabled(false);
//        column_list.setLayoutManager(columnManager);
    }

    private void initData() {
        imgslideshow.setDotSpace(12);
        imgslideshow.setDotSize(12);
        imgslideshow.setDelay(3000);
        setBanner();//获取顶部轮播图
        setContent();//下方主体内容
//        setHot();//最热问答
    }


//    private void setHot() {
//        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.HOTQUESTION, RequestMethod.POST);
//        req.set("tag","").set("page",hotPage+"").set("pageSize","10");
//        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//                if(response.isSucceed()){
//                    Question question = JSON.parseObject(response.get(), Question.class);
//                    HotQuestionAdapter adapter =new HotQuestionAdapter(getActivity(),question.getData().getData());
//                    hot_list.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//                LogUtils.log(response.get());
//            }
//        });
//    }

    private void setContent() {
        ((BaseActivity)getActivity()).showLoadDialog("HomeFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyResourceNormal + NetworkChildren.HOME, RequestMethod.GET);
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("HomeFragment");
                if(response.isSucceed()) {
                    try {
                    HomeContent homeContent = JSON.parseObject(response.get(), HomeContent.class);
                    HomeSchoolAdapter schoolAdapter = new HomeSchoolAdapter(getActivity(), homeContent.getSchools());
                    school_list.setAdapter(schoolAdapter);
                    HomeActivityAdapter activityAdapter =new HomeActivityAdapter(getActivity(),homeContent.getOpen());
                    activity_list.setAdapter(activityAdapter);
                    HomeDocumentAdapter documentAdapter =new HomeDocumentAdapter(getActivity(),homeContent.getShopContent().getData());
                    document_list.setAdapter(documentAdapter);
//                    ColumnAdapter columnAdapter= new ColumnAdapter(getActivity(),homeContent.getSpecialColumn());
//                    column_list.setAdapter(columnAdapter);
                    }catch (JSONException e){

                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("HomeFragment");
            }
        });
    }

    //顶部轮播图
    private void setBanner(){
        ((BaseActivity)getActivity()).showLoadDialog("HomeFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyResourceNormal+ NetworkChildren.HOMEBANNER,RequestMethod.POST);
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("HomeFragment");
                if(response.isSucceed()) {
                    try {
                        final HomeBanner homeBanner = JSON.parseObject(response.get(), HomeBanner.class);
                        final List<LittleData> banners = homeBanner.getSyBanner();
                        for (int i = 0; i < banners.size(); i++) {
                            imgslideshow.addImageUrl(NetworkTitle.DomainSmartApplyResourceNormal + banners.get(i).getImage());
                        }
                        imgslideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                int judge = banners.get(position).getJudge();
                                String relationId = banners.get(position).getRelationId();
                                if (!TextUtils.isEmpty(relationId)) {
                                    if (judge == 1) {
                                        InformationActivity.start(getActivity(), banners.get(position).getRelationId(), 0, "");
                                    } else {
                                        GoodsDetailActivity.start(getActivity(), banners.get(position).getRelationId());
                                    }
                                }
                            }
                        });
                        imgslideshow.commit();
                    }catch (JSONException e){

                    }
                }
            }
            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("HomeFragment");
            }
        });
    }

    private void findView(View view) {
        imgslideshow = (ImageSlideshow) view.findViewById(R.id.imgslideshow);
        search_btn = (RelativeLayout) view.findViewById(R.id.search_btn);
        school = (RelativeLayout) view.findViewById(R.id.school);
        ranking = (RelativeLayout) view.findViewById(R.id.ranking);
        specialty = (RelativeLayout) view.findViewById(R.id.specialty);
        knowledge_base = (RelativeLayout) view.findViewById(R.id.knowledge_base);
        do_test = (RelativeLayout) view.findViewById(R.id.do_test);
        internship = (RelativeLayout) view.findViewById(R.id.internship);
        mechanism = (RelativeLayout) view.findViewById(R.id.mechanism);
        adviser = (RelativeLayout) view.findViewById(R.id.adviser);
        school_more = (TextView) view.findViewById(R.id.school_more);
        activity_more = (TextView) view.findViewById(R.id.activity_more);
        document_more = (TextView) view.findViewById(R.id.document_more);
        school_list = (StrongRecycler) view.findViewById(R.id.school_list);
        activity_list = (StrongRecycler) view.findViewById(R.id.activity_list);
        document_list = (StrongRecycler) view.findViewById(R.id.document_list);
//        hot_ask = (TextView) view.findViewById(R.id.hot_ask);
//        column = (TextView) view.findViewById(R.id.column);
//        hot_list = (RecyclerView) view.findViewById(R.id.hot_list);
//        column_list = (RecyclerView) view.findViewById(R.id.column_list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
        imgslideshow.releaseResource();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.hot_ask:
//                hot_ask.setTextColor(getResources().getColor(R.color.mainGreen));
//                column.setTextColor(getResources().getColor(R.color.mainTextColor));
//                hot_list.setVisibility(View.VISIBLE);
//                column_list.setVisibility(View.GONE);
//                break;
//            case R.id.column:
//                column.setTextColor(getResources().getColor(R.color.mainGreen));
//                hot_ask.setTextColor(getResources().getColor(R.color.mainTextColor));
//                column_list.setVisibility(View.VISIBLE);
//                hot_list.setVisibility(View.GONE);
//                break;
            case R.id.school:
                SearchSchoolActivity.start(getActivity());
                break;
            case R.id.ranking:
                RankingActivity.start(getActivity());
                break;
            case R.id.specialty:
                MajorActivity.start(getActivity());
                break;
            case R.id.knowledge_base:
                KnowledgeBaseActivity.start(getActivity());
                break;
            case R.id.do_test:
                DoTestActivity.start(getActivity());
                break;
            case R.id.internship:
                InternshipActivity.start(getActivity());
                break;
            case R.id.mechanism:
                MechanismActivity.start(getActivity());
                break;
            case R.id.adviser:
                DocumentMoreActivity.start(getActivity());
                break;
            case R.id.school_more:
                SearchSchoolActivity.start(getActivity());
                break;
            case R.id.activity_more:
                ActMoreActivity.start(getActivity());
                break;
            case R.id.document_more:
                DocumentMoreActivity.start(getActivity());
                break;
            case R.id.search_btn:
                SearchActivity.start(getActivity(),0);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("HomeFragment");
    }
}
