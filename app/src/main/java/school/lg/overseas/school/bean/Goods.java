package school.lg.overseas.school.bean;

/**
 * Created by Administrator on 2017/12/20.
 */

public class Goods {
    private String id;
    private String pid;
    private GoodsDetail data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public GoodsDetail getData() {
        return data;
    }

    public void setData(GoodsDetail data) {
        this.data = data;
    }
}
