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

import adpter.ZlphDetailAdapter;
import bean.ZlphDetailBean;
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

public class ZlphDetail extends BaseActivity {
    //    @BindView(R.id.zlph_detail_indicator)
//    MagicIndicator zlph_detail_indicator;
//    @BindView(R.id.zlph_detail_viewpager)
//    ViewPager zlph_detail_viewpager;
//    private static final String[] CHANNELS = new String[]{"矿石", "夹石及断层带"};
//    private List<String> mDataList = Arrays.asList(CHANNELS);
    @BindView(R.id.zlph_detail_title)
    TextView zlph_detail_title;
    @BindView(R.id.zlph_detail_date)
    TextView zlph_detail_date;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<ZlphDetailBean> searchDatas;
    private List<ZlphDetailBean> mDatas;
    private ZlphDetailAdapter adapter;
    private String type;
    private String techid;
    private String ProcID;
//    //年选择器
//    private OptionsPickerView pvCustomOptions;
//    //季度选择器
//    private OptionsPickerView qOptions;
//    //月选择器
//    private OptionsPickerView mOptions;
//
//    private ArrayList<DateBean> dateItem = new ArrayList<>();
//    private ArrayList<String> qdateItem = new ArrayList<>();
//    private ArrayList<String> mdateItem = new ArrayList<>();
//
    private List<ZlphDetailBean.Content> indexList;
//    private int yindex = 0;
//    private int qindex = 0;
//    private int mindex = 0;
//    private int initYear = 2006;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zlph_detail);
        //绑定fragment
        ButterKnife.bind(this);
        isFirstEnter = true;
        initData();
    }

    @Override
    protected void initData() {
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new MineralZlph());
//        fragments.add(new StoneAndFault());
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
//        zlph_detail_viewpager.setAdapter(adapter);
//        initMagicIndicator();
        Intent intent = getIntent();
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        String titleName = "";

        //设置标题  初始化时间
        type = intent.getStringExtra("type");
        if (type.equals(MyData.YEAR)) {
            titleName = "年计划质量平衡";
            zlph_detail_date.setText(DateUtils.mYear());
            //初始化年份数据
            getYearCardData(zlph_detail_date);
        } else if (type.equals(MyData.QUARTER)) {
            titleName = "季度计划质量平衡";
            String month = DateUtils.mMonth();
            String qt = "";
            if (month.equals("1") || month.equals("2") || month.equals("3")) {
                qt = "1";
            } else if (month.equals("4") || month.equals("5") || month.equals("6")) {
                qt = "2";
            } else if (month.equals("7") || month.equals("8") || month.equals("9")) {
                qt = "3";
            } else if (month.equals("10") || month.equals("11") || month.equals("12")) {
                qt = "4";
            }
            zlph_detail_date.setText(DateUtils.mYear() + "-" + qt);
            //初始化年份数据
            getYearCardData(zlph_detail_date);
            //初始化季度数据
            getQCardData(zlph_detail_date);
        } else if (type.equals(MyData.MONTH)) {
            titleName = "月计划质量平衡";
            //年月格式展示
            zlph_detail_date.setText(DateUtils.getStringDate());
        }

        zlph_detail_title.setText(titleName);
        techid = intent.getStringExtra("techid");
        ProcID = intent.getStringExtra("procid");
        initRefresh();
        MonitorEditext();

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
                        search_clear.setVisibility(View.VISIBLE);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
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
            searchDatas = new ArrayList<ZlphDetailBean>();
            for (ZlphDetailBean bean : mDatas) {
                try {
                    if (bean.getPlace().contains(str)) {
                        searchDatas.add(bean);
                    }

                    for (ZlphDetailBean.Content entity : bean.getList()) {
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
     * 清空搜索
     */
    @OnClick(R.id.search_clear)
    void ClearSearch() {
        search_edittext.setText("");
        search_clear.setVisibility(View.GONE);
    }


    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }


    /**
     * 弹出来选择时间
     */
    @OnClick(R.id.zlph_detail_date)
    void DateChoose() {
        if (type.equals(MyData.YEAR)) {
            //初始化选择器
            initYearPicker(this, zlph_detail_date, refreshLayout);
        } else if (type.equals(MyData.QUARTER)) {
            //初始化选择器
            initQuarterPicker(this, zlph_detail_date, refreshLayout);
        } else if (type.equals(MyData.MONTH)) {
            //初始化选择器
            initMonthPicker(this, zlph_detail_date, refreshLayout);
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
                zlph_detail_date.setEnabled(false);
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
//                    ShowToast.showShort(ZlphDetail.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(ZlphDetail.this, R.string.refreshSuccess);
                    setRecyclerView();
                    zlph_detail_date.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
        if (type.equals(MyData.QUARTER)) {
            String intentYear = zlph_detail_date.getText().toString().split("-")[0];
            String intentPlanstage = zlph_detail_date.getText().toString().split("-")[1];
            mConnect(intentYear, intentPlanstage);
        } else {
            mConnect(zlph_detail_date.getText().toString());
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
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);
                            mDatas.clear();
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < data.length(); i++) {
                                    indexList = new ArrayList<ZlphDetailBean.Content>();
                                    ZlphDetailBean bean = new ZlphDetailBean();
                                    bean.setPlace(data.optJSONObject(i).getString("projectname"));

                                    JSONArray index = data.optJSONObject(i).getJSONArray("index");
                                    for (int j = 0; j < index.length(); j++) {
                                        ZlphDetailBean.Content content = new ZlphDetailBean.Content();
                                        content.setName(index.optJSONObject(j).getString("name"));
                                        content.setValue(index.optJSONObject(j).getString("value"));
                                        if (!index.optJSONObject(j).getString("name").contains("矿种")){
                                            indexList.add(content);
                                        }
                                    }
                                    bean.setList(indexList);
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new ZlphDetailAdapter(ZlphDetail.this, R.layout.kcjhdetail_item, mDatas);
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
                                ShowToast.showShort(ZlphDetail.this, err);
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
                        ShowToast.showShort(ZlphDetail.this, R.string.connect_err);
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
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);
                            mDatas.clear();
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < data.length(); i++) {
                                    indexList = new ArrayList<ZlphDetailBean.Content>();
                                    ZlphDetailBean bean = new ZlphDetailBean();
                                    bean.setPlace(data.optJSONObject(i).getString("projectname"));

                                    JSONArray index = data.optJSONObject(i).getJSONArray("index");
                                    for (int j = 0; j < index.length(); j++) {
                                        ZlphDetailBean.Content content = new ZlphDetailBean.Content();
                                        content.setName(index.optJSONObject(j).getString("name"));
                                        content.setValue(index.optJSONObject(j).getString("value"));
                                        indexList.add(content);
                                    }
                                    bean.setList(indexList);
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new ZlphDetailAdapter(ZlphDetail.this, R.layout.kcjhdetail_item, mDatas);
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
                                ShowToast.showShort(ZlphDetail.this, err);
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
                        ShowToast.showShort(ZlphDetail.this, R.string.connect_err);
                    }
                });
    }


    /**
     * 设置tablayout
     */
//    private void initMagicIndicator() {
//        zlph_detail_indicator.setBackgroundColor(Color.parseColor("#455a64"));
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mDataList == null ? 0 : mDataList.size();
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.WHITE);
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        zlph_detail_viewpager.setCurrentItem(index);
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
////                indicator.setLineHeight(UIUtil.dip2px(context, 6));
////                indicator.setLineWidth(UIUtil.dip2px(context, 10));
////                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
////                indicator.setStartInterpolator(new AccelerateInterpolator());
////                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//                indicator.setColors(Color.parseColor("#ff4081"));
//                return indicator;
//            }
//
//
//            /**
//             * 可以改变某个tab的长度
//             * @param context
//             * @param index
//             * @return
//             */
////            @Override
////            public float getTitleWeight(Context context, int index) {
////                if (index == 0) {
////                    return 2.0f;
////                } else if (index == 1) {
////                    return 1.2f;
////                } else {
////                    return 1.0f;
////                }
////            }
//        });
//        zlph_detail_indicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(zlph_detail_indicator, zlph_detail_viewpager);
//    }
}
