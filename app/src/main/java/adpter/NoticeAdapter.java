package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.NoticeBean;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;

public class NoticeAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {

    public NoticeAdapter(@LayoutRes int layoutResId, @Nullable List<NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item) {
        helper.setText(R.id.mynotification_listview_item_title, item.getTitle());
        helper.setText(R.id.mynotification_listview_item_time_tv, "发布时间:" + item.getDate());
        BGABadgeLinearLayout notice_item_badge = helper.getView(R.id.notice_item_badge);

        if (item.getMessagestae().contains("XXZT001")) {
            notice_item_badge.showCirclePointBadge();
        } else {
            notice_item_badge.hiddenBadge();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
