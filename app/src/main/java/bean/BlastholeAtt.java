package bean;


import android.view.View;

public class BlastholeAtt {
    //分组id
    private String fzid;
    //炮孔id
    private String pkid;
    //炮孔view
    private View view;
    //X轴坐标
    private int x;
    //Y轴坐标
    private int y;
    //分组名
    private String groupName;
    //选中状态
    private boolean checked;

    public String getFzid() {
        return fzid;
    }

    public void setFzid(String fzid) {
        this.fzid = fzid;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
