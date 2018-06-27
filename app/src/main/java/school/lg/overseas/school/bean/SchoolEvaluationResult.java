package school.lg.overseas.school.bean;


/**
 * Created by Administrator on 2018/1/12.
 */

public class SchoolEvaluationResult {
    private int code;
    private SchoolEvaluationUser user;
    private SchoolEvaluationScore score;
    private SchoolEvaluationData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SchoolEvaluationUser getUser() {
        return user;
    }

    public void setUser(SchoolEvaluationUser user) {
        this.user = user;
    }

    public SchoolEvaluationScore getScore() {
        return score;
    }

    public void setScore(SchoolEvaluationScore score) {
        this.score = score;
    }

    public SchoolEvaluationData getData() {
        return data;
    }

    public void setData(SchoolEvaluationData data) {
        this.data = data;
    }
}
