package com.leon.lfilepickerlibrary.model;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leon.lfilepickerlibrary.adapter.ExpandableAdapter;

public class Lv1Item extends AbstractExpandableItem<String> implements MultiItemEntity {
    public String title;
    public String tid;
    //    public Level1Item(String title) {
//        this.title = title;
//    }
    private String projectid;
    private String projectname;
    private String projectcode;
    private String pks;
    private String uploadtime;
    private String sffz;

    @Override
    public int getItemType() {
        return ExpandableAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
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
}
