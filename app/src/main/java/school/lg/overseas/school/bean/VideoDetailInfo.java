package school.lg.overseas.school.bean;

import com.boredream.bdvideoplayer.bean.IVideoInfo;

public class VideoDetailInfo implements IVideoInfo {

    public String title;
    public String videoPath;

    @Override
    public String getVideoTitle() {
        return title;
    }

    @Override
    public String getVideoPath() {
        return videoPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

}
