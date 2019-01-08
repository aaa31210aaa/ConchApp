package com.leon.lfilepickerlibrary.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leon.lfilepickerlibrary.R;
import com.leon.lfilepickerlibrary.model.ProjectNameBean;

import java.util.List;

public class ProjectNameAdapter extends BaseQuickAdapter<ProjectNameBean, BaseViewHolder> {

    public ProjectNameAdapter(@LayoutRes int layoutResId, @Nullable List<ProjectNameBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectNameBean item) {
        helper.setText(R.id.dialog_list_item_projectname, item.getProjectName());
    }
}
