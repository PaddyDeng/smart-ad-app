package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class HomeBanner {
    private List<LittleData> sy_banner;
    private HomeData data;

    public List<LittleData> getSyBanner() {
        return sy_banner;
    }

    public void setSyBanner(List<LittleData> syBanner) {
        this.sy_banner = syBanner;
    }

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }
}
