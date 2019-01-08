package adpter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.LspBean;

/**
 * 石灰石生产列表适配器
 */

public class LspAdapter extends BaseQuickAdapter<LspBean, BaseViewHolder> {


    public LspAdapter(@LayoutRes int layoutResId, @Nullable List<LspBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LspBean item) {
        helper.setImageResource(R.id.lime_stone_production_item_img, item.getImage());
    }
}
