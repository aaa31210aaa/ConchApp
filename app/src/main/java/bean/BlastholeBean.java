package bean;


public class BlastholeBean {
    private int position;
    private String id;
    private boolean isVisible;
    //作业地点
    private String projectname;
    //定位时间
    private String locationTime;
    //炮孔数
    private String blastholeNum;
    //分组信息
    private String grouping;
    //上传时间
    private String uploadtime;

    //图片
    private int image;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public String getBlastholeNum() {
        return blastholeNum;
    }

    public void setBlastholeNum(String blastholeNum) {
        this.blastholeNum = blastholeNum;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
}
