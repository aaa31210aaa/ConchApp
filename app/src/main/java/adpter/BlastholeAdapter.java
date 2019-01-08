package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.BlastholeBean;

public class BlastholeAdapter extends BaseQuickAdapter<BlastholeBean, BaseViewHolder> {

    public BlastholeAdapter(@LayoutRes int layoutResId, @Nullable List<BlastholeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlastholeBean item) {
        helper.setText(R.id.project_name, "作业地点：" + item.getProjectname());
        helper.setText(R.id.blasthole_num, "炮孔数：" + item.getBlastholeNum());
        helper.setText(R.id.grouping, "分组情况：" + item.getGrouping());
        helper.setText(R.id.uploadtime, "上传时间：" + item.getUploadtime());
    }
}
