package school.lg.overseas.school.bean;

/**
 * Created by Administrator on 2018/1/12.
 */

public class SchoolEvaluationScore {
    private SchoolEvaluationWork gpa;
    private SchoolEvaluationWork gmat;
    private SchoolEvaluationWork toefl;
    private SchoolEvaluationWork school;
    private SchoolEvaluationWork work;

    public SchoolEvaluationWork getGpa() {
        return gpa;
    }

    public void setGpa(SchoolEvaluationWork gpa) {
        this.gpa = gpa;
    }

    public SchoolEvaluationWork getGmat() {
        return gmat;
    }

    public void setGmat(SchoolEvaluationWork gmat) {
        this.gmat = gmat;
    }

    public SchoolEvaluationWork getToefl() {
        return toefl;
    }

    public void setToefl(SchoolEvaluationWork toefl) {
        this.toefl = toefl;
    }

    public SchoolEvaluationWork getSchool() {
        return school;
    }

    public void setSchool(SchoolEvaluationWork school) {
        this.school = school;
    }

    public SchoolEvaluationWork getWork() {
        return work;
    }

    public void setWork(SchoolEvaluationWork work) {
        this.work = work;
    }
}
