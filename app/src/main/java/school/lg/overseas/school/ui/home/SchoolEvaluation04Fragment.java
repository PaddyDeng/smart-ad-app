package school.lg.overseas.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.bean.MajorSelect;
import school.lg.overseas.school.bean.SchoolEvaluation;
import school.lg.overseas.school.callback.SchoolListener;
import school.lg.overseas.school.utils.HtmlUtil;

/**
 * Created by Administrator on 2017/12/27.
 */

public class SchoolEvaluation04Fragment extends BaseFragment implements View.OnClickListener {
    private TextView destination_t,education_tv,major_t,major_tv,last,submit;
    private RelativeLayout education_rl,major_rl;
    private SchoolEvaluation se;
    private SchoolListener listener;
    private MajorSelect majorSelect;
    private Country country;

    public void setListener(SchoolEvaluation se, SchoolListener listener){
        this.se=se;
        this.listener =listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_evaluation_04,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void setClick() {
        education_rl.setOnClickListener(this);
        major_rl.setOnClickListener(this);
        last.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initView() {
        destination_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>留学目的地："));
        major_t.setText(HtmlUtil.fromHtml("<font color=\"#ff0000\">*</font>专    业："));
    }

    private void findView(View v) {
        destination_t = (TextView) v.findViewById(R.id.destination_t);
        education_rl = (RelativeLayout) v.findViewById(R.id.education_rl);
        education_tv = (TextView) v.findViewById(R.id.education_tv);
        major_t = (TextView) v.findViewById(R.id.major_t);
        major_rl = (RelativeLayout) v.findViewById(R.id.major_rl);
        major_tv = (TextView) v.findViewById(R.id.major_tv);
        last = (TextView) v.findViewById(R.id.last);
        submit = (TextView) v.findViewById(R.id.submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.education_rl:
                Intent intent1 =new Intent (getActivity(),IntentionalStateActivity.class);
                if(null!=country){
                    intent1.putExtra("position",country.getPosition());
                }
                startActivityForResult(intent1,123);
                break;
            case R.id.major_rl:
                Intent intent =new Intent(getActivity(),ChooseMajorActivity.class);
                if(null!=majorSelect){
                    intent.putExtra("titleTag",majorSelect.getTitleP());
                    intent.putExtra("contentTag",majorSelect.getContentP());
                }
                startActivityForResult(intent,121);
                break;
            case R.id.last:
                listener.setListener(0,se);
                break;
            case R.id.submit:
                if(TextUtils.isEmpty(se.getDestination())){
                    Toast.makeText(getActivity(),"请选择您的意向国家",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(se.getMajor())){
                    Toast.makeText(getActivity(),"请选择您的意向专业",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.setListener(1,se);
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
                se.setMajor(majorSelect.getTitleId());
                se.setMajor_name2(majorSelect.getTitleName()+"-"+majorSelect.getContentName());
            }
        }
        if(requestCode==123){
            if(resultCode==124){
                String select = data.getStringExtra("select");
                country = JSON.parseObject(select, Country.class);
                education_tv.setText(country.getName());
                se.setDestination(country.getId());
            }
        }
    }
}
