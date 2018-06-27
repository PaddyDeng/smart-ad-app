package school.lg.overseas.school.ui.home;

import android.content.Intent;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.EnrollEvaluation;
import school.lg.overseas.school.bean.MajorSelect;
import school.lg.overseas.school.bean.Pickers;
import school.lg.overseas.school.callback.EnrollListener;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.view.PickerScrollView;

/**
 * Created by Administrator on 2018/1/14.
 */

public class Enroll03Fragment extends BaseFragment implements View.OnClickListener {
    private TextView education_t,education_tv,graduate_school_t,graduate_school_tv,school_detail_t,major_t,major_tv,last,next,n,y,school_name,major_name;
    private PickerScrollView psv;
    private RelativeLayout education_rl,graduate_school_rl,major_rl;
    private EditText school_detail_et;
    private EnrollEvaluation ee;
    private EnrollListener listener;
    private PopupWindow popupWindow;
    private int popTag;
    private int popPosition;
    private List<Pickers> educationList,schoolList;
    private MajorSelect majorSelect;

    public void setListener(EnrollEvaluation ee, EnrollListener listener){
        this.ee =ee;
        this.listener =listener;
        school_name.setText(TextUtils.isEmpty(ee.getSchoolName())?"":ee.getSchoolName());
        major_name.setText(TextUtils.isEmpty(ee.getMajorName())?"":ee.getMajorName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll_03,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void setClick() {
        education_rl.setOnClickListener(this);
        graduate_school_rl.setOnClickListener(this);
        major_rl.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void initView() {
        
        education_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>目前学历:"));
        graduate_school_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>就读/毕业院校:"));
        school_detail_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>详细填写学校名称:"));
        major_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>专        业:"));

        educationList =new ArrayList<>();
        educationList.add(new Pickers(0,"博士"));educationList.add(new Pickers(1,"硕士"));educationList.add(new Pickers(2,"本科"));
        educationList.add(new Pickers(3,"专科"));educationList.add(new Pickers(4,"高中"));educationList.add(new Pickers(5,"初中"));
        schoolList =new ArrayList<>();
        schoolList.add(new Pickers(1,"清北复交浙大"));schoolList.add(new Pickers(2,"985学校"));schoolList.add(new Pickers(3,"211学校"));
        schoolList.add(new Pickers(4,"非211本科"));schoolList.add(new Pickers(5,"专科"));

        View pop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_education, null);
        n = (TextView) pop.findViewById(R.id.n);
        y = (TextView) pop.findViewById(R.id.y);
        psv = (PickerScrollView) pop.findViewById(R.id.psv);
        n.setOnClickListener(this);
        y.setOnClickListener(this);
        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        psv.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                popPosition = pickers.getPosition();
            }
        });
    }

    private void findView(View v) {
        school_name = (TextView) v.findViewById(R.id.school_name);
        major_name = (TextView) v.findViewById(R.id.major_name);
        education_t = (TextView) v.findViewById(R.id.education_t);
        education_rl = (RelativeLayout) v.findViewById(R.id.education_rl);
        education_tv = (TextView) v.findViewById(R.id.education_tv);
        graduate_school_t = (TextView) v.findViewById(R.id.graduate_school_t);
        graduate_school_rl = (RelativeLayout) v.findViewById(R.id.graduate_school_rl);
        graduate_school_tv = (TextView) v.findViewById(R.id.graduate_school_tv);
        school_detail_t = (TextView) v.findViewById(R.id.school_detail_t);
        school_detail_et = (EditText) v.findViewById(R.id.school_detail_et);
        major_t = (TextView) v.findViewById(R.id.major_t);
        major_rl = (RelativeLayout) v.findViewById(R.id.major_rl);
        major_tv = (TextView) v.findViewById(R.id.major_tv);
        last = (TextView) v.findViewById(R.id.last);
        next = (TextView) v.findViewById(R.id.next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.education_rl:
                popTag = 0;
                List<Pickers> educationList1 = new ArrayList<>();
                educationList1.addAll(educationList);
                psv.setData(educationList1);
                psv.setSelected(0);
                popupWindow.showAtLocation(getActivity().findViewById(R.id.ll), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.graduate_school_rl:
                popTag = 1;
                List<Pickers> schoolList1 = new ArrayList<>();
                schoolList1.addAll(schoolList);
                psv.setData(schoolList1);
                psv.setSelected(0);
                popupWindow.showAtLocation(getActivity().findViewById(R.id.ll), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.major_rl:
                Intent intent = new Intent(getActivity(), ChooseMajorActivity.class);
                if (null != majorSelect) {
                    intent.putExtra("titleTag", majorSelect.getTitleP());
                    intent.putExtra("contentTag", majorSelect.getContentP());
                }
                startActivityForResult(intent, 121);
                break;
            case R.id.last:
                listener.setListener(0, ee);
                break;
            case R.id.next:
                if (TextUtils.isEmpty(ee.getEducation())) {
                    Toast.makeText(getActivity(), "请选择您目前的学历", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ee.getSchool())) {
                    Toast.makeText(getActivity(), "请选择您当前就读/毕业院校", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(school_detail_et.getText())) {
                    Toast.makeText(getActivity(), "请填写您学校详细名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                ee.setAttendSchool(school_detail_et.getText().toString());
                //判断专业是否选择
                if (TextUtils.isEmpty(ee.getMajor())) {
                    Toast.makeText(getActivity(), "请选择您当前专业", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.setListener(1, ee);
                break;
            case R.id.n:
                popupWindow.dismiss();
                break;
            case R.id.y:
                popupWindow.dismiss();
                if (popTag == 0) {
                    education_tv.setText(educationList.get(popPosition).getShowConetnt());
                    ee.setEducation(educationList.get(popPosition).getShowConetnt());
                } else if (popTag == 1) {
                    if(popPosition!=0) {
                        graduate_school_tv.setText(schoolList.get(popPosition - 1).getShowConetnt());
                        ee.setSchool(schoolList.get(popPosition - 1).getPosition() + "");
                    }else{
                        graduate_school_tv.setText(schoolList.get(popPosition).getShowConetnt());
                        ee.setSchool(schoolList.get(popPosition).getPosition() + "");
                    }
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==121){
            if(resultCode==122){
                String back = data.getStringExtra("majorSelect");
                majorSelect = JSON.parseObject(back, MajorSelect.class);
                major_tv.setText(majorSelect.getTitleName()+"-"+majorSelect.getContentName());
                ee.setMajor(majorSelect.getContentName());
                ee.setMajor_top(majorSelect.getContentId());
                major_tv.setText(majorSelect.getContentName());
            }
        }
    }
}
