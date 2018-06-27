package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */

public class SchoolTestList {
    private int code;
    private List<SchoolTest> schoolTest;
    private List<Enroll> probabilityTest;

    public List<Enroll> getProbabilityTest() {
        return probabilityTest;
    }

    public void setProbabilityTest(List<Enroll> probabilityTest) {
        this.probabilityTest = probabilityTest;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SchoolTest> getSchoolTest() {
        return schoolTest;
    }

    public void setSchoolTest(List<SchoolTest> schoolTest) {
        this.schoolTest = schoolTest;
    }
}
