package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.BackgroundEavluation;
import school.lg.overseas.school.callback.BackgroundListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.KeyboardUtils;
import school.lg.overseas.school.utils.LogUtils;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BackgroundEvaluationActivity extends BaseActivity {
    private int tag;
    private ImageView back;
    private LinearLayout main_ll;
    private BackgroundEvaluation01Fragment fragment01;
    private BackgroundEvaluation02Fragment fragment02;
    private BackgroundEvaluation03Fragment fragment03;
    private BackgroundEavluation be;
    private FrameLayout fl;
    private ScrollView sv;

    public static void start(Context context){
        Intent intent =new Intent(context,BackgroundEvaluationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_background_evaluation);
        findView();
        initView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void scrollTo(int i){
        sv.scrollTo(0,i);
    }

    private void initView() {
        be = new BackgroundEavluation();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment01 = new BackgroundEvaluation01Fragment();
        fragment02 = new BackgroundEvaluation02Fragment();
        fragment03 = new BackgroundEvaluation03Fragment();
        fragment01.setListener(be, new BackgroundListener() {
            @Override
            public void setListener(int i,BackgroundEavluation be1) {
//                be=be1;
                tag=1;
                getSupportFragmentManager().beginTransaction().hide(fragment01).show(fragment02).commit();
            }
        });
        fragment02.setListener(be, new BackgroundListener() {
            @Override
            public void setListener(int i,BackgroundEavluation be1) {
                if(i==0){
                    tag=0;
                    getSupportFragmentManager().beginTransaction().hide(fragment02).show(fragment01).commit();
                }else{
//                    be=be1;
                    //提交
                    Log.i("测试",be.toString());
                    toSend(be);
                    tag=2;
                    getSupportFragmentManager().beginTransaction().hide(fragment02).show(fragment03).commit();
                }
            }
        });
        ft.add(R.id.fl,fragment01,fragment01.getClass().getSimpleName());
        ft.add(R.id.fl,fragment02,fragment02.getClass().getSimpleName());
        ft.add(R.id.fl,fragment03,fragment03.getClass().getSimpleName());
        ft.hide(fragment02).hide(fragment03);
        tag=0;
        ft.commit();
    }

    private void toSend(BackgroundEavluation be) {
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.BACKE, RequestMethod.POST);
        req.set("planTime",be.getTime()).set("country",be.getCountry()).set("major",be.getMajor()).set("gmat",be.getGmat())
                .set("toefl",be.getToefl()).set("ielts",be.getIelts()).set("experience",be.getWork())
                .set("supplement",be.getUnderstand()).set("uName",be.getName()).set("phone",be.getPhone()).set("weChat",be.getNum());
        for (int i = 0; i < be.getConcerns().size(); i++) {
            req.set("emphases["+i+"]",be.getConcerns().get(i));
        }
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }
        });
    }

    private void findView() {
        sv= (ScrollView) findViewById(R.id.sv);
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("背景测评");
        back = (ImageView) title.findViewById(R.id.back);
        main_ll = (LinearLayout) findViewById(R.id.main_ll);
        fl = (FrameLayout) findViewById(R.id.fl);
    }

    @Override
    protected boolean preBackExitPage() {
        if(tag==1){
            tag=0;
            getSupportFragmentManager().beginTransaction().hide(fragment02).show(fragment01).commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
