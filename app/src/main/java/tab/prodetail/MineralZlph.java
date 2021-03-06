package tab.prodetail;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import utils.BaseFragment;
import utils.DateUtils;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.BaseActivity.isFirstEnter;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineralZlph extends BaseFragment {
    private View view;
    @BindView(R.id.mineral_date)
    TextView mineral_date;
    @BindView(R.id.mineral_search_edittext)
    EditText mineral_search_edittext;
    @BindView(R.id.mineral_search_clear)
    ImageView mineral_search_clear;
    @BindView(R.id.mineral_rv)
    RecyclerView mineral_rv;
    @BindView(R.id.mineral_refresh)
    SmartRefreshLayout mineral_refresh;
    private List<ZlphDetailBean> searchDatas;
    private List<ZlphDetailBean> mDatas;
    private ZlphDetailAdapter adapter;
    private String token;
    public MineralZlph() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_mineral_zlph, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        token  = PortIpAddress.getToken(getActivity());
        mDatas = new ArrayList<>();
        //初始化当前年份
        mineral_date.setText(DateUtils.mYear());
        initRefresh();
        MonitorEditext();
    }


    /**
     * 前一年
     */
    @OnClick(R.id.mineral_before)
    void Before() {
        int mYear = Integer.parseInt(mineral_date.getText().toString());
        String str = String.valueOf(--mYear);
        mineral_date.setText(str);
    }


    /**
     * 后一年
     */
    @OnClick(R.id.mineral_next)
    void Next() {
        int mYear = Integer.parseInt(mineral_date.getText().toString());
        String str = String.valueOf(++mYear);
        mineral_date.setText(str);
    }


    /**
     * 监听搜索框
     */
    private void MonitorEditext() {
        mineral_search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, count + "----");
                if (mDatas != null) {
                    if (mineral_search_edittext.length() > 0) {
                        mineral_refresh.setEnableRefresh(false);
                        mineral_refresh.setEnableLoadmore(false);
                        mineral_search_clear.setVisibility(View.VISIBLE);
                        search(mineral_search_edittext.getText().toString().trim());
                    } else {
                        mineral_refresh.setEnableRefresh(true);
                        mineral_refresh.setEnableLoadmore(true);
                        mineral_search_clear.setVisibility(View.GONE);
                        if (adapter!=null){
                            adapter.setNewData(mDatas);
                        }
                    }
                } else {
                    if (mineral_search_edittext.length() > 0) {
                        mineral_search_clear.setVisibility(View.VISIBLE);
                    } else {
                        mineral_search_clear.setVisibility(View.GONE);
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
            for (ZlphDetailBean entity : mDatas) {
                try {
                    if (entity.getPlace().contains(str)) {
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
    @OnClick(R.id.mineral_search_clear)
    public void ClearSearch() {
        mineral_search_edittext.setText("");
        mineral_search_clear.setVisibility(View.GONE);
    }


    private void initRefresh() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mineral_rv.setLayoutManager(llm);
        mineral_rv.setHasFixedSize(true);
        mineral_rv.addItemDecoration(new DividerItemDecoration(getActivity()));

        if (isFirstEnter) {
            isFirstEnter = false;
            mineral_refresh.autoRefresh();//第一次进入触发自动刷新
        }

        //刷新
        mineral_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(1, ShowToast.refreshTime);
            }
        });

        //加载
        mineral_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
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
//                    ShowToast.showShort(getActivity(), R.string.loadSuccess);
                    mineral_refresh.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(getActivity(), R.string.refreshSuccess);
                    setRecyclerView();
                    break;
                default:
                    break;
            }
        }
    };

    private void setRecyclerView() {
        mConnect(mineral_date.getText().toString());
        mineral_refresh.finishRefresh();
    }

    private void mConnect(String date) {
        OkGo.<String>get("")
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .params("", date)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ZlphDetailBean bean = new ZlphDetailBean();
                                mDatas.add(bean);
                            }

                            if (adapter == null) {
//                                adapter = new ZlphDetailAdapter(R.layout.zlphdetail_item, mDatas);
                                adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                adapter.isFirstOnly(false);
                                mineral_rv.setAdapter(adapter);
                            } else {
                                adapter.setNewData(mDatas);
                            }
                            //如果无数据设置空布局
                            if (mDatas.size() == 0) {
                                adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) mineral_rv.getParent());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

}
