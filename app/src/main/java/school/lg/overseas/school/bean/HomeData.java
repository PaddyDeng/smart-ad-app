package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class HomeData {
    private int count;
    private int total;
    private List<LittleData> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<LittleData> getData() {
        return data;
    }

    public void setData(List<LittleData> data) {
        this.data = data;
    }
}
