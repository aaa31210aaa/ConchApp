package bean;


public class AlarmBean {
    //id
    private String alarmId;
    //预警名称
    private String alarmName;
    //预警等级名称
    private String alarmlevelname;
    //发布时间
    private String addTime;
    //处理状态
    private String handleType;
    //处理说明
    private String handlecontent;
    //详情
    private String memo;

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getHandlecontent() {
        return handlecontent;
    }

    public void setHandlecontent(String handlecontent) {
        this.handlecontent = handlecontent;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAlarmlevelname() {
        return alarmlevelname;
    }

    public void setAlarmlevelname(String alarmlevelname) {
        this.alarmlevelname = alarmlevelname;
    }
}
