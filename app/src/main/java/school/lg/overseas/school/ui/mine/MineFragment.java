package school.lg.overseas.school.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.Personal;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.MainActivity;
import school.lg.overseas.school.ui.home.BackgroundEvaluationActivity;
import school.lg.overseas.school.ui.other.LoginActivity;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.view.overscroll.FastAndOverScrollScrollView;

/**
 * 我的
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private TextView name,fans_num,follow_num,questions_num,answer_num,collection_num;
    private ImageView portrait;
    private RelativeLayout questions_rl,answer_rl,collection_rl,evaluation_rl,last_read_rl,personal_data_rl,service_rl,feedback_rl,evaluate_rl;
    private View fans_rl,follow_rl;
    private PersonalDetail data;
    private boolean login;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        findView(view);
        setClick();
    }

    private void initView() {
        name.setText(TextUtils.isEmpty(data.getNickname())?data.getUserName():data.getNickname());
        new GlideUtils().loadCircle(getActivity(),NetworkTitle.DomainSmartApplyResourceNormal+data.getImage(),portrait);
        fans_num.setText(data.getFans());
        follow_num.setText(data.getFollow());
        questions_num.setText(data.getQuestionNum());
        answer_num.setText(data.getAnswerNum());
    }

    private void setClick() {
        portrait.setOnClickListener(this);
        fans_rl.setOnClickListener(this);
        follow_rl.setOnClickListener(this);
        questions_rl.setOnClickListener(this);
        answer_rl.setOnClickListener(this);
        collection_rl.setOnClickListener(this);
        last_read_rl.setOnClickListener(this);
        personal_data_rl.setOnClickListener(this);
        service_rl.setOnClickListener(this);
        feedback_rl.setOnClickListener(this);
        evaluation_rl.setOnClickListener(this);
    }

    private void initData() {
        ((BaseActivity)getActivity()).showLoadDialog();
        List<Reading> readings = PracticeManager.getInstance().queryAll(1);
        collection_num.setText(null==readings?"0":readings.size()+"");
        String session = SharedPreferencesUtils.getSession(getActivity(), 1);
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.USERINFO, RequestMethod.POST);
            if (!TextUtils.isEmpty(session)) {
                req.setHeader("Cookie", "PHPSESSID=" + session);
            }
            ((MainActivity) getActivity()).request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    ((BaseActivity)getActivity()).dismissLoadDialog();
                    if (response.isSucceed()) {
                        try {
                            Personal datas = JSON.parseObject(response.get(), Personal.class);
                            if(datas.getCode()==1) {
                                data = datas.getData();
                                SharedPreferencesUtils.setPersonal(getActivity(), data);
                                initView();
                            }else{
                                name.setText("未登录");
                                new GlideUtils().loadResCircle(getActivity(),R.mipmap.head_defult,portrait);
                                fans_num.setText("0");
                                follow_num.setText("0");
                                questions_num.setText("0");
                                answer_num.setText("0");
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

    private void findView(View v) {
        FastAndOverScrollScrollView all = (FastAndOverScrollScrollView) v.findViewById(R.id.all);
        name = (TextView) v.findViewById(R.id.name);
        portrait = (ImageView) v.findViewById(R.id.portrait);
        fans_num = (TextView) v.findViewById(R.id.fans_num);
        follow_num = (TextView) v.findViewById(R.id.follow_num);
        questions_rl = (RelativeLayout) v.findViewById(R.id.questions_rl);
        answer_rl = (RelativeLayout) v.findViewById(R.id.answer_rl);
        collection_rl = (RelativeLayout) v.findViewById(R.id.collection_rl);
        questions_num = (TextView) v.findViewById(R.id.questions_num);
        answer_num = (TextView) v.findViewById(R.id.answer_num);
        collection_num = (TextView) v.findViewById(R.id.collection_num);
        evaluation_rl = (RelativeLayout) v.findViewById(R.id.evaluation_rl);
        last_read_rl = (RelativeLayout) v.findViewById(R.id.last_read_rl);
        personal_data_rl = (RelativeLayout) v.findViewById(R.id.personal_data_rl);
        service_rl = (RelativeLayout) v.findViewById(R.id.service_rl);
        feedback_rl = (RelativeLayout) v.findViewById(R.id.feedback_rl);
        evaluate_rl = (RelativeLayout) v.findViewById(R.id.evaluate_rl);
        fans_rl = v.findViewById(R.id.fans_rl);
        follow_rl = v.findViewById(R.id.follow_rl);
    }

    @Override
    public void onClick(View v) {
        login = SharedPreferencesUtils.isLogin(getActivity());
        switch (v.getId()){
            case R.id.portrait:
                //判断是否登录
                if(!login) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 11);
                }else{
                    PersonalCenterActivity.start(getActivity());
                }
                break;
            case R.id.fans_rl:
                if(login) {
                    FansListActivity.start(getActivity(), 0, "");
                }else {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.follow_rl:
                if(login) {
                    FansListActivity.start(getActivity(), 1, "");
                }else {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.questions_rl:
                if(login) {
                    MyQuestionActivity.start(getActivity(),0,"");
                }else {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.answer_rl:
                if(login) {
                    MyQuestionActivity.start(getActivity(), 1, "");
                }else{
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.collection_rl:
                CollectionActivity.start(getActivity());
                break;
            case R.id.last_read_rl:
                LatelyActivity.start(getActivity());
                break;
            case R.id.personal_data_rl:
                PersonalCenterActivity.start(getActivity());
                break;
            case R.id.service_rl:
                BackgroundEvaluationActivity.start(getActivity());
                break;
            case R.id.feedback_rl:
                FeedbackActivity.start(getActivity());
                break;
            case R.id.evaluation_rl:
                if(login) {
                    MyEvaluationActivity.start(getActivity());
                }else {
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==11){
            if(resultCode==10){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

}
