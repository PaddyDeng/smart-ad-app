package school.lg.overseas.school.bean;

import java.util.List;

public class RemarkData {

    private String remarkNum;
    private String id;
    private String title;
    private String content;
    private String like;
    private String createTime;
    private String uid;
    private String type;
    private String icon;
    private String publisher;
    private String viewCount;
    private String belong;
    private boolean likeId;
    private String likeNum;
    private Object image;
    private List<ReplyBean> reply;
    private int recyclePosition;
    private int isLive;//是否直播
    private String liveTitle;//直播标题
    private String liveImg;//直播图片
    private int isPay;//该次直播是否充值
    private int integral;//雷豆数
    private String qq;
    private int needNum;

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public String getRemarkNum() {
        return remarkNum;
    }

    public void setRemarkNum(String remarkNum) {
        this.remarkNum = remarkNum;
    }

    public int getRecyclePosition() {
        return recyclePosition;
    }

    public void setRecyclePosition(int recyclePosition) {
        this.recyclePosition = recyclePosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public boolean isLikeId() {
        return likeId;
    }

    public void setLikeId(boolean likeId) {
        this.likeId = likeId;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLiveImg() {
        return liveImg;
    }

    public void setLiveImg(String liveImg) {
        this.liveImg = liveImg;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
