package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

public class RankingSubItemBean {
    private List<Year> years;
    private RankingSubItemDatas data;

    public List<Year> getYears() {
        return years;
    }

    public void setYears(List<Year> years) {
        this.years = years;
    }

    public RankingSubItemDatas getData() {
        return data;
    }

    public void setData(RankingSubItemDatas data) {
        this.data = data;
    }
}
