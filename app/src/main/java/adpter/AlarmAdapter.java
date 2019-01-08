package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.AlarmBean;

public class AlarmAdapter extends BaseQuickAdapter<AlarmBean, BaseViewHolder> {

    public AlarmAdapter(@LayoutRes int layoutResId, @Nullable List<AlarmBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, AlarmBean item) {
        helper.setText(R.id.mynotification_listview_item_title, item.getAlarmName());
        helper.setText(R.id.item_lv, "预警等级：" + item.getAlarmlevelname());
        helper.setText(R.id.mynotification_listview_item_time_tv, "发布时间:" + item.getAddTime());
    }
}
