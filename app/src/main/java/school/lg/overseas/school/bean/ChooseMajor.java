package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class ChooseMajor {
    private List<ScreenLittleData> major;
    private List<Country> country;

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public List<ScreenLittleData> getMajor() {
        return major;
    }

    public void setMajor(List<ScreenLittleData> major) {
        this.major = major;
    }
}
