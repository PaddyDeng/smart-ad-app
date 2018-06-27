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
import school.lg.overseas.school.bean.SchoolEvaluation;
import school.lg.overseas.school.callback.SchoolListener;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SchoolEvaluation01Fragment extends BaseFragment{
    private TextView gpa_t,gamt_t,toefl_t,next;
    private EditText gpa_et,gmat_et,toefl_et;
    private SchoolEvaluation se;
    private SchoolListener listener;

    public void setListener(SchoolEvaluation se, SchoolListener listener){
        this.se =se;
        this.listener =listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_evaluation_01,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void setClick() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(gpa_et.getText())
                        ||Double.valueOf(gpa_et.getText().toString())<2.5d
                        ||(Double.valueOf(gpa_et.getText().toString())>4.0d&&Double.valueOf(gpa_et.getText().toString())<50d)
                        ||Double.valueOf(gpa_et.getText().toString())>100d){
                    Toast.makeText(getActivity(),"gpa 成绩在2.5至4.0之间或50至100之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(gmat_et.getText())){
                    if(Double.valueOf(gmat_et.getText().toString())<200d
                            ||Double.valueOf(gmat_et.getText().toString())>340d&&Double.valueOf(gmat_et.getText().toString())<400d
                            ||Double.valueOf(gmat_et.getText().toString())>800d){
                        Toast.makeText(getActivity(),"gmat 成绩在400至800之间，gre 成绩在200至340之间",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                se.setResult_gpa(gpa_et.getText().toString());
                se.setResult_gmat(TextUtils.isEmpty(gmat_et.getText())?"":gmat_et.getText().toString());
                if(TextUtils.isEmpty(toefl_et.getText())
                        ||Double.valueOf(toefl_et.getText().toString())<5.0d
                        ||(9.0d<Double.valueOf(toefl_et.getText().toString())&&Double.valueOf(toefl_et.getText().toString())<60d)
                        ||Double.valueOf(toefl_et.getText().toString())>120d){
                    Toast.makeText(getActivity(),"toefl 成绩在60至120之间，ielts 成绩在5.0至9.0之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                se.setResult_toefl(toefl_et.getText().toString());
                listener.setListener(0,se);
            }
        });
    }

    private void initView() {
        gpa_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>GPA:"));
        toefl_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>TOEFL/IELTS:"));
    }

    private void findView(View v) {
        gpa_t = (TextView) v.findViewById(R.id.gpa_t);
        gpa_et = (EditText) v.findViewById(R.id.gpa_et);
        gamt_t = (TextView) v.findViewById(R.id.gamt_t);
        gmat_et = (EditText) v.findViewById(R.id.gmat_et);
        toefl_t = (TextView) v.findViewById(R.id.toefl_t);
        toefl_et = (EditText) v.findViewById(R.id.toefl_et);
        next = (TextView) v.findViewById(R.id.next);
    }
}
