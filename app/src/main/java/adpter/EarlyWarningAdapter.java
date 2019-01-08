package adpter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.WarningBean;

public class EarlyWarningAdapter extends BaseMultiItemQuickAdapter<WarningBean, BaseViewHolder> {
    private String ym;

    public EarlyWarningAdapter(List data, String ym) {
        super(data);
        addItemType(WarningBean.LAYOUT1, R.layout.sctjlayout1);
        addItemType(WarningBean.LAYOUT2, R.layout.sctjlayout2);
        addItemType(WarningBean.LAYOUT3, R.layout.sctjlayout3);
        this.ym = ym;
    }

    @Override
    protected void convert(BaseViewHolder helper, WarningBean item) {
        switch (helper.getItemViewType()) {
            case WarningBean.LAYOUT1:
                WarningBean.ShsZb data1 = (WarningBean.ShsZb) item.getData();
                helper.setText(R.id.tv1, data1.getEarningName());
                helper.setText(R.id.tv2, data1.getXlshszb());
                helper.setText(R.id.tv3, data1.getSjshszb());
                helper.setText(R.id.tv4, data1.getPcl());
                break;
            case WarningBean.LAYOUT2:
                WarningBean.KH data2 = (WarningBean.KH) item.getData();
                helper.setText(R.id.tv1, data2.getEarningName());
                helper.setText(R.id.tv2, data2.getSdzb());
                helper.setText(R.id.tv3, data2.getSjzb());
                helper.setText(R.id.tv4, data2.getPcl());
                break;
            case WarningBean.LAYOUT3:
                WarningBean.MonthKl data3 = (WarningBean.MonthKl) item.getData();
                helper.setText(R.id.tv1, data3.getEarningName());
                helper.setText(R.id.tv2, data3.getYjhkl());
                helper.setText(R.id.tv3, data3.getYjhckl());
                helper.setText(R.id.tv4, data3.getYsjckl());
                if (ym.equals("EarlyWarning")) {
                    helper.setText(R.id.tv5, data3.getKlce());
                } else {
                    helper.setText(R.id.tv5, data3.getWcl());
                }

                break;
        }
    }
}
