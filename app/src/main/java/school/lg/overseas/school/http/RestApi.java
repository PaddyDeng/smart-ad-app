package school.lg.overseas.school.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import school.lg.overseas.school.bean.ResultBean;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
import school.lg.overseas.school.ui.dicovery.bean.CommendResultBean;
import school.lg.overseas.school.ui.dicovery.bean.LaudBean;

/**
 * Created by Administrator on 2018/6/20.
 */

public interface RestApi {



    @GET(NetworkChildren.STUDY_ABROAD)
    Observable<AbroadHomeBean> getStudyAbroad(@Query("category") String category , @Query("page") String page);


    @GET(NetworkChildren.GET_DETAILS)
    Observable<ArticalDetailBean> getDetails(@Query("id") String id  ,@Query("page") String page);


    @FormUrlEncoded
    @POST(NetworkChildren.FABULOUS_CONTENT)
    Observable<LaudBean> fabulousContent(@Field("contentId") String contentId);

    @FormUrlEncoded
    @POST(NetworkChildren.USER_COMMENT)
    Observable<CommendResultBean> userComment(@Field("contentId") String contentId , @Field("content") String content);


    @GET(NetworkChildren.COMMENT_FABUOUS)
    Observable<ResultBean> commentFabulous(@Query("commentId") String commentId) ;

    @FormUrlEncoded
    @POST(NetworkChildren.LOAD_COMMENT)
    Observable<ArticalDetailBean.CommentBean> loadComment(@Field("contentId") String contentId ,@Field("page") String page);

    @FormUrlEncoded
    @POST(NetworkChildren.USER_REPLY)
    Observable<ResultBean> userReply(@FieldMap Map<String ,String> fields);
}
