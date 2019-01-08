package bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class WarningBean implements MultiItemEntity {
    public static final int LAYOUT1 = 1;
    public static final int LAYOUT2 = 2;
    public static final int LAYOUT3 = 3;

    private int itemType;
    private Object data;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public WarningBean(int itemType, Object data) {
        this.itemType = itemType;
        this.data = data;
    }


    public static class MonthKl {
        private int id;
        //预警信息名称
        private String earningName;
        //月计划矿量
        private String yjhkl;
        //月计划出矿量
        private String yjhckl;
        //月实际出矿量
        private String ysjckl;
        //矿量差额
        private String klce;
        //完成率
        private String wcl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEarningName() {
            return earningName;
        }

        public void setEarningName(String earningName) {
            this.earningName = earningName;
        }

        public String getYjhkl() {
            return yjhkl;
        }

        public void setYjhkl(String yjhkl) {
            this.yjhkl = yjhkl;
        }

        public String getYjhckl() {
            return yjhckl;
        }

        public void setYjhckl(String yjhckl) {
            this.yjhckl = yjhckl;
        }

        public String getYsjckl() {
            return ysjckl;
        }

        public void setYsjckl(String ysjckl) {
            this.ysjckl = ysjckl;
        }

        public String getKlce() {
            return klce;
        }

        public void setKlce(String klce) {
            this.klce = klce;
        }

        public String getWcl() {
            return wcl;
        }

        public void setWcl(String wcl) {
            this.wcl = wcl;
        }
    }

    public static class ShsZb {
        private int id;
        //预警信息名称
        private String earningName;
        //下料石灰石指标
        private String xlshszb;
        //实际石灰石指标
        private String sjshszb;
        //偏差率
        private String pcl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEarningName() {
            return earningName;
        }

        public void setEarningName(String earningName) {
            this.earningName = earningName;
        }

        public String getXlshszb() {
            return xlshszb;
        }

        public void setXlshszb(String xlshszb) {
            this.xlshszb = xlshszb;
        }

        public String getSjshszb() {
            return sjshszb;
        }

        public void setSjshszb(String sjshszb) {
            this.sjshszb = sjshszb;
        }

        public String getPcl() {
            return pcl;
        }

        public void setPcl(String pcl) {
            this.pcl = pcl;
        }
    }

    public static class KH {
        private int id;
        //预警信息名称
        private String earningName;
        //设定指标
        private String sdzb;
        //实际指标
        private String sjzb;
        //偏差率
        private String pcl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEarningName() {
            return earningName;
        }

        public void setEarningName(String earningName) {
            this.earningName = earningName;
        }

        public String getSdzb() {
            return sdzb;
        }

        public void setSdzb(String sdzb) {
            this.sdzb = sdzb;
        }

        public String getSjzb() {
            return sjzb;
        }

        public void setSjzb(String sjzb) {
            this.sjzb = sjzb;
        }

        public String getPcl() {
            return pcl;
        }

        public void setPcl(String pcl) {
            this.pcl = pcl;
        }
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
