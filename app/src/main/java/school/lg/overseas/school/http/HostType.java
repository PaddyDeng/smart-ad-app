package school.lg.overseas.school.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class HostType {
    @IntDef({SMARTAPPLY_URL_HOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {
    }
    @HostType.HostTypeChecker
    public static final int SMARTAPPLY_URL_HOST = 4;

}
