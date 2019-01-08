package bean;


import java.util.List;

public class UpdataBean {
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
        private String appmemo;
        private String verCode;
        private String url;

        public String getAppmemo() {
            return appmemo;
        }

        public void setAppmemo(String appmemo) {
            this.appmemo = appmemo;
        }

        public String getVerCode() {
            return verCode;
        }

        public void setVerCode(String verCode) {
            this.verCode = verCode;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
