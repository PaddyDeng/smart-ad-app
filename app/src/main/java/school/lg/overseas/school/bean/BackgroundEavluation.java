package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BackgroundEavluation {
    private String time;//计划出国时间
    private String country;//意向国家
    private String major;//意向专业
    private String gmat;//gmat分数
    private String toefl;//托福分数
    private String ielts;//雅思分数
    private String work;//实习经历
    private List<String> concerns;//关心的问题
    private String understand;//补充
    private String name;//姓名
    private String phone;//电话号码
    private String num;//QQ/微信

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


    public String getGmat() {
        return gmat;
    }

    public void setGmat(String gmat) {
        this.gmat = gmat;
    }

    public String getToefl() {
        return toefl;
    }

    public void setToefl(String toefl) {
        this.toefl = toefl;
    }

    public String getIelts() {
        return ielts;
    }

    public void setIelts(String ielts) {
        this.ielts = ielts;
    }


    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public List<String> getConcerns() {
        return concerns;
    }

    public void setConcerns(List<String> concerns) {
        this.concerns = concerns;
    }

    public String getUnderstand() {
        return understand;
    }

    public void setUnderstand(String understand) {
        this.understand = understand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "BackgroundEavluation{" +
                "time='" + time + '\'' +
                ", country='" + country + '\'' +
                ", major='" + major + '\'' +
                ", gmat='" + gmat + '\'' +
                ", toefl='" + toefl + '\'' +
                ", ielts='" + ielts + '\'' +
                ", work='" + work + '\'' +
                ", concerns=" + concerns +
                ", understand='" + understand + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
