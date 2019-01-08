package bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leon.lfilepickerlibrary.adapter.ExpandableAdapter;

import java.util.List;

public class BlastholeExpandBean extends AbstractExpandableItem<String> implements MultiItemEntity {
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
        private String parentid;
        private String parentname;
        private List<DataArrayBean> dataArray;

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getParentname() {
            return parentname;
        }

        public void setParentname(String parentname) {
            this.parentname = parentname;
        }

        public List<DataArrayBean> getDataArray() {
            return dataArray;
        }

        public void setDataArray(List<DataArrayBean> dataArray) {
            this.dataArray = dataArray;
        }

        public static class DataArrayBean {
            private String projectid;
            private String projectname;
            private String projectcode;
            private String pks;
            private String uploadtime;
            private String sffz;
            public String title;
            public String tid;

            public String getProjectid() {
                return projectid;
            }

            public void setProjectid(String projectid) {
                this.projectid = projectid;
            }

            public String getProjectname() {
                return projectname;
            }

            public void setProjectname(String projectname) {
                this.projectname = projectname;
            }

            public String getProjectcode() {
                return projectcode;
            }

            public void setProjectcode(String projectcode) {
                this.projectcode = projectcode;
            }

            public String getPks() {
                return pks;
            }

            public void setPks(String pks) {
                this.pks = pks;
            }

            public String getUploadtime() {
                return uploadtime;
            }

            public void setUploadtime(String uploadtime) {
                this.uploadtime = uploadtime;
            }

            public String getSffz() {
                return sffz;
            }

            public void setSffz(String sffz) {
                this.sffz = sffz;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }
        }
    }

    @Override
    public int getItemType() {
        return ExpandableAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
