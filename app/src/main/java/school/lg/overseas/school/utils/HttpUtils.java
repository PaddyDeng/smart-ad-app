package school.lg.overseas.school.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import school.lg.overseas.school.MyApplication;

/**
 * Created by Administrator on 2018/3/26.
 */

public class HttpUtils {

    public static boolean getHttpMsgSu(int code) {
        if (code == 1) {
            return true;
        }
        return false;
    }

    public static String onError(Throwable throwable) {
        String errorMsg = "";
        if (throwable instanceof HttpException) {
            switch (((HttpException) throwable).code()) {
                case 403:
                    errorMsg = "没有权限访问此链接！";
                    break;
                case 504:
                    if (isConnected(MyApplication.getInstance())) {
                        errorMsg = "网络连接超时！";
                    } else {
                        errorMsg = "没有联网哦！";
                    }
                    break;
                default:
                    errorMsg = ((HttpException) throwable).message();
                    break;
            }
        } else if (throwable instanceof UnknownHostException) {
            if (isConnected(MyApplication.getInstance())) {
                errorMsg = "网络连接超时！";
            } else {
                errorMsg = "没有联网哦！";
            }
        } else if (throwable instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时！";
        }
        return errorMsg;
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
