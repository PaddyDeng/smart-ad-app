package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

/**
 *  shopContent  数据类
 */
public class ShopContent {
    private int totalPage ;
    private List<LittleData> data ;
    private int count ;
    private int page ;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<LittleData> getData() {
        return data;
    }

    public void setData(List<LittleData> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
