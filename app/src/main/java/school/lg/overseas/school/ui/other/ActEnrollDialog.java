package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ActEnroll;
import school.lg.overseas.school.bean.PraiseBack;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.PhoneAndEmailUtils;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ActEnrollDialog extends Dialog {
    private Context context;
    private ImageView close_btn;
    private EditText name_et,phone_et;
    private TextView submit;
    private String id,title;
    public ActEnrollDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context =context;
    }

    public void setData(String id,String title){
        this.id=id;
        this.title=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_act_enroll);
        close_btn = (ImageView) findViewById(R.id.close_btn);
        name_et = (EditText) findViewById(R.id.name_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        submit = (TextView) findViewById(R.id.submit);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name_et.getText())){
                    Toast.makeText(context,"请填写您的姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = name_et.getText().toString();
                if(TextUtils.isEmpty(phone_et.getText())|| !PhoneAndEmailUtils.isMobileNO(phone_et.getText().toString())){
                    Toast.makeText(context,"请正确填写您的电话",Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone =phone_et.getText().toString();

                ActEnroll actEnroll =new ActEnroll();
                actEnroll.setCatId("236");
                actEnroll.setName(name);
                List<String> list =new ArrayList<>();
                list.add(id);
                list.add(phone);
                list.add(title);
                list.add("Android 留学");
                actEnroll.setExtend(list);
                ((BaseActivity)context).showLoadDialog();
                Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.ACTIVITYENCOLL, RequestMethod.POST);
                req.set("catId","236").set("name",name)
                        .set("extend[0]",TextUtils.isEmpty(id)?"0":id)
                        .set("extend[1]",phone)
                        .set("extend[2]",title)
                        .set("extend[3]","Android 留学");
                ((BaseActivity)context).request(0, req, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        ((BaseActivity)context).dismissLoadDialog();
                        if(response.isSucceed()){
                            try {
                                PraiseBack praiseBack=JSON.parseObject(response.get(), PraiseBack.class);
                                if(praiseBack.getCode()==1){
                                    Toast.makeText(context,"报名成功,请等待我们的联系哦",Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }else{
                                    Toast.makeText(context,"报名失败了",Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }catch (JSONException e){

                            }

                        }
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        ((BaseActivity)context).dismissLoadDialog();
                    }
                });
            }
        });
    }
}
