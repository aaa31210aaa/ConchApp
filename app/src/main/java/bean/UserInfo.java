package bean;


public class UserInfo {
    public String code;
    public String message;
    public String access_token;
    public String username;
    public String userid;
    public String deptname;
    public String teamid;
    public String headurl;


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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    @Override
    public String toString() {
        return "{" +
                "success='" + code + '\'' +
                ", errormessage='" + message + '\'' +
                ", access_token='" + access_token + '\'' +
                ", username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                ", deptname='" + deptname + '\'' +
                '}';
    }
}
