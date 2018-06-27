package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 院校详情
 */

public class SchoolDetails {
    private int code;
    private int country;
    private List<ScreenLittleData> major;
    private LittleData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public List<ScreenLittleData> getMajor() {
        return major;
    }

    public void setMajor(List<ScreenLittleData> major) {
        this.major = major;
    }

    public LittleData getData() {
        return data;
    }

    public void setData(LittleData data) {
        this.data = data;
    }

    //    public List<LittleData> getData() {
//        return data;
//    }
//
//    public void setData(List<LittleData> data) {
//        this.data = data;
//    }
}
