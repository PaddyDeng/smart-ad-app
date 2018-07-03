package school.lg.overseas.school.ui.dicovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.ResultBean;
import school.lg.overseas.school.callback.ItemSelectListener;
import school.lg.overseas.school.callback.SelectListener;
import school.lg.overseas.school.http.HttpUtil;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.adapter.AbraodInfoAdapter;
import school.lg.overseas.school.ui.dicovery.adapter.AbroadCommentAdapter;
import school.lg.overseas.school.ui.dicovery.adapter.LabelAdapter;
import school.lg.overseas.school.ui.dicovery.bean.AbroadBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.ui.dicovery.bean.CommendResultBean;
import school.lg.overseas.school.ui.dicovery.bean.LaudBean;
import school.lg.overseas.school.utils.C;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HtmlUtil;
import school.lg.overseas.school.utils.HttpUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.PopHelper;
import school.lg.overseas.school.utils.ShareUtils01;
import school.lg.overseas.school.utils.StringUtils;
import school.lg.overseas.school.view.AndroidBug5497Workaround;
import school.lg.overseas.school.view.DislocationLayoutManager;

/**
 * 全部评论下的recyclerView 在加载更多中会不流畅的现象，需要下一任解决一下
 */
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
    TextView content;
    @BindView(R.id.commend_num)
    TextView commendNum;
    @BindView(R.id.collection)
    ImageView collection;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.commend)
    RelativeLayout commend;
    @BindView(R.id.commend_rl)
    RelativeLayout commendRl;
    @BindView(R.id.root)
    RelativeLayout root;

    private LabelAdapter labelAdapter;
    private AbraodInfoAdapter abraodInfoAdapter;
    private AbroadCommentAdapter abroadCommentAdapter;
    private List<ArticalDetailBean.CommentBean.DataBeanX> dataBeanXList;
    private List<String> labels;
    private List<AbroadBean> abroadBeanList;
    private String id;
    private int page = 1;
    private PopHelper popHelper;
    private Map<String, String> fields;
    private ArticalDetailBean articalDetailBean ;
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
                addNet(id);
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
        refresh.setEnableAutoLoadMore(true);
        refresh.setEnableOverScrollBounce(true);//是否启用越界回弹
        refresh.setEnableScrollContentWhenLoaded(true);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                page = 1 ;
                addNet(id);
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                loadComment(id);
            }
        });
        initPop();
        fields = new HashMap<>();
    }
    
    /**
     * 评论pop
     */
    private void initPop() {
        popHelper = PopHelper.create(this);
        popHelper.setPopListener(new PopHelper.PopListener() {
            @Override
            public boolean popListener(String content, String id, boolean isArtic) {
                final boolean[] isResult = {false};
                if (isArtic) {
                    addToCompositeDis(HttpUtil.commendResult(id
                            , content).doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            showLoadDialog();
                        }
                    }).subscribe(new Consumer<CommendResultBean>() {
                        @Override
                        public void accept(@NonNull CommendResultBean commendResultBean) throws Exception {
                            dismissLoadDialog();
                            if (commendResultBean.getCode() == 1) {
                                isResult[0] = true;
                                toast(commendResultBean.getMessage());
                                popHelper.hide();
//                            addNet(id);
                            } else if (commendResultBean.getCode() == -1) {
                                LoginHelper.needLogin(ArticleDetailsActivity.this, "您还未登录，请先登录");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            dismissLoadDialog();
                            toast(HttpUtils.onError(throwable));

                        }
                    }));
                } else {
                    fields.put("content", content);
                    addToCompositeDis(HttpUtil.userReply(fields)
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(@NonNull Disposable disposable) throws Exception {
                                    showLoadDialog();
                                }
                            }).subscribe(new Consumer<ResultBean>() {
                                @Override
                                public void accept(@NonNull ResultBean resultBean) throws Exception {
                                    dismissLoadDialog();
                                    if (resultBean.getCode() == 1) {
                                        isResult[0] = true;
                                        toast(resultBean.getMessage());
                                        popHelper.hide();
                                    } else if (resultBean.getCode() == -1) {
                                        LoginHelper.needLogin(ArticleDetailsActivity.this, "您还未登录，请先登录");
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    dismissLoadDialog();
                                    HttpUtils.onError(throwable);
                                }
                            }));
                }
                return isResult[0];
            }
        });
    }

    private void initAdapter() {
        labels = new ArrayList<>();
        labelAdapter = new LabelAdapter(this, labels);
        DislocationLayoutManager dislocationLayoutManager = new DislocationLayoutManager();
        labelList.setLayoutManager(dislocationLayoutManager);
        labelList.setAdapter(labelAdapter);
        abroadBeanList = new ArrayList<>();
        abraodInfoAdapter = new AbraodInfoAdapter(this, abroadBeanList);
        abraodInfoAdapter.setSelectListener(new SelectListener() {
            @Override
            public void select(int position) {
                AbroadBean abroadBean =  abroadBeanList.get(position);
                ArticleDetailsActivity.start(ArticleDetailsActivity.this ,abroadBean.getId());
            }
        });
        hotRecommend.setLayoutManager(new LinearLayoutManager(this));
        hotRecommend.setAdapter(abraodInfoAdapter);
        dataBeanXList = new ArrayList<>();
        abroadCommentAdapter = new AbroadCommentAdapter(this, dataBeanXList);
        abroadCommentAdapter.setSelectListener(new ItemSelectListener() {
            @Override
            public void itemSelectListener(RecyclerView.ViewHolder holder, int poisition) {
                ArticalDetailBean.CommentBean.DataBeanX dataBeanX = dataBeanXList.get(poisition);
                String commentId = dataBeanX.getId();
                commentFabulous(holder, commentId);
            }

            @Override
            public void select(int poistion) {
                if (popHelper != null)
                    popHelper.setId(id, false);
                popHelper.show(root);
                ArticalDetailBean.CommentBean.DataBeanX dataBeanX = dataBeanXList.get(poistion);
                if (dataBeanX != null) {
                    fields.put("contentId", dataBeanX.getContentId());
                    fields.put("replyName", dataBeanX.getNickname());
                    fields.put("replyUid", dataBeanX.getUid());
                    fields.put("commentId", dataBeanX.getId());
                }
            }
        });
        allCommend.setLayoutManager(new LinearLayoutManager(this));
        allCommend.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        allCommend.setAdapter(abroadCommentAdapter);
        labelList.setNestedScrollingEnabled(false);
        hotRecommend.setNestedScrollingEnabled(false);
        allCommend.setNestedScrollingEnabled(false);

    }

    /**
     * 评论点赞
     * @param viewHolder
     * @param commentId
     */
    private void commentFabulous(final RecyclerView.ViewHolder viewHolder, String commentId) {
        addToCompositeDis(HttpUtil.commentFabulous(commentId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                }).subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean resultBean) throws Exception {
                        dismissLoadDialog();
                        if (resultBean.getCode() == 1) {
                            AbroadCommentAdapter.AbroadCommentHolder holder = (AbroadCommentAdapter.AbroadCommentHolder) viewHolder;
                            try {
                                int fabus = Integer.parseInt(holder.num.getText().toString().trim()) + 1;
                                holder.num.setText(fabus + "");
                            } catch (Exception e) {
                                addNet(id);
                            }
                        }
                        toast(resultBean.getMessage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        toast(HttpUtils.onError(throwable));
                    }
                }));
    }

    private void addNet(String id) {
        addToCompositeDis(HttpUtil.getDetails(id, page + "")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
                    }
                }).subscribe(new Consumer<ArticalDetailBean>() {
                    @Override
                    public void accept(@NonNull ArticalDetailBean articalDetailBean) throws Exception {
//                        dismissLoadDialog();
                        if (articalDetailBean != null) refUi(articalDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
                        toast(HttpUtils.onError(throwable));
                        if (page > 1) page--;
                    }
                }));
    }


    /**
     * 加载更多评论
     *
     * @param contentId
     */
    private void loadComment(String contentId) {
        addToCompositeDis(HttpUtil.loadComment(contentId, page + "")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                }).subscribe(new Consumer<ArticalDetailBean.CommentBean>() {
                    @Override
                    public void accept(@NonNull ArticalDetailBean.CommentBean commentBeanResultBean) throws Exception {
                        dismissLoadDialog();
                        if (commentBeanResultBean != null) referUiComment(commentBeanResultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        toast(HttpUtils.onError(throwable));
                        page--;
                    }
                }));
    }

    /**
     * 刷新评论
     */
    private void referUiComment(ArticalDetailBean.CommentBean commentBean) {

        if (commentBean.getCount() > 0) {
            allCommendTitle.setVisibility(View.VISIBLE);
            commentNum.setVisibility(View.VISIBLE);
            commentNum.setText("（" + commentBean.getCount() + "）");
            commendNum.setVisibility(View.VISIBLE);
            commendNum.setText(commentBean.getCount() + "");
            if (commentBean.getData() != null && commentBean.getData().size() > 0) {
                dataBeanXList.addAll(commentBean.getData());
                abroadCommentAdapter.notifyDataSetChanged();
            } else {
                refresh.finishLoadMoreWithNoMoreData();
                if (page > 1) page--;
            }
        } else {

        }
    }

    /**
     * @param articalDetailBean
     */
    public void refUi(ArticalDetailBean articalDetailBean) {
        this.articalDetailBean = articalDetailBean ;
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
                commentNum.setVisibility(View.VISIBLE);
                commentNum.setText("（" + commentBean.getCount() + "）");
                commendNum.setVisibility(View.VISIBLE);
                commendNum.setText(commentBean.getCount() + "");
                dataBeanXList.clear();
                if (commentBean.getData() != null && commentBean.getData().size() > 0) {
                    dataBeanXList.addAll(commentBean.getData());
                    abroadCommentAdapter.notifyDataSetChanged();
                }
            } else {
                commentNum.setVisibility(View.GONE);
                allCommendTitle.setVisibility(View.GONE);
                commendNum.setVisibility(View.GONE);
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


    @OnClick({R.id.back, R.id.laud_ll, R.id.commend_rl, R.id.content ,R.id.title_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finishWithAnim();
                break;
            case R.id.laud_ll:
                pointLaud();
                break;
            case R.id.commend_rl:
//                popHelper.show(root);
                break;
            case R.id.content:
                popHelper.setId(id, true);
                popHelper.show(root);
                break;
            case R.id.title_img:
                ShareUtils01.toShare(ArticleDetailsActivity.this,C.ARTICAL_SHARE + "?id="+articalDetailBean.getData().getId() ,articalDetailBean.getData().getTitle());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
