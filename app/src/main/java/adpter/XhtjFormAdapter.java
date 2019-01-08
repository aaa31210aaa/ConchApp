package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.XhtjFormBean;
import tab.Form;

public class XhtjFormAdapter extends BaseQuickAdapter<XhtjFormBean.DataBean, BaseViewHolder> {
    private String type;

    public XhtjFormAdapter(@LayoutRes int layoutResId, @Nullable List<XhtjFormBean.DataBean> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, XhtjFormBean.DataBean item) {
        helper.setText(R.id.xhtjform_detail_material, item.getMaterial());
        helper.setText(R.id.xhtjform_detail_purchasecount, item.getPurchasecount());
        helper.setText(R.id.xhtjform_detail_consume, item.getConsume());

        if (type.equals(Form.XHTJDAY)) {
            helper.getView(R.id.store_ll).setVisibility(View.VISIBLE);
            helper.setText(R.id.xhtjform_detail_store, item.getStore());
        } else {
            helper.getView(R.id.store_ll).setVisibility(View.GONE);
        }

    }
}
