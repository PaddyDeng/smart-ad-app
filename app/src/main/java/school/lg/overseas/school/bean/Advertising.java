package school.lg.overseas.school.bean;

import android.bluetooth.le.AdvertiseData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class Advertising implements Serializable {
    private int code ;
    private String message ;
    private AdverData data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AdverData getData() {
        return data;
    }

    public void setData(AdverData data) {
        this.data = data;
    }


}
