package bean;


import java.util.List;

//MultiItemEntity
public class SbyxFormBean {
    /**
     * code : 0000
     * message : 获取成功
     * time : 115
     * fault : 1640
     * perforation : 272.0
     * yield : 564.55
     * consume : 0.0
     * data
     */

    private String code;
    private String message;
    private String time;
    private String fault;
    private String perforation;
    private String yield;
    private String consume;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getPerforation() {
        return perforation;
    }

    public void setPerforation(String perforation) {
        this.perforation = perforation;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String devname;
        private String devid;
        private String time;
        private String fault;
        private String perforation;
        private String yield;
        private String psjcl;
        private String consume;
        private String clay;

        public String getDevname() {
            return devname;
        }

        public void setDevname(String devname) {
            this.devname = devname;
        }

        public String getDevid() {
            return devid;
        }

        public void setDevid(String devid) {
            this.devid = devid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFault() {
            return fault;
        }

        public void setFault(String fault) {
            this.fault = fault;
        }

        public String getPerforation() {
            return perforation;
        }

        public void setPerforation(String perforation) {
            this.perforation = perforation;
        }

        public String getYield() {
            return yield;
        }

        public void setYield(String yield) {
            this.yield = yield;
        }

        public String getPsjcl() {
            return psjcl;
        }

        public void setPsjcl(String psjcl) {
            this.psjcl = psjcl;
        }

        public String getConsume() {
            return consume;
        }

        public void setConsume(String consume) {
            this.consume = consume;
        }

        public String getClay() {
            return clay;
        }

        public void setClay(String clay) {
            this.clay = clay;
        }
    }

//    public static final int LAYOUT1 = 1;
//    public static final int LAYOUT2 = 2;
//
//    //运行时间
//    private String runningTime;
//    //故障时间
//    private String failureTime;
//    //穿孔米数
//    private String perforationNum;
//    //产量
//    private String yield;
//    //柴油消耗
//    private String oilConsume;
//
//
//    private int itemType;
//    private Object data;
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
//
//    public SbyxFormBean(int itemType, Object data) {
//        this.itemType = itemType;
//        this.data = data;
//    }
//
//    @Override
//    public int getItemType() {
//        return itemType;
//    }
//
//    public String getRunningTime() {
//        return runningTime;
//    }
//
//    public void setRunningTime(String runningTime) {
//        this.runningTime = runningTime;
//    }
//
//    public String getFailureTime() {
//        return failureTime;
//    }
//
//    public void setFailureTime(String failureTime) {
//        this.failureTime = failureTime;
//    }
//
//    public String getPerforationNum() {
//        return perforationNum;
//    }
//
//    public void setPerforationNum(String perforationNum) {
//        this.perforationNum = perforationNum;
//    }
//
//    public String getYield() {
//        return yield;
//    }
//
//    public void setYield(String yield) {
//        this.yield = yield;
//    }
//
//    public String getOilConsume() {
//        return oilConsume;
//    }
//
//    public void setOilConsume(String oilConsume) {
//        this.oilConsume = oilConsume;
//    }
//
//
//    public static class CDM30 {
//        private int id;
//        // 设备
//        private String equipment;
//        //运行时间
//        private String runningTime;
//        //故障时间
//        private String failureTime;
//        //穿孔米数
//        private String perforationNum;
//        //柴油消耗
//        private String oilConsume;
//        //完好率
//        private String intactRate;
//        //台时
//        private String workingHours;
//        //柴油单耗
//        private String dieselConsume;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getEquipment() {
//            return equipment;
//        }
//
//        public void setEquipment(String equipment) {
//            this.equipment = equipment;
//        }
//
//        public String getRunningTime() {
//            return runningTime;
//        }
//
//        public void setRunningTime(String runningTime) {
//            this.runningTime = runningTime;
//        }
//
//        public String getFailureTime() {
//            return failureTime;
//        }
//
//        public void setFailureTime(String failureTime) {
//            this.failureTime = failureTime;
//        }
//
//        public String getPerforationNum() {
//            return perforationNum;
//        }
//
//        public void setPerforationNum(String perforationNum) {
//            this.perforationNum = perforationNum;
//        }
//
//        public String getOilConsume() {
//            return oilConsume;
//        }
//
//        public void setOilConsume(String oilConsume) {
//            this.oilConsume = oilConsume;
//        }
//
//        public String getIntactRate() {
//            return intactRate;
//        }
//
//        public void setIntactRate(String intactRate) {
//            this.intactRate = intactRate;
//        }
//
//        public String getWorkingHours() {
//            return workingHours;
//        }
//
//        public void setWorkingHours(String workingHours) {
//            this.workingHours = workingHours;
//        }
//
//        public String getDieselConsume() {
//            return dieselConsume;
//        }
//
//        public void setDieselConsume(String dieselConsume) {
//            this.dieselConsume = dieselConsume;
//        }
//
//
//    }
//
//    public static class No13305 {
//        private int id;
//        //设备
//        private String equipment;
//        //运行时间
//        private String runningTime;
//        //故障时间
//        private String failureTime;
//        //穿孔米数
//        private String perforationNum;
//        //柴油消耗
//        private String oilConsume;
//        //完好率
//        private String intactRate;
//        //台时
//        private String workingHours;
//        //产量
//        private String yield;
//        //粘土产量
//        private String clayYield;
//        //柴油单耗
//        private String dieselConsume;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getEquipment() {
//            return equipment;
//        }
//
//        public void setEquipment(String equipment) {
//            this.equipment = equipment;
//        }
//
//        public String getRunningTime() {
//            return runningTime;
//        }
//
//        public void setRunningTime(String runningTime) {
//            this.runningTime = runningTime;
//        }
//
//        public String getFailureTime() {
//            return failureTime;
//        }
//
//        public void setFailureTime(String failureTime) {
//            this.failureTime = failureTime;
//        }
//
//        public String getPerforationNum() {
//            return perforationNum;
//        }
//
//        public void setPerforationNum(String perforationNum) {
//            this.perforationNum = perforationNum;
//        }
//
//        public String getOilConsume() {
//            return oilConsume;
//        }
//
//        public void setOilConsume(String oilConsume) {
//            this.oilConsume = oilConsume;
//        }
//
//        public String getIntactRate() {
//            return intactRate;
//        }
//
//        public void setIntactRate(String intactRate) {
//            this.intactRate = intactRate;
//        }
//
//        public String getWorkingHours() {
//            return workingHours;
//        }
//
//        public void setWorkingHours(String workingHours) {
//            this.workingHours = workingHours;
//        }
//
//        public String getYield() {
//            return yield;
//        }
//
//        public void setYield(String yield) {
//            this.yield = yield;
//        }
//
//        public String getClayYield() {
//            return clayYield;
//        }
//
//        public void setClayYield(String clayYield) {
//            this.clayYield = clayYield;
//        }
//
//        public String getDieselConsume() {
//            return dieselConsume;
//        }
//
//        public void setDieselConsume(String dieselConsume) {
//            this.dieselConsume = dieselConsume;
//        }
//    }


}
