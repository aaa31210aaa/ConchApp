package bean;

import java.util.List;

/**
 * 卡车操作设备
 */
public class TruckBean {

    private String code;
    private String message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String bookid;
        private String useid;
        private String workdate;
        private String deviceid;
        private String devname;
        private String driver;
        private String teamgroupid;
        private String classes;
        private String runningtime;
        private String faulttime;
        private String useamount;

        public String getBookid() {
            return bookid;
        }

        public void setBookid(String bookid) {
            this.bookid = bookid;
        }

        public String getUseid() {
            return useid;
        }

        public void setUseid(String useid) {
            this.useid = useid;
        }

        public String getWorkdate() {
            return workdate;
        }

        public void setWorkdate(String workdate) {
            this.workdate = workdate;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getDevname() {
            return devname;
        }

        public void setDevname(String devname) {
            this.devname = devname;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getTeamgroupid() {
            return teamgroupid;
        }

        public void setTeamgroupid(String teamgroupid) {
            this.teamgroupid = teamgroupid;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public String getRunningtime() {
            return runningtime;
        }

        public void setRunningtime(String runningtime) {
            this.runningtime = runningtime;
        }

        public String getFaulttime() {
            return faulttime;
        }

        public void setFaulttime(String faulttime) {
            this.faulttime = faulttime;
        }

        public String getUseamount() {
            return useamount;
        }

        public void setUseamount(String useamount) {
            this.useamount = useamount;
        }
    }
}
