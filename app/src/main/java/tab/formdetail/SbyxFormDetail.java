package tab.formdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.SbyxFormAdapter;
import bean.SbyxFormBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.Form;
import utils.BaseActivity;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.CustomDatePicker.getMonthCardData;
import static utils.CustomDatePicker.getWeekCardData;
import static utils.CustomDatePicker.getYearCardData;
import static utils.CustomDatePicker.initDayPicker;
import static utils.CustomDatePicker.initMonthPicker;
import static utils.CustomDatePicker.initWeekPicker;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class SbyxFormDetail extends BaseActivity {
    @BindView(R.id.sbyxform_detail_title)
    TextView sbyxform_detail_title;
    @BindView(R.id.sbyxform_detail_date)
    TextView sbyxform_detail_date;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<SbyxFormBean.DataBean> searchDatas;
    private List<SbyxFormBean.DataBean> mDatas;

    private SbyxFormAdapter adapter;
    private View headView;

    private String type;
    private String dateKey = "";
    private String weekKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbyx_form_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        isFirstEnter = true;
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        mDatas = new ArrayList<>();
        headView = View.inflate(SbyxFormDetail.this, R.layout.sbyxform_head, null);

        if (type.equals(Form.SBYXDAY)) {
            sbyxform_detail_title.setText("设备日报");
            dateKey = "day";
            //初始化时间 选择器
            sbyxform_detail_date.setText(DateUtils.dateToStr(DateUtils.getNow()));
        } else if (type.equals(Form.SBYXWEEK)) {
            sbyxform_detail_title.setText("设备周报");
            dateKey = "yearmonth";
            weekKey = "week";
            sbyxform_detail_date.setText(DateUtils.getStringDate() + "-" + "1");
            //初始化数据
            getYearCardData(sbyxform_detail_date);
            getMonthCardData(sbyxform_detail_date);
            getWeekCardData();
        } else if (type.equals(Form.SBYXMONTH)) {
            sbyxform_detail_title.setText("设备月报");
            dateKey = "month";
            sbyxform_detail_date.setText(DateUtils.getStringDate());
        }

        initRefresh();
        MonitorEditext();
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    @OnClick(R.id.sbyxform_detail_date)
    void DateChoose() {
        if (type.equals(Form.SBYXDAY)) {
            //初始化年月日选择器
            initDayPicker(this, sbyxform_detail_date, refreshLayout);
        } else if (type.equals(Form.SBYXWEEK)) {
            initWeekPicker(this, sbyxform_detail_date, refreshLayout);
        } else if (type.equals(Form.SBYXMONTH)) {
            initMonthPicker(this, sbyxform_detail_date, refreshLayout);
        }
    }

    /**
     * 监听搜索框
     */
    private void MonitorEditext() {
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, count + "----");
                if (mDatas != null) {
                    if (search_edittext.length() > 0) {
                        refreshLayout.setEnableRefresh(false);
                        refreshLayout.setEnableLoadmore(false);
                        search_clear.setVisibility(View.VISIBLE);
                        sbyxform_detail_date.setEnabled(false);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
                        sbyxform_detail_date.setEnabled(true);
                        search_clear.setVisibility(View.GONE);
                        if (adapter != null) {
                            adapter.setNewData(mDatas);
                        }
                    }
                } else {
                    if (search_edittext.length() > 0) {
                        search_clear.setVisibility(View.VISIBLE);
                    } else {
                        search_clear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索框
    private void search(String str) {
        if (mDatas != null) {
            searchDatas = new ArrayList<SbyxFormBean.DataBean>();
            for (SbyxFormBean.DataBean entity : mDatas) {
//                try {
//                    switch (entity.getItemType()) {
//                        case SbyxFormBean.LAYOUT1:
//                            SbyxFormBean.CDM30 bean1 = (SbyxFormBean.CDM30) entity.getData();
//                            if (bean1.getEquipment().contains(str) || bean1.getRunningTime().contains(str) || bean1.getFailureTime().contains(str) || bean1.getPerforationNum().contains(str) ||
//                                    bean1.getOilConsume().contains(str) || bean1.getIntactRate().contains(str) || bean1.getWorkingHours().contains(str) || bean1.getDieselConsume().contains(str)) {
//                                searchDatas.add(entity);
//                            }
//                            break;
//                        case SbyxFormBean.LAYOUT2:
//                            SbyxFormBean.No13305 bean2 = (SbyxFormBean.No13305) entity.getData();
//                            if (bean2.getEquipment().contains(str) || bean2.getRunningTime().contains(str) || bean2.getFailureTime().contains(str) || bean2.getPerforationNum().contains(str) ||
//                                    bean2.getOilConsume().contains(str) || bean2.getIntactRate().contains(str) || bean2.getWorkingHours().contains(str) || bean2.getYield().contains(str) ||
//                                    bean2.getClayYield().contains(str) || bean2.getDieselConsume().contains(str)) {
//                                searchDatas.add(entity);
//                            }
//                            break;
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                try {
                    if (entity.getDevname().contains(str) || entity.getTime().contains(str) || entity.getFault().contains(str) || entity.getPerforation().contains(str) || entity.getYield().contains(str)
                            || entity.getConsume().contains(str) || entity.getClay().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
    }

    /**
     * 清除搜索框内容
     */
    @OnClick(R.id.search_clear)
    public void ClearSearch() {
        search_edittext.setText("");
        search_clear.setVisibility(View.GONE);
    }


    private void initRefresh() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新
        }

        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                sbyxform_detail_date.setEnabled(false);
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
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
                    if (type.equals(Form.SBYXWEEK)) {
                        String yearmonth = sbyxform_detail_date.getText().toString().substring(0, 7);
                        String week = sbyxform_detail_date.getText().toString().substring(8);
                        setRecyclerView(dateKey, yearmonth, weekKey, week);
                    } else {
                        setRecyclerView(dateKey, sbyxform_detail_date.getText().toString(), weekKey, "");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView(String dateKey, String date, String weekKey, String weekdate) {
//        headView = View.inflate(this, R.layout.sbyxform_head, null);
//        TextView sbyxform_detail_type = (TextView) headView.findViewById(R.id.sbyxform_detail_type);
//        TextView sbyxform_detail_date = (TextView) headView.findViewById(R.id.sbyxform_detail_date);
//        TextView sbyxform_detail_runningTime = (TextView) headView.findViewById(R.id.sbyxform_detail_runningTime);
//        TextView sbyxform_detail_failureTime = (TextView) headView.findViewById(R.id.sbyxform_detail_failureTime);
//        TextView sbyxform_detail_perforationNum = (TextView) headView.findViewById(R.id.sbyxform_detail_perforationNum);
//        TextView sbyxform_detail_yield = (TextView) headView.findViewById(R.id.sbyxform_detail_yield);
//        TextView sbyxform_detail_oilConsume = (TextView) headView.findViewById(R.id.sbyxform_detail_oilConsume);
//        if (type.equals(Form.SBYXDAY)) {
//            sbyxform_detail_type.setText("日设备情况");
//        } else if (type.equals(Form.SBYXWEEK)) {
//            sbyxform_detail_type.setText("周设备情况");
//        } else if (type.equals(Form.SBYXMONTH)) {
//            sbyxform_detail_type.setText("月设备情况");
//        }
//        sbyxform_detail_date.setText(DateUtils.getStringDateShort());
//        sbyxform_detail_runningTime.setText("运行时间总量");
//        sbyxform_detail_failureTime.setText("故障时间总量");
//        sbyxform_detail_perforationNum.setText("穿孔总量");
//        sbyxform_detail_yield.setText("产量总量");
//        sbyxform_detail_oilConsume.setText("柴油消耗总量");
//
//
//        mDatas = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            switch (i % 2) {
//                case 0:
//                    SbyxFormBean.CDM30 data1 = new SbyxFormBean.CDM30();
//                    data1.setId(i);
//                    data1.setEquipment("设备" + i);
//                    data1.setRunningTime("运行" + i);
//                    data1.setFailureTime("故障" + i);
//                    data1.setPerforationNum("穿孔" + i);
//                    data1.setOilConsume("柴油消耗" + i);
//                    data1.setIntactRate("完好率" + i);
//                    data1.setWorkingHours("台时" + i);
//                    data1.setDieselConsume("柴油单耗" + i);
//                    mDatas.add(new SbyxFormBean(SbyxFormBean.LAYOUT1, data1));
//                    break;
//                case 1:
//                    SbyxFormBean.No13305 data2 = new SbyxFormBean.No13305();
//                    data2.setId(i);
//                    data2.setEquipment("设备" + i);
//                    data2.setRunningTime("运行" + i);
//                    data2.setFailureTime("故障" + i);
//                    data2.setPerforationNum("穿孔" + i);
//                    data2.setOilConsume("柴油消耗" + i);
//                    data2.setIntactRate("完好率" + i);
//                    data2.setWorkingHours("台时" + i);
//                    data2.setYield("产量" + i);
//                    data2.setClayYield("粘土产量" + i);
//                    data2.setDieselConsume("柴油单耗" + i);
//                    mDatas.add(new SbyxFormBean(SbyxFormBean.LAYOUT2, data2));
//                    break;
//            }
//        }
//
//
//        if (adapter == null) {
//            adapter = new SbyxFormAdapter(mDatas);
//            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//            adapter.isFirstOnly(false);
//            adapter.addHeaderView(headView);
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                }
//            });
//        } else {
//            adapter.setNewData(mDatas);
//        }
        OkGo.<String>get(PortIpAddress.countDevice())
                .params(PortIpAddress.TOKEN_KEY, PortIpAddress.getToken(this))
                .params(PortIpAddress.USERID_KEY, PortIpAddress.getUserId(this))
                .params(weekKey, weekdate)
                .params(dateKey, date)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                TextView sbyxform_detail_type = (TextView) headView.findViewById(R.id.sbyxform_detail_type);
                                TextView sbyxform_detail_time = (TextView) headView.findViewById(R.id.sbyxform_detail_time);
                                TextView sbyxform_detail_fault = (TextView) headView.findViewById(R.id.sbyxform_detail_fault);
                                TextView sbyxform_detail_perforation = (TextView) headView.findViewById(R.id.sbyxform_detail_perforation);
                                TextView sbyxform_detail_psjcl = (TextView) headView.findViewById(R.id.sbyxform_detail_psjcl);
                                TextView sbyxform_detail_yield = (TextView) headView.findViewById(R.id.sbyxform_detail_yield);
                                TextView sbyxform_detail_consume = (TextView) headView.findViewById(R.id.sbyxform_detail_consume);
                                TextView sbyxform_detail_nt = (TextView) headView.findViewById(R.id.sbyxform_detail_nt);

                                if (type.equals(Form.SBYXDAY)) {
                                    sbyxform_detail_type.setText("日设备情况");
                                } else if (type.equals(Form.SBYXWEEK)) {
                                    sbyxform_detail_type.setText("周设备情况");
                                } else if (type.equals(Form.SBYXMONTH)) {
                                    sbyxform_detail_type.setText("月设备情况");
                                }

                                mDatas.clear();
                                sbyxform_detail_time.setText("运行时间：" + jsonObject.getString("yxsj"));
                                sbyxform_detail_yield.setText("其他设备产量：" + jsonObject.getString("qtsbcl"));
                                sbyxform_detail_fault.setText("故障时间：" + jsonObject.getString("gzsj"));
                                sbyxform_detail_perforation.setText("穿孔米数：" + jsonObject.getString("ckms"));
                                sbyxform_detail_psjcl.setText("破碎机产量：" + jsonObject.getString("psjcl"));
                                sbyxform_detail_consume.setText("柴油消耗：" + jsonObject.getString("cyxh"));
                                sbyxform_detail_nt.setText("黏土：" + jsonObject.getString("nt"));

                                for (int i = 0; i < data.length(); i++) {
                                    SbyxFormBean.DataBean bean = new SbyxFormBean.DataBean();
                                    bean.setDevid("设备编号：" + data.optJSONObject(i).getString("deviceid"));
//                                    bean.setDevname(data.optJSONObject(i).getString("devname"));
                                    bean.setTime("运行时间：" + data.optJSONObject(i).getString("yxsj"));
                                    bean.setFault("故障时间：" + data.optJSONObject(i).getString("gzsj"));
                                    bean.setPerforation("穿孔米数：" + data.optJSONObject(i).getString("ckms"));
                                    bean.setYield("其他设备产量：" + data.optJSONObject(i).getString("qtsbcl"));
                                    bean.setPsjcl("破碎机产量：" + data.optJSONObject(i).getString("psjcl"));
                                    bean.setConsume("柴油消耗：" + data.optJSONObject(i).getString("cyxh"));
                                    bean.setClay("黏土：" + data.optJSONObject(i).getString("nt"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new SbyxFormAdapter(R.layout.sbyxdetail_item, mDatas);
                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                    adapter.isFirstOnly(false);
                                    adapter.addHeaderView(headView);
                                    recyclerView.setAdapter(adapter);

                                } else {
                                    adapter.setNewData(mDatas);
                                }

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }
                            } else {
                                ShowToast.showShort(SbyxFormDetail.this, err);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(SbyxFormDetail.this, R.string.connect_err);
                    }
                });
        refreshLayout.finishRefresh();
        sbyxform_detail_date.setEnabled(true);
    }


}
