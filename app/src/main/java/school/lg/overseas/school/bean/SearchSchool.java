package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 院校
 */

public class SearchSchool {
    private String id;
    private String pid;
    private String catId;
    private String name;
    private String title;
    private String image;
    private String createTime;
    private String sort;
    private String userId;
    private String viewCount;
    private String rank;
    private String place;
    private String listeningFile;
    private String seat;
    private List<SearchSchoolLitle> major;
    private int country;

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getListeningFile() {
        return listeningFile;
    }

    public void setListeningFile(String listeningFile) {
        this.listeningFile = listeningFile;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public List<SearchSchoolLitle> getMajor() {
        return major;
    }

    public void setMajor(List<SearchSchoolLitle> major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "SearchSchool{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", catId='" + catId + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sort='" + sort + '\'' +
                ", userId='" + userId + '\'' +
                ", viewCount='" + viewCount + '\'' +
                ", rank='" + rank + '\'' +
                ", place='" + place + '\'' +
                ", listeningFile='" + listeningFile + '\'' +
                ", seat='" + seat + '\'' +
                ", major=" + major +
                ", country=" + country +
                '}';
    }
}
