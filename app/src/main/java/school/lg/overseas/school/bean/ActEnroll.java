package school.lg.overseas.school.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ActEnroll {
    private String catId;
    private String name;
    private List<String> extend;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExtend() {
        return extend;
    }

    public void setExtend(List<String> extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        StringBuffer sb =new StringBuffer();
        sb.append("[");
        for (int i = 0; i < extend.size(); i++) {
            if(i!=0)sb.append(",");
            sb.append(extend.get(i));
        }
        sb.append("]");
        return "catId='" + catId + '\'' +
                "&name='" + name + '\'' +
                "&extend=" + sb.toString();
    }
}
