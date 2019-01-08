package bean;


import java.util.List;

public class SctjFormBean {
    private String code;
    private String message;
    private List<CountindexBean> countindex;
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

    public List<CountindexBean> getCountindex() {
        return countindex;
    }

    public void setCountindex(List<CountindexBean> countindex) {
        this.countindex = countindex;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class CountindexBean {
        /**
         * name : 爆落量
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class DataBean {
        private String pbdetailid;
        private String projectname;
        private List<IndexBean> index;

        public String getPbdetailid() {
            return pbdetailid;
        }

        public void setPbdetailid(String pbdetailid) {
            this.pbdetailid = pbdetailid;
        }

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
        }

        public List<IndexBean> getIndex() {
            return index;
        }

        public void setIndex(List<IndexBean> index) {
            this.index = index;
        }

        public static class IndexBean {
            /**
             * name : 爆落量
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }



}
