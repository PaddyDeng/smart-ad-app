package school.lg.overseas.school.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/14.
 */

public class TitleTag implements Serializable {

    private String id;
    private String name;
    private String title;
    private boolean isClose;
//    private String type;
//    private String createTime;
//    private String userId;


    public TitleTag() {
    }

    public TitleTag(String id, String name, String title, boolean isClose) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.isClose = isClose;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
}
