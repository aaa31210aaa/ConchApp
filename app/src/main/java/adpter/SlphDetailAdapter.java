package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.SlphDetailBean;

public class SlphDetailAdapter extends BaseQuickAdapter<SlphDetailBean, BaseViewHolder> {

    public SlphDetailAdapter(@LayoutRes int layoutResId, @Nullable List<SlphDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SlphDetailBean item) {
        helper.setText(R.id.kcjhdetail_item_place,item.getPlace());
        helper.setText(R.id.slphdetail_item_ncbyl, item.getNcbyl());
        helper.setText(R.id.slphdetail_item_ncbylqx, item.getNcbylqx());

        helper.setText(R.id.slphdetail_item_nmbyl, item.getNmbyl());
        helper.setText(R.id.slphdetail_item_nmbylqx, item.getNmbylqx());

        helper.setText(R.id.slphdetail_item_ncczl, item.getNcczl());
        helper.setText(R.id.slphdetail_item_ncczlqx, item.getNcczlqx());

        helper.setText(R.id.slphdetail_item_nmczl, item.getNmczl());
        helper.setText(R.id.slphdetail_item_nmczlqx, item.getNmczlqx());

        helper.setText(R.id.slphdetail_item_nckzl, item.getNckzl());
        helper.setText(R.id.slphdetail_item_nckzlqx, item.getNckzlqx());

        helper.setText(R.id.slphdetail_item_nmkzl, item.getNmkzl());
        helper.setText(R.id.slphdetail_item_nmkzlqx, item.getNmkzlqx());
    }
}
