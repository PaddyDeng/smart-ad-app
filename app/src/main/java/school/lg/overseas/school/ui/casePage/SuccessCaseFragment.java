package school.lg.overseas.school.ui.casePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.SuccessCaseAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.HotCase;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.manager.AutoLinearLayoutManager;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.other.DialogWait;
import school.lg.overseas.school.utils.LogUtils;

/**
 * 成功案例
 */

public class SuccessCaseFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private TextView again;
    private String tag;
    private List<LittleData> datas;
    private int page=1;
    private DialogWait dialogWait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_case_item,container,false);
        return v;
    }

    public void setLoad(boolean isMore){
        if(!isMore)page=1;
        else page++;
        initData(isMore);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        again = (TextView) view.findViewById(R.id.again);
        recyclerView = (RecyclerView) view.findViewById(R.id.item_list);
        Bundle bundle = getArguments();
        AutoLinearLayoutManager manager =new AutoLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
//                setLoad(true);
            }
        });
        if(null!=bundle){
            tag = bundle.getString("tag");
        }
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
                again.setVisibility(View.GONE);
            }
        });
        initData(false);
    }

    private void initData(final boolean isMore) {
        dialogWait = new DialogWait(getActivity());
        dialogWait.show();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SUCCESSCASE, RequestMethod.POST);
        req.set("page",page+"").set("pageSize","10").set("catId",TextUtils.isEmpty(tag)?"":tag);
        ((MainActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(null!=dialogWait&&dialogWait.isShowing())dialogWait.dismiss();
                if(response.isSucceed()){
                    try {
                        HotCase hotCase = JSON.parseObject(response.get(), HotCase.class);
                        List<LittleData> data = hotCase.getData();
                        if(!isMore){
                            if(null==data){
                                again.setVisibility(View.VISIBLE);
                                return;
                            }
                            datas=data;
                            SuccessCaseAdapter successAdapter = new SuccessCaseAdapter(getActivity(), datas);
                            recyclerView.setAdapter(successAdapter);
                        }else{
                            datas.addAll(data);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                if(!isMore)again.setVisibility(View.VISIBLE);
                if(null!=dialogWait&&dialogWait.isShowing())dialogWait.dismiss();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null!=dialogWait&&dialogWait.isShowing())dialogWait.dismiss();
    }
}
