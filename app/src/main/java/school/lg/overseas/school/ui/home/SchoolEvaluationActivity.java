package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.w3c.dom.Text;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Pickers;
import school.lg.overseas.school.bean.SchoolDetails;
import school.lg.overseas.school.bean.SchoolEvaluation;
import school.lg.overseas.school.callback.SchoolListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.NeedLoginDialog;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import school.lg.overseas.school.view.PickerScrollView;

/**
 * 选校测评
 */

public class SchoolEvaluationActivity extends BaseActivity{
    private ImageView back,school_back_iv,personal_back_iv,direction_iv,iv1,iv2,iv3;
    private TextView school_back_tv,personal_back_tv,direction_tv,tv1,tv2,tv3;
    private View v1,v2,v3;
    private SchoolEvaluation se;
    private SchoolEvaluation01Fragment fragment01;
    private SchoolEvaluation02Fragment fragment02;
    private SchoolEvaluation03Fragment fragment03;
    private SchoolEvaluation04Fragment fragment04;
    private int tag;

    public static void start(Context context){
        Intent intent =new Intent(context,SchoolEvaluationActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_school_evaluation);
        findView();
        initView();
    }

    private void initView() {
        se=new SchoolEvaluation();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment01 =new SchoolEvaluation01Fragment();
        fragment02 =new SchoolEvaluation02Fragment();
        fragment03 =new SchoolEvaluation03Fragment();
        fragment04 =new SchoolEvaluation04Fragment();
        fragment01.setListener(se, new SchoolListener() {
            @Override
            public void setListener(int i, SchoolEvaluation se) {
                tag=1;
                school_back_iv.setSelected(true);
                school_back_tv.setTextColor(getResources().getColor(R.color.mainGreen));
                v1.setBackgroundColor(getResources().getColor(R.color.mainGreen));
                iv1.setSelected(true);
                tv1.setTextColor(getResources().getColor(R.color.mainGreen));
                getSupportFragmentManager().beginTransaction().hide(fragment01).show(fragment02).commit();
            }
        });
        fragment02.setListener(se, new SchoolListener() {
            @Override
            public void setListener(int i, SchoolEvaluation se) {
                if(i==0){
                    tag=0;
                    school_back_iv.setSelected(false);
                    school_back_tv.setTextColor(getResources().getColor(R.color.white));
                    v1.setBackgroundColor(getResources().getColor(R.color.white));
                    iv1.setSelected(false);
                    tv1.setTextColor(getResources().getColor(R.color.black));
                    getSupportFragmentManager().beginTransaction().hide(fragment02).show(fragment01).commit();
                }else {
                    tag=2;
                    personal_back_iv.setSelected(true);
                    personal_back_tv.setTextColor(getResources().getColor(R.color.mainGreen));
                    v2.setBackgroundColor(getResources().getColor(R.color.mainGreen));
                    iv2.setSelected(true);
                    tv2.setTextColor(getResources().getColor(R.color.mainGreen));
                    getSupportFragmentManager().beginTransaction().hide(fragment02).show(fragment03).commit();
                }
            }
        });
        fragment03.setListener(se, new SchoolListener() {
            @Override
            public void setListener(int i, SchoolEvaluation se) {
                if(i==0){
                    tag=1;
                    personal_back_iv.setSelected(false);
                    personal_back_tv.setTextColor(getResources().getColor(R.color.white));
                    v2.setBackgroundColor(getResources().getColor(R.color.white));
                    iv2.setSelected(false);
                    tv2.setTextColor(getResources().getColor(R.color.black));
                    getSupportFragmentManager().beginTransaction().hide(fragment03).show(fragment02).commit();
                }else{
                    tag=3;
                    direction_iv.setSelected(true);
                    direction_tv.setTextColor(getResources().getColor(R.color.mainGreen));
                    v3.setBackgroundColor(getResources().getColor(R.color.mainGreen));
                    iv3.setSelected(true);
                    tv3.setTextColor(getResources().getColor(R.color.mainGreen));
                    getSupportFragmentManager().beginTransaction().hide(fragment03).show(fragment04).commit();
                }
            }
        });
        fragment04.setListener(se, new SchoolListener() {
            @Override
            public void setListener(int i, SchoolEvaluation se) {
                if(i==0){
                    tag=2;
                    direction_iv.setSelected(false);
                    direction_tv.setTextColor(getResources().getColor(R.color.white));
                    v3.setBackgroundColor(getResources().getColor(R.color.white));
                    iv3.setSelected(false);
                    tv3.setTextColor(getResources().getColor(R.color.black));
                    getSupportFragmentManager().beginTransaction().hide(fragment04).show(fragment03).commit();
                }else {
                    toSubmit(se);
                }
            }
        });
        ft.add(R.id.fl,fragment01).add(R.id.fl,fragment02).add(R.id.fl,fragment03).add(R.id.fl,fragment04);
        ft.hide(fragment02).hide(fragment03).hide(fragment04).commit();
        tag=0;
    }

    private void toSubmit(SchoolEvaluation se) {
        String session = SharedPreferencesUtils.getSession(SchoolEvaluationActivity.this,1);
        if(TextUtils.isEmpty(session)){
            LoginHelper.needLogin(SchoolEvaluationActivity.this,"需要先登录才能继续哦");
            return;
        }
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.SCHOOLEVALUATIONSUBMIT, RequestMethod.POST);
        req.set("result_gpa",se.getResult_gpa()).set("result_gmat",se.getResult_gmat()).set("result_toefl",se.getResult_toefl())
                .set("active",se.getActive()).set("destination",se.getDestination()).set("education",se.getEducation())
                .set("live",se.getLive()).set("major",se.getMajor()).set("major_name1",se.getMajor_name1())
                .set("major_name2",se.getMajor_name2()).set("major_top",se.getMajor_top()).set("price",se.getPrice())
                .set("project",se.getProject()).set("school",se.getSchool()).set("schoolName",se.getSchoolName())
                .set("school_major",se.getSchool_major()).set("studyTour",se.getStudyTour()).set("work",se.getWork());
        req.setHeader("Cookie","PHPSESSID="+session);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                LogUtils.log(response.get());
                if(response.isSucceed()){
                    try {
                        SchoolDetails schoolDetails = JSON.parseObject(response.get(), SchoolDetails.class);
                        if(schoolDetails.getCode()==1){
                            SchoolEvaluationResultActivity.start(SchoolEvaluationActivity.this,"");
                            finish();
                        }else{
                            NeedLoginDialog dialog =new NeedLoginDialog(SchoolEvaluationActivity.this,R.style.AlphaDialogAct);
                            dialog.show();
                        }
                    }catch (JSONException e){

                    }

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                dismissLoadDialog();
            }
        });
    }


    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("选校测评");
        back = (ImageView) title.findViewById(R.id.back);
        school_back_iv = (ImageView) findViewById(R.id.school_back_iv);
        school_back_tv = (TextView) findViewById(R.id.school_back_tv);
        personal_back_iv = (ImageView) findViewById(R.id.personal_back_iv);
        personal_back_tv = (TextView) findViewById(R.id.personal_back_tv);
        direction_iv = (ImageView) findViewById(R.id.direction_iv);
        direction_tv = (TextView) findViewById(R.id.direction_tv);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
