package school.lg.overseas.school.http;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import school.lg.overseas.school.BuildConfig;
import school.lg.overseas.school.MyApplication;

//链接
public class RetrofitProvider {
    public static final String DomainSmartApplyNormal="http://www.smartapply.cn/cn/";
    public static final String DomainSmartApplyResourceNormal="http://www.smartapply.cn/";
    private static SparseArray<Retrofit> sparseArray = new SparseArray<>();

    private static SparseArray<Retrofit> sparseArrayWord = new SparseArray<>();
    private Context context ;
    private RetrofitProvider() {
    }

    public static Retrofit getInstance(@HostType.HostTypeChecker int hostType ) {
        Retrofit instance = sparseArray.get(hostType);
        if (instance == null) {
            synchronized (RetrofitProvider.class) {
                if (instance == null) {
                    instance = SingletonHolder.create(hostType);
                    sparseArray.put(hostType, instance);
                }
            }
        }
        return instance;
    }


    private static class SingletonHolder {

        private static Retrofit create(@HostType.HostTypeChecker int type) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            String url = null;
            switch (type) {
                case HostType.SMARTAPPLY_URL_HOST:
                    url = DomainSmartApplyNormal;
                    break;
            }
            builder.networkInterceptors().add(new CookiesInterceptor(MyApplication.getInstance()));

            return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }
    }



}
