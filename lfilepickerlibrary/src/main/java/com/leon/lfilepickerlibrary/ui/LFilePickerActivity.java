package com.leon.lfilepickerlibrary.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leon.lfilepickerlibrary.R;
import com.leon.lfilepickerlibrary.adapter.ExpandableAdapter;
import com.leon.lfilepickerlibrary.adapter.PathAdapter;
import com.leon.lfilepickerlibrary.filter.LFileFilter;
import com.leon.lfilepickerlibrary.model.Lv0Item;
import com.leon.lfilepickerlibrary.model.Lv1Item;
import com.leon.lfilepickerlibrary.model.ParamEntity;
import com.leon.lfilepickerlibrary.model.ProjectNameBean;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.leon.lfilepickerlibrary.utils.DialogUtil;
import com.leon.lfilepickerlibrary.utils.DividerItemDecoration;
import com.leon.lfilepickerlibrary.utils.FileUtils;
import com.leon.lfilepickerlibrary.utils.MyDialog;
import com.leon.lfilepickerlibrary.utils.PortIpAddress;
import com.leon.lfilepickerlibrary.widget.EmptyRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leon.lfilepickerlibrary.utils.PortIpAddress.CODE;
import static com.leon.lfilepickerlibrary.utils.PortIpAddress.MESSAGE;
import static com.leon.lfilepickerlibrary.utils.PortIpAddress.SUCCESS_CODE;
import static com.leon.lfilepickerlibrary.utils.PortIpAddress.TOKEN_KEY;
import static com.leon.lfilepickerlibrary.utils.PortIpAddress.USERID_KEY;

public class LFilePickerActivity extends AppCompatActivity {
    private final String TAG = "FilePickerLeon";
    private EmptyRecyclerView mRecylerView;
    private View mEmptyView;
    private TextView mTvPath;
    private TextView mTvBack;
    private Button mBtnAddBook;
    private String mPath;
    private List<File> mListFiles;
    private ArrayList<String> mListNumbers = new ArrayList<String>();//存放选中条目的数据地址
    private PathAdapter mPathAdapter;
    private Toolbar mToolbar;
    private ParamEntity mParamEntity;
    private final int RESULTCODE = 1024;
    private LFileFilter mFilter;
    private boolean mIsAllSelected = false;
    private Menu mMenu;
    //存放作业地点id
    private Map<String, String> placeId = new HashMap<>();
    private AlertDialog alertDialog;
    private List<ProjectNameBean> searchDatas;
    private List<MultiItemEntity> mDatas;
    private ExpandableAdapter adapter;
    private RecyclerView dialog_item_rv;
    private ProjectNameBean projectNameBean;
    private MyDialog myDialog;
    private Dialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lfile_picker);
        mParamEntity = (ParamEntity) getIntent().getExtras().getSerializable("param");
        initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initToolbar();
        if (!checkSDState()) {
            Toast.makeText(this, R.string.NotFoundPath, Toast.LENGTH_SHORT).show();
            return;
        }
        mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mTvPath.setText(mPath);
        mFilter = new LFileFilter(mParamEntity.getFileTypes());
        mListFiles = getFileList(mPath);
        mPathAdapter = new PathAdapter(mListFiles, this, mFilter, mParamEntity.isMutilyMode());
        mRecylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPathAdapter.setmIconStyle(mParamEntity.getIconStyle());
        mRecylerView.setAdapter(mPathAdapter);
        mRecylerView.setmEmptyView(mEmptyView);
        initListener();

    }

    /**
     * 获得作业地点
     */
    private void getPlace() {
        myDialog = new MyDialog(LFilePickerActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_item, null);
        dialog_item_rv = (RecyclerView) layout.findViewById(R.id.dialog_item_rv);
        myDialog.show();
        myDialog.setCancelable(true);
        myDialog.setContentView(layout);// show方法要在前面

        mDatas = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(LFilePickerActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dialog_item_rv.setLayoutManager(llm);
        dialog_item_rv.setHasFixedSize(true);
        dialog_item_rv.addItemDecoration(new DividerItemDecoration(LFilePickerActivity.this));
        loadDialog = DialogUtil.createLoadingDialog(LFilePickerActivity.this, R.string.loading);
        getPlaceData();
    }


    /**
     * 获取数据
     */
    private void getPlaceData() {
        OkGo.<String>get(PortIpAddress.getPlace())
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
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
                                        lv1.setTid(projectid);
                                        lv1.setTitle(projectname);
                                        placeId.put(parentname + projectname, projectid);
                                        lv0.addSubItem(lv1);
                                    }
                                    res.add(lv0);
                                }
                                mDatas = res;
//                                for (int i = 0; i < data.length(); i++) {
//                                    ProjectNameBean bean = new ProjectNameBean();
//                                    String projectname = data.optJSONObject(i).getString("projectname");
//                                    String projectid = data.optJSONObject(i).getString("projectid");
//                                    placeId.put(projectname, projectid);
//                                    bean.setProjectName(projectname);
//                                    mDatas.add(bean);
//                                }
                            } else {
                                Toast.makeText(LFilePickerActivity.this, err, Toast.LENGTH_SHORT).show();
                            }
//                            mMenu.getItem(1).setTitle(mDatas.get(0).getProjectName());
//                            ArrayAdapter adapter = new ArrayAdapter<String>(LFilePickerActivity.this, android.R.layout.simple_list_item_1, adress_arr);
//                            alertDialog = new AlertDialog.Builder(LFilePickerActivity.this)
//                                    .setTitle("请选择作业地点")
//                                    .setIcon(R.mipmap.adressimg)
//                                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            mMenu.getItem(1).setTitle(adress_arr[which]);
//                                        }
//                                    }).create();

                            if (adapter == null) {
//                                adapter = new ExpandableItemAdapter(mDatas);
//                                dialog_item_rv.setAdapter(adapter);
                                adapter = new ExpandableAdapter(mDatas, myDialog, mMenu);
                                dialog_item_rv.setAdapter(adapter);
                            } else {
                                adapter.setNewData(mDatas);
                            }

                            //选择作业地点
//                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                    projectNameBean = (ProjectNameBean) adapter.getData().get(position);
//                                    myDialog.dismiss();
//                                    mMenu.getItem(1).setTitle(projectNameBean.getProjectName());
//                                }
//                            });

                            loadDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDialog != null) {
            myDialog.dismiss();
        }
    }

    /**
     * 更新Toolbar展示
     */
    private void initToolbar() {
        if (mParamEntity.getTitle() != null) {
            mToolbar.setTitle(mParamEntity.getTitle());
        }
        if (mParamEntity.getTitleColor() != null) {
            mToolbar.setTitleTextColor(Color.parseColor(mParamEntity.getTitleColor())); //设置标题颜色
        }
        if (mParamEntity.getBackgroundColor() != null) {
            mToolbar.setBackgroundColor(Color.parseColor(mParamEntity.getBackgroundColor()));
        }
        if (!mParamEntity.isMutilyMode()) {
            mBtnAddBook.setVisibility(View.GONE);
        }
        switch (mParamEntity.getBackIcon()) {
            case Constant.BACKICON_STYLEONE:
                mToolbar.setNavigationIcon(R.mipmap.backincostyleone);
                break;
            case Constant.BACKICON_STYLETWO:
                mToolbar.setNavigationIcon(R.mipmap.backincostyletwo);
                break;
            case Constant.BACKICON_STYLETHREE:
                //默认风格
                break;
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 添加点击事件处理
     */
    private void initListener() {
        // 返回目录上一级
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempPath = new File(mPath).getParent();
                if (tempPath == null) {
                    return;
                }
                mPath = tempPath;
                mListFiles = getFileList(mPath);
                mPathAdapter.setmListData(mListFiles);
                mPathAdapter.updateAllSelelcted(false);
                mIsAllSelected = false;
                updateMenuTitle();
                mBtnAddBook.setText(getString(R.string.Selected));
                mRecylerView.scrollToPosition(0);
                setShowPath(mPath);
                //清除添加集合中数据
                mListNumbers.clear();
                if (mParamEntity.getAddText() != null) {
                    mBtnAddBook.setText(mParamEntity.getAddText());
                } else {
                    mBtnAddBook.setText(R.string.Selected);
                }
            }
        });

        mPathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
                if (mParamEntity.isMutilyMode()) {
                    if (mListFiles.get(position).isDirectory()) {
                        //如果当前是目录，则进入继续查看目录
                        chekInDirectory(position);
                        mPathAdapter.updateAllSelelcted(false);
                        mIsAllSelected = false;
                        updateMenuTitle();
                        mBtnAddBook.setText(getString(R.string.Selected));
                    } else {
                        //如果已经选择则取消，否则添加进来
                        if (mListNumbers.contains(mListFiles.get(position).getAbsolutePath())) {
                            mListNumbers.remove(mListFiles.get(position).getAbsolutePath());
                        } else {
                            mListNumbers.add(mListFiles.get(position).getAbsolutePath());
                        }

//                        if (mParamEntity.getAddText() != null) {
//                            mBtnAddBook.setText(mParamEntity.getAddText() + "( " + mListNumbers.size() + " )");
//                        } else {
//                            mBtnAddBook.setText(getString(R.string.Selected) + "( " + mListNumbers.size() + " )");
//                        }

                        //先判断是否达到最大数量，如果数量达到上限提示，否则继续添加
                        if (mParamEntity.getMaxNum() > 0 && mListNumbers.size() > mParamEntity.getMaxNum()) {
                            Toast.makeText(LFilePickerActivity.this, R.string.OutSize, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    //单选模式直接返回
                    if (mListFiles.get(position).isDirectory()) {
                        chekInDirectory(position);
                        return;
                    }
                    mListNumbers.add(mListFiles.get(position).getAbsolutePath());
                    chooseDone();
                }

            }
        });

        mBtnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListNumbers.size() < 1) {
                    String info = mParamEntity.getNotFoundFiles();
                    if (TextUtils.isEmpty(info)) {
                        Toast.makeText(LFilePickerActivity.this, R.string.NotFoundFiles, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LFilePickerActivity.this, info, Toast.LENGTH_SHORT).show();
                    }
                } else if (mListNumbers.size() > 1) {
                    Toast.makeText(LFilePickerActivity.this, R.string.onlyOne, Toast.LENGTH_SHORT).show();
                } else if (mListNumbers.size() == 1) {
                    //返回
                    chooseDone();
                }
            }
        });
    }


    /**
     * 点击进入目录
     *
     * @param position
     */
    private void chekInDirectory(int position) {
        mPath = mListFiles.get(position).getAbsolutePath();
        setShowPath(mPath);
        //更新数据源
        mListFiles = getFileList(mPath);
        mPathAdapter.setmListData(mListFiles);
        mPathAdapter.notifyDataSetChanged();
        mRecylerView.scrollToPosition(0);
    }

    /**
     * 完成提交
     */
    private void chooseDone() {
        //判断是否数量符合要求
        if (mParamEntity.getMaxNum() > 0 && mListNumbers.size() > mParamEntity.getMaxNum()) {
            Toast.makeText(LFilePickerActivity.this, R.string.OutSize, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMenu.getItem(1).toString().equals("作业地点")) {
            Toast.makeText(this, "请选择作业地点后提交", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("paths", mListNumbers);
            intent.putExtra("adress", placeId.get(mMenu.getItem(1).toString()));
            setResult(RESULT_OK, intent);
            this.finish();
        }

    }

    /**
     * 根据地址获取当前地址下的所有目录和文件，并且排序
     *
     * @param path
     * @return List<File>
     */
    private List<File> getFileList(String path) {
        File file = new File(path);
        List<File> list = FileUtils.getFileListByDirPath(path, mFilter);
        return list;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRecylerView = (EmptyRecyclerView) findViewById(R.id.recylerview);
        mTvPath = (TextView) findViewById(R.id.tv_path);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mBtnAddBook = (Button) findViewById(R.id.btn_addbook);
        mEmptyView = findViewById(R.id.empty_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mParamEntity.getAddText() != null) {
            mBtnAddBook.setText(mParamEntity.getAddText());
        }

        //获得作业地点
        getPlace();
    }

    /**
     * 检测SD卡是否可用
     */
    private boolean checkSDState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 显示顶部地址
     *
     * @param path
     */
    private void setShowPath(String path) {
        mTvPath.setText(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_selecteall_cancel) {
            //将当前目录下所有文件选中或者取消
            mPathAdapter.updateAllSelelcted(!mIsAllSelected);
            mIsAllSelected = !mIsAllSelected;
            if (mIsAllSelected) {
                for (File mListFile : mListFiles) {
                    if (!mListFile.isDirectory()) {
                        if (!mListNumbers.contains(mListFile.getAbsolutePath())) {
                            mListNumbers.add(mListFile.getAbsolutePath());
                        }
                    }
                    if (mParamEntity.getAddText() != null) {
                        mBtnAddBook.setText(mParamEntity.getAddText() + "( " + mListNumbers.size() + " )");
                    } else {
                        mBtnAddBook.setText(getString(R.string.Selected) + "( " + mListNumbers.size() + " )");
                    }
                }
            } else {
                mListNumbers.clear();
                mBtnAddBook.setText(getString(R.string.Selected));
            }
            updateMenuTitle();
        } else if (item.getItemId() == R.id.action_selecteall_place) {
            if (myDialog != null) {
                myDialog.show();
            }
        }
        return true;
    }

    /**
     * 更新选项菜单文字
     */
    public void updateMenuTitle() {

        if (mIsAllSelected) {
            mMenu.getItem(0).setTitle(getString(R.string.Cancel));
        } else {
            mMenu.getItem(0).setTitle(getString(R.string.SelectAll));
        }
    }

}
