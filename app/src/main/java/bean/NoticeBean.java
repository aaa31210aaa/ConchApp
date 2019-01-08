package bean;


public class NoticeBean {
    private int position;
    private String noticeId;
    private String messagestae; //未读已读状态
    private String title;
    private String date;
    private String content;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getMessagestae() {
        return messagestae;
    }

    public void setMessagestae(String messagestae) {
        this.messagestae = messagestae;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
