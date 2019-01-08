package tab.homedetail.ledger;


import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.conchapp.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import adpter.ProductionLedgerAdapter;
import bean.TodoBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.BaseFragment;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.ShowToast;


/**
 * 生产台账
 */
public class ProductionLedger extends BaseFragment {
    private View view;
    private View headView;

    @BindView(R.id.search_edittext)
    EditText search_edittext;

    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private TextView production_date;
    private TextView shift;
    private TextView production_process;

    private List<TodoBean> searchDatas;
    private List<TodoBean> mDatas;
    private ProductionLedgerAdapter adapter;

    private boolean isFirstEnter;


    public ProductionLedger() {
        // Required empty public constructor
    }


    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_production_ledger, null);
        headView = View.inflate(getActivity(), R.layout.production_ledger_headview, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        //生产日期
        production_date = headView.findViewById(R.id.production_date);
        //班次
        shift = headView.findViewById(R.id.shift);
        //生产工序
        production_process = headView.findViewById(R.id.production_process);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        initRefresh();
//      MonitorEditext();
    }


    private void initRefresh() {
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新
        }

        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(1, ShowToast.refreshTime);
            }
        });

        //加载
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(0, ShowToast.refreshTime);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    ShowToast.showShort(getActivity(), R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    mConnect();
                    break;
                default:
                    break;
            }
        }
    };





    private void mConnect() {
        mDatas.clear();
        //获得当前日期
        production_date.setText(DateUtils.getStringDateShort());
        //获取当前小时
        int hours = Integer.parseInt(DateUtils.getHour());
        if (hours > 8 && hours < 18) {
            shift.setText("白班");
        } else if (hours > 18 && hours < 24) {
            shift.setText("中班");
        } else if (hours > 24 && hours < 8) {
            shift.setText("晚班");
        }
        //生产工序根据班组班次决定
        production_process.setText("");


        for (int i = 0; i < 5; i++) {
            TodoBean bean = new TodoBean();
            bean.setMattername("标题" + i);
            bean.setMemo("内容" + i);
            mDatas.add(bean);
        }

        if (adapter == null) {
            adapter = new ProductionLedgerAdapter(R.layout.notice_list_item, mDatas);
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.isFirstOnly(true);
            adapter.addHeaderView(headView);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setHeaderView(headView);
            adapter.setNewData(mDatas);
        }
        //如果无数据设置空布局
        if (mDatas.size() == 0) {
            adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
        }

        refreshLayout.finishRefresh();
    }


}
