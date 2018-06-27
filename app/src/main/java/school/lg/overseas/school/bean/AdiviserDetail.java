package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class AdiviserDetail {
    private List<LittleData> data;
    private List<Answer> answer;

    public List<LittleData> getData() {
        return data;
    }

    public void setData(List<LittleData> data) {
        this.data = data;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }
}
