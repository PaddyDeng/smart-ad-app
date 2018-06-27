package school.lg.overseas.school.ui.dicovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import q.rorbin.badgeview.QBadgeView;
import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.http.HttpUtil;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.adapter.AbraodInfoAdapter;
import school.lg.overseas.school.ui.dicovery.adapter.AbroadCommentAdapter;
import school.lg.overseas.school.ui.dicovery.adapter.LabelAdapter;
import school.lg.overseas.school.ui.dicovery.bean.AbroadBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.ui.dicovery.bean.LaudBean;
import school.lg.overseas.school.utils.C;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.HttpUtils;
import school.lg.overseas.school.utils.StringUtils;
import school.lg.overseas.school.view.AndroidBug5497Workaround;
import school.lg.overseas.school.view.DislocationLayoutManager;

public class ArticleDetailsActivity extends BaseActivity {
    private static final String TAG = ArticleDetailsActivity.class.getSimpleName();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.title_img)
    ImageView titleImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.attention)
    TextView attention;
    @BindView(R.id.label_list)
    RecyclerView labelList;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.laud)
    TextView laud;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.laud_ll)
    LinearLayout laudLl;
    @BindView(R.id.hot_recommend)
    RecyclerView hotRecommend;
    @BindView(R.id.comment_num)
    TextView commentNum;
    @BindView(R.id.all_commend)
    RecyclerView allCommend;
    @BindView(R.id.all_commend_title)
    RelativeLayout allCommendTitle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.commend_title)
    TextView commendTitle;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.commend_num)
    ImageView commendNum;
    @BindView(R.id.collection)
    ImageView collection;

    private LabelAdapter labelAdapter;
    private AbraodInfoAdapter abraodInfoAdapter;
    private AbroadCommentAdapter abroadCommentAdapter;
    private List<ArticalDetailBean.CommentBean.DataBeanX> dataBeanXList;
    private List<String> labels;
    private List<AbroadBean> abroadBeanList;
    private String id;
    private int page = 1;

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        AndroidBug5497Workaround.assistActivity(this);
        ButterKnife.bind(this);
        init();
        Intent intent = getIntent();
        if (intent != null) {
            try {
                id = intent.getStringExtra("id");
                addNet(id, true);
            } catch (Exception e) {
                e.printStackTrace();
                toast("传值出错，请返回重进");
            }
        }
    }

    private void init() {
        titleImg.setImageResource(R.mipmap.share);
        titleImg.setVisibility(View.VISIBLE);
        titleTv.setText("文章详情");
        initAdapter();
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        web.setWebViewClient(new MyWebViewClient());
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                refresh.finishRefresh();
                addNet(id, true);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                refresh.finishLoadMore();
                page++;
                addNet(id, false);
            }
        });
        new QBadgeView(this).bindTarget(commendNum).setBadgeNumber(20).setBadgeGravity(Gravity.RIGHT);
    }

    private void initAdapter() {
        labels = new ArrayList<>();
        labelAdapter = new LabelAdapter(this, labels);
        DislocationLayoutManager dislocationLayoutManager = new DislocationLayoutManager();
        labelList.setLayoutManager(dislocationLayoutManager);
        labelList.setAdapter(labelAdapter);
        abroadBeanList = new ArrayList<>();
        abraodInfoAdapter = new AbraodInfoAdapter(this, abroadBeanList);
        hotRecommend.setLayoutManager(new LinearLayoutManager(this));
        hotRecommend.setAdapter(abraodInfoAdapter);
        dataBeanXList = new ArrayList<>();
        abroadCommentAdapter = new AbroadCommentAdapter(this, dataBeanXList);
        allCommend.setLayoutManager(new LinearLayoutManager(this));
        allCommend.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        allCommend.setAdapter(abroadCommentAdapter);
        labelList.setNestedScrollingEnabled(false);
        hotRecommend.setNestedScrollingEnabled(false);
        allCommend.setNestedScrollingEnabled(false);
    }

    private void addNet(String id, final boolean isClearData) {
        addToCompositeDis(HttpUtil.getDetails(id, page + "")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                }).subscribe(new Consumer<ArticalDetailBean>() {
                    @Override
                    public void accept(@NonNull ArticalDetailBean articalDetailBean) throws Exception {
                        dismissLoadDialog();
                        if (articalDetailBean != null) refUi(articalDetailBean, isClearData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        toast(HttpUtils.onError(throwable));
                        if (page > 1) page--;
                    }
                }));
    }


    /**
     * @param articalDetailBean
     * @param isClearData       是否是loadMore   true  否  false  是loadMore
     */
    public void refUi(ArticalDetailBean articalDetailBean, boolean isClearData) {
        if (articalDetailBean.getData() != null) {
            ArticalDetailBean.DataBean dataBean = articalDetailBean.getData();
            title.setText(dataBean.getTitle());
            time.setText(dataBean.getArticle());
            ArticalDetailBean.DataBean.EditUserBean editUserBean = dataBean.getEditUser();
            if (editUserBean != null) {
                new GlideUtils().loadCircle(this, NetworkTitle.DomainSmartApplyNormal + editUserBean.getImage(), head);
                name.setText(editUserBean.getNickname());
                if (editUserBean.getFollow() == C.LGAttention)
                    attention.setVisibility(View.VISIBLE);
                else attention.setVisibility(View.GONE);
            }
            String s = HtmlUtil.repairContent(dataBean.getAlternatives(), NetworkTitle.DomainSmartApplyResourceNormal);
            String html = HtmlUtil.getHtml(s, 0);
            web.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
            labels.clear();
            labels.addAll(StringUtils.ArrayFromString(",", dataBean.getCnName()));
            labelAdapter.notifyDataSetChanged();
            laud.setText(dataBean.getFabulous() + "人点赞");
        }
        if (articalDetailBean.getRecommend() != null && articalDetailBean.getRecommend().size() > 0) {
            abroadBeanList.clear();
            abroadBeanList.addAll(articalDetailBean.getRecommend());
            abraodInfoAdapter.notifyDataSetChanged();
        }


        if (articalDetailBean.getComment() != null) {
            ArticalDetailBean.CommentBean commentBean = articalDetailBean.getComment();
            if (commentBean.getCount() > 0) {
                allCommendTitle.setVisibility(View.VISIBLE);
                commentNum.setText("（" + commentBean.getCount() + "）");
                if (!isClearData) {
                    dataBeanXList.clear();
                }
                if (commentBean.getData() != null && commentBean.getData().size() > 0) {
                    dataBeanXList.addAll(commentBean.getData());
                    abroadCommentAdapter.notifyDataSetChanged();
                }
            } else {
                allCommendTitle.setVisibility(View.GONE);
            }
        }

    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        web.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }


    @OnClick({R.id.back, R.id.laud_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finishWithAnim();
                break;
            case R.id.laud_ll:
                pointLaud();
                break;
        }
    }

    //  点赞
    public void pointLaud() {
        addToCompositeDis(HttpUtil.fabulousContent(id)
                .subscribe(new Consumer<LaudBean>() {
                    @Override
                    public void accept(@NonNull LaudBean laudBean) throws Exception {
                        if (laudBean != null) referLaud(laudBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        toast(HttpUtils.onError(throwable) + "  throwable");
                    }
                }));
    }

    public void referLaud(LaudBean laudBean) {
        laud.setText(laudBean.getNum() + "人点赞");
    }
}
