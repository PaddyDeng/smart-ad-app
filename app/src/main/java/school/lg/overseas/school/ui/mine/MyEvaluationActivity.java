package school.lg.overseas.school.ui.mine;

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
import school.lg.overseas.school.ui.home.EnrollActivity;
import school.lg.overseas.school.ui.home.SchoolEvaluationActivity;

/**
 * 我的测评
 */

public class MyEvaluationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RelativeLayout choose_evaluation,school_evaluation;
    private TextView join_choose_school,join_school;
    public static void start(Context context){
        Intent intent =new Intent(context,MyEvaluationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluation);
        findView();
        setClick();
    }


    private void setClick() {
        back.setOnClickListener(this);
        choose_evaluation.setOnClickListener(this);
        school_evaluation.setOnClickListener(this);
        join_choose_school.setOnClickListener(this);
        join_school.setOnClickListener(this);
    }

    private void findView() {
        TextView title_tv = (TextView)findViewById(R.id.title_tv);
        title_tv.setText("我的测评");
        back = (ImageView)findViewById(R.id.back);
        choose_evaluation = (RelativeLayout) findViewById(R.id.choose_evaluation);
        school_evaluation = (RelativeLayout) findViewById(R.id.school_evaluation);
        join_choose_school = (TextView) findViewById(R.id.join_choose_school);
        join_school = (TextView) findViewById(R.id.join_school);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.join_choose_school:
                SchoolEvaluationActivity.start(MyEvaluationActivity.this);
                break;
            case R.id.join_school:
                EnrollActivity.start(MyEvaluationActivity.this);
                break;
            case R.id.choose_evaluation:
                MyChooseSchoolActivity.start(MyEvaluationActivity.this);
                break;
            case R.id.school_evaluation:
                MyEnrollActivity.start(MyEvaluationActivity.this);
                break;
        }
    }

}
