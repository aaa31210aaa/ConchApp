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

import adpter.NoticeAdapter;
import bean.NoticeBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.homedetail.noticedetail.NoticeDetail;
import utils.BaseActivity;
import utils.DividerItemDecoration;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

/**
 * 通知公告
 */
public class Notice extends BaseActivity {
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<NoticeBean> searchDatas;
    private List<NoticeBean> mDatas;
    //第一次进入页面
    private NoticeAdapter adapter;
    private NoticeBean noticeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        isFirstEnter = true;
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
            for (NoticeBean entity : mDatas) {
                try {
                    if (entity.getTitle().contains(str) || entity.getDate().contains(str) || entity.getContent().contains(str)) {
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
//                    ShowToast.showShort(Notice.this, R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(Notice.this, R.string.refreshSuccess);
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
//            NoticeBean bean = new NoticeBean();
//            bean.setNoticeId(i);
//            bean.setTitle("标题" + i);
//            bean.setContent("内容" + i);
//            bean.setDate("2017-08-18");
//            mDatas.add(bean);
//        }
//
//        if (adapter == null) {
//            adapter = new NoticeAdapter(R.layout.notice_list_item, mDatas);
//            notice_rv.setAdapter(adapter);
//
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    noticeBean = (NoticeBean) adapter.getData().get(position);
//                    ShowToast.showShort(Notice.this, "点击的是" + noticeBean.getNoticeId());
//                    Intent intent = new Intent(Notice.this, NoticeDetail.class);
//                    intent.putExtra("noticeId", noticeBean.getNoticeId());
//                    startActivity(intent);
//                }
//            });
//        } else {
//            adapter.setNewData(mDatas);
//        }
    }


    /**
     * 调用通知公告接口
     */
    private void mConnect() {
        OkGo.<String>get(PortIpAddress.NoticeMessage())
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

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    NoticeBean bean = new NoticeBean();
                                    bean.setPosition(i);
                                    bean.setNoticeId(jsonArray.optJSONObject(i).getString("messageid"));
                                    bean.setTitle(jsonArray.optJSONObject(i).getString("messagetitle"));
                                    bean.setDate(jsonArray.optJSONObject(i).getString("mestime"));
                                    bean.setContent(jsonArray.optJSONObject(i).getString("mescontent"));
                                    bean.setMessagestae(jsonArray.optJSONObject(i).getString("messagestae"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new NoticeAdapter(R.layout.notice_list_item, mDatas);
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

                                //子项点击事件
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        noticeBean = (NoticeBean) adapter.getData().get(position);
//                                      ShowToast.showShort(Notice.this, "点击的是" + noticeBean.getNoticeId());
                                        Intent intent = new Intent(Notice.this, NoticeDetail.class);
                                        intent.putExtra("messagetitle", noticeBean.getTitle());
                                        intent.putExtra("mestime", noticeBean.getDate());
                                        intent.putExtra("mescontent", noticeBean.getContent());
                                        startActivity(intent);
                                        MessageRead(noticeBean.getNoticeId());
                                    }
                                });
                            } else {
                                ShowToast.showShort(Notice.this, err);
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
                        ShowToast.showShort(Notice.this, R.string.connect_err);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    /**
     * 点击后调用已读
     */
    private void MessageRead(String mid) {
        OkGo.<String>get(PortIpAddress.MessageState())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("mid", mid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(Notice.this, R.string.connect_err);
                    }
                });
    }



}

