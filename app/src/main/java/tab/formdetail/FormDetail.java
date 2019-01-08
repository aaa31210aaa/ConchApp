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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import adpter.SctjFormAdapter;
import bean.SctjFormBean;
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

public class FormDetail extends BaseActivity {
    @BindView(R.id.form_detail_title)
    TextView form_detail_title;
    @BindView(R.id.form_detail_date)
    TextView form_detail_date;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<SctjFormBean.DataBean> searchDatas;
    private List<SctjFormBean.DataBean> mDatas;
    private SctjFormAdapter adapter;
    private View headView;
    private String type;
    private String url;
    private List<SctjFormBean.DataBean.IndexBean> indexList;
    private String dateKey = "";
    private String weekKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        //初始化时间 选择器
        if (type.equals(Form.SCTJDAY)) {
            form_detail_title.setText("生产日报");
            dateKey = "day";
            form_detail_date.setText(DateUtils.dateToStr(DateUtils.getNow()));
        } else if (type.equals(Form.SCTJWEEK)) {
            form_detail_title.setText("生产周报");
            dateKey = "yearmonth";
            weekKey = "week";
            form_detail_date.setText(DateUtils.getStringDate() + "-" + "1");
            //初始化数据
            getYearCardData(form_detail_date);
            getMonthCardData(form_detail_date);
            getWeekCardData();
        } else if (type.equals(Form.SCTJMONTH)) {
            form_detail_title.setText("生产月报");
            dateKey = "month";
            form_detail_date.setText(DateUtils.getStringDate());
        }

        initRefresh();
        MonitorEditext();
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 日期选择
     */
    @OnClick(R.id.form_detail_date)
    void DateChoose() {
        if (type.equals(Form.SCTJDAY)) {
            initDayPicker(this, form_detail_date, refreshLayout);
        } else if (type.equals(Form.SCTJWEEK)) {
            initWeekPicker(this, form_detail_date, refreshLayout);
        } else if (type.equals(Form.SCTJMONTH)) {
            initMonthPicker(this, form_detail_date, refreshLayout);
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
                        form_detail_date.setEnabled(false);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
                        form_detail_date.setEnabled(true);
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
            searchDatas = new ArrayList<SctjFormBean.DataBean>();
            for (SctjFormBean.DataBean bean : mDatas) {
                try {
                    if (bean.getProjectname().contains(str)) {
                        searchDatas.add(bean);
                    }
                    for (SctjFormBean.DataBean.IndexBean entity : bean.getIndex()) {
                        if (entity.getName().contains(str) || entity.getValue().contains(str)) {
                            searchDatas.add(bean);
                        }
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
                form_detail_date.setEnabled(false);
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
//                    ShowToast.showShort(FormDetail.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(FormDetail.this, R.string.refreshSuccess);
                    setRecyclerView();
                    form_detail_date.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
//        headView = View.inflate(this, R.layout.sctjform_head, null);
//        TextView sctjform_head_daycomplete = (TextView) headView.findViewById(R.id.sctjform_head_daycomplete);
//        TextView sctjform_head_date = (TextView) headView.findViewById(R.id.sctjform_head_date);
//        switch (type) {
//            case "sctjday":
//                sctjform_head_daycomplete.setText("日完成情况");
//                break;
//            case "sctjweek":
//                sctjform_head_daycomplete.setText("周完成情况");
//                break;
//            case "sctjmonth":
//                sctjform_head_daycomplete.setText("月完成情况");
//                break;
//        }
//        sctjform_head_date.setText(DateUtils.getStringDateShort());
//
//        mDatas = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            FormBean bean = new FormBean();
//            bean.setSection("路段" + i);
//            bean.setExplosionVolume("爆落量" + i);
//            bean.setOreOutput("出矿量" + i);
//            bean.setSaveOre("存矿量" + i);
//            bean.setTransitVolume("转场量" + i);
//            bean.setCalcium("高钙" + i);
//            bean.setSilicon("高硅" + i);
//            bean.setMagnesium("高镁" + i);
//            bean.setClayStone("泥夹石" + i);
//            mDatas.add(bean);
//        }
//
//
//        if (adapter == null) {
//            adapter = new SctjFormAdapter(R.layout.sctjform_item, mDatas);
//            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//            adapter.isFirstOnly(false);
//            adapter.addHeaderView(headView);
//            form_detail_rv.setAdapter(adapter);
//
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                }
//            });
//        } else {
//            adapter.setNewData(mDatas);
//        }
        if (type.equals(Form.SCTJWEEK)) {
            String yearmonth = form_detail_date.getText().toString().substring(0, 7);
            String week = form_detail_date.getText().toString().substring(8);
            mConnect(PortIpAddress.CountProduction(), dateKey, yearmonth, weekKey, week);
        } else {
            mConnect(PortIpAddress.CountProduction(), dateKey, form_detail_date.getText().toString(), weekKey, "");
        }
    }

    /**
     * 生产统计
     */
    private void mConnect(String url, String dateKey, String date, String weekKey, String weekdate) {
        OkGo.<String>get(url)
                .tag(this)
                .params(PortIpAddress.TOKEN_KEY, PortIpAddress.getToken(this))
                .params(PortIpAddress.USERID_KEY, PortIpAddress.getUserId(this))
                .params(weekKey, weekdate)
                .params(dateKey, date)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            JSONArray countindex = jsonObject.getJSONArray("countindex");
                            String err = jsonObject.getString(MESSAGE);
                            headView = View.inflate(FormDetail.this, R.layout.sctjform_head, null);
                            TextView sctjform_head_daycomplete = (TextView) headView.findViewById(R.id.sctjform_head_daycomplete);
                            LinearLayout sctjform_head_ll = (LinearLayout) headView.findViewById(R.id.sctjform_head_ll);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                sctjform_head_ll.removeAllViews();
                                if (type.equals(Form.SCTJDAY)) {
                                    sctjform_head_daycomplete.setText("日完成情况");
                                } else if (type.equals(Form.SCTJWEEK)) {
                                    sctjform_head_daycomplete.setText("周完成情况");
                                } else if (type.equals(Form.SCTJMONTH)) {
                                    sctjform_head_daycomplete.setText("月完成情况");
                                }

                                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams tvLps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);


                                for (int i = 0; i < countindex.length(); i++) {
                                    LinearLayout llh = new LinearLayout(FormDetail.this);
                                    llh.setLayoutParams(lps);
                                    llh.setOrientation(LinearLayout.HORIZONTAL);
                                    TextView titleTv = new TextView(FormDetail.this);
                                    titleTv.setLayoutParams(tvLps);
                                    titleTv.setGravity(Gravity.CENTER);
                                    titleTv.setBackgroundResource(R.drawable.editext_yb);
                                    String name = countindex.optJSONObject(i).getString("name");
                                    titleTv.setText(name);
                                    TextView contentTv = new TextView(FormDetail.this);
                                    contentTv.setLayoutParams(tvLps);
                                    contentTv.setGravity(Gravity.CENTER);
                                    contentTv.setBackgroundResource(R.drawable.editext_yb);
                                    String value = countindex.optJSONObject(i).getString("value");
                                    contentTv.setText(value);
                                    Log.e(TAG, name + "----" + value);
                                    if (type.equals(Form.SCTJDAY)) {
                                        llh.addView(contentTv);
                                        llh.addView(titleTv);
                                    } else {
                                        if (!name.equals("存矿量")) {
                                            llh.addView(titleTv);
                                            llh.addView(contentTv);
                                        }
                                    }
                                    sctjform_head_ll.addView(llh);
                                }


                                for (int j = 0; j < data.length(); j++) {
                                    indexList = new ArrayList<SctjFormBean.DataBean.IndexBean>();
                                    SctjFormBean.DataBean bean = new SctjFormBean.DataBean();
                                    bean.setProjectname(data.optJSONObject(j).getString("projectname"));
                                    JSONArray index = data.optJSONObject(j).getJSONArray("index");

                                    for (int k = 0; k < index.length(); k++) {
                                        SctjFormBean.DataBean.IndexBean indexbean = new SctjFormBean.DataBean.IndexBean();
                                        if (type.equals(Form.SCTJDAY)) {
                                            indexbean.setName(index.optJSONObject(k).getString("name"));
                                            indexbean.setValue(index.optJSONObject(k).getString("value"));
                                            indexList.add(indexbean);
                                        } else {
                                            if (!index.optJSONObject(k).getString("name").equals("存矿量")) {
                                                indexbean.setName(index.optJSONObject(k).getString("name"));
                                                indexbean.setValue(index.optJSONObject(k).getString("value"));
                                                indexList.add(indexbean);
                                            }
                                        }
                                    }
                                    bean.setIndex(indexList);
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new SctjFormAdapter(R.layout.kcjhdetail_item, mDatas);
                                    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                    adapter.isFirstOnly(false);
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
                            } else {
                                ShowToast.showShort(FormDetail.this, err);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(FormDetail.this, R.string.connect_err);
                    }
                });
        refreshLayout.finishRefresh();
    }


}
