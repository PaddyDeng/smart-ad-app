package school.lg.overseas.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.EnrollEvaluation;
import school.lg.overseas.school.bean.SearchSchool;
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.EnrollListener;
import school.lg.overseas.school.ui.other.SelectSchoolActivity;
import school.lg.overseas.school.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/1/13.
 */

public class Enroll01Fragment extends BaseFragment implements View.OnClickListener {
    private TextView school_et,major_et,first_submit;
    private EnrollEvaluation ee;
    private EnrollListener listener;
    private SearchSchool searchSchool;
    private List<SearchSchoolLitle> major;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll_01,container,false);
    }

    public void setListener(EnrollEvaluation ee,List<SearchSchoolLitle> major, EnrollListener listener){
        this.ee=ee;
        this.listener=listener;
        this.major=major;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void initView() {
        if(null!=major){
            school_et.setText(ee.getSchoolName());
            if(!TextUtils.isEmpty(ee.getMajorName())) {
                major_et.setText(ee.getMajorName());
            }
        }
    }

    private void setClick() {
        school_et.setOnClickListener(this);
        major_et.setOnClickListener(this);
        first_submit.setOnClickListener(this);
    }

    private void findView(View v) {
        school_et = (TextView) v.findViewById(R.id.school_et);
        major_et = (TextView) v.findViewById(R.id.major_et);
        first_submit = (TextView) v.findViewById(R.id.first_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_et:
                Intent intent =new Intent(getActivity(), SelectSchoolActivity.class);
                startActivityForResult(intent,107);
                break;
            case R.id.major_et:
                if(null==major){
                    Toast.makeText(getActivity(),"请先选择学校",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 =new Intent(getActivity(),EvaluationMajorActivity.class);
                intent1.putExtra("majors",JSON.toJSONString(major));
                startActivityForResult(intent1,121);

                break;
            case R.id.first_submit:
                listener.setListener(0,ee);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==107){
            if(resultCode==108){
                String select = data.getStringExtra("select");
                searchSchool = JSON.parseObject(select, SearchSchool.class);
                int country=data.getIntExtra("country",0);
                if(country==0){
                    country= SharedPreferencesUtils.getCountry(getActivity());
                }
                if(country==0)country=1;
                ee.setCountry(country+"");
                ee.setSchoolName(searchSchool.getName());
                ee.setSchoolRank(searchSchool.getRank());
                school_et.setText(searchSchool.getName());
                major = searchSchool.getMajor();
            }
        }
        if(requestCode==121){
            if(resultCode==122){
                int back = data.getIntExtra("select",-1);
                SearchSchoolLitle searchSchoolLitle = major.get(back);
                major_et.setText(searchSchoolLitle.getName());
                ee.setMajorName(searchSchoolLitle.getName());
            }
        }
    }
}