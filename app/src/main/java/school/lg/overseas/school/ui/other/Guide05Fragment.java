package school.lg.overseas.school.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.AdverData;
import school.lg.overseas.school.bean.Advertising;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.AdvActivity;
import school.lg.overseas.school.ui.AdvertisingActivity;
import school.lg.overseas.school.ui.MainActivity;

/**
 * Created by Administrator on 2018/2/2.
 */

public class Guide05Fragment extends BaseFragment{
    private ImageView iv;
    private View v1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_guide05,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       iv= (ImageView) view.findViewById(R.id.iv);
       v1= view.findViewById(R.id.v1);
        Glide.with(getActivity()).load(R.mipmap.guide_5).into(iv);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

//    /**
//     *  获取广告页
//     */
//    public void  getGdie() {
//        final Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.OPEN_MAP);
//        ((BaseActivity)getActivity()).request(0, req, new SimpleResponseListener<String>() {
//            @Override
//            public void onStart(int what) {
//
//            }
//
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//
//                Advertising advertising = (Advertising) JSON.parseObject(response.get() ,Advertising.class);
//                AdverData data = advertising.getData();
//                String imgUrl = data.getImage();
//                String jumpUrl = data.getAnswer();
//                AdvertisingActivity.setAdvert(getActivity() ,imgUrl ,5, jumpUrl);
//
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFinish(int what) {
//
//            }
//        });
//    }
}
