package school.lg.overseas.school.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.ConcernAdapter;
import school.lg.overseas.school.base.BaseFragment;
import school.lg.overseas.school.bean.BackgroundEavluation;
import school.lg.overseas.school.callback.BackgroundListener;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.PhoneAndEmailUtils;

/**
 * 背景测评第二页
 */

public class BackgroundEvaluation02Fragment extends BaseFragment implements View.OnClickListener {
    private BackgroundListener listener;
    private BackgroundEavluation be;
    private TextView concern_t,name_t,phone_t,num_t,previous_step,submit;
    private RecyclerView concern_list;
    private EditText understand_et,name_et,phone_et,num_et;
    private ConcernAdapter adapter;

    public void setListener(BackgroundEavluation be,BackgroundListener listener){
        this.listener =listener;
        this.be =be;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_eavluation_02,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initView();
        setClick();
    }

    private void setClick() {
        previous_step.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initView() {
        String[] strs ={"6<font color=\"#ff0000\">*</font> 你最关心的问题:",
                "8<font color=\"#ff0000\">*</font> 您的姓名:",
                "9<font color=\"#ff0000\">*</font> 您的电话:",
                "10<font color=\"#ff0000\">*</font> QQ/微信:"};
        concern_t.setText(HtmlUtil.fromHtml(strs[0]));
        name_t.setText(HtmlUtil.fromHtml(strs[1]));
        phone_t.setText(HtmlUtil.fromHtml(strs[2]));
        num_t.setText(HtmlUtil.fromHtml(strs[3]));
        String[] options ={"服务流程","出国考试课程","选校定位","申请步骤","申请费用","奖学金","推荐顾问","其他"};
        GridLayoutManager manager =new GridLayoutManager(getActivity(),3);
        concern_list.setLayoutManager(manager);
        adapter =new ConcernAdapter(getActivity(),options);
        concern_list.setAdapter(adapter);
    }

    private void findView(View v) {
        concern_t = (TextView) v.findViewById(R.id.concern_t);
        concern_list = (RecyclerView) v.findViewById(R.id.concern_list);
        understand_et = (EditText) v.findViewById(R.id.understand_et);
        name_t = (TextView) v.findViewById(R.id.name_t);
        name_et = (EditText) v.findViewById(R.id.name_et);
        phone_t = (TextView) v.findViewById(R.id.phone_t);
        phone_et = (EditText) v.findViewById(R.id.phone_et);
        num_t = (TextView) v.findViewById(R.id.num_t);
        num_et = (EditText) v.findViewById(R.id.num_et);
        previous_step = (TextView) v.findViewById(R.id.previous_step);
        submit = (TextView) v.findViewById(R.id.submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_step:
                listener.setListener(0,be);
                break;
            case R.id.submit:
                List<String> selects = adapter.getSelects();
                if(selects.size()==0){
                    Toast.makeText(getActivity(),"请选择你关心的问题",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setConcerns(selects);
                if(!TextUtils.isEmpty(understand_et.getText()))be.setUnderstand(understand_et.getText().toString());
                if(TextUtils.isEmpty(name_et.getText())){
                    Toast.makeText(getActivity(),"请填写你的名字",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setName(name_et.getText().toString());
                if(TextUtils.isEmpty(phone_et.getText())){
                    Toast.makeText(getActivity(),"请正确填写你的电话",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!PhoneAndEmailUtils.isMobileNO(phone_et.getText().toString())){
                    Toast.makeText(getActivity(),"请填写有效的电话号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setPhone(phone_et.getText().toString());
                if(TextUtils.isEmpty(num_et.getText())){
                    Toast.makeText(getActivity(),"请填写你的QQ/微信",Toast.LENGTH_SHORT).show();
                    return;
                }
                be.setNum(num_et.getText().toString());
                listener.setListener(1,be);
                break;
        }
    }
}
