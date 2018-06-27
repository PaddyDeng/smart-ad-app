package school.lg.overseas.school.http;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
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
}
