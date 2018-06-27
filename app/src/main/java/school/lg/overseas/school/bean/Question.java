package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 问答
 */

public class Question {
    private List<Tags> tags;
    private QuestionDatas data;

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public QuestionDatas getData() {
        return data;
    }

    public void setData(QuestionDatas data) {
        this.data = data;
    }
}
