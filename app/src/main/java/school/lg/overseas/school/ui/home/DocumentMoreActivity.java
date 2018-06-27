package school.lg.overseas.school.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import school.lg.overseas.school.adapter.GoodsAdapter;
import school.lg.overseas.school.adapter.GoodsSelectAdapter;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Country;
import school.lg.overseas.school.bean.GoodsDetail;
import school.lg.overseas.school.bean.ShoppingMall;
import school.lg.overseas.school.callback.EndLessOnScrollListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.NetworkChildren;
import school.lg.overseas.school.http.NetworkTitle;

/**
 * 更多特色文书（留学文书）
 */

public class DocumentMoreActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back,type_i,country_i;
    private LinearLayout type_ll,country_ll;
    private TextView type_t,country_t,comprehensive,volume,price,newest;
    private RecyclerView list_view,item_list;
    private List<TextView> textViews;
    private List<Country> serviceTypes,countrys;
    private List<GoodsDetail> datas;
    private int pages=1;
    private int oldTag=0;
    private int tag=0;
    private String category="",country="";
    private int categoryId=-1,countryId=-1;
    private PopupWindow popupWindow;

    public static void start(Context context){
        Intent intent =new Intent(context,DocumentMoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_more);
        findView();
        initView();
        initData(1,false);
        setClick();
    }

    private void initData(final int page, final boolean isMore) {
        showLoadDialog();
        Request<String> req = NoHttp.createStringRequest(NetworkTitle.DomainSmartApplyNormal + NetworkChildren.GOODSLIST, RequestMethod.POST);
        req.set("category",category).set("country",country).set("page",page+"");
        switch (oldTag){
            case 1:
                req.set("buyNum","1");
                break;
            case 2:
                req.set("price","1");
                break;
            case 3:
                req.set("time","1");
                break;
        }
        request(0, req, new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                dismissLoadDialog();
                if(response.isSucceed()){
                    try {
                        ShoppingMall shoppingMall = JSON.parseObject(response.get(), ShoppingMall.class);
                        serviceTypes = shoppingMall.getServiceTypes();
                        Country serviceType = new Country();
                        serviceType.setId("");
                        serviceType.setName("所有服务");
                        serviceTypes.add(0,serviceType);
                        countrys = shoppingMall.getCountrys();
                        Country country =new Country();
                        country.setId("");
                        country.setName("所有国家");
                        countrys.add(0,country);
                        List<GoodsDetail> data = shoppingMall.getData().getData();
                        if(isMore){
                            datas.addAll(data);
                            list_view.getAdapter().notifyDataSetChanged();
                        }else {
                            datas=data;
                            GoodsAdapter adapter = new GoodsAdapter(DocumentMoreActivity.this, datas);
                            list_view.setAdapter(adapter);
                        }
                        pages=page;
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

    private void initView() {
        textViews =new ArrayList<>();
        textViews.add(comprehensive);
        textViews.add(volume);
        textViews.add(price);
        textViews.add(newest);
        comprehensive.setSelected(true);
        comprehensive.setTextColor(getResources().getColor(R.color.mainGreen));
        LinearLayoutManager manager =new LinearLayoutManager(DocumentMoreActivity.this,LinearLayoutManager.VERTICAL,false);
        list_view.setLayoutManager(manager);
        list_view.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
//                initData(pages+1,true);
            }
        });
        View popView =getLayoutInflater().inflate(R.layout.fragment_recycler,null);
        item_list = (RecyclerView) popView.findViewById(R.id.item_list);
        LinearLayoutManager itemtManage =new LinearLayoutManager(DocumentMoreActivity.this,LinearLayoutManager.VERTICAL,false);
        item_list.setLayoutManager(itemtManage);
        popupWindow =new PopupWindow(popView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
    }

    private void setClick() {
        back.setOnClickListener(this);
        type_ll.setOnClickListener(this);
        country_ll.setOnClickListener(this);
        comprehensive.setOnClickListener(this);
        volume.setOnClickListener(this);
        price.setOnClickListener(this);
        newest.setOnClickListener(this);
    }


    private void findView() {
        View title = findViewById(R.id.title);
        TextView title_tv = (TextView) title.findViewById(R.id.title_tv);
        title_tv.setText("出国留学");
        back = (ImageView) title.findViewById(R.id.back);
        type_ll = (LinearLayout) findViewById(R.id.type_ll);
        type_t = (TextView) findViewById(R.id.type_t);
        type_i = (ImageView) findViewById(R.id.type_i);
        country_ll = (LinearLayout) findViewById(R.id.country_ll);
        country_t = (TextView) findViewById(R.id.country_t);
        country_i = (ImageView) findViewById(R.id.country_i);
        comprehensive = (TextView) findViewById(R.id.comprehensive);
        volume = (TextView) findViewById(R.id.volume);
        price = (TextView) findViewById(R.id.price);
        newest = (TextView) findViewById(R.id.newest);
        list_view = (RecyclerView) findViewById(R.id.list_view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.type_ll:
                setPop(1);
                break;
            case R.id.country_ll:
                setPop(2);
                break;
            case R.id.comprehensive:
                setSelect(0);
                break;
            case R.id.volume:
                setSelect(1);
                break;
            case R.id.price:
                setSelect(2);
                break;
            case R.id.newest:
                setSelect(3);
                break;
        }
    }
    private void setSelect(int i){
        textViews.get(oldTag).setSelected(false);
        textViews.get(oldTag).setTextColor(getResources().getColor(R.color.black));
        textViews.get(i).setSelected(true);
        textViews.get(i).setTextColor(getResources().getColor(R.color.mainGreen));
        oldTag=i;
        initData(1,false);
    }
    private void setPop(int i){
        country_i.setSelected(false);
        country_t.setTextColor(getResources().getColor(R.color.black));
        type_i.setSelected(false);
        type_t.setTextColor(getResources().getColor(R.color.black));
        if(tag==i){
            popupWindow.dismiss();
            tag=0;
            return;
        }
        if(i==1){
            type_i.setSelected(true);
            type_t.setTextColor(getResources().getColor(R.color.mainGreen));
            GoodsSelectAdapter typeAdapter = new GoodsSelectAdapter(DocumentMoreActivity.this, serviceTypes, new SelectListener() {
                @Override
                public void select(int position) {
                    categoryId=position;
                    category=serviceTypes.get(position).getId();
                    type_t.setText(serviceTypes.get(position).getName());
                    type_i.setSelected(false);
                    type_t.setTextColor(getResources().getColor(R.color.black));
                    initData(1,false);
                    popupWindow.dismiss();
                    tag=0;
                }
            });
            if (categoryId != -1) typeAdapter.setSelectPos(categoryId);
            item_list.setAdapter(typeAdapter);
            popupWindow.showAsDropDown(type_ll, 0, 10);
        }else if(i==2){
            country_i.setSelected(true);
            country_t.setTextColor(getResources().getColor(R.color.mainGreen));
            GoodsSelectAdapter countryAdapter = new GoodsSelectAdapter(DocumentMoreActivity.this, countrys, new SelectListener() {
                @Override
                public void select(int position) {
                    countryId=position;
                    country=countrys.get(position).getId();
                    country_t.setText(countrys.get(position).getName());
                    country_i.setSelected(false);
                    country_t.setTextColor(getResources().getColor(R.color.black));
                    initData(1,false);
                    popupWindow.dismiss();
                    tag=0;
                }
            });
            if (countryId != -1) countryAdapter.setSelectPos(countryId);
            item_list.setAdapter(countryAdapter);
            popupWindow.showAsDropDown(type_ll, 0, 10);
        }
        tag=i;
    }

}
