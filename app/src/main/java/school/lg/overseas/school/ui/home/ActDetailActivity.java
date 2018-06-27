package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ActivityDetail;
import school.lg.overseas.school.bean.LittleData;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.other.ActEnrollDialog;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.LogUtils;
import school.lg.overseas.school.utils.WaitUtils;

/**
 * 活动详情（收藏好像没有)
 */

public class ActDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_tv,name,time,time_length,teacher,content,teacher_detail,enroll;
    private ImageView back,iv;
    private SwipeRefreshLayout refresh;
    private ViewPager viewPager;
    private LittleData data;
    private int oldTag=-1;
    private boolean isCollection;

    public static void start(Context context,String id){
        Intent intent =new Intent(context,ActDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        findView();
        setSelect(0);
        initData();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(this);
        enroll.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
    }


    private void initData() {
        if(null!=getIntent()){
            String id = getIntent().getStringExtra("id");
            showLoadDialog();
            Request<String> req = NoHttp.createStringRequest(NetworkTitle.OPEN + NetworkChildren.ACTIVITYDETAIL, RequestMethod.POST);
            req.set("id",id);
            request(0, req, new SimpleResponseListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    dismissLoadDialog();
                    if(response.isSucceed()){
                        try {
                            ActivityDetail activityDetail = JSON.parseObject(response.get(), ActivityDetail.class);
                            data = activityDetail.getParent();
                            List<Reading> readings = PracticeManager.getInstance().queryForId(1, 3, data.getId());
                            if(null!=readings&&readings.size()!=0){
                                isCollection=true;

                            }else{
                                isCollection=false;

                            }
                            Reading reading = new Reading();
                            reading.setTag(0);
                            reading.setType(3);
                            reading.setId(data.getId());
                            reading.setTitle(data.getName());
                            PracticeManager.getInstance().insert(reading);

                            title_tv.setText(data.getName());
                            name.setText(data.getName());
                            new GlideUtils().load(ActDetailActivity.this,NetworkTitle.OPEN+data.getImage(),iv);
                            time.setText("开课时间:"+data.getCnName());
                            time_length.setText("课程时长:"+data.getProblemComplement());
                            teacher.setText("授课老师:"+data.getListeningFile());
                            List<View> views =new ArrayList<View>();
                            View curriculumView = LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.fragment_curriculum_content,null);
                            WebView web = (WebView) curriculumView.findViewById(R.id.web);
                            web.loadDataWithBaseURL(null, HtmlUtil.getHtml(HtmlUtil.repairContent(data.getSentenceNumber(),NetworkTitle.OPEN),0),"text/html","utf-8",null);
                            View teacherView =LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.fragment_teacher,null);
                            ImageView portrait = (ImageView) teacherView.findViewById(R.id.portrait);
                            TextView teacher_name = (TextView) teacherView.findViewById(R.id.teacher_name);
                            TextView teacher_introduce = (TextView) teacherView.findViewById(R.id.teacher_introduce);
                            new GlideUtils().loadCircle(ActDetailActivity.this,NetworkTitle.OPEN+data.getArticle(),portrait);
                            teacher_name.setText(data.getListeningFile());
                            teacher_introduce.setText(data.getAnswer());
                            views.add(curriculumView);
                            views.add(teacherView);
                            viewPager.setAdapter(new MyViewPageAdapter(views));
                            content.setOnClickListener(ActDetailActivity.this);
                            teacher_detail.setOnClickListener(ActDetailActivity.this);
                            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    setSelect(position);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            });
                        }catch (JSONException e){

                        }

                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    dismissLoadDialog();
                }
            });
        }
    }

    private void findView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        title_tv = (TextView) findViewById(R.id.title_tv);
        back = (ImageView) findViewById(R.id.back);
        iv = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        time_length = (TextView) findViewById(R.id.time_length);
        teacher = (TextView) findViewById(R.id.teacher);
        content = (TextView) findViewById(R.id.content);
        teacher_detail = (TextView) findViewById(R.id.teacher_detail);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        enroll= (TextView) findViewById(R.id.enroll);
    }
    private boolean setSelect(int i){
        if(oldTag==i)return false;
        if(i==0){
            content.setSelected(true);
            content.setTextColor(getResources().getColor(R.color.mainGreen));
            teacher_detail.setSelected(false);
            teacher_detail.setTextColor(getResources().getColor(R.color.black));
        }else{
            content.setSelected(false);
            content.setTextColor(getResources().getColor(R.color.black));
            teacher_detail.setSelected(true);
            teacher_detail.setTextColor(getResources().getColor(R.color.mainGreen));
        }
        oldTag=i;
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.enroll:
                ActEnrollDialog dialog = new ActEnrollDialog(ActDetailActivity.this,R.style.AlphaDialogAct);
                dialog.setData(data.getId(),data.getName());
                dialog.show();
                break;
            case R.id.content:
                if(setSelect(0))viewPager.setCurrentItem(0);
                break;
            case R.id.teacher_detail:
                if(setSelect(1))viewPager.setCurrentItem(1);
                break;
        }
    }

}
