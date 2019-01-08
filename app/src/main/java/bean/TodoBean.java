package bean;


public class TodoBean {
    //id
    private String matterid;
    //待办事宜名称
    private String mattername;
    //事宜内容
    private String memo;
    //时间
    private String addtime;

    public String getMatterid() {
        return matterid;
    }

    public void setMatterid(String matterid) {
        this.matterid = matterid;
    }

    public String getMattername() {
        return mattername;
    }

    public void setMattername(String mattername) {
        this.mattername = mattername;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
