package school.lg.overseas.school.http;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import school.lg.overseas.school.utils.SharedPreferencesUtils;


public class CookiesInterceptor implements Interceptor {

    private Context mContext;
    public CookiesInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request compressedRequest;
        String cookie ;
        cookie = SharedPreferencesUtils.getSession(mContext ,1);
        compressedRequest = request.newBuilder()
                .header("cookie", "PHPSESSID=" + cookie)
                .build();
        Response originalResponse = chain.proceed(compressedRequest);
        return originalResponse;
    }

}
