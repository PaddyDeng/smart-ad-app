package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 院校列表
 */

public class SearchSchoolList {
    private int totalPage;
    private int count;
    private List<SearchSchool> data;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SearchSchool> getData() {
        return data;
    }

    public void setData(List<SearchSchool> data) {
        this.data = data;
    }
}
