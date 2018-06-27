package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class HomeContent {
    private List<LittleData> schools;
    private List<LittleData> activity;
    private List<LittleData> document;
    private List<LittleData> specialColumn;
    private List<LittleData> open;
    private ShopContent shopContent;

    public ShopContent getShopContent() {
        return shopContent;
    }

    public void setShopContent(ShopContent shopContent) {
        this.shopContent = shopContent;
    }

    public List<LittleData> getOpen() {
        return open;
    }

    public void setOpen(List<LittleData> open) {
        this.open = open;
    }

    public List<LittleData> getSchools() {
        return schools;
    }

    public void setSchools(List<LittleData> schools) {
        this.schools = schools;
    }

    public List<LittleData> getActivity() {
        return activity;
    }

    public void setActivity(List<LittleData> activity) {
        this.activity = activity;
    }

    public List<LittleData> getDocument() {
        return document;
    }

    public void setDocument(List<LittleData> document) {
        this.document = document;
    }

    public List<LittleData> getSpecialColumn() {
        return specialColumn;
    }

    public void setSpecialColumn(List<LittleData> specialColumn) {
        this.specialColumn = specialColumn;
    }
}
