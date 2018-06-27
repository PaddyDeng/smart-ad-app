package school.lg.overseas.school.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class Answer {
    private String id;
    private String pId;
    private String userId;
    private String replyUser;
    private String qId;
    private String question;
    private String content;
    private String addTime;
    private String tag;
    private String type;
    private String browse;
    private String follow;
    private String questionType;
    private String answer;
    private String praise;
    private String adviserId;
    private String see;
    private String replyUid;
    private String userName;
    private String username;
    private String image;
    private String nickname;
    private boolean fabulous;
    private List<Reply> reply;

    public List<Reply> getReply() {
        return reply;
    }

    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(String adviserId) {
        this.adviserId = adviserId;
    }

    public String getSee() {
        return see;
    }

    public void setSee(String see) {
        this.see = see;
    }

    public String getReplyUid() {
        return replyUid;
    }

    public void setReplyUid(String replyUid) {
        this.replyUid = replyUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isFabulous() {
        return fabulous;
    }

    public void setFabulous(boolean fabulous) {
        this.fabulous = fabulous;
    }
}
