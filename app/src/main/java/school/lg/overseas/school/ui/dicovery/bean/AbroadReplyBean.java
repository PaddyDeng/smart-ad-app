package school.lg.overseas.school.ui.dicovery.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/21.
 */

public class AbroadReplyBean implements Serializable {

    /**
     * id : 2091
     * contentId : 5381
     * pid : 2086
     * uid : 26967
     * content : 嗯
     * type : 2
     * createTime : 1528350827
     * replyName : 恩蜗居拉黑
     * fane : 0
     * nickname : 怎么改不了
     * image : /files/upload/image5b2223f4ed2f6.jpg
     * viewType  :  1 ;
     */

    private String id;
    private String contentId;
    private String pid;
    private String uid;
    private String content;
    private String type;
    private String createTime;
    private String replyName;
    private String fane;
    private String nickname;
    private String image;
    private int viewType = 1 ;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getFane() {
        return fane;
    }

    public void setFane(String fane) {
        this.fane = fane;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
