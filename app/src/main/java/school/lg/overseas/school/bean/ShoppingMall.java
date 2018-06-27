package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 商城
 */

public class ShoppingMall {
    private List<Country> serviceTypes;
    private List<Country> countrys;
    private GoodsData data;

    public List<Country> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<Country> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<Country> getCountrys() {
        return countrys;
    }

    public void setCountrys(List<Country> countrys) {
        this.countrys = countrys;
    }

    public GoodsData getData() {
        return data;
    }

    public void setData(GoodsData data) {
        this.data = data;
    }
}
