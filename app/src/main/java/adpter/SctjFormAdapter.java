package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.ArrayList;
import java.util.List;

import bean.SctjFormBean;

public class SctjFormAdapter extends BaseQuickAdapter<SctjFormBean.DataBean, BaseViewHolder> {
    private int[] rid = {R.id.kcjhdetail1, R.id.kcjhdetail2, R.id.kcjhdetail3, R.id.kcjhdetail4, R.id.kcjhdetail5, R.id.kcjhdetail6, R.id.kcjhdetail7
            , R.id.kcjhdetail8, R.id.kcjhdetail9, R.id.kcjhdetail10, R.id.kcjhdetail11, R.id.kcjhdetail12, R.id.kcjhdetail13, R.id.kcjhdetail14
            , R.id.kcjhdetail15, R.id.kcjhdetail16, R.id.kcjhdetail17, R.id.kcjhdetail18, R.id.kcjhdetail19, R.id.kcjhdetail20};
    private List<View> tvList = new ArrayList<>();



    public SctjFormAdapter(@LayoutRes int layoutResId, @Nullable List<SctjFormBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SctjFormBean.DataBean item) {
        helper.setText(R.id.kcjhdetail_item_place, item.getProjectname());
        tvList.clear();
        for (int i = 0; i < rid.length; i++) {
            tvList.add(helper.getView(rid[i]));
            tvList.get(i).setVisibility(View.VISIBLE);
        }

        for (int j = 0; j < item.getIndex().size(); j++) {
            tvList.get(j).setBackgroundResource(R.drawable.editext_yb);
            tvList.get(j).setPadding(15, 15, 15, 15);
            ((TextView) tvList.get(j)).setText(item.getIndex().get(j).getName() + "：" + item.getIndex().get(j).getValue());
        }

        //根据长度隐藏没有数据的textview
        for (int k = 0; k < (rid.length - item.getIndex().size()); k++) {
            tvList.get((rid.length - k) - 1).setVisibility(View.GONE);
        }


    }
}
