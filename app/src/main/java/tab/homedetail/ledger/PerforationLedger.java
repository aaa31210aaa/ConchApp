package tab.homedetail.ledger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import butterknife.OnClick;
import utils.BaseActivity;
import utils.CustomDatePicker;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.ShowToast;

/**
 * 穿孔台账
 */
public class PerforationLedger extends BaseActivity {
    //    @BindView(R.id.perforation_viewpager)
//    ViewPager perforation_viewpager;
//    @BindView(R.id.perforation_tablayout)
//    TabLayout perforation_tablayout;
    private View view;

    @BindView(R.id.search_edittext)
    EditText search_edittext;

    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.production_date)
    TextView production_date;
    @BindView(R.id.shift)
    Spinner shift;
    private String select_spinner = "";


    private List<TodoBean> searchDatas;
    private List<TodoBean> mDatas;
    private ProductionLedgerAdapter adapter;

    private boolean isFirstEnter;
    private CustomDatePicker customDatePicker;
    public static int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perforation_ledger);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        //设置tab
//        SetTab();
//        headView = View.inflate(this, R.layout.production_ledger_headview, null);

        //获得当前日期
        production_date.setText(DateUtils.getStringDateShort());
        customDatePicker = new CustomDatePicker();
        production_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePicker.CustomDatePickDialog(PerforationLedger.this, production_date,refreshLayout);
            }
        });

        //班次
//        String[] shift_arr = getResources().getStringArray(R.array.shift_arr);
//        shift.setText(shift_arr[0]);
//        setPopWindow(shift, shift_arr);
        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] shift = getResources().getStringArray(R.array.shift_arr);
                select_spinner = shift[i];
                ShowToast.showShort(PerforationLedger.this, "选择的是" + select_spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        mDatas = new ArrayList<>();
        isFirstEnter = true;
        initRefresh();
        MonitorEditext();
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    @OnClick(R.id.add_fbt)
    void AddLedger() {
        startActivityForResult(new Intent(this, AddLedger.class), REQUEST_CODE);
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
                    ShowToast.showShort(PerforationLedger.this, R.string.loadSuccess);
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

        for (int i = 0; i < 10; i++) {
            TodoBean bean = new TodoBean();
            bean.setMattername("标题" + i);
            bean.setMemo("内容" + i);
            mDatas.add(bean);
        }

        if (adapter == null) {
            adapter = new ProductionLedgerAdapter(R.layout.notice_list_item, mDatas);
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.isFirstOnly(true);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setNewData(mDatas);
        }
        //如果无数据设置空布局
        if (mDatas.size() == 0) {
            adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
        }
        refreshLayout.finishRefresh();

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }


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
            for (TodoBean entity : mDatas) {
                try {
                    if (entity.getMattername().contains(str) || entity.getMemo().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setNewData(searchDatas);
            }
        }
    }


//    private void SetTab() {
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new ProductionLedger());
//        fragments.add(new EquipmentInfo());
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"生产台账", "设备信息"});
//
//        perforation_viewpager.setOffscreenPageLimit(1);
//
//        perforation_viewpager.setAdapter(adapter);
//        //关联图文
//        perforation_tablayout.setupWithViewPager(perforation_viewpager);
//        perforation_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                perforation_viewpager.setCurrentItem(tab.getPosition(), false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
}
