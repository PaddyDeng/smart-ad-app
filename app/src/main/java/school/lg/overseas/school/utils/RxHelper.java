package school.lg.overseas.school.utils;


import io.reactivex.annotations.NonNull;
import android.util.Log;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import school.lg.overseas.school.http.SchedulerTransformer;

/**
 * Created by fire on 2017/9/7  17:08.
 */

public class RxHelper {

    private static void log(String msg) {
        Log.e(RxHelper.class.getSimpleName(), msg);
    }

    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (throwable instanceof InterruptedException) {
                    log("Thread interrupted");
                } else if (throwable instanceof InterruptedIOException) {
                    log("Io interrupted");
                } else if (throwable instanceof SocketException) {
                    log("Socket error");
                } else {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public static Observable<Integer> countDown(final int time) {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .take(time + 1)
                .flatMap(new Function<Long, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Long aLong) throws Exception {
                        return Observable.just(time - aLong.intValue());
                    }
                })
                .compose(new SchedulerTransformer<Integer>());
    }

    public static Observable<Integer> delay(final int time) {
        return Observable
                .just(time)
                .delay(time, TimeUnit.SECONDS)
                .compose(new SchedulerTransformer<Integer>());
    }
}
