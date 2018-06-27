package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;

/**
 * 留学评估系统
 */

public class DoTestActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RelativeLayout background_rl,school_rl,enroll_rl;

    public static void start(Context context){
        Intent intent =new Intent(context,DoTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_test);
        findView();
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        background_rl.setOnClickListener(this);
        school_rl.setOnClickListener(this);
        enroll_rl.setOnClickListener(this);
    }

    private void initData() {

    }

    private void findView() {
        RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("留学评估系统");
        back = (ImageView) title.findViewById(R.id.back);
        background_rl = (RelativeLayout) findViewById(R.id.background_rl);
        school_rl = (RelativeLayout) findViewById(R.id.school_rl);
        enroll_rl = (RelativeLayout) findViewById(R.id.enroll_rl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.background_rl:
                BackgroundEvaluationActivity.start(DoTestActivity.this);
                break;
            case R.id.school_rl:
                SchoolEvaluationActivity.start(DoTestActivity.this);
                break;
            case R.id.enroll_rl:
                EnrollActivity.start(DoTestActivity.this);
                break;
        }
    }

}
