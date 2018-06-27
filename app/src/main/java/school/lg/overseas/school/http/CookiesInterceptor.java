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
    private boolean isBaidu ;
    public CookiesInterceptor(Context mContext) {
        this.mContext = mContext;
        isBaidu = false ;
    }

    public CookiesInterceptor(Context mContext , boolean isBaidu) {
        this.mContext = mContext;
        this.isBaidu = true ;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request compressedRequest;
        String cookie ;
        cookie = SharedPreferencesUtils.getSession(mContext ,1);
        compressedRequest = request.newBuilder()
                .header("cookie", cookie)
                .build();
        Log.e("cookie", "intercept: " + cookie );
        Response originalResponse = chain.proceed(compressedRequest);
        return originalResponse;
    }

}
