package school.lg.overseas.school.ui.dicovery.bean;

/**
 * Created by Administrator on 2018/6/21.
 */

public class AbroadBean {
    /**
     * id : 5425
     * pid : 0
     * catId : 238
     * name : 自身实力不够到英国G5，我还能去哪些英国学校？
     * title : 自身实力不够到英国G5，我还能去哪些英国学校？
     * image :
     * createTime : 2018-06-15 09:21:46
     * sort : 5425
     * userId : 24759
     * viewCount : 1263
     * show : 1
     * fabulous : 0
     * fine : 0
     * catName : 留学动态
     * answer : 英国学校从历史文化到学术特长各有所长，因此各自团结到不同的联盟或者团队中，就形成了总能见到的"G5精英大学"、"红砖大学"和"罗素集团大学"这些名词。
     * article : 2018-06-15
     * editUser : {"nickname":"雷哥留学@锦鲤小姐姐","image":"/cn/images/editor-user/JL.png","follow":2}
     */

    private String id;
    private String pid;
    private String catId;
    private String name;
    private String title;
    private String image;
    private String createTime;
    private String sort;
    private String userId;
    private String viewCount;
    private String show;
    private String fabulous;
    private String fine;
    private String catName;
    private String answer;
    private String article;
    private AbroadBean.EditUserBean editUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getFabulous() {
        return fabulous;
    }

    public void setFabulous(String fabulous) {
        this.fabulous = fabulous;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public EditUserBean getEditUser() {
        return editUser;
    }

    public void setEditUser(EditUserBean editUser) {
        this.editUser = editUser;
    }

    public static class EditUserBean {
        /**
         * nickname : 雷哥留学@锦鲤小姐姐
         * image : /cn/images/editor-user/JL.png
         * follow : 2     1  已关注   2   未关注
         */

        private String nickname;
        private String image;
        private int follow;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }
    }
}
