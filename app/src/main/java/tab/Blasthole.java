package tab;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.administrator.conchapp.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.model.Lv0Item;
import com.leon.lfilepickerlibrary.model.Lv1Item;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.BlastholeExpandAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.blastholedetail.BlastholeDetail;
import utils.BaseFragment;
import utils.Consant;
import utils.CustomDatePicker;
import utils.DividerItemDecoration;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;

import static android.app.Activity.RESULT_OK;
import static utils.PermissionUtil.STORAGE_REQUESTCODE;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Blasthole extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.blasthole_date)
    TextView blasthole_date;
    @BindView(R.id.blasthole_group_tv)
    Spinner blasthole_group_tv;
    private String itemString = "已分组";
    private Map<String, String> groupMap;
    private List<File> files; //选择的文件集合

    private List<MultiItemEntity> searchDatas;
    //    private List<BlastholeBean> mDatas;
    private List<MultiItemEntity> mDatas;

//    private BlastholeAdapter adapter;

    private BlastholeExpandAdapter adapter;

    private Lv1Item blastholeBean = new Lv1Item();
    private boolean isFirstEnter = true;
    private ProgressDialog progressDialog;

    private int MAX_PROGRESS = 100;


    public Blasthole() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        View view = View.inflate(getActivity(), R.layout.fragment_blasthole, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        groupMap = new HashMap<>();
        groupMap.put("已分组", "false");
        groupMap.put("未分组", "true");
        //初始化当前日期
        blasthole_date.setText("全部日期");
        initSpinner();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        if (adapter == null) {
//            adapter = new BlastholeAdapter(R.layout.blasthole_item, mDatas);
//            adapter.bindToRecyclerView(recyclerView);
//            recyclerView.setAdapter(adapter);
            adapter = new BlastholeExpandAdapter(this, mDatas, blastholeBean);
            recyclerView.setAdapter(adapter);
        }

        mDatas = new ArrayList<>();
        initRefresh();
//        MonitorEditext();
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


    private void initSpinner() {
        // 数据源手动添加
        ArrayAdapter<String> spinnerAadapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spiner_text_item, getDataSource());
        spinnerAadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        blasthole_group_tv.setAdapter(spinnerAadapter);

        blasthole_group_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (adapterView.getId() == R.id.blasthole_group_tv) {
                itemString = blasthole_group_tv.getItemAtPosition(i).toString();
                refreshLayout.autoRefresh();
                return;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public List<String> getDataSource() {
        List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("已分组");
        spinnerList.add("未分组");
        return spinnerList;
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        if (arg0.getId() == R.id.blasthole_group_tv) {
            itemString = blasthole_group_tv.getItemAtPosition(arg2).toString();
            refreshLayout.autoRefresh();
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
//                    ShowToast.showShort(getActivity(), R.string.loadSuccess);
                    refreshLayout.finishLoadmore();
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


    /**
     * 监听搜索框
     */
//    private void MonitorEditext() {
//        searchDatas = new ArrayList<>();
//        search_edittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e(TAG, count + "----");
//                if (mDatas != null) {
//                    if (search_edittext.length() > 0) {
//                        refreshLayout.setEnableRefresh(false);
//                        refreshLayout.setEnableLoadmore(false);
//                        search_clear.setVisibility(View.VISIBLE);
//                        search(search_edittext.getText().toString().trim());
//                    } else {
//                        refreshLayout.setEnableRefresh(true);
//                        refreshLayout.setEnableLoadmore(true);
//                        search_clear.setVisibility(View.GONE);
//                        if (adapter != null) {
//                            adapter.setNewData(mDatas);
//                        }
//                    }
//                } else {
//                    if (search_edittext.length() > 0) {
//                        search_clear.setVisibility(View.VISIBLE);
//                    } else {
//                        search_clear.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    //搜索框
//    private void search(String str) {
//        if (mDatas != null) {
//            searchDatas.clear();
//            for (MultiItemEntity entity : mDatas) {
//                try {
//                    if (entity.getItemType() == TYPE_LEVEL_1) {
//                        searchDatas.add(entity);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                adapter.setNewData(searchDatas);
//            }
//        }
//    }

    /**
     * 日期选择
     */
    @OnClick(R.id.blasthole_date)
    void DateSelect() {
        CustomDatePicker.initDayPicker(getActivity(), blasthole_date, refreshLayout);
    }

    /**
     * 查询全部
     */
    @OnClick(R.id.blasthole_fbt)
    void SelectAll() {
        blasthole_date.setText("全部日期");
        refreshLayout.autoRefresh();
    }


    /**
     * 清除搜索框内容
     */
    @OnClick(R.id.search_clear)
    public void ClearSearch() {
        search_edittext.setText("");
        search_clear.setVisibility(View.GONE);
    }


    private void setRecyclerView() {
        String date = "";

        if (!blasthole_date.getText().toString().equals("全部日期")) {
            date = blasthole_date.getText().toString();
        }

        OkGo.<String>get(utils.PortIpAddress.getBlasthole())
                .tag(getActivity())
                .params("uploadtime", date)
                .params("sffz", groupMap.get(itemString))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        Log.e(TAG, response.body().toString());
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
//                            String err = jsonObject.getString(MESSAGE);
//                            mDatas.clear();
//                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
//                                for (int i = 0; i < data.length(); i++) {
//                                    BlastholeBean bean = new BlastholeBean();
//                                    bean.setPosition(i);
//                                    bean.setId(data.optJSONObject(i).getString("projectid"));
//                                    bean.setProjectname(data.optJSONObject(i).getString("projectname"));
//                                    bean.setBlastholeNum(data.optJSONObject(i).getString("pks"));
//                                    bean.setUploadtime(data.optJSONObject(i).getString("uploadtime"));
//                                    if (data.optJSONObject(i).getString("sffz").equals("true")) {
//                                        bean.setGrouping("未分组");
//                                    } else {
//                                        bean.setGrouping("已分组");
//                                    }
//                                    mDatas.add(bean);
//                                }
//
//
//                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                        blastholeBean = (BlastholeBean) adapter.getData().get(position);
//                                        Intent intent = new Intent(getActivity(), BlastholeDetail.class);
//                                        intent.putExtra("projectId", blastholeBean.getId());
////                                        intent.putExtra("time", blastholeBean.getLocationTime());
//                                        intent.putExtra("projectname", blastholeBean.getProjectname());
//                                        intent.putExtra("num", blastholeBean.getBlastholeNum());
//                                        intent.putExtra("sffz", blastholeBean.getGrouping());
//                                        startActivityForResult(intent, 10);
//                                    }
//                                });
//
//                                adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//                                    @Override
//                                    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                                        blastholeBean = (BlastholeBean) adapter.getData().get(position);
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                        builder.setTitle(R.string.delete_title);
//                                        builder.setMessage(R.string.delete_content);
//                                        builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Delete(blastholeBean.getId());
//                                            }
//                                        });
//
//                                        builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        });
//                                        builder.show();
//                                        return false;
//                                    }
//                                });
//                                adapter.setNewData(mDatas);
//
//                                //如果无数据设置空布局
//                                if (mDatas.size() == 0) {
//                                    adapter.setEmptyView(R.layout.nodata_layout, (ViewGroup) recyclerView.getParent());
//                                }
//
//                            } else {
//                                ShowToast.showShort(getActivity(), err);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        refreshLayout.finishRefresh();
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(PortIpAddress.CODE).equals(PortIpAddress.SUCCESS_CODE)) {
                                ArrayList<MultiItemEntity> res = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    String parentname = data.optJSONObject(i).getString("parentname");
                                    Lv0Item lv0 = new Lv0Item();
                                    lv0.setTitle(parentname);
                                    JSONArray dataArr = data.optJSONObject(i).getJSONArray("dataArray");
                                    for (int j = 0; j < dataArr.length(); j++) {
                                        Lv1Item lv1 = new Lv1Item();
                                        String projectid = dataArr.optJSONObject(j).getString("projectid");
                                        String projectname = dataArr.optJSONObject(j).getString("projectname");
                                        projectname = projectname.substring(projectname.lastIndexOf("/") + 1);
                                        String pks = dataArr.optJSONObject(j).getString("pks");
                                        String sffz = dataArr.optJSONObject(j).getString("sffz");
                                        if (sffz.equals("true")) {
                                            sffz = "未分组";
                                        } else {
                                            sffz = "已分组";
                                        }
                                        String uploadtime = dataArr.optJSONObject(j).getString("uploadtime");
                                        lv1.setProjectid(projectid);
                                        lv1.setProjectname(projectname);
                                        lv1.setPks(pks);
                                        lv1.setSffz(sffz);
                                        lv1.setUploadtime(uploadtime);
//                                      placeId.put(parentname + projectname, projectid);
                                        lv0.addSubItem(lv1);
                                    }
                                    res.add(lv0);
                                }
                                mDatas = res;
                                adapter = new BlastholeExpandAdapter(Blasthole.this, mDatas, blastholeBean);
                                recyclerView.setAdapter(adapter);
                            } else {
                                ShowToast.showShort(getActivity(), err);
                            }

                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    blastholeBean = (Lv1Item) adapter.getData().get(position);
                                    Intent intent = new Intent(getActivity(), BlastholeDetail.class);
                                    intent.putExtra("projectId", blastholeBean.getProjectid());
                                    intent.putExtra("projectname", blastholeBean.getProjectname());
                                    intent.putExtra("num", blastholeBean.getPks());
                                    intent.putExtra("sffz", blastholeBean.getSffz());
                                    startActivityForResult(intent, 10);
                                }
                            });

                            refreshLayout.finishRefresh();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh();
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    /**
     * 删除一条
     */
    private void Delete(String projectid) {
        OkGo.<String>get(PortIpAddress.DeleteBlasthole())
                .tag(this)
                .params("projectid", projectid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                ShowToast.showShort(getActivity(), "删除成功");
                                refreshLayout.autoRefresh();
                            } else {
                                ShowToast.showShort(getActivity(), "删除失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }


    /**
     * 选择文件
     */
    @OnClick(R.id.blasthole_filechoose)
    void FileChoose() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 10);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//        }
        if (ContextCompat.checkSelfPermission(getActivity(), PermissionUtil.WriteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtil.FileRWPermission(this);
        } else {
            if (SDUtils.hasSdcard()) {
                OpenFileManage();
            } else {
                ShowToast.showShort(getActivity(), R.string.nosdcard);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, paramArrayOfInt, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == STORAGE_REQUESTCODE) {
                OpenFileManage();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == STORAGE_REQUESTCODE) {
                ShowToast.showShort(getActivity(), R.string.getPermissionErr);
            }
        }
    };

    /**
     * 打开自定义文件管理器选择文件
     */
    private void OpenFileManage() {
        new LFilePicker()
                .withSupportFragment(this)
                .withRequestCode(Consant.REQUESTCODE_FROM_FRAGMENT)//响应码
                .withIconStyle(Constant.ICON_STYLE_BLUE)  //设置文件夹风格
                .withBackIcon(Constant.BACKICON_STYLEONE) //返回按钮风格
                .withBackgroundColor("#1195db")
                .withTitle("文件选择")
                .withFileFilter(new String[]{".txt"}) //文件过滤
                .withMaxNum(1)
                .start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Consant.REQUESTCODE_FROM_FRAGMENT) {
                files = new ArrayList<>();
                List<String> list = data.getStringArrayListExtra("paths");
                String projectid = data.getStringExtra("adress");
                for (String s : list) {
//                    ShowToast.showShort(getActivity(), s);
                    File file = new File(s);
                    files.add(file);
                }
                FileUpLoad(files, projectid);
//                ShowProgressDialog();
            }
        }
    }


    /**
     * 上传炮孔文件
     *
     * @param files
     * @param projectid
     */
    private void FileUpLoad(List<File> files, String projectid) {
        OkGo.<String>post(utils.PortIpAddress.upLoadProjectinfoData())
                .tag(this)
                .params("projectid", projectid)
                .addFileParams("file", files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String mes = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                refreshLayout.autoRefresh();
                                ShowToast.showShort(getActivity(), mes);
                            } else {
                                ShowToast.showShort(getActivity(), mes);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void uploadProgress(final Progress progress) {
                        super.uploadProgress(progress);
//                        mProgressBar.setProgress((int) (100 * progress.fraction));
//                        mTextView2.setText("已上传" + currentSize/1024/1024 + "MB, 共" + totalSize/1024/1024 + "MB;");

//                        if (progressDialog == null) {
//                            ShowProgressDialog();
//                        }
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while ((progress.fraction * 100) < MAX_PROGRESS) {
//                                    progressDialog.setProgress((int) progress.fraction * 100);
//                                }
//                                // 进度达到最大值后，窗口消失
//                                progressDialog.cancel();
//
//                            }
//                        }).start();
                        if (progressDialog == null) {
                            ShowProgressDialog();
                        }

                        if ((int) progress.fraction * 100 < MAX_PROGRESS) {
                            progressDialog.setProgress((int) (progress.fraction * 100));
                        } else {
                            progressDialog.cancel();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    /**
     * 展示进度条dialog
     */
    private void ShowProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgress(0);
        progressDialog.setTitle("文件上传中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MAX_PROGRESS);
        progressDialog.show();
    }

    private void showCustomizeDialog() {
        AlertDialog.Builder customdialog = new AlertDialog.Builder(getActivity());
        View dialogView = View.inflate(getActivity(), R.layout.custom_dialog, null);
        customdialog.setTitle("文件上传中...")
                .setView(dialogView);
        customdialog.show();
    }


    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
