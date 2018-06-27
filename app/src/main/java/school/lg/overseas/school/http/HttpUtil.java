package school.lg.overseas.school.http;

import io.reactivex.Observable;
import school.lg.overseas.school.ui.dicovery.AbroadHomeBean;
import school.lg.overseas.school.ui.dicovery.bean.ArticalDetailBean;
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

}
