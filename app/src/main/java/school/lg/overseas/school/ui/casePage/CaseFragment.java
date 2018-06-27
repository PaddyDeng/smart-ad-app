package school.lg.overseas.school.ui.casePage;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.HotCaseAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HotCase;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.other.DialogWait;
import school.lg.overseas.school.utils.WaitUtils;
import school.lg.overseas.school.view.ObservableScrollView;

/**
 * 案例
 */

public class CaseFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = CaseFragment.class.getSimpleName();
    private TextView title;
    private ImageView top_iv;
    private TextView hot_more,success_more;
    private ObservableScrollView scroll;
    private RelativeLayout loading;
    private RecyclerView hot_recycler,title_list,top_title_list;
    private List<TitleTag> titles;
    private TitleAdapter titleAdapter;
    private List<Fragment> fragmentList;
    private List<String> fragmentTags;
    private List<LittleData> hot;
    private int oldPage=0;
    private int scrollTag=0;
    private int titleHeight,topHeight;
    private boolean isFirst=true;
    private int scrollY;
    private int index =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initHot(false);
        initSuccessTitle();
        setClick();
    }

    private void setClick() {
        hot_more.setOnClickListener(this);
        success_more.setOnClickListener(this);
    }


    private void initSuccessTitle() {
        String[] catIds ={"282","283","284","285","286"};
        String[] names ={"美国留学","英国留学","澳洲加拿大","香港新加坡","欧洲国家"};
        titles =new ArrayList<>();
        for (int i = 0; i < catIds.length; i++) {
            TitleTag successCaseTitle = new TitleTag();
            successCaseTitle.setId(catIds[i]);
            successCaseTitle.setName(names[i]);
            titles.add(successCaseTitle);
        }
        fragmentList =new ArrayList<>();
        fragmentTags =new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            SuccessCaseFragment successCaseFragment = new SuccessCaseFragment();
            Bundle bundle =new Bundle();
            bundle.putString("tag",titles.get(i).getId());
            successCaseFragment.setArguments(bundle);
            fragmentList.add(successCaseFragment);
            fragmentTags.add("CaseLittleFragment"+i);
        }
        getChildFragmentManager().beginTransaction().add(R.id.fl,fragmentList.get(0),fragmentTags.get(0)).commit();

        LinearLayoutManager titleManage =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(titleManage);
        LinearLayoutManager topTitleManage =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        top_title_list.setLayoutManager(topTitleManage);
        titleAdapter = new TitleAdapter(getActivity(), titles, new SelectListener() {
            @Override
            public void select(int position) {
                if(oldPage==0&&position==0)return;
                title_list.scrollToPosition(position);
                top_title_list.scrollToPosition(position);
                if(!isFirst)scroll.scrollTo(0,scrollY);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment fragment = fragmentList.get(position);
                if(!fragment.isAdded()){
                    ft.hide(fragmentList.get(oldPage)).add(R.id.fl,fragmentList.get(position),fragmentTags.get(position));
                }else {
                    ft.hide(fragmentList.get(oldPage)).show(fragmentList.get(position));
                }
                ft.commit();
                oldPage=position;
            }
        });
        title_list.setAdapter(titleAdapter);
        top_title_list.setAdapter(titleAdapter);
        title_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollTag=1;
                return false;
            }
        });
        top_title_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollTag=2;
                return false;
            }
        });
        title_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(scrollTag==1)top_title_list.scrollBy(dx,dy);
            }
        });
        top_title_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(scrollTag==2)title_list.scrollBy(dx,dy);
            }
        });
        ViewTreeObserver title_wto = title_list.getViewTreeObserver();
        title_wto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect frame = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;//状态栏高度
                topHeight=titleHeight+statusBarHeight;
                title_list.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        scroll.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int[] location =new int[2];
                title_list.getLocationOnScreen(location);
                int locationY =location[1];

                if(locationY<=topHeight && (top_title_list.getVisibility()==View.GONE||top_title_list.getVisibility()==View.INVISIBLE)){
                    top_title_list.setVisibility(View.VISIBLE);
                    if(isFirst){
                        scrollY=y;
                        isFirst=false;
                    }
                }
                if(locationY>topHeight && top_title_list.getVisibility()==View.VISIBLE){
                    top_title_list.setVisibility(View.GONE);
                    isFirst=true;
                }
            }
        });
        scroll.setOnTouchListener(new TouchListenerImpl());
    }

    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:

                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=scroll.getChildAt(0).getMeasuredHeight();
                    boolean show = WaitUtils.isRunning(getActivity().getClass().getSimpleName());
                    if(scrollY==0&&index>0&&!show){
                        index=0;
                        System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
//                        loading.setVisibility(View.VISIBLE);
//                        initHot(true);
//                        SuccessCaseFragment fragment = (SuccessCaseFragment) fragmentList.get(oldPage);
//                        fragment.setLoad(false);
                    }
                    if((scrollY+height)==scrollViewMeasuredHeight&&index>0&&!show){
                        index=0;
                        SuccessCaseFragment fragment = (SuccessCaseFragment) fragmentList.get(oldPage);
                        fragment.setLoad(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    index++;
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    private void initView() {
        LinearLayoutManager hotManage =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        hot_recycler.setLayoutManager(hotManage);
        hot_recycler.setNestedScrollingEnabled(false);
        ViewTreeObserver titleVto = title.getViewTreeObserver();
        titleVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titleHeight = title.getHeight();
                title.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void initHot(final boolean isRefresh) {
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.HOTCASE, RequestMethod.POST);
        req.set("strip","4");
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(loading.getVisibility()==View.VISIBLE)loading.setVisibility(View.GONE);
                if(response.isSucceed()){
                    try {
                        HotCase hotCase = JSON.parseObject(response.get(), HotCase.class);
                        hot = hotCase.getHot();
                        if(!isRefresh||hot_recycler.getAdapter()==null) {
                            HotCaseAdapter hotAdapter = new HotCaseAdapter(getActivity(), hot);
                            hot_recycler.setAdapter(hotAdapter);
                        }else{
                            hot_recycler.getAdapter().notifyDataSetChanged();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
            }

            @Override
            public void onFinish(int what) {
                if(loading.getVisibility()==View.VISIBLE)loading.setVisibility(View.GONE);
            }
        });
    }

    private void findView(View v) {
        title = (TextView) v.findViewById(R.id.title);
        scroll = (ObservableScrollView) v.findViewById(R.id.scroll);
        top_iv = (ImageView) v.findViewById(R.id.top_iv);
        hot_more = (TextView) v.findViewById(R.id.hot_more);
        hot_recycler = (RecyclerView) v.findViewById(R.id.hot_recycler);
        success_more = (TextView) v.findViewById(R.id.success_more);
        title_list = (RecyclerView) v.findViewById(R.id.title_list);
        top_title_list =(RecyclerView) v.findViewById(R.id.top_title_list);
        loading = (RelativeLayout) v.findViewById(R.id.loading);
    }


    @Override
    public void onClick(View v) {
        SuccessCaseActivity.start(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
