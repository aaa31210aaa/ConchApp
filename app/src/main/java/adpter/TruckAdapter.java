package adpter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.conchapp.R;

import java.util.List;

import bean.TruckBean;
import utils.BaseSwipeMenuRvAdapter;

public class TruckAdapter extends BaseSwipeMenuRvAdapter<TruckAdapter.ViewHolder> {

    private List<TruckBean.DataBean> mDataList;

    public TruckAdapter(Context context) {
        super(context);
    }

    public void notifyDataSetChanged(List<TruckBean.DataBean> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mDataList.get(position));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTitle2;
        TextView tvTitle3;
        TextView tvTitle4;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTitle2 = (TextView) itemView.findViewById(R.id.tv_title2);
            tvTitle3 = (TextView) itemView.findViewById(R.id.tv_title3);
            tvTitle4 = (TextView) itemView.findViewById(R.id.tv_title4);
        }

        public void setData(TruckBean.DataBean item) {
            this.tvTitle.setText("设备:" + item.getDevname());
            this.tvTitle2.setText("运行时间(h):" + item.getRunningtime());
            this.tvTitle3.setText("故障时间(h):" + item.getFaulttime());
            this.tvTitle4.setText("柴油消耗(kg):" + item.getUseamount());
        }


    }

    public List<TruckBean.DataBean> getData() {
        return mDataList;
    }


}
