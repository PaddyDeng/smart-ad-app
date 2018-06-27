package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class SchoolEvaluationData {
    private int score;
    private List<SchoolEvaluationRes> res;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<SchoolEvaluationRes> getRes() {
        return res;
    }

    public void setRes(List<SchoolEvaluationRes> res) {
        this.res = res;
    }
}
