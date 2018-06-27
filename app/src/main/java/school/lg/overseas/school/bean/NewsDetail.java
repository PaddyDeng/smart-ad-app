package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class NewsDetail {
    private String id;
    private String title;
    private String content;
    private String cnContent;
    private String imageContent;
    private String uid;
    private String createTime;
    private String dateTime;
    private String hot;
    private String catId;
    private String viewCount;
    private String radioTitle;
    private String sort;
    private String lastReplayTime;
    private String username;
    private String nickname;
    private String image;
    private int isReply;
    private List<ReplyBean> Reply;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCnContent() {
        return cnContent;
    }

    public void setCnContent(String cnContent) {
        this.cnContent = cnContent;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getRadioTitle() {
        return radioTitle;
    }

    public void setRadioTitle(String radioTitle) {
        this.radioTitle = radioTitle;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLastReplayTime() {
        return lastReplayTime;
    }

    public void setLastReplayTime(String lastReplayTime) {
        this.lastReplayTime = lastReplayTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public List<ReplyBean> getReply() {
        return Reply;
    }

    public void setReply(List<ReplyBean> reply) {
        Reply = reply;
    }
}
