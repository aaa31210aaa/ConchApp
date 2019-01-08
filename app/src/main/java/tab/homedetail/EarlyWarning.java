package tab.homedetail;

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

import adpter.AlarmAdapter;
import bean.AlarmBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.homedetail.earlywarningdetail.AlarmDetail;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class EarlyWarning extends BaseActivity {
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<AlarmBean> searchDatas;
    private List<AlarmBean> mDatas;
    //    private EarlyWarningAdapter adapter;
    //    private WarningBean warningBean;
    private AlarmAdapter adapter;
    private AlarmBean alarmBean;

    private String ym = "EarlyWarning";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_early_warning);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
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
        Log.e(TAG, str);
        if (mDatas != null) {
            searchDatas.clear();
            for (AlarmBean entity : mDatas) {
//                switch (entity.getItemType()) {
//                    case WarningBean.LAYOUT1:
//                        WarningBean.ShsZb bean1 = (WarningBean.ShsZb) entity.getData();
//                        if (bean1.getEarningName().contains(str) || bean1.getXlshszb().contains(str) || bean1.getSjshszb().contains(str) || bean1.getPcl().contains(str)) {
//                            searchDatas.add(entity);
//                        }
//                        break;
//                    case WarningBean.LAYOUT2:
//                        WarningBean.KH bean2 = (WarningBean.KH) entity.getData();
//                        if (bean2.getEarningName().contains(str) || bean2.getSdzb().contains(str) || bean2.getSjzb().contains(str) || bean2.getPcl().contains(str)) {
//                            searchDatas.add(entity);
//                        }
//                        break;
//                    case WarningBean.LAYOUT3:
//                        WarningBean.MonthKl bean3 = (WarningBean.MonthKl) entity.getData();
//                        if (bean3.getEarningName().contains(str) || bean3.getYjhkl().contains(str) || bean3.getYjhckl().contains(str) || bean3.getYsjckl().contains(str) || bean3.getKlce().contains(str)) {
//                            searchDatas.add(entity);
//                        }
//                        break;
//                }
                try {
                    if (entity.getAlarmName().contains(str) || entity.getAddTime().contains(str) || entity.getHandleType().contains(str)
                            || entity.getHandlecontent().contains(str) || entity.getMemo().contains(str)) {
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
//                    ShowToast.showShort(EarlyWarning.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(EarlyWarning.this, R.string.refreshSuccess);
                    setRecyclerView();
                    break;
                default:
                    break;
            }
        }
    };


    private void setRecyclerView() {
        mConnect();
//        mDatas = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            switch (i % 3) {
//                case 0:
//                    WarningBean.ShsZb data1 = new WarningBean.ShsZb();
//                    data1.setId(i);
//                    data1.setEarningName("石灰石" + i);
//                    data1.setXlshszb("下料石灰石指标" + i);
//                    data1.setSjshszb("实际石灰石指标" + i);
//                    data1.setPcl("偏差量" + i);
//                    mDatas.add(new WarningBean(WarningBean.LAYOUT1, data1));
//                    break;
//                case 1:
//                    WarningBean.KH data2 = new WarningBean.KH();
//                    data2.setId(i);
//                    data2.setEarningName("KH" + i);
//                    data2.setSdzb("设定指标" + i);
//                    data2.setSjzb("实际指标" + i);
//                    data2.setPcl("偏差量" + i);
//                    mDatas.add(new WarningBean(WarningBean.LAYOUT2, data2));
//                    break;
//                case 2:
//                    WarningBean.MonthKl data3 = new WarningBean.MonthKl();
//                    data3.setId(i);
//                    data3.setEarningName("台阶" + i);
//                    data3.setYjhkl("月计划矿量" + i);
//                    data3.setYjhckl("月计划出矿量" + i);
//                    data3.setYsjckl("月实际出矿量" + i);
//                    data3.setKlce("矿量差额" + i);
//                    mDatas.add(new WarningBean(WarningBean.LAYOUT3, data3));
//                    break;
//            }
//        }
//
//        if (adapter == null) {
//            adapter = new EarlyWarningAdapter(mDatas, ym);
//            early_warning_rv.setAdapter(adapter);
//
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    warningBean = (WarningBean) adapter.getData().get(position);
//                    switch (warningBean.getItemType()) {
//                        case WarningBean.LAYOUT1:
//                            break;
//                        case WarningBean.LAYOUT2:
//                            break;
//                        case WarningBean.LAYOUT3:
//                            break;
//                    }
////                    WarningBean.MonthKl data1 = (WarningBean.MonthKl) warningBean.getData();
////                    ShowToast.showShort(EarlyWarning.this, "点击的是" + data1.getId());
//                }
//            });
//        } else {
//            adapter.setNewData(mDatas);
//        }

    }


    /**
     * 调用预警接口
     */
    private void mConnect() {
        OkGo.<String>get(PortIpAddress.Alarm())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)){
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    AlarmBean bean = new AlarmBean();
                                    bean.setAlarmId(jsonArray.optJSONObject(i).getString("alarmid"));
                                    bean.setAlarmName(jsonArray.optJSONObject(i).getString("alarmtitle"));
                                    bean.setAlarmlevelname(jsonArray.optJSONObject(i).getString("alarmlevelname"));
                                    bean.setAddTime(jsonArray.optJSONObject(i).getString("addtime"));
                                    bean.setMemo(jsonArray.optJSONObject(i).getString("memo"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new AlarmAdapter(R.layout.notice_list_item, mDatas);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.setNewData(mDatas);
                                }

                                //如果无数据设置空布局
                                if (mDatas.size() == 0) {
                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
                                }

                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        alarmBean = (AlarmBean) adapter.getData().get(position);
                                        Intent intent = new Intent(EarlyWarning.this, AlarmDetail.class);
                                        intent.putExtra("ym", ym);
                                        intent.putExtra("alarmlevelname", alarmBean.getAlarmName());
                                        intent.putExtra("addtime", alarmBean.getAddTime());
                                        intent.putExtra("memo", alarmBean.getMemo());
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                ShowToast.showShort(EarlyWarning.this,err);
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
                        ShowToast.showShort(EarlyWarning.this, R.string.connect_err);
                    }
                });
    }


}
