package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.EnrollBackAdapter;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.EnrollEvaluation;
import school.lg.overseas.school.callback.EnrollListener;

/**
 * Created by Administrator on 2018/1/15.
 */

public class Enroll04Fragment extends BaseFragment implements View.OnClickListener {
    private TextView school_name,major_name,last,next;
    private RecyclerView list_view;
    private EnrollEvaluation ee;
    private EnrollListener listener;
    private List<String> strings;
    private EnrollBackAdapter adapter;

    public void setListener(EnrollEvaluation ee, EnrollListener listener){
        this.ee=ee;
        this.listener=listener;
        school_name.setText(TextUtils.isEmpty(ee.getSchoolName())?"":ee.getSchoolName());
        major_name.setText(TextUtils.isEmpty(ee.getMajorName())?"":ee.getMajorName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll_04,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
    }

    private void initView() {
        strings =new ArrayList<>();
        strings.add("500强/四大实习（工作）经验");
        strings.add("外企实习（工作）经验");
        strings.add("国企实习（工作）经验");
        strings.add("私企实习（工作）经验");
        strings.add("相关专业项目比赛经验");
        strings.add("海外游学经验");
        strings.add("公益活动");
        strings.add("获奖经历");
        LinearLayoutManager manager =new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        adapter =new EnrollBackAdapter(getActivity(),strings);
        list_view.setAdapter(adapter);
    }

    private void findView(View v) {
        school_name = (TextView) v.findViewById(R.id.school_name);
        major_name = (TextView) v.findViewById(R.id.major_name);
        list_view = (RecyclerView) v.findViewById(R.id.list_view);
        last = (TextView) v.findViewById(R.id.last);
        next = (TextView) v.findViewById(R.id.next);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last:
                listener.setListener(0,ee);
                break;
            case R.id.next:
                List<String> selects = adapter.getSelects();
                if(selects.contains("0"))ee.setBigFour("1");
                if(selects.contains("1"))ee.setForeignCompany("1");
                if(selects.contains("2"))ee.setEnterprises("1");
                if(selects.contains("3"))ee.setPrivateEnterprise("1");
                if(selects.contains("4"))ee.setProject("1");
                if(selects.contains("5"))ee.setStudy("1");
                if(selects.contains("6"))ee.setPublicBenefit("1");
                if(selects.contains("7"))ee.setAwards("1");
                listener.setListener(1,ee);
                break;
        }
    }
}
