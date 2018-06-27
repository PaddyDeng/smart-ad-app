package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.Pickers;
import school.lg.overseas.school.bean.SchoolEvaluation;
import school.lg.overseas.school.callback.SchoolListener;
import school.lg.overseas.school.view.PickerScrollView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class SchoolEvaluation03Fragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout company_rl;
    private TextView company_tv,last,next,n,y;
    private PickerScrollView psv;
    private PopupWindow popupWindow;
    private EditText experience_et,project_et,study_abroad_et,welfare_et,wnning_et;
    private SchoolEvaluation se;
    private SchoolListener listener;
    private int popPosition;
    private List<Pickers> experience;

    public void setListener(SchoolEvaluation se, SchoolListener listener){
        this.se =se;
        this.listener =listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_evaluation_03,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void initView() {
        experience=new ArrayList<>();
        experience.add(new Pickers(1,"国内四大"));experience.add(new Pickers(2,"500强"));
        experience.add(new Pickers(3,"外企"));experience.add(new Pickers(4,"国企"));
        experience.add(new Pickers(5,"私企"));
        View pop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_education, null);
        n = (TextView) pop.findViewById(R.id.n);
        y = (TextView) pop.findViewById(R.id.y);
        psv = (PickerScrollView) pop.findViewById(R.id.psv);
        n.setOnClickListener(this);
        y.setOnClickListener(this);
        popupWindow = new PopupWindow(pop, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        psv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                popPosition = pickers.getPosition();
            }
        });
    }

    private void setClick() {
        company_rl.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void findView(View v) {
        company_rl = (RelativeLayout) v.findViewById(R.id.company_rl);
        company_tv = (TextView) v.findViewById(R.id.company_tv);
        experience_et = (EditText) v.findViewById(R.id.experience_et);
        project_et = (EditText) v.findViewById(R.id.project_et);
        study_abroad_et = (EditText) v.findViewById(R.id.study_abroad_et);
        welfare_et = (EditText) v.findViewById(R.id.welfare_et);
        wnning_et = (EditText) v.findViewById(R.id.wnning_et);
        last = (TextView) v.findViewById(R.id.last);
        next = (TextView) v.findViewById(R.id.next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.company_rl:
                List<Pickers> experience1 =new ArrayList<>();
                experience1.addAll(experience);
                psv.setData(experience1);
                psv.setSelected(0);
                popupWindow.showAtLocation(getActivity().findViewById(R.id.ll), Gravity.BOTTOM,0,0);
                break;
            case R.id.last:
                listener.setListener(0,se);
                break;
            case R.id.next:
                if(!TextUtils.isEmpty(experience_et.getText()))se.setLive(experience_et.getText().toString());
                if(!TextUtils.isEmpty(project_et.getText()))se.setProject(project_et.getText().toString());
                if(!TextUtils.isEmpty(study_abroad_et.getText()))se.setStudyTour(study_abroad_et.getText().toString());
                if(!TextUtils.isEmpty(welfare_et.getText()))se.setActive(welfare_et.getText().toString());
                if(!TextUtils.isEmpty(wnning_et.getText()))se.setPrice(wnning_et.getText().toString());
                listener.setListener(1,se);
                break;
            case R.id.n:
                popupWindow.dismiss();
                break;
            case R.id.y:
                popupWindow.dismiss();
                if(popPosition!=0) {
                    company_tv.setText(experience.get(popPosition - 1).getShowConetnt());
                    se.setWork(experience.get(popPosition - 1).getPosition() + "");
                }else{
                    company_tv.setText(experience.get(popPosition).getShowConetnt());
                    se.setWork(experience.get(popPosition).getPosition() + "");
                }
                break;
        }
    }
}
