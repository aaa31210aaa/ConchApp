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

import adpter.SlphDetailAdapter;
import bean.SlphDetailBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.CustomDatePicker.initYearPicker;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class SlphjhDetail extends BaseActivity {
    @BindView(R.id.slph_detail_title)
    TextView slph_detail_title;
    @BindView(R.id.slph_detail_date)
    TextView slph_detail_date;
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<SlphDetailBean> searchDatas;
    private List<SlphDetailBean> mDatas;
    private SlphDetailAdapter adapter;
    private String techid;
    private String ProcID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slphjh_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mDatas = new ArrayList<>();
        isFirstEnter = true;

        //初始化时间
        slph_detail_date.setText(DateUtils.mYear());

        techid = intent.getStringExtra("techid");
        ProcID = intent.getStringExtra("procid");
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
                        slph_detail_date.setEnabled(false);
                        search_clear.setVisibility(View.VISIBLE);
                        search(search_edittext.getText().toString().trim());
                    } else {
                        refreshLayout.setEnableRefresh(true);
                        refreshLayout.setEnableLoadmore(true);
                        slph_detail_date.setEnabled(true);
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
            searchDatas = new ArrayList<SlphDetailBean>();
            for (SlphDetailBean bean : mDatas) {
                try {
                    if (bean.getPlace().contains(str) || bean.getNcbyl().contains(str) || bean.getNcbylqx().contains(str) || bean.getNmbyl().contains(str) || bean.getNmbylqx().contains(str)
                            || bean.getNcczl().contains(str) || bean.getNcczlqx().contains(str) || bean.getNmczl().contains(str) || bean.getNmczlqx().contains(str)
                            || bean.getNckzl().contains(str) || bean.getNckzlqx().contains(str) || bean.getNmkzl().contains(str) || bean.getNmkzlqx().contains(str)) {
                        searchDatas.add(bean);
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


    @OnClick(R.id.slph_detail_date)
    void DateChoose() {
        //初始化选择器
        initYearPicker(this,slph_detail_date,refreshLayout);
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
                slph_detail_date.setEnabled(false);
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
//                    ShowToast.showShort(KcjhDetail.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(KcjhDetail.this, R.string.refreshSuccess);
                    setRecyclerView();
                    slph_detail_date.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
        mConnect(slph_detail_date.getText().toString());
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
                                    SlphDetailBean bean = new SlphDetailBean();
                                    bean.setPlace(data.optJSONObject(i).getString("projectname"));
                                    bean.setNcbyl(data.optJSONObject(i).getString("ncbyl"));
                                    bean.setNcbylqx(data.optJSONObject(i).getString("ncbylqx"));
                                    bean.setNmbyl(data.optJSONObject(i).getString("nmbyl"));
                                    bean.setNmbylqx(data.optJSONObject(i).getString("nmbylqx"));
                                    bean.setNcczl(data.optJSONObject(i).getString("ncczl"));
                                    bean.setNcczlqx(data.optJSONObject(i).getString("ncczlqx"));
                                    bean.setNmczl(data.optJSONObject(i).getString("nmczl"));
                                    bean.setNmczlqx(data.optJSONObject(i).getString("nmczlqx"));
                                    bean.setNckzl(data.optJSONObject(i).getString("nckzl"));
                                    bean.setNckzlqx(data.optJSONObject(i).getString("nckclqx"));
                                    bean.setNmkzl(data.optJSONObject(i).getString("nmkzl"));
                                    bean.setNmkzlqx(data.optJSONObject(i).getString("nmkzlqx"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new SlphDetailAdapter(R.layout.slphdetail_item, mDatas);
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
                                ShowToast.showShort(SlphjhDetail.this, err);
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
                        ShowToast.showShort(SlphjhDetail.this, R.string.connect_err);

                    }
                });
    }


}
