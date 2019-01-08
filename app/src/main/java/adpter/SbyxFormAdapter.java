package adpter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.conchapp.R;

import java.util.List;

import bean.SbyxFormBean;

//BaseMultiItemQuickAdapter
public class SbyxFormAdapter extends BaseQuickAdapter<SbyxFormBean.DataBean, BaseViewHolder> {

    public SbyxFormAdapter(@LayoutRes int layoutResId, @Nullable List<SbyxFormBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SbyxFormBean.DataBean item) {
        helper.setText(R.id.sbyxdetail_item_deviceid,item.getDevid());
        helper.setText(R.id.sbyxdetail_item_time, item.getTime());
        helper.setText(R.id.sbyxdetail_item_fault, item.getFault());
        helper.setText(R.id.sbyxdetail_item_perforation, item.getPerforation());
        helper.setText(R.id.sbyxdetail_item_yield, item.getYield());
        helper.setText(R.id.sbyxdetail_item_psjcl, item.getPsjcl());
        helper.setText(R.id.sbyxdetail_item_consume, item.getConsume());
        helper.setText(R.id.sbyxdetail_item_clay, item.getClay());
    }

//    public SbyxFormAdapter(List<SbyxFormBean> data) {
//        super(data);
//        addItemType(SbyxFormBean.LAYOUT1, R.layout.sbyxlayout1);
//        addItemType(SbyxFormBean.LAYOUT2, R.layout.sbyxlayout2);
//    }

//    @Override
//    protected void convert(BaseViewHolder helper, SbyxFormBean item) {
//        switch (helper.getItemViewType()) {
//            case SbyxFormBean.LAYOUT1:
//                SbyxFormBean.CDM30 data1 = (SbyxFormBean.CDM30) item.getData();
//                helper.setText(R.id.sbyxlayout1_equipment, data1.getEquipment());
//                helper.setText(R.id.sbyxlayout1_runTime, data1.getRunningTime());
//                helper.setText(R.id.sbyxlayout1_failureTime, data1.getFailureTime());
//                helper.setText(R.id.sbyxlayout1_perforationNum, data1.getPerforationNum());
//                helper.setText(R.id.sbyxlayout1_oilConsume, data1.getOilConsume());
//                helper.setText(R.id.sbyxlayout1_intactRate, data1.getIntactRate());
//                helper.setText(R.id.sbyxlayout1_workingHours, data1.getWorkingHours());
//                helper.setText(R.id.sbyxlayout1_dieselConsume, data1.getDieselConsume());
//                break;
//            case SbyxFormBean.LAYOUT2:
//                SbyxFormBean.No13305 data2 = (SbyxFormBean.No13305) item.getData();
//                helper.setText(R.id.sbyxlayout2_equipment, data2.getEquipment());
//                helper.setText(R.id.sbyxlayout2_runTime, data2.getRunningTime());
//                helper.setText(R.id.sbyxlayout2_failureTime, data2.getFailureTime());
//                helper.setText(R.id.sbyxlayout2_perforationNum, data2.getPerforationNum());
//                helper.setText(R.id.sbyxlayout2_oilConsume, data2.getOilConsume());
//                helper.setText(R.id.sbyxlayout2_yield, data2.getYield());
//                helper.setText(R.id.sbyxlayout2_intactRate, data2.getIntactRate());
//                helper.setText(R.id.sbyxlayout2_workingHours, data2.getWorkingHours());
//                helper.setText(R.id.sbyxlayout2_clayYield, data2.getClayYield());
//                helper.setText(R.id.sbyxlayout2_dieselConsume, data2.getDieselConsume());
//
//                break;
//        }
//    }


}
