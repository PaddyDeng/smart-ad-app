package school.lg.overseas.school.ui.dicovery;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.Know;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.HttpUtil;
import school.lg.overseas.school.http.JavaBeanRequest;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.adapter.AbraodInfoAdapter;
import school.lg.overseas.school.ui.dicovery.adapter.HotAdapter;
import school.lg.overseas.school.ui.dicovery.bean.AbroadBean;
import school.lg.overseas.school.utils.HttpUtils;

/**
 * 留学动态
 */
public class AbroadInfoFragment extends BaseFragment {
    private static final String TAG = AbroadInfoFragment.class.getSimpleName();
    private static final int LGTrendsPlan = 245;  //  留学规划啊
    private static final int LGTrendsCountry = 244;  //留学国家
    private static final int LGTrendsProcedure = 242;  // 留学手续
    private static final int LGTrendsExam = 243;  // 留学考试

    @BindView(R.id.hot_rl)
    RecyclerView hotRl;
    @BindView(R.id.abroad_plan_img)
    ImageView abroadPlanImg;
    @BindView(R.id.abroad_plan_txt)
    TextView abroadPlanTxt;
    @BindView(R.id.abroad_plan)
    AutoLinearLayout abroadPlan;
    @BindView(R.id.abroad_country_img)
    ImageView abroadCountryImg;
    @BindView(R.id.abroad_country_txt)
    TextView abroadCountryTxt;
    @BindView(R.id.abroad_country)
    AutoLinearLayout abroadCountry;
    @BindView(R.id.abroad_procedure_img)
    ImageView abroadProcedureImg;
    @BindView(R.id.abroad_procedure_txt)
    TextView abroadProcedureTxt;
    @BindView(R.id.abroad_procedure)
    AutoLinearLayout abroadProcedure;
    @BindView(R.id.abroad_test_img)
    ImageView abroadTestImg;
    @BindView(R.id.abroad_test_txt)
    TextView abroadTestTxt;
    @BindView(R.id.abroad_test)
    AutoLinearLayout abroadTest;
    @BindView(R.id.discuss_recyclerView)
    RecyclerView discussRecyclerView;

    Unbinder unbinder;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private int oldPage = -1;
    private int category =LGTrendsPlan ;
    private Map<Integer, AutoLinearLayout> autoLinearLayoutMap;   //  标题
    private Map<Integer, ImageView> imageViewMap;   //  图片
    private Map<Integer, TextView> textViewMap;     //  文字


    private HotAdapter hotAdapter;  //  顶部头条adapter
    private List<AbroadHomeBean.ToutiaoBean> toutiaoBeanList;
    private List<AbroadBean> abroadBeanList ;
    private AbraodInfoAdapter abraodInfoAdapter ;
    private int page = 1;  //分页

    public AbroadInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abroad_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        autoLinearLayoutMap = new HashMap<>();
        autoLinearLayoutMap.put(1, abroadPlan);
        autoLinearLayoutMap.put(2, abroadCountry);
        autoLinearLayoutMap.put(3, abroadProcedure);
        autoLinearLayoutMap.put(4, abroadTest);
        imageViewMap = new HashMap<>();
        imageViewMap.put(1, abroadPlanImg);
        imageViewMap.put(2, abroadCountryImg);
        imageViewMap.put(3, abroadProcedureImg);
        imageViewMap.put(4, abroadTestImg);
        textViewMap = new HashMap<>();
        textViewMap.put(1, abroadPlanTxt);
        textViewMap.put(2, abroadCountryTxt);
        textViewMap.put(3, abroadProcedureTxt);
        textViewMap.put(4, abroadTestTxt);
        setSelect(1);
        toutiaoBeanList = new ArrayList<>();
        hotAdapter = new HotAdapter(getActivity(), toutiaoBeanList);
        hotAdapter.setSelectListener(new SelectListener() {
            @Override
            public void select(int position) {
                ArticleDetailsActivity.start(getActivity() ,toutiaoBeanList.get(position).getId());
            }
        });
        hotRl.setLayoutManager(new LinearLayoutManager(getActivity()));
        hotRl.setAdapter(hotAdapter);
        abroadBeanList = new ArrayList<>();
        abraodInfoAdapter = new AbraodInfoAdapter(getActivity() ,abroadBeanList);
        abraodInfoAdapter.setSelectListener(new SelectListener() {
            @Override
            public void select(int position) {
                ArticleDetailsActivity.start(getActivity() ,abroadBeanList.get(position).getId());
            }
        });
        discussRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        discussRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity() ,DividerItemDecoration.VERTICAL));
        discussRecyclerView.setAdapter(abraodInfoAdapter);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1 ;
              addNet(false);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++ ;
                refreshLayout.finishLoadMore(true);
                addNet(true);
            }
        });
    }

    @OnClick({R.id.abroad_plan, R.id.abroad_country, R.id.abroad_procedure, R.id.abroad_test})
    public void setSelect(View view) {
        switch (view.getId()) {
            case R.id.abroad_plan:
                category = LGTrendsPlan;
                setSelect(1);
                break;
            case R.id.abroad_country:
                category  = LGTrendsCountry;
                setSelect(2);
                break;
            case R.id.abroad_procedure:
                category = LGTrendsProcedure;
                setSelect(3);
                break;
            case R.id.abroad_test:
                category = LGTrendsExam;
                setSelect(4);
                break;
        }
    }

    private void setSelect(int position) {
        if (oldPage != -1) {
            autoLinearLayoutMap.get(oldPage).setSelected(false);
            imageViewMap.get(oldPage).setSelected(false);
            textViewMap.get(oldPage).setTextColor(getResources().getColor(R.color.black));
        }
        autoLinearLayoutMap.get(position).setSelected(true);
        autoLinearLayoutMap.get(position).setSelected(true);
        textViewMap.get(position).setTextColor(getResources().getColor(R.color.white));
        oldPage = position;
        switch (position) {
            case 1:
                page = 1 ;
                addNet(false);
                break;
            case 2:
                page = 1 ;
                addNet(false);
                break;
            case 3:
                page = 1 ;
                addNet(false);
                break;
            case 4:
                page = 1 ;
                addNet(false);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 请求数据
     */
    public void addNet(final boolean isClearData) {
        addToCompositeDis(HttpUtil.getStudyAbroad(category +"" ,page+"")
        .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Disposable disposable) throws Exception {

            }
        }).subscribe(new Consumer<AbroadHomeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AbroadHomeBean abroadHomeBean) throws Exception {

                        if (abroadHomeBean != null) referUi(abroadHomeBean ,isClearData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        toast(HttpUtils.onError(throwable));

                    }
                }));

    }


    /**
     * 刷新Ui
     * @param abroad
     */
    private void referUi(AbroadHomeBean abroad , boolean isClearData) {
        toutiaoBeanList.clear();
        toutiaoBeanList.addAll(abroad.getToutiao());
        hotAdapter.notifyDataSetChanged();
        if (!isClearData){
            abroadBeanList.clear();
        }
        abroadBeanList.addAll(abroad.getAbroad());
        abraodInfoAdapter.notifyDataSetChanged();
    }
}
