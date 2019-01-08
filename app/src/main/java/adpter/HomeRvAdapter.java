package adpter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.HomeRvBean;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;

public class HomeRvAdapter extends BaseQuickAdapter<HomeRvBean, BaseViewHolder> {

    public HomeRvAdapter(int layoutResId, List<HomeRvBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRvBean item) {
        helper.setImageResource(R.id.home_rvitem_img, item.getImage());
        helper.setText(R.id.home_rvitem_tv, item.getTvName());
        BGABadgeLinearLayout homerv_item_badge = helper.getView(R.id.homerv_item_badge);

        if (!item.getMsgNum().equals("0")){
            homerv_item_badge.showTextBadge(item.getMsgNum());
        }
    }
}
