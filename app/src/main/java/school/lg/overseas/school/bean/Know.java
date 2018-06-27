package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class Know {
    private List<Apply> apply;
    private List<Apply> applyVideo;
    private List<ApplyChildData> videoAnalysis;

    public List<Apply> getApply() {
        return apply;
    }

    public void setApply(List<Apply> apply) {
        this.apply = apply;
    }

    public List<Apply> getApplyVideo() {
        return applyVideo;
    }

    public void setApplyVideo(List<Apply> applyVideo) {
        this.applyVideo = applyVideo;
    }

    public List<ApplyChildData> getVideoAnalysis() {
        return videoAnalysis;
    }

    public void setVideoAnalysis(List<ApplyChildData> videoAnalysis) {
        this.videoAnalysis = videoAnalysis;
    }
}
