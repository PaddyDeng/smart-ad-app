package school.lg.overseas.school.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.EvaluationMajorAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.SearchSchoolLitle;
import school.lg.overseas.school.callback.SelectListener;

/**
 * 测评专业
 */

public class EvaluationMajorActivity extends BaseActivity {
    private RecyclerView list_view;
    private ImageView back;
    private SwipeRefreshLayout refresh;
    private List<SearchSchoolLitle> data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_major);
        back = (ImageView) findViewById(R.id.back);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("测评专业");
        list_view = (RecyclerView) findViewById(R.id.list_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(false);
            }
        });
        LinearLayoutManager manager =new LinearLayoutManager(EvaluationMajorActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        initData();
    }

    private void initData() {
        if(null!=getIntent()){
            String majors = getIntent().getStringExtra("majors");
            data = JSON.parseArray(majors, SearchSchoolLitle.class);
            EvaluationMajorAdapter adapter =new EvaluationMajorAdapter(EvaluationMajorActivity.this, data, new SelectListener() {
                @Override
                public void select(int position) {
                    Intent intent =new Intent();
                    intent.putExtra("select",position);
                    setResult(122,intent);
                    finish();
                }
            });
            list_view.setAdapter(adapter);
        }
    }

}
