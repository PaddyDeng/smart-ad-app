package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.BackgroundEavluation;
import school.lg.overseas.school.callback.BackgroundListener;
import school.lg.overseas.school.ui.other.EditDialog;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * 背景测评第一页
 */

public class BackgroundEvaluation01Fragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView time_t,country_t,major_t,examination_t,internship_t,next_1;
    private EditText time_et,country_et,major_et,gmat_et,toefl_et,ielts_et,internship_et;
    private LinearLayout examination_y,examination_n,internship_y,internship_n;
    private ImageView examination_yiv,examination_niv,internship_yiv,internship_niv;
    private BackgroundListener listener;
    private BackgroundEavluation be;
    private boolean isExamination =true,isInternship=true,isFirst;
    private EditDialog dialog;


    public void setListener(BackgroundEavluation be,BackgroundListener listener){
        this.listener =listener;
        this.be=be;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_eavluation_01,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void initView() {
        String[] strs ={"1<font color=\"#ff0000\">*</font> 计划出国时间：",
                "2<font color=\"#ff0000\">*</font> 意向国家：",
                "3<font color=\"#ff0000\">*</font> 意向专业：",
                "4<font color=\"#ff0000\">*</font> 是否参加过以下考试：",
                "5<font color=\"#ff0000\">*</font> 是否有实习经历："};
        time_t.setText(HtmlUtil.fromHtml(strs[0]));
        country_t.setText(HtmlUtil.fromHtml(strs[1]));
        major_t.setText(HtmlUtil.fromHtml(strs[2]));
        examination_t.setText(HtmlUtil.fromHtml(strs[3]));
        internship_t.setText(HtmlUtil.fromHtml(strs[4]));
        examination_y.setSelected(true);
        internship_y.setSelected(true);
    }

    private void findView(View v) {
        time_t = (TextView) v.findViewById(R.id.time_t);
        time_et = (EditText) v.findViewById(R.id.time_et);
        country_t = (TextView) v.findViewById(R.id.country_t);
        country_et = (EditText) v.findViewById(R.id.country_et);
        major_t = (TextView) v.findViewById(R.id.major_t);
        major_et = (EditText) v.findViewById(R.id.major_et);
        examination_t = (TextView) v.findViewById(R.id.examination_t);
        examination_y = (LinearLayout) v.findViewById(R.id.examination_y);
        examination_yiv = (ImageView) v.findViewById(R.id.examination_yiv);
        examination_n = (LinearLayout) v.findViewById(R.id.examination_n);
        examination_niv = (ImageView) v.findViewById(R.id.examination_niv);
        gmat_et = (EditText) v.findViewById(R.id.gmat_et);
        toefl_et = (EditText) v.findViewById(R.id.toefl_et);
        ielts_et = (EditText) v.findViewById(R.id.ielts_et);
        internship_t = (TextView) v.findViewById(R.id.internship_t);
        internship_y = (LinearLayout) v.findViewById(R.id.internship_y);
        internship_yiv = (ImageView) v.findViewById(R.id.internship_yiv);
        internship_n = (LinearLayout) v.findViewById(R.id.internship_n);
        internship_niv = (ImageView) v.findViewById(R.id.internship_niv);
        internship_et = (EditText) v.findViewById(R.id.internship_et);
        next_1 = (TextView) v.findViewById(R.id.next_1);
    }

    private void setClick() {
        next_1.setOnClickListener(this);
        examination_y.setOnClickListener(this);
        examination_n.setOnClickListener(this);
        internship_y.setOnClickListener(this);
        internship_n.setOnClickListener(this);
        time_et.setOnFocusChangeListener(this);
        country_et.setOnFocusChangeListener(this);
        major_et.setOnFocusChangeListener(this);
        gmat_et.setOnFocusChangeListener(this);
        toefl_et.setOnFocusChangeListener(this);
        ielts_et.setOnFocusChangeListener(this);
        internship_et.setOnFocusChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_1:
                if(TextUtils.isEmpty(time_et.getText())) {
                    Toast.makeText(getActivity(), "请填写计划出国时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setTime(time_et.getText().toString());
                if(TextUtils.isEmpty(country_et.getText())){
                    Toast.makeText(getActivity(),"请填写意向国家",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setCountry(country_et.getText().toString());
                if(TextUtils.isEmpty(major_et.getText())){
                    Toast.makeText(getActivity(),"请填写意向专业",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setMajor(major_et.getText().toString());
                if(isExamination) {
                    if (TextUtils.isEmpty(gmat_et.getText()) && TextUtils.isEmpty(toefl_et.getText()) && TextUtils.isEmpty(ielts_et.getText())) {
                        Toast.makeText(getActivity(), "如若参加过考试，请填写你的考试成绩", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(toefl_et.getText())&&
                            (Double.valueOf(toefl_et.getText().toString()) < 60d
                            || Double.valueOf(toefl_et.getText().toString()) > 120d)) {
                        Toast.makeText(getActivity(), "toefl 成绩在60至120之间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(ielts_et.getText())
                            &&( Double.valueOf(ielts_et.getText().toString()) < 5.0d
                            || Double.valueOf(ielts_et.getText().toString()) > 9.0d) ){
                        Toast.makeText(getActivity(), "ielts 成绩在5.0至9.0之间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(gmat_et.getText())) {
                        if ((Double.valueOf(gmat_et.getText().toString()) < 400 || Double.valueOf(gmat_et.getText().toString()) > 800)) {
                            Toast.makeText(getActivity(), "gamt 成绩在400至900之间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                be.setGmat(TextUtils.isEmpty(gmat_et.getText())?"":gmat_et.getText().toString());
                be.setToefl(TextUtils.isEmpty(toefl_et.getText())?"":toefl_et.getText().toString());
                be.setIelts(TextUtils.isEmpty(ielts_et.getText())?"":ielts_et.getText().toString());

                if(isInternship&&TextUtils.isEmpty(internship_et.getText())){
                    Toast.makeText(getActivity(),"如若有实习经历，请填写你的实习经历",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setWork(internship_et.getText().toString());
                listener.setListener(0,be);
                break;
            case R.id.examination_y:
                isExamination=true;
                examination_y.setSelected(true);
                examination_n.setSelected(false);
                break;
            case R.id.examination_n:
                isExamination=false;
                examination_y.setSelected(false);
                examination_n.setSelected(true);
                break;
            case R.id.internship_y:
                isInternship=true;
                internship_y.setSelected(true);
                internship_n.setSelected(false);
                break;
            case R.id.internship_n:
                isInternship=false;
                internship_y.setSelected(false);
                internship_n.setSelected(true);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        if(b) {
            switch (v.getId()) {
                case R.id.time_et:
                    break;
                case R.id.country_et:
                    break;
                case R.id.major_et:
                    break;
                case R.id.gmat_et:
                    break;
                case R.id.toefl_et:
                    break;
                case R.id.ielts_et:
                    break;
                case R.id.internship_et:
                    break;
            }
        }
    }

}
