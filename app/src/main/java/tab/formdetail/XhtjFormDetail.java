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

import adpter.XhtjFormAdapter;
import bean.XhtjFormBean;
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
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class XhtjFormDetail extends BaseActivity {
    @BindView(R.id.xhtjform_detail_title)
    TextView xhtjform_detail_title;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.xhtjform_detail_date)
    TextView xhtjform_detail_date;
    //    @BindView(R.id.weektv)
//    TextView weektv;
    private List<XhtjFormBean.DataBean> searchDatas;
    private List<XhtjFormBean.DataBean> mDatas;
    private XhtjFormAdapter adapter;
    private String type;
    private String url = "";
    private String dateKey = "";
    private String weekKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xhtj_form_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        isFirstEnter = true;
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        mDatas = new ArrayList<>();

        if (type.equals(Form.XHTJDAY)) {
            xhtjform_detail_title.setText("消耗日报");
            dateKey = "countdate";
            url = PortIpAddress.ConsumeByDay();
            //初始化时间 选择器
            xhtjform_detail_date.setText(DateUtils.dateToStr(DateUtils.getNow()));
        } else if (type.equals(Form.XHTJWEEK)) {
            xhtjform_detail_title.setText("消耗周报");
            dateKey = "yearmonth";
            weekKey = "week";
            url = PortIpAddress.ConsumeByWeek();
            xhtjform_detail_date.setText(DateUtils.getStringDate() + "-" + "1");
            //初始化数据
            getYearCardData(xhtjform_detail_date);
            getMonthCardData(xhtjform_detail_date);
            getWeekCardData();
        } else if (type.equals(Form.XHTJMONTH)) {
            xhtjform_detail_title.setText("消耗月报");
            dateKey = "month";
            url = PortIpAddress.ConsumeByMonth();
            xhtjform_detail_date.setText(DateUtils.getStringDate());
        }
        initRefresh();
        MonitorEditext();
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    @OnClick(R.id.xhtjform_detail_date)
    void DateChoose() {
        if (type.equals(Form.XHTJDAY)) {
            //初始化年月日选择器
            initDayPicker(this, xhtjform_detail_date, refreshLayout);
        } else if (type.equals(Form.XHTJWEEK)) {
            initWeekPicker(this, xhtjform_detail_date, refreshLayout);
        } else if (type.equals(Form.XHTJMONTH)) {
            initMonthPicker(this, xhtjform_detail_date, refreshLayout);
        }
    }


    //初始化数据
//    private void getYearCardData() {
//        for (int i = 0; i < 40; i++) {
//            ++initYear;
//            String str = String.valueOf(initYear);
//            dateItem.add(new DateBean(i, str));
//            String st = xhtjform_detail_date.getText().toString();
//
//            if (str.equals(st.split("-")[0])) {
//                yindex = i;
//            }
//        }
//    }
//
//    private void getWeekCardData() {
//        for (int i = 0; i < 40; i++) {
//            ArrayList<ArrayList<String>> wdateItemArr = new ArrayList<>();
//
//            for (int j = 0; j < 12; j++) {
//                ArrayList<String> listItem = new ArrayList<>();
//
//                int year = Integer.parseInt(dateItem.get(i).getPickerViewText());
//                int month = Integer.parseInt(mdateItem.get(i).get(j));
//
//                for (int k = 1; k <= DateUtils.getWeekNum(year, month); k++) {
//                    listItem.add(k + "");
//                }
//                wdateItemArr.add(listItem);
//            }
//            wdateItem.add(wdateItemArr);
//        }
//
//    }
//
//    private void getMonthCardData() {
//        String st = xhtjform_detail_date.getText().toString();
//
//        for (int i = 0; i < 40; i++) {
//            ArrayList<String> mdateItemArr = new ArrayList<>();
//            for (int j = 1; j <= 12; j++) {
////            mdateItem.add(String.valueOf(index));
//                mdateItemArr.add(String.valueOf(j));
//            }
//
//            for (int k = 0; k < mdateItemArr.size(); k++) {
//                //判断我当前月在集合的第几位
//                if (Integer.parseInt(mdateItemArr.get(k)) == Integer.parseInt(st.split("-")[1])) {
//                    mindex = k;
//                }
//            }
//            mdateItem.add(mdateItemArr);
//        }
//    }
//
//
//    //初始化日报选择器
//    private void initDayPicker() {
//        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
//        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(2006, 0, 0);
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2026, 12, 31);
//        //时间选择器
//        dayOption = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {//选中事件回调
//                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
//                /*btn_Time.setText(getTime(date));*/
//                xhtjform_detail_date.setText(getTime(date));
//                refreshLayout.autoRefresh();
//            }
//        })
//                //年月日时分秒 的显示与否，不设置则默认全部显示
//                .setType(new boolean[]{true, true, true, false, false, false})
//                .setLabel("年", "月", "日", "", "", "")
//                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(21)
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
//                .setDecorView(null)
//                .build();
//    }
//
//    //初始化周报选择器
//    private void initWeekPicker() {
//        weekOption = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                String str1 = dateItem.get(options1).getPickerViewText();
//                String str2 = mdateItem.get(options1).get(options2);
//                String str3 = wdateItem.get(options1).get(options2).get(options3);
//                int weeknum = DateUtils.getWeekNum(Integer.parseInt(str1), Integer.parseInt(str2));
//                if (Integer.parseInt(str2) < 10) {
//                    str2 = "0" + str2;
//                }
//                yindex = options1;
//                mindex = options2;
//                xhtjform_detail_date.setText(str1 + "-" + str2 + "-" + str3);
//            }
//        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
//            @Override
//            public void customLayout(View v) {
//                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
////              final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
//                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
//                tvSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        weekOption.returnData();
//                        weekOption.dismiss();
//                        refreshLayout.autoRefresh();
//                    }
//                });
//
//                ivCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        weekOption.dismiss();
//                    }
//                });
//            }
//        })
//                .setLabels("年", "月", "周")
//                .setSelectOptions(yindex, mindex)
//                .build();
//        weekOption.setPicker(dateItem, mdateItem, wdateItem);
//    }
//
//    //初始化月报选择器
//    private void initMonthPicker() {
//        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
//        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//
//        startDate.set(2006, 0, 0);
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2026, 11, 0);
//        //时间选择器
//        monthOption = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {//选中事件回调
//                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
//                /*btn_Time.setText(getTime(date));*/
//                xhtjform_detail_date.setText(getTime(date));
//                refreshLayout.autoRefresh();
//            }
//        })
//                //年月日时分秒 的显示与否，不设置则默认全部显示
//                .setType(new boolean[]{true, true, false, false, false, false})
//                .setLabel("年", "月", "", "", "", "")
//                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(21)
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
//                .setDecorView(null)
//                .build();
//    }
//
//    private String getTime(Date date) {
//        //可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//        return format.format(date);
//    }
//
//    /**
//     * 日报dialog
//     */
//    private void DayDialog() {
//        if (dayOption != null) {
//            dayOption.show();
//        }
//    }
//
//    /**
//     * 周报dialog
//     */
//    private void WeekDialog() {
//        if (weekOption != null) {
//            weekOption.show();
//        }
//    }
//
//    /**
//     * 月报dialog
//     */
//    private void MonthDialog() {
//        if (monthOption != null) {
//            monthOption.show();
//        }
//    }


    /**
     * 监听搜索框
     */
    private void MonitorEditext() {
        searchDatas = new ArrayList<>();
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
                        xhtjform_detail_date.setEnabled(false);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
                        xhtjform_detail_date.setEnabled(true);
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
            searchDatas.clear();
            for (XhtjFormBean.DataBean entity : mDatas) {
                try {
                    if (entity.getMaterial().contains(str) || entity.getPurchasecount().contains(str) || entity.getConsume().contains(str) || entity.getStore().contains(str)) {
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
                xhtjform_detail_date.setEnabled(false);
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
//                    ShowToast.showShort(XhtjFormDetail.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(XhtjFormDetail.this, R.string.refreshSuccess);
                    setRecyclerView();
                    xhtjform_detail_date.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
//        for (int i = 0; i < 20; i++) {
//            XhtjFormBean bean = new XhtjFormBean();
//            bean.setId(i);
//            bean.setTypeName("类型" + i);
//            bean.setYearStock("年库存" + i);
//            bean.setMonthStock("月库存" + i);
//            bean.setDayStock("日实际" + i);
//            mDatas.add(bean);
//        }
//
//        if (adapter == null) {
//            adapter = new XhtjFormAdapter(R.layout.xhtjform_detail_item, mDatas);
//            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//            adapter.isFirstOnly(false);
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
        if (type.equals(Form.XHTJWEEK)) {
            String yearmonth = xhtjform_detail_date.getText().toString().substring(0, 7);
            String week = xhtjform_detail_date.getText().toString().substring(8);
            mConnect(url, dateKey, yearmonth, weekKey, week);
        } else {
            mConnect(url, dateKey, xhtjform_detail_date.getText().toString(), weekKey, "");
        }

    }


    /**
     * 消耗统计
     */
    private void mConnect(String url, String datekey, String date, String weekKey, String week) {
        OkGo.<String>get(url)
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params(datekey, date)
                .params(weekKey, week)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    XhtjFormBean.DataBean bean = new XhtjFormBean.DataBean();
                                    bean.setMaterial(data.optJSONObject(i).getString("Material"));
                                    bean.setPurchasecount(data.optJSONObject(i).getString("purchasecount"));
                                    bean.setConsume(data.optJSONObject(i).getString("consume"));

                                    if (type.equals(Form.XHTJDAY)) {
                                        bean.setStore(data.optJSONObject(i).getString("store"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new XhtjFormAdapter(R.layout.xhtjform_detail_item, mDatas, type);
                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                    adapter.isFirstOnly(false);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }


                            } else {
                                ShowToast.showShort(XhtjFormDetail.this, err);
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
                        ShowToast.showShort(XhtjFormDetail.this, R.string.connect_err);
                    }
                });
    }


    private void mConnect(String url, String yearmonth, String week) {
        OkGo.<String>get(url)
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("yearmonth", yearmonth)
                .params("week", week)
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
                                    XhtjFormBean.DataBean bean = new XhtjFormBean.DataBean();
                                    bean.setMaterial(jsonArray.optJSONObject(i).getString("Material"));
                                    bean.setPurchasecount(jsonArray.optJSONObject(i).getString("purchasecount"));
                                    bean.setConsume(jsonArray.optJSONObject(i).getString("consume"));

                                    if (type.equals(Form.XHTJDAY)) {
                                        bean.setStore(jsonArray.optJSONObject(i).getString("store"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new XhtjFormAdapter(R.layout.xhtjform_detail_item, mDatas, type);
                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                    adapter.isFirstOnly(false);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }
                            } else {
                                ShowToast.showShort(XhtjFormDetail.this, err);
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
                        ShowToast.showShort(XhtjFormDetail.this, R.string.connect_err);
                    }
                });
    }

}
