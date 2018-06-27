package school.lg.overseas.school.bean;

/**
 * Created by Administrator on 2018/3/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 首页广告弹窗
 */
public class HomeAdver implements Parcelable {
    private String image;
    private String url;
    private int maxdisplay;
    private String id;
    private boolean judge;

    public boolean isJudge() {
        return judge;
    }

    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMaxdisplay() {
        return maxdisplay;
    }

    public void setMaxdisplay(int maxdisplay) {
        this.maxdisplay = maxdisplay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<HomeAdver> CREATOR = new Creator<HomeAdver>() {
        @Override
        public HomeAdver createFromParcel(Parcel source) {
            return new HomeAdver(source);
        }

        @Override
        public HomeAdver[] newArray(int size) {
            return new HomeAdver[size];
        }
    };

    protected HomeAdver(Parcel in) {
        image = in.readString();
        url = in.readString();
        maxdisplay = in.readInt();
        id = in.readString();
        judge = in.readByte() != 0;
    }

    public HomeAdver(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(maxdisplay);
        dest.writeString(url);
        dest.writeByte((byte) (judge ? 1 : 0));
        dest.writeString(id);
    }
}
