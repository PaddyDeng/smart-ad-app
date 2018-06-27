package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class NewsDatas {
    private List<TitleTag> catChild;
    private List<NewsData> data;

    public List<TitleTag> getCatChild() {
        return catChild;
    }

    public void setCatChild(List<TitleTag> catChild) {
        this.catChild = catChild;
    }

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
}
