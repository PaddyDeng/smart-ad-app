package school.lg.overseas.school.bean;

import java.util.List;

public class RemarkBean {
    private List<RemarkData> data;
    private String count;

    public List<RemarkData> getData() {
        return data;
    }

    public void setData(List<RemarkData> data) {
        this.data = data;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
