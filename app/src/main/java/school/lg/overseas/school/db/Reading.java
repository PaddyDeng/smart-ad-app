package school.lg.overseas.school.db;

/**
 * Created by Administrator on 2018/1/16.
 */

public class Reading {
    private String id;
    private String title;//标题
    private String time;
    private int type;//0 学校 1 专业 2问答 3活动 4 知识库
    private int tag;//1收藏 0查看
    private String s;//官网
    private String schoolName;//学校名
    private String enMajprName;//专业英文名
    private String country;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Reading() {
    }

    public Reading(int tag,int type,String id, String title) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.tag = tag;
    }
    public String getEnMajprName() {
        return enMajprName;
    }

    public void setEnMajprName(String enMajprName) {
        this.enMajprName = enMajprName;
    }
    public String getName() {
        return schoolName;
    }

    public void setName(String name) {
        this.schoolName = name;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
