package school.lg.overseas.school.ui.communication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import school.lg.overseas.school.adapter.ComplaintsAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.QuestionLableAdapter;
import school.lg.overseas.school.adapter.QuestionListAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.Question;
import school.lg.overseas.school.bean.QuestionData;
import school.lg.overseas.school.bean.RemarkData;
import school.lg.overseas.school.bean.RemarkDatas;
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.RecyclerSelectListener01;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.dicovery.ReleaseGossipActivity;
import school.lg.overseas.school.ui.other.ReplyDialog;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 问答(吐槽还没添加)
 */

public class CommunicationFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = CommunicationFragment.class.getSimpleName();
    private RelativeLayout to_ask_rl,to_ask;
    private RecyclerView main_title;
    private ViewPager list_view;
    private List<TitleTag> titleTags,nowTitleTags;
    private List<RecyclerView> recyclerViews;
    private List<View> views;
    private Map<String,List<QuestionData>> datas;
    private QuestionLableAdapter titleAdapter;
    private List<Integer> tags;
    private List<Integer> pages;
    private boolean isNet;

    //   吐槽界面
    private List<RemarkData> datas_complants;
    private ComplaintsAdapter adapter;
    private int titleIds,contentIds;
    private String type="1",replyUser="",replyUserNam="";
    private RecyclerView list_view_complants ;  //  吐槽界面
    private int page_complants ;
    private boolean isComplants = false ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_communication,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        initView();
        setClick();
        list_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleAdapter.setSelectP(position);
                if (position == 7){
                    isComplants = true ;
                    if (!tags.contains(7)) initData(1,false);
                }else {
                    isComplants = false ;
                    if (!tags.contains(position)) {
                        initData(nowTitleTags.get(position).getId(), 1, position, false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setClick() {
        to_ask_rl.setOnClickListener(this);
        to_ask.setOnClickListener(this);
    }

    private void initView() {
        GridLayoutManager titleManager =new GridLayoutManager(getActivity(),4);
        main_title.setLayoutManager(titleManager);
        initTitleData();
    }

    private void initTitleData() {
        ((BaseActivity)getActivity()).showLoadDialog("CommunicationFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.QUESTIONTAG);
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("CommunicationFragment");
                if(response.isSucceed()) {
                    isNet=true;
                    try {
                        titleTags = JSON.parseArray(response.get(), TitleTag.class);
                        String questionTag = SharedPreferencesUtils.getQuestionTag(getActivity());
                        List<String> tags = JSON.parseArray(questionTag, String.class);
                        nowTitleTags =new ArrayList<>();
                        if(null!=tags){
                            for (int i = 0; i < titleTags.size()-1; i++) {
                                if(tags.contains(titleTags.get(i).getId())){
                                    titleTags.get(i).setClose(true);
                                }else{
                                    nowTitleTags.add(titleTags.get(i));
                                }
                            }
                        }else{
                            nowTitleTags=titleTags;
                        }
                        final TitleTag titleTag = new TitleTag();
                        titleTag.setName("吐槽");
                        titleTag.setId("");
                        nowTitleTags.add(titleTag);
                        views =new ArrayList<>();
                        recyclerViews =new ArrayList<>();
                        datas =new HashMap<>();
                        CommunicationFragment.this.tags = new ArrayList<>();
                        pages =new ArrayList<>();
                        for (int i = 0; i < nowTitleTags.size()-1; i++) {
                            final int u =i;
                            pages.add(1);
                            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recycler, null);
                            LinearLayoutManager manager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            //   viewPager  添加View
                            RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.item_list);
                            final SwipeRefreshLayout  refresh= (SwipeRefreshLayout) v.findViewById(R.id.refresh);
                            recyclerView.setLayoutManager(manager);
                            recyclerViews.add(recyclerView);
                            views.add(v);
                            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    initData(nowTitleTags.get(u).getId(),1,u,false);
                                    refresh.setRefreshing(false);
                                }
                            });
                            recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
                                @Override
                                public void onLoadMore(int currentPage) {
                                    initData(nowTitleTags.get(u).getId(),pages.get(u)+1,u,true);
                                }
                            });
                        }
                        /**
                         * 此处需要添加吐槽页的layout到views集合中,就可以了
                         */
                        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_complaints, null);
                        LinearLayoutManager manager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        //   viewPager  添加View
                         list_view_complants = (RecyclerView) v.findViewById(R.id.list_view);
                        final SwipeRefreshLayout  refresh= (SwipeRefreshLayout) v.findViewById(R.id.refresh);
                        list_view_complants.setLayoutManager(manager);
                        recyclerViews.add(list_view_complants);
                        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                initData(1,false);
                                refresh.setRefreshing(false);
                            }
                        });
                        list_view_complants.addOnScrollListener(new EndLessOnScrollListener(manager) {
                            @Override
                            public void onLoadMore(int currentPage) {
                                initData(page_complants+1,true);
                            }
                        });
                        views.add(v);
                        initData("",1,0,false);

                        list_view.setAdapter(new MyViewPageAdapter(views));
                        titleAdapter =new QuestionLableAdapter(getActivity(), nowTitleTags, new SelectListener() {
                            @Override
                            public void select(int position) {
                                list_view.setCurrentItem(position);
                            }
                        });
                        main_title.setAdapter(titleAdapter);
                    }catch (JSONException e){
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("CommunicationFragment");
                titleTags =new ArrayList<>();
                TitleTag titleTag =new TitleTag();
                titleTag.setName("全部");
                titleTag.setId("");
            }
        });
    }

    private void initData(String tag, final int page, final int type, final boolean isMore) {
        ((MainActivity)getActivity()).showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.HOTQUESTION, RequestMethod.POST);
        req.set("tag",tag).set("page",page+"").set("pageSize","10");
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((MainActivity)getActivity()).dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        Question question = JSON.parseObject(response.get(), Question.class);
                        List<QuestionData> data = question.getData().getData();
                        if(!isMore){
                            tags.add(type);
                            datas.put(type+"",data);
                            QuestionListAdapter adapter =new QuestionListAdapter(getActivity(),datas.get(type+""));
                            recyclerViews.get(type).setAdapter(adapter);
                        }else{
                            datas.get(type+"").addAll(data);
                            recyclerViews.get(type).getAdapter().notifyDataSetChanged();
                        }
                        pages.set(type,page);
                    }catch (JSONException e){

                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((MainActivity)getActivity()).dismissLoadDialog();
            }
        });
    }


    private void findView(View view) {
        to_ask_rl = (RelativeLayout) view.findViewById(R.id.to_ask_rl);
        to_ask = (RelativeLayout) view.findViewById(R.id.to_ask);
        main_title = (RecyclerView) view.findViewById(R.id.main_title);
//        manage = (ImageView) view.findViewById(R.id.manage);
        list_view = (ViewPager) view.findViewById(R.id.list_view);
    }


    //   获取吐槽页面数据
    private void initData(final int page, final boolean isMore) {
        ((BaseActivity)getActivity()).showLoadDialog("ComplaintsFragment");
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.GOSSIPURL + NetworkChildren.COMPLAINTS, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set("belong","3");
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("ComplaintsFragment");
                try {
                    RemarkDatas remarkDatas = JSON.parseObject(response.get(), RemarkDatas.class);
                    List<RemarkData> data = remarkDatas.getData().getData();
                    if(!isMore&&remarkDatas.getNum()!=0){
                        RemarkData remarkData = new RemarkData();
                        remarkData.setRemarkNum(remarkDatas.getNum()+"");
                        data.add(0,remarkData);
                    }
                    if(isMore){
                        datas_complants.addAll(data);
                        list_view.getAdapter().notifyDataSetChanged();
                    }else{
                        tags.add(7);
                        datas_complants=data;
                        adapter =new ComplaintsAdapter(getActivity(), datas_complants, new RecyclerSelectListener01() {
                            @Override
                            public void setListener(int titleId, int contentId) {
                                titleIds=titleId;
                                contentIds=contentId;
                                final RemarkData data1=datas_complants.get(titleId);
                                ReplyDialog dialog =new ReplyDialog(getActivity());
                                dialog.show();
                                if(contentId==-1){
                                    type="1";
                                    replyUser="";
                                    replyUserNam="";
                                    dialog.setHint("我也来说一句");
                                }else{
                                    type="2";
                                    ReplyBean replyBean =data1.getReply().get(contentId);
                                    replyUser=replyBean.getUid();
                                    replyUserNam=replyBean.getuName();
                                    dialog.setHint("@"+replyUserNam);
                                }
                                dialog.setListener(new ReplyListener() {
                                    @Override
                                    public void setListener(String s) {
                                        send(s,data1);
                                    }
                                });
                            }
                        });
                        adapter.notifyDataSetChanged();
                        list_view_complants.setAdapter(adapter);

                    }
                    page_complants=page;
                }catch (JSONException e){

                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("ComplaintsFragment");
            }
        });
    }


    private void send(final String s, RemarkData data){
        PersonalDetail personalDetail =SharedPreferencesUtils.getPersonalDetail(getActivity());
        String session=SharedPreferencesUtils.getSession(getActivity(),1);
        if(null==personalDetail|| TextUtils.isEmpty(session)){
            LoginHelper.needLogin(getActivity(),"需要登录才能评论哦");
            return;
        }
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainGossipNormal + NetworkChildren.REPLY, RequestMethod.POST);
        req.set("content",s).set("type",type).set("id",data.getId()).set("gossipUser",data.getUid())
                .set("uName",TextUtils.isEmpty(personalDetail.getNickname())?personalDetail.getUserName():personalDetail.getNickname())
                .set("userImage",personalDetail.getImage())
                .set("replyUser",replyUser).set("replyUserName",replyUserNam)
                .set("belong","1");
        req.setHeader("Cookie","PHPSESSID="+session);
        ((BaseActivity)getActivity()).showLoadDialog();
        ((BaseActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        PraiseBack praiseBack =JSON.parseObject(response.get(), PraiseBack.class);
                        if(praiseBack.getCode()==1){
                            Toast.makeText(getActivity(),"评论成功",Toast.LENGTH_SHORT).show();
                            ReplyBean replyBean =new ReplyBean();
                            replyBean.setContent(s);
                            if(contentIds!=-1) {
                                replyBean.setReplyUser(replyUser);
                                replyBean.setReplyUserName(replyUserNam);
                                replyBean.setType("2");
                            }else{
                                replyBean.setType("1");
                            }
                            PersonalDetail personalDetail01 =SharedPreferencesUtils.getPersonalDetail(getActivity());
                            replyBean.setUid(personalDetail01.getUid());
                            replyBean.setuName(TextUtils.isEmpty(personalDetail01.getNickname())?personalDetail01.getUserName():personalDetail01.getNickname());
                            replyBean.setUserImage(personalDetail01.getImage());
                            datas_complants.get(titleIds).getReply().add(replyBean);
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getActivity(),praiseBack.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.manage:
//                Intent intent =new Intent(getActivity(),LableManageActivity.class);
//                startActivityForResult(intent,101);
//                break;
            case R.id.to_ask_rl:
                SearchActivity.start(getActivity(),1);
                break;
            case R.id.to_ask:
                ask();
                break;
        }
    }

    /**
     * 提问跳转
     */
    public void ask(){
        Log.e(TAG, "ask: " +isComplants);
        if (isComplants){
            if(!SharedPreferencesUtils.isLogin(getActivity())){
                LoginHelper.needLogin(getActivity(),"需要登录才能发布哦");
                return;
            }else{
                Intent intent = new Intent(getActivity(),ReleaseGossipActivity.class);
                startActivityForResult(intent,103);
            }
        }else{
            Intent intent1 =new Intent(getActivity(),ToAskActivity.class);
            startActivityForResult(intent1,105);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101){
            if(resultCode==102){
                initTitleData();
            }
        }
        if(requestCode==105){
            if(resultCode==106){
                initTitleData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("CommunicationFragment");
    }
}
