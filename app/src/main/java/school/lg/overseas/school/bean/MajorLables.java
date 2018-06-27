package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 专业解析列表标题
 */

public class MajorLables {
    private List<MajorLable> category;
    private MajorDatas data;

    public MajorDatas getData() {
        return data;
    }

    public void setData(MajorDatas data) {
        this.data = data;
    }

    public List<MajorLable> getCategory() {
        return category;
    }

    public void setCategory(List<MajorLable> category) {
        this.category = category;
    }
}
