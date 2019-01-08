package bean;


import java.util.List;

public class XhtjFormBean {

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
        private String Material;
        private String purchasecount;
        private String consume;
        private String store;


        public String getMaterial() {
            return Material;
        }

        public void setMaterial(String Material) {
            this.Material = Material;
        }

        public String getPurchasecount() {
            return purchasecount;
        }

        public void setPurchasecount(String purchasecount) {
            this.purchasecount = purchasecount;
        }

        public String getConsume() {
            return consume;
        }

        public void setConsume(String consume) {
            this.consume = consume;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

    }
}
