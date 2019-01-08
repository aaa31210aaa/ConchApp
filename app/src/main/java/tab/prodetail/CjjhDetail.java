package tab.prodetail;

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

import adpter.CjPlanDetailAdapter;
import bean.CjPlanBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.MyData;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.CustomDatePicker.getQCardData;
import static utils.CustomDatePicker.getYearCardData;
import static utils.CustomDatePicker.initMonthPicker;
import static utils.CustomDatePicker.initQuarterPicker;
import static utils.CustomDatePicker.initYearPicker;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class CjjhDetail extends BaseActivity {
    @BindView(R.id.cjjhdetail_title)
    TextView cjjhdetail_title;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.cjjh_detail_date)
    TextView cjjh_detail_date;
    //    @BindView(R.id.cjjh_detail_before)
//    ImageButton cjjh_detail_before;
//    @BindView(R.id.cjjh_detail_next)
//    ImageButton cjjh_detail_next;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<CjPlanBean> searchDatas;
    private List<CjPlanBean> mDatas;
    private CjPlanDetailAdapter adapter;
    private String techid;
    private String ProcID;
    private String value;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjjh_detail);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        Intent intent = getIntent();
        String titleName = "";


        //设置标题  初始化时间
        type = intent.getStringExtra("type");
        if (type.equals(MyData.YEAR)) {
            titleName = "年采掘计划";
            cjjh_detail_date.setText(DateUtils.mYear());
        } else if (type.equals(MyData.QUARTER)) {
            titleName = "季度采掘计划";
            String month = DateUtils.mMonth();
            String qt = "";
            if (month.equals("1") || month.equals("2") || month.equals("3")) {
                qt = "01";
            } else if (month.equals("4") || month.equals("5") || month.equals("6")) {
                qt = "02";
            } else if (month.equals("7") || month.equals("8") || month.equals("9")) {
                qt = "03";
            } else if (month.equals("10") || month.equals("11") || month.equals("12")) {
                qt = "04";
            }
            cjjh_detail_date.setText(DateUtils.mYear() + "-" + qt);
            //初始化年份数据
            getYearCardData(cjjh_detail_date);
            //初始化季度数据
            getQCardData(cjjh_detail_date);

        } else if (type.equals(MyData.MONTH)) {
            titleName = "月采掘计划";
            //年月格式展示
            cjjh_detail_date.setText(DateUtils.getStringDate());
        }

        cjjhdetail_title.setText(titleName);
        techid = intent.getStringExtra("techid");
        ProcID = intent.getStringExtra("procid");
        initRefresh();
        MonitorEditext();

    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

//    @OnClick(cjjh_detail_before)
//    void Before() {
//        int mYear = Integer.parseInt(cjjh_detail_date.getText().toString());
//        String str = String.valueOf(--mYear);
//        cjjh_detail_date.setText(str);
//        cjjh_detail_refresh.autoRefresh();
//    }
//
//    @OnClick(cjjh_detail_next)
//    void Next() {
//        int mYear = Integer.parseInt(cjjh_detail_date.getText().toString());
//        String str = String.valueOf(++mYear);
//        cjjh_detail_date.setText(str);
//        cjjh_detail_refresh.autoRefresh();
//    }

    /**
     * 清除搜索框内容
     */
    @OnClick(R.id.search_clear)
    public void ClearSearch() {
        search_edittext.setText("");
        search_clear.setVisibility(View.GONE);
    }

    /**
     * 弹出来选择时间
     */
    @OnClick(R.id.cjjh_detail_date)
    void DateChoose() {
        if (type.equals(MyData.YEAR)) {
            //初始化选择器
            initYearPicker(this, cjjh_detail_date, refreshLayout);
        } else if (type.equals(MyData.QUARTER)) {
//            QuarterDialog();
            //初始化选择器
            initQuarterPicker(this, cjjh_detail_date, refreshLayout);
        } else if (type.equals(MyData.MONTH)) {
//            MonthDialog();
            //初始化选择器
            initMonthPicker(this, cjjh_detail_date, refreshLayout);
        }
    }


    /**
     * 月选择
     */
    private void MonthDialog() {
        ShowToast.showShort(this, "月份");
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
                if (mDatas != null) {
                    if (search_edittext.length() > 0) {
                        refreshLayout.setEnableRefresh(false);
                        refreshLayout.setEnableLoadmore(false);
//                        cjjh_detail_before.setEnabled(false);
//                        cjjh_detail_next.setEnabled(false);
                        cjjh_detail_date.setEnabled(false);
                        search_clear.setVisibility(View.VISIBLE);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
//                        cjjh_detail_before.setEnabled(true);
//                        cjjh_detail_next.setEnabled(true);
                        cjjh_detail_date.setEnabled(true);
                        search_clear.setVisibility(View.GONE);
                        if (adapter!=null){
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
            searchDatas = new ArrayList<CjPlanBean>();
            for (CjPlanBean entity : mDatas) {
                try {
                    if (entity.getPlace().contains(str) || entity.getName1().contains(str) || entity.getName2().contains(str) || entity.getName3().contains(str)
                            || entity.getName4().contains(str) || entity.getValue1().contains(str) || entity.getValue2().contains(str) || entity.getValue3().contains(str)
                            || entity.getValue4().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
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
                cjjh_detail_date.setEnabled(false);
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
//                    ShowToast.showShort(CjjhDetail.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(CjjhDetail.this, R.string.refreshSuccess);
                    setRecyclerView();
                    cjjh_detail_date.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
        if (type.equals(MyData.QUARTER)) {
            String intentYear = cjjh_detail_date.getText().toString().split("-")[0];
            String intentPlanstage = cjjh_detail_date.getText().toString().split("-")[1];
            mConnect(intentYear, intentPlanstage);
        } else {
            mConnect(cjjh_detail_date.getText().toString());
        }
    }

    private void mConnect(String date) {
        OkGo.<String>get(PortIpAddress.PlanDetail())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("techid", techid)
                .params("procid", ProcID)
                .params("yearvalue", date)
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
                                    CjPlanBean bean = new CjPlanBean();
                                    bean.setPlace(jsonArray.optJSONObject(i).getString("projectname"));
                                    bean.setName1(jsonArray.optJSONObject(i).getString("name1"));
                                    bean.setName2(jsonArray.optJSONObject(i).getString("name2"));
                                    bean.setName3(jsonArray.optJSONObject(i).getString("name3"));
                                    bean.setName4(jsonArray.optJSONObject(i).getString("name4"));
                                    bean.setValue1(jsonArray.optJSONObject(i).getString("value1"));
                                    bean.setValue2(jsonArray.optJSONObject(i).getString("value2"));
                                    bean.setValue3(jsonArray.optJSONObject(i).getString("value3"));
                                    bean.setValue4(jsonArray.optJSONObject(i).getString("value4"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new CjPlanDetailAdapter(R.layout.cjjhdetail_item, mDatas);
//                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//                                    adapter.isFirstOnly(false);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }
                            } else {
                                ShowToast.showShort(CjjhDetail.this, err);
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
                        ShowToast.showShort(CjjhDetail.this, R.string.connect_err);
                    }
                });
    }

    private void mConnect(String year, String planstage) {
        OkGo.<String>get(PortIpAddress.PlanDetail())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("techid", techid)
                .params("procid", ProcID)
                .params("yearvalue", year)
                .params("planstage", planstage)
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
                                    CjPlanBean bean = new CjPlanBean();
                                    bean.setPlace(jsonArray.optJSONObject(i).getString("projectname"));
                                    bean.setName1(jsonArray.optJSONObject(i).getString("name1"));
                                    bean.setName2(jsonArray.optJSONObject(i).getString("name2"));
                                    bean.setName3(jsonArray.optJSONObject(i).getString("name3"));
                                    bean.setName4(jsonArray.optJSONObject(i).getString("name4"));
                                    bean.setValue1(jsonArray.optJSONObject(i).getString("value1"));
                                    bean.setValue2(jsonArray.optJSONObject(i).getString("value2"));
                                    bean.setValue3(jsonArray.optJSONObject(i).getString("value3"));
                                    bean.setValue4(jsonArray.optJSONObject(i).getString("value4"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new CjPlanDetailAdapter(R.layout.cjjhdetail_item, mDatas);
                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                    adapter.isFirstOnly(false);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }
                            } else {
                                ShowToast.showShort(CjjhDetail.this, err);
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
                        ShowToast.showShort(CjjhDetail.this, R.string.connect_err);
                    }
                });
    }

}
