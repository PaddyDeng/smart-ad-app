package school.lg.overseas.school.bean;

/**
 * Created by Administrator on 2018/1/17.
 */

public class Fans {
    private String id;
    private String uid;
    private String followUser;
    private String createTime;
    private String username;
    private String image;
    private String nickname;
    private boolean isBoolean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFollowUser() {
        return followUser;
    }

    public void setFollowUser(String followUser) {
        this.followUser = followUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }
}
