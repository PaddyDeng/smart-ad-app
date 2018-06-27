package school.lg.overseas.school.ui.dicovery;

import java.util.List;

import school.lg.overseas.school.ui.dicovery.bean.AbroadBean;

/**
 * Created by Administrator on 2018/6/19.
 */

public class AbroadHomeBean {

    private List<ToutiaoBean> toutiao;
    private List<AbroadBean> abroad;

    public List<ToutiaoBean> getToutiao() {
        return toutiao;
    }

    public void setToutiao(List<ToutiaoBean> toutiao) {
        this.toutiao = toutiao;
    }

    public List<AbroadBean> getAbroad() {
        return abroad;
    }

    public void setAbroad(List<AbroadBean> abroad) {
        this.abroad = abroad;
    }

    public static class ToutiaoBean {
        /**
         * id : 5338
         * pid : 0
         * catId : 238
         * name : 【5月留学公开课】BA专业全方位解析！
         * title : 【5月留学公开课】BA专业全方位解析！
         * image : /files/attach/images/20180523/1527042263450811.png
         * createTime : 2018-05-23 10:24:35
         * sort : 5338
         * userId : 24758
         * viewCount : 744
         * show : 1
         * fabulous : 0
         * fine : 0
         * catName : 留学动态
         * answer : Business Analytics商业分析，这是一个将商科和工科相结合的交叉学科，受到了越来越多人的喜爱。那么，申请BA需要哪些条件呢？怎样提高自己的竞争力呢？
         * article : 2018-05-23 10:22:18
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
    }

}
