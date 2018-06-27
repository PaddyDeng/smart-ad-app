package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.Enroll;
import school.lg.overseas.school.callback.EnrollCloseListener;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * Created by Administrator on 2018/1/17.
 */

public class EnrollResultDialog extends Dialog {
    private EnrollCloseListener listener;

    private TextView school_major,probability,transcend,explain,confirm;
    public EnrollResultDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public void setData(Enroll enroll, EnrollCloseListener listener){
        this.listener=listener;
        school_major.setText(HtmlUtil.fromHtml("申请到<font color=\"#6f9701\">"+enroll.getSchool()+"-"+enroll.getMajor()+"</font>专业的成功率为："));
        probability.setText(enroll.getPercent()+"%");
        String score="";
        Log.i("测试",enroll.getScore());
        int scores = Integer.valueOf(enroll.getScore());
        if (scores <= 50) score = "30";
	    else if ( scores > 50 && scores <= 60)  score = "40";
	    else if ( scores > 60 && scores <= 70)  score = "55";
	    else if ( scores > 70 && scores <= 80)  score = "70";
	    else if ( scores > 80 && scores <= 90)  score = "80";
	    else if ( scores > 90 && scores <= 100) score = "85";
        transcend.setText(HtmlUtil.fromHtml("已超过<font color=\"#ff6600\">"+score+"%</font>的测试者"));
        explain.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>此报告匹配标准以近5年留学录取成功大数据为技术支撑并不能百分之百代表实际录取结果，仅供参考。"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enroll_result);
        findView();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setLinstener(true);
                dismiss();
            }
        });
    }

    private void findView() {
        school_major = (TextView) findViewById(R.id.school_major);
        probability = (TextView) findViewById(R.id.probability);
        transcend = (TextView) findViewById(R.id.transcend);
        explain = (TextView) findViewById(R.id.explain);
        confirm = (TextView) findViewById(R.id.confirm);
    }
}
