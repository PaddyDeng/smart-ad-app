package school.lg.overseas.school.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.MyApplication;
import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.AppVersion;
import school.lg.overseas.school.bean.HomeAdver;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.casePage.CaseFragment;
import school.lg.overseas.school.ui.communication.CommunicationFragment;
import school.lg.overseas.school.ui.dicovery.DiscoveryFragment;
import school.lg.overseas.school.ui.home.HomeFragment;
import school.lg.overseas.school.ui.mine.MineFragment;
import school.lg.overseas.school.ui.other.ActionDialog;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.SharedPreferencesUtils;
import util.UpdateAppUtils;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout home, schoolChoose, communication, discovery, mine;
    private ImageView home_iv, school_iv, communication_iv, discovery_iv, mine_iv;
    private TextView home_tv, school_tv, communication_tv, discovery_tv, mine_tv;

    private FrameLayout fl;
    private List<RelativeLayout> rls;
    private List<ImageView> ivs;
    private List<TextView> tvs;
    private List<Fragment> fragmentList;
    private int oldPage = -1;
    private static MainActivity mInstance;

    public static MainActivity getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        setContentView(R.layout.activity_main);
        findView();
        initView();
        setClick();
        getServiceVersionCode();
        showAdver();
        LoginHelper.againLogin(MainActivity.this, 1);
    }

    public void findView() {
        fl = (FrameLayout) findViewById(R.id.fl);
        home = (RelativeLayout) findViewById(R.id.home);
        schoolChoose = (RelativeLayout) findViewById(R.id.choose_school);
        communication = (RelativeLayout) findViewById(R.id.communication);
        discovery = (RelativeLayout) findViewById(R.id.discovery);
        mine = (RelativeLayout) findViewById(R.id.mine);
        home_iv = (ImageView) findViewById(R.id.home_iv);
        school_iv = (ImageView) findViewById(R.id.school_iv);
        communication_iv = (ImageView) findViewById(R.id.communication_iv);
        discovery_iv = (ImageView) findViewById(R.id.discovery_iv);
        mine_iv = (ImageView) findViewById(R.id.mine_iv);
        home_tv = (TextView) findViewById(R.id.home_tv);
        school_tv = (TextView) findViewById(R.id.school_tv);
        communication_tv = (TextView) findViewById(R.id.communication_tv);
        discovery_tv = (TextView) findViewById(R.id.discovery_tv);
        mine_tv = (TextView) findViewById(R.id.mine_tv);
    }

    private void initView() {
        rls = new ArrayList<>();
        rls.add(home);
        rls.add(communication);
        rls.add(discovery);
        rls.add(schoolChoose);
        rls.add(mine);
        ivs = new ArrayList<>();
        ivs.add(home_iv);
        ivs.add(communication_iv);
        ivs.add(discovery_iv);
        ivs.add(school_iv);
        ivs.add(mine_iv);
        tvs = new ArrayList<>();
        tvs.add(home_tv);
        tvs.add(communication_tv);
        tvs.add(discovery_tv);
        tvs.add(school_tv);
        tvs.add(mine_tv);

        fragmentList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        CommunicationFragment communicationFragment = new CommunicationFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        CaseFragment caseFragment = new CaseFragment();
        MineFragment mineFragment = new MineFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(communicationFragment);
        fragmentList.add(discoveryFragment);
        fragmentList.add(caseFragment);
        fragmentList.add(mineFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, homeFragment, homeFragment.getClass().getName()).commit();
        setChoose(0);
    }

    private void setClick() {
        home.setOnClickListener(this);
        schoolChoose.setOnClickListener(this);
        communication.setOnClickListener(this);
        discovery.setOnClickListener(this);
        mine.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                setChoose(0);
                break;
            case R.id.communication:
                setChoose(1);
                break;
            case R.id.discovery:
                setChoose(2);
                break;
            case R.id.choose_school:
                setChoose(3);
                break;
            case R.id.mine:
                setChoose(4);
                break;
        }
    }

    public void setChoose(int i) {
        if (i != oldPage) {
            if (oldPage == -1) oldPage = 0;
            ivs.get(oldPage).setSelected(false);
            tvs.get(oldPage).setTextColor(getResources().getColor(R.color.mainTextColor));
            ivs.get(i).setSelected(true);
            tvs.get(i).setTextColor(getResources().getColor(R.color.mainGreen));
            chooseFragment(i);
            oldPage = i;
        }
    }

    /**
     *   显示首页弹窗广告
     */
    private void showAdver(){
        final Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ADVER_MAIN);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onStart(int what) {
                super.onStart(what);
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                HomeAdver homeAdver = JSON.parseObject(response.get(),HomeAdver.class);
                String id = SharedPreferencesUtils.getActionId(mContext);
                int times = SharedPreferencesUtils.getCurrentTIMES(mContext);
                if (!TextUtils.equals(id, homeAdver.getId())) {
                    times = 0;
                    SharedPreferencesUtils.setActionId(mContext, homeAdver.getId());
                }
                if (homeAdver.isJudge() && times < homeAdver.getMaxdisplay()) {
                    SharedPreferencesUtils.setCurrentTIMES(mContext, ++times);
                    ActionDialog.getInstance(homeAdver).showDialog(getSupportFragmentManager());
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);

            }
            @Override
            public void onFinish(int what) {
                super.onFinish(what);
            }
        });
    }
    private void chooseFragment(int i) {
        if (oldPage == 0 && i == 0) return;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fragmentList.get(i);
        if (!fragment.isAdded()) {
            ft.hide(fragmentList.get(oldPage)).add(R.id.fl, fragmentList.get(i), fragmentList.get(i).getClass().getName());
        } else {
            ft.hide(fragmentList.get(oldPage)).show(fragmentList.get(i));
        }
        ft.commit();
    }

    /**
     *   app更新
     */
    public void updata(){
        try {
            int appCode = Integer.parseInt(MyApplication.VersionCode);
            Log.e(TAG, "updata: " + appCode );
            UpdateAppUtils.from(this)
                    .serverVersionCode(appCode)
                    .apkPath(MyApplication.apkPath)
                    .isForce(true)
                    .update();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取服务器app版本号
     */
    public void  getServiceVersionCode(){
        final Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.UPDATA);
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onStart(int what) {
                super.onStart(what);
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                AppVersion appVersion = JSON.parseObject(response.get(),AppVersion.class);
                MyApplication.VersionCode = appVersion.getVersionNumber();
                MyApplication.apkPath = appVersion.getVersionAddress();
                updata();
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
            }

            @Override
            public void onFinish(int what) {
                super.onFinish(what);
            }
        });
    }

}
