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

import bean.KcPlanBean;

public class KcPlanDetailAdapter extends BaseQuickAdapter<KcPlanBean, BaseViewHolder> {
    private int[] rid = {R.id.kcjhdetail1, R.id.kcjhdetail2, R.id.kcjhdetail3, R.id.kcjhdetail4, R.id.kcjhdetail5, R.id.kcjhdetail6, R.id.kcjhdetail7
            , R.id.kcjhdetail8, R.id.kcjhdetail9, R.id.kcjhdetail10, R.id.kcjhdetail11, R.id.kcjhdetail12, R.id.kcjhdetail13, R.id.kcjhdetail14
            , R.id.kcjhdetail15, R.id.kcjhdetail16, R.id.kcjhdetail17, R.id.kcjhdetail18, R.id.kcjhdetail19, R.id.kcjhdetail20};
    private List<View> tvList = new ArrayList<>();

    public KcPlanDetailAdapter(@LayoutRes int layoutResId, @Nullable List<KcPlanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, KcPlanBean item) {
        helper.setText(R.id.kcjhdetail_item_place, item.getPlace());

        tvList.clear();
        for (int i = 0; i < rid.length; i++) {
            tvList.add(helper.getView(rid[i]));
            tvList.get(i).setVisibility(View.VISIBLE);
        }

        for (int j = 0; j < item.getList().size(); j++) {
//            tvList.get(j).setBackgroundResource(R.drawable.editext_yb);
            tvList.get(j).setPadding(15, 15, 15, 15);
            ((TextView) tvList.get(j)).setText(item.getList().get(j).getName() + "：" + item.getList().get(j).getValue() +"(t)");
        }

        //根据长度隐藏没有数据的textview
        for (int k = 0; k < (rid.length - item.getList().size()); k++) {
            tvList.get((rid.length - k) - 1).setVisibility(View.GONE);
        }

        //创建textview添加之前要清空
//        view.removeAllViews();

//        for (KcPlanBean.Content bean : item.getList()) {
//            //创建一行
//            LinearLayout llh = new LinearLayout(context);
//            llh.setLayoutParams(lps);
//            llh.setOrientation(LinearLayout.HORIZONTAL);
//
//            //创建一行中的内容
//            TextView textName = new TextView(context);
//            TextView textValue = new TextView(context);
//            textName.setLayoutParams(lpsName);
//            textName.setBackgroundResource(R.drawable.editext_yb);
//            textValue.setLayoutParams(lpsValue);
//            textValue.setBackgroundResource(R.drawable.editext_yb);
//            textName.setGravity(Gravity.CENTER);
//            textValue.setGravity(Gravity.CENTER);
//
//            textName.setText(bean.getName());
//            textValue.setText(bean.getValue());
//            llh.addView(textName);
//            llh.addView(textValue);
//            view.addView(llh);
//        }
    }
}
