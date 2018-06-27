package school.lg.overseas.school.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.adapter.LatelyAdapter;
import school.lg.overseas.school.adapter.MyViewPageAdapter;
import school.lg.overseas.school.adapter.TitleAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.TitleTag;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.ui.other.LatelyDeleteAllDialog;

/**
 * 最近浏览
 */

public class LatelyActivity extends BaseActivity{
    private ImageView back;
    private TextView title_right;
    private RecyclerView title_list;
    private ViewPager viewpager;
    private List<View> views;
    private List<TextView> agains;
    private List<RecyclerView> recyclerViews;
    public static void start(Context context){
        Intent intent =new Intent(context,LatelyActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lately);
        findView();
        initView();
        setClick();
    }

    private void setClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatelyDeleteAllDialog dialog =new LatelyDeleteAllDialog(LatelyActivity.this,R.style.AlphaDialogAct);
                dialog.setContext("确定要全部清空吗？");
                dialog.show();
            }
        });
    }


    public void initView() {
        List<TitleTag> titles =new ArrayList<>();
        titles.add(new TitleTag(null,"学校",null,false));
        titles.add(new TitleTag(null,"专业",null,false));
        titles.add(new TitleTag(null,"问答",null,false));
        titles.add(new TitleTag(null,"活动",null,false));
        titles.add(new TitleTag(null,"知识库",null,false));
        views = new ArrayList<>();
        agains =new ArrayList<>();
        recyclerViews =new ArrayList<>();
        LinearLayoutManager titleManager =new LinearLayoutManager(LatelyActivity.this,LinearLayoutManager.HORIZONTAL,false);
        title_list.setLayoutManager(titleManager);
        final TitleAdapter titleAdapter =new TitleAdapter(LatelyActivity.this, titles, new SelectListener() {
            @Override
            public void select(int position) {
                viewpager.setCurrentItem(position);
            }
        });
        title_list.setAdapter(titleAdapter);
        for (int i = 0; i < titles.size(); i++) {
            List<Reading> readings = PracticeManager.getInstance().queryForType(0, i);
            View v = LayoutInflater.from(LatelyActivity.this).inflate(R.layout.fragment_recycler,null);
            TextView again = (TextView) v.findViewById(R.id.again);
            again.setText("这里什么都没有哦");
            RecyclerView list_view = (RecyclerView) v.findViewById(R.id.item_list);
            final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh.setRefreshing(false);
                }
            });
            LinearLayoutManager manager =new LinearLayoutManager(LatelyActivity.this,LinearLayoutManager.VERTICAL,false);
            list_view.setLayoutManager(manager);
            if(null==readings||readings.size()==0){
                again.setVisibility(View.VISIBLE);
            }else {
                again.setVisibility(View.GONE);
            }
            LatelyAdapter adapter = new LatelyAdapter(LatelyActivity.this, readings);
            list_view.setAdapter(adapter);
            agains.add(again);
            recyclerViews.add(list_view);
            views.add(v);
        }
        viewpager.setAdapter(new MyViewPageAdapter(views));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                titleAdapter.setSelectPos(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("浏览历史");
        title_right = (TextView) title.findViewById(R.id.title_right);
        title_right.setText("全部清空");
        title_right.setVisibility(View.VISIBLE);
        back = (ImageView) title.findViewById(R.id.back);
        title_list = (RecyclerView) findViewById(R.id.title_list);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

}
