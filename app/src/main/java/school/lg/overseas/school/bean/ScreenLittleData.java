package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 查院校筛选
 */

public class ScreenLittleData {
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
    private String sort;
    private String majorCount;
    private List<ScreenLittleData> child;
    private List<SearchSchoolLitle> content;

    public String getMajorCount() {
        return majorCount;
    }

    public void setMajorCount(String majorCount) {
        this.majorCount = majorCount;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<ScreenLittleData> getChild() {
        return child;
    }

    public void setChild(List<ScreenLittleData> child) {
        this.child = child;
    }

    public List<SearchSchoolLitle> getContent() {
        return content;
    }

    public void setContent(List<SearchSchoolLitle> content) {
        this.content = content;
    }
}
