package school.lg.overseas.school.bean;

/**
 * 点赞返回
 */

public class PraiseBack {
    private int code;
    private String message;
    private int num;
    private String likeNum;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
