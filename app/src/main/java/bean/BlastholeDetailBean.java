package bean;


import android.view.View;
import android.widget.TextView;

import java.util.List;

public class BlastholeDetailBean {
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
        private ProjectBean project;
        private List<FzlistBean> fzlist;
        private List<PklistBean> pklist;

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public List<FzlistBean> getFzlist() {
            return fzlist;
        }

        public void setFzlist(List<FzlistBean> fzlist) {
            this.fzlist = fzlist;
        }

        public List<PklistBean> getPklist() {
            return pklist;
        }

        public void setPklist(List<PklistBean> pklist) {
            this.pklist = pklist;
        }

        public static class ProjectBean {

            private String projectid;

            public String getProjectid() {
                return projectid;
            }

            public void setProjectid(String projectid) {
                this.projectid = projectid;
            }
        }

        public static class FzlistBean {
            private String uuid;
            private String name;
            private TextView view;

            public TextView getView() {
                return view;
            }

            public void setView(TextView view) {
                this.view = view;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PklistBean {
            private View view;
            private String uuid;
            private String x;
            private String y;
            private String assayGroupId;
            private boolean checked;

            public View getView() {
                return view;
            }

            public void setView(View view) {
                this.view = view;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getAssayGroupId() {
                return assayGroupId;
            }

            public void setAssayGroupId(String assayGroupId) {
                this.assayGroupId = assayGroupId;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }
        }
    }
}
