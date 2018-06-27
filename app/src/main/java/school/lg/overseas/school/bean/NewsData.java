package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class NewsData {
    private String id;
    private String title;
    private List<String> imageContent;
    private String dateTime;
    private String hot;
    private String viewCount;
    private String username;
    private String nickname;
    private String replyNum;
    private String cnContent;


    //    private String uid;
    //    private String createTime;
    //    private String catId;
    //    private String sort;
    //    private String lastReplayTime;
    //    private String image;
    //    private String content;
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


    public String getCnContent() {
        return cnContent;
    }

    public void setCnContent(String cnContent) {
        this.cnContent = cnContent;
    }

    public List<String> getImageContent() {
        return imageContent;
    }

    public void setImageContent(List<String> imageContent) {
        this.imageContent = imageContent;
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


    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
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


    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }
}
