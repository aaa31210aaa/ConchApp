package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.CjPlanBean;

public class CjPlanDetailAdapter extends BaseQuickAdapter<CjPlanBean, BaseViewHolder> {
    public CjPlanDetailAdapter(@LayoutRes int layoutResId, @Nullable List<CjPlanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CjPlanBean item) {
        helper.setText(R.id.cjjhdetail_item_place, item.getPlace());
        helper.setText(R.id.cjjhdetail_item_value1, item.getName1() + "：" + item.getValue1() + "(t)");
        helper.setText(R.id.cjjhdetail_item_value2, item.getName2() + "：" + item.getValue2() + "(t)");
        helper.setText(R.id.cjjhdetail_item_value3, item.getName3() + "：" + item.getValue3() + "(t)");
        helper.setText(R.id.cjjhdetail_item_value4, item.getName4() + "：" + item.getValue4() + "(t)");
    }
}
