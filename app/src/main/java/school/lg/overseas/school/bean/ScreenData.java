package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class ScreenData {
    List<ScreenLittleData> country;
    List<ScreenLittleData> rank;
    List<ScreenLittleData> major;

    public List<ScreenLittleData> getCountry() {
        return country;
    }

    public void setCountry(List<ScreenLittleData> country) {
        this.country = country;
    }

    public List<ScreenLittleData> getRank() {
        return rank;
    }

    public void setRank(List<ScreenLittleData> rank) {
        this.rank = rank;
    }

    public List<ScreenLittleData> getMajor() {
        return major;
    }

    public void setMajor(List<ScreenLittleData> major) {
        this.major = major;
    }
}
