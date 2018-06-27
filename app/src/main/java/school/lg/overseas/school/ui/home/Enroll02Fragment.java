package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.EnrollEvaluation;
import school.lg.overseas.school.callback.EnrollListener;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * Created by Administrator on 2018/1/13.
 */

public class Enroll02Fragment extends BaseFragment {
    private TextView school_name,major_name,last,next;
    private EditText gpa_et,gmat_et,toefl_et;
    private EnrollEvaluation ee;
    private EnrollListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll_02,container,false);
    }

    public void setListener(EnrollEvaluation ee, EnrollListener listener){
        this.ee=ee;
        this.listener=listener;
        initView();
        setClick();
    }

    private void initView() {
        school_name.setText(TextUtils.isEmpty(ee.getSchoolName())?"":ee.getSchoolName());
        major_name.setText(TextUtils.isEmpty(ee.getMajorName())?"":ee.getMajorName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
    }
    private void setClick() {
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setListener(0,ee);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(gpa_et.getText())
                        ||Double.valueOf(gpa_et.getText().toString())<2.5d
                        ||(Double.valueOf(gpa_et.getText().toString())>4.0d&&Double.valueOf(gpa_et.getText().toString())<50)
                        ||Double.valueOf(gpa_et.getText().toString())>100){
                    Toast.makeText(getActivity(),"gpa 成绩在2.5至4.0之间或50至100之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(gmat_et.getText())){
                        if(Double.valueOf(gmat_et.getText().toString())<200
                        ||Double.valueOf(gmat_et.getText().toString())>340&&Double.valueOf(gmat_et.getText().toString())<400
                        ||Double.valueOf(gmat_et.getText().toString())>800) {
                            Toast.makeText(getActivity(), "gmat 成绩在400至800之间，gre 成绩在200至340之间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                }
                ee.setGpa(gpa_et.getText().toString());
                ee.setGmat(TextUtils.isEmpty(gmat_et.getText())?"":gmat_et.getText().toString());
                if(TextUtils.isEmpty(toefl_et.getText())
                        ||Double.valueOf(toefl_et.getText().toString())<5.0d
                        ||(9.0d<Double.valueOf(toefl_et.getText().toString())&&Double.valueOf(toefl_et.getText().toString())<60d)
                        ||Double.valueOf(toefl_et.getText().toString())>120d){
                    Toast.makeText(getActivity(),"toefl 成绩在60至120之间，ielts 成绩在5.0至9.0之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                ee.setToefl(toefl_et.getText().toString());
                listener.setListener(1,ee);
            }
        });
    }

    private void findView(View v) {
        school_name = (TextView) v.findViewById(R.id.school_name);
        major_name = (TextView) v.findViewById(R.id.major_name);
        TextView gpa_t = (TextView) v.findViewById(R.id.gpa_t);
        gpa_et = (EditText) v.findViewById(R.id.gpa_et);
        gmat_et = (EditText) v.findViewById(R.id.gmat_et);
        TextView toefl_t = (TextView) v.findViewById(R.id.toefl_t);
        gpa_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>GPA:"));
        toefl_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>TOEFL/IELTS:"));
        toefl_et = (EditText) v.findViewById(R.id.toefl_et);
        last = (TextView) v.findViewById(R.id.last);
        next = (TextView) v.findViewById(R.id.next);
    }
}
