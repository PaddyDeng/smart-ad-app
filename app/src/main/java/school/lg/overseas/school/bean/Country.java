package school.lg.overseas.school.bean;

/**
 * Created by Administrator on 2017/12/29.
 */

public class Country {
    private String id;
    private String pid;
    private String name;
    private String image;
    private String createTime;
    private String description;
    private String userId;
    private String secondClass;
    private String isShow;
    private String isMajor;
    private String can;
    private String sort;
    private String Relatedcatid;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecondClass() {
        return secondClass;
    }

    public void setSecondClass(String secondClass) {
        this.secondClass = secondClass;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsMajor() {
        return isMajor;
    }

    public void setIsMajor(String isMajor) {
        this.isMajor = isMajor;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRelatedcatid() {
        return Relatedcatid;
    }

    public void setRelatedcatid(String relatedcatid) {
        Relatedcatid = relatedcatid;
    }
}
