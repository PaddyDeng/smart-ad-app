package school.lg.overseas.school.ui.dicovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
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
import school.lg.overseas.school.bean.ReplyBean;
import school.lg.overseas.school.bean.ResultBean;
import school.lg.overseas.school.callback.HolderSelectListener;
import school.lg.overseas.school.callback.ItemSelectListener;
import school.lg.overseas.school.http.HttpUtil;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.dicovery.adapter.AllReplyAdapter;
import school.lg.overseas.school.ui.dicovery.bean.AbroadReplyBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.ui.dicovery.bean.CommendResultBean;
import school.lg.overseas.school.utils.GlideUtils;
import school.lg.overseas.school.utils.HttpUtils;
import school.lg.overseas.school.utils.LoginHelper;
import school.lg.overseas.school.utils.PopHelper;

/**
 * 更多回复Activity
 */
public class ReplyActivity extends BaseActivity {
    private static final String TAG = ReplyActivity.class.getSimpleName();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.num_img)
    ImageView numImg;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.reply)
    RecyclerView reply;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.border)
    View border;
    @BindView(R.id.line_send_rl)
    View lineSendRl;
    @BindView(R.id.reply_content)
    TextView replyContent;
    @BindView(R.id.commend_rl)
    RelativeLayout commendRl;
    @BindView(R.id.root)
    LinearLayout root;

    private ArticalDetailBean.CommentBean.DataBeanX dataBeanX;
    private List<AbroadReplyBean> replyBeanList = new ArrayList<>();
    private AllReplyAdapter allReplyAdapter;
    private PopHelper popHelper;
    private Map<String, String> fields;

    public static void start(Context c, ArticalDetailBean.CommentBean.DataBeanX dataBeanX) {
        Intent intent = new Intent(c, ReplyActivity.class);
        intent.putExtra("data", dataBeanX);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        initAdapter();
        try {
            Intent intent = getIntent();
            if (intent != null) {
                dataBeanX = (ArticalDetailBean.CommentBean.DataBeanX) intent.getSerializableExtra("data");
                init();
            }
        } catch (Exception e) {
            toast("数据异常");
            Log.e(TAG, "onCreate>>>>>>>>>>>>>> " + e.getMessage());
        }
    }

    private void initAdapter() {
        allReplyAdapter = new AllReplyAdapter(this, replyBeanList);
        allReplyAdapter.setSelectListener(new ItemSelectListener() {
            @Override
            public void itemSelectListener(RecyclerView.ViewHolder holder, int poisition) {
                AbroadReplyBean abroadReplyBean = replyBeanList.get(poisition);
                String id = abroadReplyBean.getId();
                commentFabulous(holder, id);
            }

            @Override
            public void select(int poistion) {
                if (popHelper != null)
                    popHelper.setId(dataBeanX.getId(), false);
                popHelper.show(root);
                AbroadReplyBean replyBean = replyBeanList.get(poistion);
                if (dataBeanX != null) {
                    fields.put("contentId", replyBean.getContentId());
                    fields.put("replyName", replyBean.getNickname());
                    fields.put("replyUid", replyBean.getUid());
                    fields.put("commentId", replyBean.getId());
                }
            }
        });
        reply.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        reply.setLayoutManager(new LinearLayoutManager(this));
        reply.setAdapter(allReplyAdapter);
    }

    private void init() {
        fields = new ArrayMap<>();
        if (dataBeanX != null) {
            referUi();
            initPop();
        }
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
                                LoginHelper.needLogin(ReplyActivity.this, "您还未登录，请先登录");
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
                                        LoginHelper.needLogin(ReplyActivity.this, "您还未登录，请先登录");
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

    /**
     * 刷新数据
     */
    private void referUi() {
        if (dataBeanX.getReply() != null) {
            replyBeanList.addAll(dataBeanX.getReply());
            titleTv.setText(replyBeanList.size() + "条回复");
            name.setText(dataBeanX.getNickname());
            time.setText(dataBeanX.getCreateTime());
            content.setText(dataBeanX.getContent());
            num.setText(dataBeanX.getFane());
            new GlideUtils().loadCircle(this, NetworkTitle.DomainSmartApplyResourceNormal + dataBeanX.getImage(), head);
            allReplyAdapter.notifyDataSetChanged();
            fields.put("contentId", dataBeanX.getContentId());
            fields.put("replyName", dataBeanX.getNickname());
            fields.put("replyUid", dataBeanX.getUid());
            fields.put("commentId", dataBeanX.getId());
        }
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
                            AllReplyAdapter.AllReplyHolder holder = (AllReplyAdapter.AllReplyHolder) viewHolder;
                            try {
                                int fabus = Integer.parseInt(holder.num.getText().toString().trim()) + 1;
                                holder.num.setText(fabus + "");
                            } catch (Exception e) {
//                                addNet(id);
                                toast("结果出错");
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


    @OnClick({R.id.back, R.id.num_img, R.id.reply_content})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finishWithAnim();
                break;
            case R.id.num_img:
                commentFabulous(dataBeanX.getId());
                break;
            case R.id.reply_content:
                popHelper.show(root);
                break;

        }
    }


    /**
     * 评论点赞
     *
     * @param commentId
     */
    private void commentFabulous(String commentId) {
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
                            try {
                                int fabus = Integer.parseInt(num.getText().toString().trim()) + 1;
                                num.setText(fabus + "");
                            } catch (Exception e) {
//                                addNet(id);
                                toast("结果出错");
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
}
