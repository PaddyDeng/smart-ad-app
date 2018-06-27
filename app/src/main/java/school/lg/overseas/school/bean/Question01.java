package school.lg.overseas.school.bean;

import java.util.List;

/**
 * 问答
 */

public class Question01 {
    private List<Tags> tags;
    private QuestionDatas01 data;

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public QuestionDatas01 getData() {
        return data;
    }

    public void setData(QuestionDatas01 data) {
        this.data = data;
    }
}
