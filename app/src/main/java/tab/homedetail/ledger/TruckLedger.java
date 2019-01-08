package tab.homedetail.ledger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.TruckAdapter;
import bean.TruckBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.CustomDatePicker;
import utils.DateUtils;
import utils.PortIpAddress;
import utils.ShowToast;

import static tab.homedetail.Ledger.ProcessId;
import static tab.homedetail.Ledger.spinner_map;
import static tab.homedetail.ledger.PerforationLedger.REQUEST_CODE;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;

/**
 * 卡车运输台账
 */
public class TruckLedger extends BaseActivity implements SwipeItemClickListener {
//    @BindView(R.id.search_edittext)
//    EditText search_edittext;
//    @BindView(R.id.search_clear)
//    ImageView search_clear;
    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    //    private List<MultiItemEntity> mDatas;
//    private ExpandableItemAdapter adapter;
    @BindView(R.id.production_date)
    TextView production_date;
    @BindView(R.id.shift)
    Spinner shift;

    private String select_spinner = "";

    private CustomDatePicker customDatePicker;
    private TruckBean.DataBean bean;
    private List<TruckBean.DataBean> mDatas;
    private TruckAdapter adapter;
    private boolean TagSpinner = false;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;
    private String title_name = "";
    private static int REQUEST_ADD_MODIFY = 10;
    private List<String> runningTimeList;
    private List<String> faultTimeList;
    private List<String> useaMountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ledger);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 提交审批
     */
    @OnClick(R.id.submit_approval)
    void SubmitApproval() {

    }

    /**
     * 新增卡车运输台帐操作设备
     */
    @OnClick(R.id.add_fbt)
    void AddLedger() {
        title_name = "新增";
        Intent intent = new Intent(this, AddTruckLedger.class);
        intent.putExtra("titleName", title_name);
        intent.putExtra("workdate", production_date.getText().toString());
        intent.putExtra("driver", PortIpAddress.getUserId(this));
        intent.putExtra("teamgroupid", PortIpAddress.getTeamId(this));
        intent.putExtra("classes", select_spinner);
        intent.putExtra("procid", ProcessId);
        startActivityForResult(intent, REQUEST_ADD_MODIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_MODIFY) {
                refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    protected void initData() {
        runningTimeList = new ArrayList<>();
        faultTimeList = new ArrayList<>();
        useaMountList = new ArrayList<>();
        //获得当前日期
        production_date.setText(DateUtils.getStringDateShort());
        customDatePicker = new CustomDatePicker();

        production_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePicker.CustomDatePickDialog(TruckLedger.this, production_date, refreshLayout);
            }
        });

        //班次
        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] shift = getResources().getStringArray(R.array.shift_arr);
                select_spinner = spinner_map.get(shift[i]);
//              ShowToast.showShort(TruckLedger.this, "选择的是" + select_spinner);
                refreshLayout.autoRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(this, R.color.gray));
        mDatas = new ArrayList<>();
        adapter = new TruckAdapter(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.setSwipeItemClickListener(this);

//        if (adapter == null) {
//            adapter = new TruckAdapter(R.layout.sctjlayout1, mDatas);
//            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//            adapter.isFirstOnly(false);
//            recyclerView.setAdapter(adapter);
//        }

        recyclerView.setSwipeMenuCreator(utils.SwipeMenu.CreateSwipeMenu(this));
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(mDatas);

        isFirstEnter = true;
        initRefresh();
    }

    private void initRefresh() {
//        if (isFirstEnter) {
//            isFirstEnter = false;
//            refreshLayout.autoRefresh();//第一次进入触发自动刷新
//        }

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
                    ShowToast.showShort(TruckLedger.this, R.string.loadSuccess);
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

    /**
     * 获取数据
     */
    private void mConnect() {
        OkGo.<String>get(PortIpAddress.TruckList())
                .tag(TAG)
                .params("bean.driver", PortIpAddress.getUserId(this))
                .params("bean.workdate", production_date.getText().toString())
                .params("bean.classes", select_spinner)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new TruckBean.DataBean();
                                    bean.setBookid(jsonArray.optJSONObject(i).getString("bookid"));
                                    bean.setWorkdate(jsonArray.optJSONObject(i).getString("workdate"));
                                    bean.setClasses(jsonArray.optJSONObject(i).getString("classes"));
                                    bean.setDeviceid(jsonArray.optJSONObject(i).getString("deviceid"));
                                    bean.setDevname(jsonArray.optJSONObject(i).getString("devname"));
                                    bean.setRunningtime(jsonArray.optJSONObject(i).getString("runningtime"));
                                    bean.setFaulttime(jsonArray.optJSONObject(i).getString("faulttime"));
                                    bean.setUseamount(jsonArray.optJSONObject(i).getString("useamount"));
//                                    runningTimeList.add(jsonArray.optJSONObject(i).getString("runningtime"));
//                                    faultTimeList.add(jsonArray.optJSONObject(i).getString("faulttime"));
//                                    useaMountList.add(jsonArray.optJSONObject(i).getString("useamount"));
                                    mDatas.add(bean);
                                }

//                                adapter.setNewData(mDatas);
                                adapter.notifyDataSetChanged(mDatas);

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
//                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                    recyclerView.setVisibility(View.GONE);
                                    nodata_layout.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    nodata_layout.setVisibility(View.GONE);
                                }
                            } else {
                                ShowToast.showShort(TruckLedger.this, err);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(TruckLedger.this, R.string.connect_err);
                    }
                });
    }


    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            bean = adapter.getData().get(adapterPosition);

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//              Toast.makeText(TruckLedger.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                title_name = "修改";
                Intent intent = new Intent(TruckLedger.this, AddTruckLedger.class);
                intent.putExtra("titleName", title_name);

                intent.putExtra("bookid",bean.getBookid());
                intent.putExtra("runningtime",bean.getRunningtime());
                intent.putExtra("faulttime",bean.getFaulttime());
                startActivityForResult(intent, REQUEST_CODE);
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(TruckLedger.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * 列表item点击事件
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {

    }
}
