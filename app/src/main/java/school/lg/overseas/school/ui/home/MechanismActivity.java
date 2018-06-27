package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;

/**
 * 寻机构
 */

public class MechanismActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView mechanism_t,adviser_t;
    private int oldTag=0;
    private MechanismFragment mechanismFragment;
    private AdviserFragment adviserFragment;

    public static void start(Context context){
        Intent intent =new Intent(context,MechanismActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanism);
        findView();
        initView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        mechanism_t.setOnClickListener(this);
        adviser_t.setOnClickListener(this);
    }

    private void initView() {
        adviser_t.setSelected(true);
        adviser_t.setTextColor(getResources().getColor(R.color.mainGreen));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        adviserFragment = new AdviserFragment();
        mechanismFragment = new MechanismFragment();
        ft.add(R.id.fl,adviserFragment).add(R.id.fl,mechanismFragment);
        ft.hide(mechanismFragment);
        ft.commit();
    }

    private void findView() {
        back = (ImageView) findViewById(R.id.back);
        mechanism_t = (TextView) findViewById(R.id.mechanism_t);
        adviser_t = (TextView) findViewById(R.id.adviser_t);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.mechanism_t:
                setPage(1);
                break;
            case R.id.adviser_t:
                setPage(0);
                break;
        }
    }
    private void setPage(int i){
        if(oldTag==i)return;
        if(i==1){
            mechanism_t.setSelected(true);
            mechanism_t.setTextColor(getResources().getColor(R.color.mainGreen));
            adviser_t.setSelected(false);
            adviser_t.setTextColor(getResources().getColor(R.color.white));
            getSupportFragmentManager().beginTransaction().hide(adviserFragment).show(mechanismFragment).commit();
        }else{
            mechanism_t.setSelected(false);
            mechanism_t.setTextColor(getResources().getColor(R.color.white));
            adviser_t.setSelected(true);
            adviser_t.setTextColor(getResources().getColor(R.color.mainGreen));
            getSupportFragmentManager().beginTransaction().hide(mechanismFragment).show(adviserFragment).commit();
        }
        oldTag=i;
    }

}
