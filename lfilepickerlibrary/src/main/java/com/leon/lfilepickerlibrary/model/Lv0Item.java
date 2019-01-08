package com.leon.lfilepickerlibrary.model;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leon.lfilepickerlibrary.adapter.ExpandableAdapter;

public class Lv0Item extends AbstractExpandableItem<Lv1Item> implements MultiItemEntity {
    public String title;
    //    public Level0Item( String title) {
//        this.title = title;
//    }
    public String parentid;
    public String parentname;

    @Override
    public int getItemType() {
        return ExpandableAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
}
