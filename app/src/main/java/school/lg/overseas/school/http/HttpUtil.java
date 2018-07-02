package school.lg.overseas.school.http;

import java.util.Map;

import io.reactivex.Observable;
import school.lg.overseas.school.bean.ResultBean;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.ui.dicovery.bean.CommendResultBean;
import school.lg.overseas.school.ui.dicovery.bean.LaudBean;

import static school.lg.overseas.school.http.HostType.SMARTAPPLY_URL_HOST;

/**
 * Created by Administrator on 2018/6/20.
 */

public class HttpUtil {
    private HttpUtil() {
    }

    private static RestApi getRestApi(@HostType.HostTypeChecker int hostType) {
        return RetrofitProvider.getInstance(hostType).create(RestApi.class);
    }


    public static Observable<AbroadHomeBean> getStudyAbroad(String category ,String page){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).getStudyAbroad(category , page).compose(new SchedulerTransformer<AbroadHomeBean>());
    }

    public static Observable<ArticalDetailBean> getDetails(String id , String page){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).getDetails(id , page).compose(new SchedulerTransformer<ArticalDetailBean>());
    }


    public static Observable<LaudBean> fabulousContent(String id){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).fabulousContent(id ).compose(new SchedulerTransformer<LaudBean>());
    }

    public static Observable<CommendResultBean> commendResult(String contentId ,String content){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).userComment(contentId, content ).compose(new SchedulerTransformer<CommendResultBean>());
    }

    public static Observable<ResultBean> commentFabulous(String commentId){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).commentFabulous(commentId).compose(new SchedulerTransformer<ResultBean>());
    }

    public static Observable<ArticalDetailBean.CommentBean> loadComment(String contentId ,String page){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).loadComment(contentId , page).compose(new SchedulerTransformer<ArticalDetailBean.CommentBean>());
    }

    public static Observable<ResultBean> userReply(Map<String , String> fields){
        return HttpUtil.getRestApi(SMARTAPPLY_URL_HOST).userReply(fields).compose(new SchedulerTransformer<ResultBean>());
    }
}
