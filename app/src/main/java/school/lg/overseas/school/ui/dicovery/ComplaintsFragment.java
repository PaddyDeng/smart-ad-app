package school.lg.overseas.school.ui.dicovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.ComplaintsAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.RemarkData;
import school.lg.overseas.school.bean.RemarkDatas;
import school.lg.overseas.school.bean.Reply;
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.RecyclerSelectListener01;
import school.lg.overseas.school.callback.ReplyListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.other.ReplyDialog;
import school.lg.overseas.school.utils.KeyboardUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.utils.SpField;

/**
 * 八卦
 */

public class ComplaintsFragment extends BaseFragment{
    private SwipeRefreshLayout refresh;
    private RecyclerView list_view;
    private List<RemarkData> datas;
    private int pages;
    private String type="1",replyUser="",replyUserNam="";
    private int titleIds,contentIds;
    private ComplaintsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complaints,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        initData(1,false);
    }

    public void refresh(){
        initData(1,false);
    }


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
                        datas.addAll(data);
                        list_view.getAdapter().notifyDataSetChanged();
                    }else{
                        datas=data;
                        adapter =new ComplaintsAdapter(getActivity(), datas, new RecyclerSelectListener01() {
                            @Override
                            public void setListener(int titleId, int contentId) {
                                titleIds=titleId;
                                contentIds=contentId;
                                final RemarkData data1=datas.get(titleId);
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
                        list_view.setAdapter(adapter);
                    }
                    pages=page;
                }catch (JSONException e){

                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ((BaseActivity)getActivity()).dismissLoadDialog("ComplaintsFragment");
            }
        });
    }

    private void initView() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1,false);
                refresh.setRefreshing(false);
            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData(pages+1,true);
            }
        });
    }

    private void findView(View v) {
        refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        list_view = (RecyclerView) v.findViewById(R.id.list_view);
    }
    private void send(final String s, RemarkData data){
        PersonalDetail personalDetail =SharedPreferencesUtils.getPersonalDetail(getActivity());
        String session=SharedPreferencesUtils.getSession(getActivity(),1);
        if(null==personalDetail||TextUtils.isEmpty(session)){
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
                            Log.i("名字",personalDetail01.getNickname());
                            replyBean.setuName(TextUtils.isEmpty(personalDetail01.getNickname())?personalDetail01.getUserName():personalDetail01.getNickname());
                            replyBean.setUserImage(personalDetail01.getImage());
                            datas.get(titleIds).getReply().add(replyBean);
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
    public void onResume() {
        super.onResume();
        boolean isDelete = SharedPreferencesUtils.getDelete(getActivity(), SpField.complaints);
        if(isDelete){
            initData(1,false);
            SharedPreferencesUtils.setDelete(getActivity(),SpField.complaints,false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).dismissLoadDialog("ComplaintsFragment");
    }
}
