package adpter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.TodoBean;


public class ProductionLedgerAdapter extends BaseQuickAdapter<TodoBean, BaseViewHolder> {

    public ProductionLedgerAdapter(@LayoutRes int layoutResId, @Nullable List<TodoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodoBean item) {
        helper.setText(R.id.mynotification_listview_item_title, item.getMattername());
        helper.setText(R.id.mynotification_listview_item_time_tv, item.getMemo());
    }


}
