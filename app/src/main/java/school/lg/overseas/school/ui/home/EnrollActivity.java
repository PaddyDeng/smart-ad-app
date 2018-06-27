package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.EnrollEvaluation;
import school.lg.overseas.school.bean.EnrollResult;
import school.lg.overseas.school.bean.PersonalDetail;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.bean.SearchSchoolList;
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.EnrollCloseListener;
import school.lg.overseas.school.callback.EnrollListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.EnrollResultDialog;
import school.lg.overseas.school.ui.other.NeedLoginDialog;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * 学校录取测评
 */

public class EnrollActivity extends BaseActivity {
    private ImageView back;
    private EnrollEvaluation ee;
    private List<SearchSchoolLitle> major;
    private int tag =0;

    private Enroll01Fragment enroll01Fragment;
    private Enroll02Fragment enroll02Fragment;
    private Enroll03Fragment enroll03Fragment;
    private Enroll04Fragment enroll04Fragment;

    private boolean isSubmit;

    public static void start(Context context){
        Intent intent=new Intent(context,EnrollActivity.class);
        context.startActivity(intent);
    }

    public static void start01(Context context,String schoolName,String majorName){
        Intent intent =new Intent(context,EnrollActivity.class);
        intent.putExtra("school",schoolName);
        intent.putExtra("major",majorName);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_enroll);
        findView();
        ee= new EnrollEvaluation();
        getAgrs();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        enroll01Fragment = new Enroll01Fragment();
        enroll02Fragment =new Enroll02Fragment();
        enroll03Fragment = new Enroll03Fragment();
        enroll04Fragment =new Enroll04Fragment();
        enroll01Fragment.setListener(ee, major,new EnrollListener() {
            @Override
            public void setListener(int i, EnrollEvaluation ee) {
                tag=1;
                enroll02Fragment.setListener(ee, new EnrollListener() {
                    @Override
                    public void setListener(int i, EnrollEvaluation ee) {
                        if(i==0){
                            tag=0;
                            getSupportFragmentManager().beginTransaction().hide(enroll02Fragment).show(enroll01Fragment).commit();
                        }else{
                            tag=2;
                            enroll03Fragment.setListener(ee, new EnrollListener() {
                                @Override
                                public void setListener(int i, EnrollEvaluation ee) {
                                    if(i==0){
                                        tag=1;
                                        getSupportFragmentManager().beginTransaction().hide(enroll03Fragment).show(enroll02Fragment).commit();
                                    }else{
                                        tag=3;
                                        //第4页
                                        enroll04Fragment.setListener(ee, new EnrollListener() {
                                            @Override
                                            public void setListener(int i, EnrollEvaluation ee) {
                                                if(i==0){
                                                    tag=2;
                                                    getSupportFragmentManager().beginTransaction().hide(enroll04Fragment).show(enroll03Fragment).commit();
                                                }else{
                                                    //提交
                                                    toSubmit(ee);
                                                }
                                            }
                                        });
                                        getSupportFragmentManager().beginTransaction().hide(enroll03Fragment).show(enroll04Fragment).commit();
                                    }
                                }
                            });
                            getSupportFragmentManager().beginTransaction().hide(enroll02Fragment).show(enroll03Fragment).commit();
                        }
                    }
                });
                getSupportFragmentManager().beginTransaction().hide(enroll01Fragment).show(enroll02Fragment).commit();
            }
        });

        ft.add(R.id.fl,enroll01Fragment).add(R.id.fl,enroll02Fragment).add(R.id.fl,enroll03Fragment).add(R.id.fl,enroll04Fragment);
        ft.hide(enroll02Fragment).hide(enroll03Fragment).hide(enroll04Fragment);
        ft.commit();

    }


    private void selectSchool(String id){
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSchoolNormal + NetworkChildren.SCHOOLANDMAJOR, RequestMethod.POST);
        req.set("page","1").set("pageSize","10").set("school",id);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        SearchSchoolList searchSchoolList = JSON.parseObject(response.get(), SearchSchoolList.class);
                        List<SearchSchool> data = searchSchoolList.getData();
                        SearchSchool searchSchool = data.get(0);
                        ee.setCountry(searchSchool.getCountry()+"");
                        major = searchSchool.getMajor();
                        initView();
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
                initView();
            }
        });
    }
    private void toSubmit(EnrollEvaluation ee) {
        if(!isSubmit) {
            isSubmit=true;
            String session = SharedPreferencesUtils.getSession(EnrollActivity.this, 1);
            if (TextUtils.isEmpty(session)) {
                isSubmit=false;
                LoginHelper.needLogin(EnrollActivity.this, "需要登录才能继续哦");
                return;
            }
            showLoadDialog();
            List<String>countrys =new ArrayList<>();
            countrys.add("1");
            countrys.add("2");
            countrys.add("3");
            countrys.add("4");
            countrys.add("5");
            countrys.add("6");
            if(!countrys.contains(ee.getCountry())){
                ee.setCountry("1");
            }
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ENROLLSUBMIT, RequestMethod.POST);
            req.set("schoolName", ee.getSchoolName()).set("majorName", ee.getMajorName()).set("gpa", ee.getGpa()).set("toefl", ee.getToefl()).set("education", ee.getEducation())
                    .set("school", ee.getSchool()).set("attendSchool", ee.getAttendSchool()).set("major", ee.getMajor());
            req.setHeader("Cookie", "PHPSESSID=" + session);
            req.set("gamt", TextUtils.isEmpty(ee.getGmat()) ? "" : ee.getGmat());
            req.set("bigFour", TextUtils.isEmpty(ee.getBigFour()) ? "" : ee.getBigFour());
            req.set("foreignCompany", TextUtils.isEmpty(ee.getForeignCompany()) ? "" : ee.getForeignCompany());
            req.set("enterprises", TextUtils.isEmpty(ee.getEnterprises()) ? "" : ee.getEnterprises());
            req.set("privateEnterprise", TextUtils.isEmpty(ee.getPrivateEnterprise()) ? "" : ee.getPrivateEnterprise());
            req.set("project", TextUtils.isEmpty(ee.getProject()) ? "" : ee.getProject());
            req.set("study", TextUtils.isEmpty(ee.getStudy()) ? "" : ee.getStudy());
            req.set("publicBenefit", TextUtils.isEmpty(ee.getPublicBenefit()) ? "" : ee.getPublicBenefit());
            req.set("awards", TextUtils.isEmpty(ee.getAwards()) ? "" : ee.getAwards());
            req.set("country", ee.getCountry());
//            req.set("schoolRank", ee.getSchoolRank());
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    isSubmit=false;
                    dismissLoadDialog();
                    if (response.isSucceed()) {
                        try {
                            PraiseBack praiseBack = JSON.parseObject(response.get(), PraiseBack.class);
                            if (praiseBack.getCode() == 0) {
                                LoginHelper.needLogin(EnrollActivity.this, "登录已过期，请重新登录");
                                return;
                            } else if (praiseBack.getCode() == 1) {//发送成功
                                getResult();
                            } else {
                                Toast.makeText(EnrollActivity.this, praiseBack.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }

                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    isSubmit=false;
                    Toast.makeText(EnrollActivity.this,"网络不给力啊，请重新设置网络，再提交",Toast.LENGTH_SHORT).show();
                    dismissLoadDialog();
                }
            });
        }else{
            Toast.makeText(EnrollActivity.this,"正在提交并获取结果中，请不要重复提交哦",Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("学校录取测评");
    }

    private void getAgrs() {
        if(null!=getIntent()){
            String id = getIntent().getStringExtra("school");
            String majorName = getIntent().getStringExtra("major");
            if(!TextUtils.isEmpty(id)) {
                ee.setSchoolName(id);
                ee.setMajorName(majorName);
                selectSchool(id);
            }else{
                initView();
            }
        }else{
            initView();
        }
    }
        private void getResult(){
        PersonalDetail personalDetail = SharedPreferencesUtils.getPersonalDetail(EnrollActivity.this);
        String session = SharedPreferencesUtils.getSession(EnrollActivity.this, 1);
        Request<String> req = NoHttp.createStringRequest("http://www.smartapply.cn/cn/app-api/probability-result", RequestMethod.POST);
        req.set("uid",personalDetail.getUid());
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.isSucceed()){
                    EnrollResult enrolls = JSON.parseObject(response.get(), EnrollResult.class);
                    EnrollResultDialog dialog =new EnrollResultDialog(EnrollActivity.this,R.style.AlphaDialogAct);
                    dialog.show();
                    dialog.setData(enrolls.getData(),new EnrollCloseListener(){

                        @Override
                        public void setLinstener(boolean isClose) {
                            if(isClose){
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }
        });
    }

}
