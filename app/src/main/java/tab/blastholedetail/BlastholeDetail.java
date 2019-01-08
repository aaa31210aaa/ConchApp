package tab.blastholedetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.conchapp.R;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.BlastholeAtt;
import bean.BlastholeDetailBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;
import utils.StatusBarUtils;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;


public class BlastholeDetail extends BaseActivity {
    @BindView(R.id.blasthole_detail_ll)
    RelativeLayout blasthole_detail_ll;
    @BindView(R.id.blasthole_detail_num)
    TextView blasthole_detail_num;
    //    @BindView(R.id.blasthole_detail_groupll)
//    LinearLayout blasthole_detail_groupll;
    @BindView(R.id.blasthole_detail_grouping)
    Button blasthole_detail_grouping;
    private int index = 0;
    private int ctvid = 0;
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    //分组展示的默认组号
    private int fzid = 1;
    //分好组的数量
    private int yfzGroup = 0;
    //放大炮孔坐标的倍数
    private int multiples = 20;


    //分组数据集合
    private List<BlastholeDetailBean.DataBean.FzlistBean> blastholeAtts;
    //炮孔位置集合
    private List<BlastholeDetailBean.DataBean.PklistBean> pkList;

    //选中炮孔后存放的集合
    private List<BlastholeDetailBean.DataBean.PklistBean> blastholeChecked;

    //所有炮孔选中的集合
    private List<BlastholeDetailBean.DataBean.PklistBean> blastholeGrouped;

    //未分组的炮孔集合
    private List<BlastholeAtt> noGrouped;


    private List<Integer> x = new ArrayList<>();
    private List<Integer> y = new ArrayList<>();


    //存放分组内容
    private Map<BlastholeDetailBean.DataBean.FzlistBean, List<BlastholeDetailBean.DataBean.PklistBean>> saveMessage = new HashMap<>();
    //关联分组颜色
    private Map<String, Integer> groupColor = new HashMap<>();
    //关联炮孔组名
    private Map<String, String> pkText = new HashMap<>();

    private boolean isUploadGroup = false;

    @BindView(R.id.blasthole_detail_groupingll)
    LinearLayout blasthole_detail_groupingll;
    @BindView(R.id.clear_group)
    TextView clear_group;
    //炮孔展示分组组号
    private BlastholeAtt att;

    //作业地点id
    private String projectId;
    //是否分组标识
    private String sffz;
    //    @BindView(R.id.pkhtv)
//    HorizontalScrollView pkhtv;
    @BindView(R.id.pkscroll)
    ScrollView pkscroll;
    //炮孔数ll
    @BindView(R.id.ll_pks)
    LinearLayout ll_pks;
    private int ll_pks_height;
    //分组按钮ll
    @BindView(R.id.ll_fzbtn)
    LinearLayout ll_fzbtn;
    private int ll_fzbtn_height;
    //组号ll
    @BindView(R.id.zh_ll)
    LinearLayout zh_ll;
    private int zh_ll_height;
    //屏幕高度
    private int PmHeight;
    //显示炮孔的view的高
    private int pkview_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blasthole_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
//        for (int i = 0; i < 10; i++) {
//            int random = DateUtils.getIntRandom(1000);
//            x.add(random);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            int random = DateUtils.getIntRandom(300);
//            y.add(random);
//        }
        Intent intent = getIntent();
//        String time = intent.getStringExtra("time");
        String num = intent.getStringExtra("num");
        projectId = intent.getStringExtra("projectId");
        sffz = intent.getStringExtra("sffz");
        blasthole_detail_num.setText(num);

        blastholeAtts = new ArrayList<>();
        pkList = new ArrayList<>();

        blastholeChecked = new ArrayList<>();
        blastholeGrouped = new ArrayList<>();
        noGrouped = new ArrayList<>();
        GetViewHeight();
        getPmHeight();
        //创建圆点------------
//        for (int i = 0; i < 5; i++) {
//            final int a = i;
//            LinearLayout ll = new LinearLayout(this);
//            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            ll.setOrientation(LinearLayout.HORIZONTAL);
//            for (int j = 0; j < 5; j++) {
//                final int b = j;
//                final TextView circle = new TextView(this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80, 80);
//                lp.setMargins(j * 30, i * 40, 0, 0);
//                final int x = j * 30;
//                final int y = i * 40;
//                circle.setLayoutParams(lp);
//                circle.setGravity(Gravity.CENTER);
//                circle.setBackgroundResource(R.drawable.blastholeimg);
//                circle.setId(ctvid++);
//                circle.setPadding(0, 0, 0, 0);
//                circle.setTextColor(R.color.black);
//                circle.getPaint().setFakeBoldText(true);
//                circle.setTextSize(10);
//                ll.addView(circle);
//
//                blastholeAtts = new ArrayList<>();
//                //圆点点击事件
//                circle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View vv) {
//                        circle.setEnabled(false);
//                        circle.setTextColor(Color.WHITE);
//                        circle.setText("G" + groupId);
//                        circle.setBackgroundResource(R.drawable.blastholeimgred);
//                        att = new BlastholeAtt();
//                        att.setBid(circle.getId());
//                        att.setView(circle);
//                        att.setX(x);
//                        att.setY(y);
//                        att.setGroupName("分组" + groupId);
//                        blastholeAtts.add(att);
////                       ShowToast.showToastNowait(BlastholeDetail.this, "点击的X:" + a + "----" + "点击的Y:" + b);
//
//                        //创建展示区
//                        final TextView tv = new TextView(BlastholeDetail.this);
//                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(20, 0, 20, 0);
//                        tv.setLayoutParams(lp);
//                        tv.setBackgroundColor(R.color.gray_deep);
//                        tv.setGravity(Gravity.CENTER);
//                        tv.setPadding(20, 20, 20, 20);
//                        tv.setId(index++);
//                        tv.setText("炮孔(" + a + "," + b + ")");
//                        blasthole_detail_groupll.addView(tv);
//
//                        //展示区删除
//                        tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                blasthole_detail_groupll.removeView(v);
//                                vv.setEnabled(true);
//                                circle.setText("");
//                                circle.setBackgroundResource(R.drawable.blastholeimg);
//                            }
//                        });
//                    }
//                });
//            }
//            blasthole_detail_ll.addView(ll);
//        }------------------------
//        for (int i = 0; i < x.size(); i++) {
//            final int a = i;
//            final TextView circle = new TextView(this);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(60, 60);
//            lp.setMargins(x.get(i), y.get(i), 0, 0);
//
//            circle.setLayoutParams(lp);
//            circle.setGravity(Gravity.CENTER);
//            circle.setBackground(getShape(Color.parseColor("#cccccc")));
//            circle.setId(ctvid++);
////            circle.setPadding(0, 0, 0, 0);
////            circle.setTextColor(R.color.black);
//            circle.getPaint().setFakeBoldText(true);
//            circle.setTextSize(8);
//            blasthole_detail_ll.addView(circle);
//
//            //创建实体属性
//            att = new BlastholeAtt();
//            att.setPkid(circle.getId() + "");
//            att.setView(circle);
//            att.setX(x.get(a));
//            att.setY(y.get(a));
//            att.setChecked(false);
//            blastholeAtts.add(att);
//
//            circle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    if (isUploadGroup) {
//                        if (blastholeAtts.get(a).isChecked()) {
//                            circle.setBackground(getShape(Color.parseColor("#cccccc")));
//                            blastholeChecked.remove(blastholeAtts.get(a));
//                        } else {
//                            circle.setBackground(getShape(Color.parseColor("#000000")));
//                            blastholeChecked.add(blastholeAtts.get(a));
//                        }
//
//                        if (blastholeAtts.get(a).isChecked()) {
//                            blastholeAtts.get(a).setChecked(false);
//                        } else {
//                            blastholeAtts.get(a).setChecked(true);
//                        }
//                    } else {
//                        ShowToast.showShort(BlastholeDetail.this, "请先确认分组信息再添加炮孔信息");
//                    }
//                    //如果已经选中,再点击取消选中
////                    if (blastholeAtts.size() != 0) {
////                        for (BlastholeAtt bean : blastholeAtts) {
////                            if (bean.getView() == v) {
////                                blastholeAtts.remove(bean);
////                                circle.setBackgroundResource(R.drawable.blastholeimg);
////                            } else {
////                                ShowToast.showToastNowait(BlastholeDetail.this, "移除数组中第" + circle.getId());
////
////                                circle.setBackgroundResource(R.drawable.blastholeblack);
////                                blastholeAtts.add(att);
////                                removeX.add(att.getX());
////                                removeY.add(att.getY());
////                            }
////                        }
////                    } else {
////                        ShowToast.showToastNowait(BlastholeDetail.this, "移除数组中第" + circle.getId());
//////                        Log.e(TAG, groupX.size() + "----" + groupY.size());
//////                        Log.e(TAG, x.size() + "------" + y.size());
////                        circle.setBackgroundResource(R.drawable.blastholeblack);
////                        blastholeAtts.add(att);
////                        removeX.add(att.getX());
////                        removeY.add(att.getY());
////
////                    }
//                }
//            });
//        }
        getGroupData(projectId);
//        pkscroll.setRotationX(180);
    }


    private void GetViewHeight() {
        ll_pks.post(new Runnable() {
            @Override
            public void run() {
                ll_pks_height = ll_pks.getMeasuredHeight();
            }
        });

        ll_fzbtn.post(new Runnable() {
            @Override
            public void run() {
                ll_fzbtn_height = ll_fzbtn.getMeasuredHeight();
            }
        });

        zh_ll.post(new Runnable() {
            @Override
            public void run() {
                zh_ll_height = zh_ll.getMeasuredHeight();
            }
        });
    }

    private void getPmHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        PmHeight = dm.heightPixels;
    }


    /**
     * 获得分组接口数据
     */
    private void getGroupData(String id) {
        dialog = DialogUtil.createLoadingDialog(BlastholeDetail.this, R.string.loading_write);
        OkGo.<String>get(PortIpAddress.getBlastholeDetail())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("projectid", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);
                            //分组list集合
                            JSONArray fzlist = data.getJSONObject(0).getJSONArray("fzlist");
                            //炮孔list集合
                            JSONArray pklist = data.getJSONObject(0).getJSONArray("pklist");

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                blastholeAtts.clear();
                                pkList.clear();
                                blasthole_detail_groupingll.removeAllViews();
                                blasthole_detail_ll.removeAllViews();
                                //加载分组数据
                                for (int i = 0; i < fzlist.length(); i++) {
                                    BlastholeDetailBean.DataBean.FzlistBean bean = new BlastholeDetailBean.DataBean.FzlistBean();
                                    bean.setUuid(fzlist.optJSONObject(i).getString("uuid"));
                                    bean.setName(fzlist.optJSONObject(i).getString("name"));
                                    blastholeAtts.add(bean);
                                }

                                //加载炮孔位置信息
                                for (int i = 0; i < pklist.length(); i++) {
                                    BlastholeDetailBean.DataBean.PklistBean bean = new BlastholeDetailBean.DataBean.PklistBean();
                                    bean.setUuid(pklist.optJSONObject(i).getString("uuid"));
                                    bean.setAssayGroupId(pklist.optJSONObject(i).getString("assayGroupId"));
                                    bean.setX(pklist.optJSONObject(i).getString("x"));
                                    bean.setY(pklist.optJSONObject(i).getString("y"));
                                    pkList.add(bean);
                                }
                                Log.e("测试", pklist.length() + "");
                                //创建分组
                                initFzList(blastholeAtts);
                                //创建炮孔
                                initPkList(pkList);

                            } else {
                                ShowToast.showShort(BlastholeDetail.this, err);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(BlastholeDetail.this, R.string.connect_err);
                    }
                });
    }

    /**
     * 创建分组区
     */
    private void initFzList(final List<BlastholeDetailBean.DataBean.FzlistBean> list) {
        for (int i = 0; i < list.size(); i++) {
            final int a = i;
            final TextView tv = new TextView(this);
            lp.setMargins(20, 0, 20, 0);
            tv.setLayoutParams(lp);
            final int color = StatusBarUtils.GetColor();

            groupColor.put(list.get(i).getUuid(), color);

            tv.setBackgroundColor(color);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(20, 20, 20, 20);
            tv.setId(i + 1);
            tv.setText(list.get(i).getName() + "组");
            tv.setTextColor(Color.WHITE);

            //已经分好的组 不能继续点击操作
            if (sffz.equals("已分组")) {
                tv.setEnabled(false);
            } else {
                tv.setEnabled(true);
            }

            blasthole_detail_groupingll.addView(tv);
            //分组id作为键   名字作为值
            pkText.put(list.get(i).getUuid(), list.get(i).getName());

            //分组区点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (blastholeChecked.size() > 0) {
                        ShowToast.showShort(BlastholeDetail.this, "分组" + tv.getId());
                        //获得随机的颜色
                        int color = StatusBarUtils.GetColor();
                        tv.setEnabled(false);
                        tv.setBackgroundColor(color);
                        yfzGroup++;
                        for (BlastholeDetailBean.DataBean.PklistBean bean : blastholeChecked) {
                            bean.getView().setEnabled(false);
                            ((TextView) bean.getView()).setText(String.format("%02d", tv.getId()));
                            ((TextView) bean.getView()).setTextColor(Color.WHITE);
                            bean.getView().setBackground(getShape(color));
                        }
                        blastholeGrouped.addAll(blastholeChecked);
                        //这里存放组号和每组的炮孔信息
                        List<BlastholeDetailBean.DataBean.PklistBean> mlist = new ArrayList<BlastholeDetailBean.DataBean.PklistBean>();
                        mlist.addAll(blastholeChecked);
                        saveMessage.put(blastholeAtts.get(a), mlist);
                        blastholeChecked.clear();
                    } else {
                        ShowToast.showShort(BlastholeDetail.this, "请选择炮孔后再进行分组");
                    }
                }
            });
        }
        groupColor.put("null", Color.parseColor("#cccccc"));
    }

    /**
     * 创建炮孔区
     */
    private void initPkList(final List<BlastholeDetailBean.DataBean.PklistBean> list) {
//        pkview_height = PmHeight - (int) getResources().getDimension(R.dimen.x190);
//        - (ll_pks_height + ll_fzbtn_height + zh_ll_height)
        for (int i = 0; i < list.size(); i++) {
            int a = i;
            TextView circle = new TextView(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50, 50);
            float x = Float.parseFloat(list.get(i).getX());
            float mx = Math.round(x);
            int xx = (int) (mx * multiples);

            float y = Float.parseFloat(list.get(i).getY());
//            int yy = pkview_height - ((int) (y * multiples));
            float my = Math.round(y);
            int yy = (int) (my * multiples);
            lp.setMargins(xx, yy, 0, 0);
            Log.e("测试", xx + "---" + yy);
            circle.setLayoutParams(lp);
            circle.setGravity(Gravity.CENTER);
            list.get(i).setView(circle);

            String assid = list.get(i).getAssayGroupId();
            int color = groupColor.get(assid);
            circle.setBackground(getShape(color));
            circle.setText(pkText.get(list.get(i).getAssayGroupId()));
            circle.setId(ctvid++);
//          circle.setPadding(0, 0, 0, 0);
            circle.setTextColor(Color.WHITE);
            circle.getPaint().setFakeBoldText(true);
            circle.setTextSize(10);
            blasthole_detail_ll.addView(circle);

            circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (list.get(0).getAssayGroupId().equals("null")) {
                        if (list.get(a).isChecked()) {
                            //未选中状态 是灰色 从存放已选中的集合中移除这个
                            circle.setBackground(getShape(Color.parseColor("#cccccc")));
                            blastholeChecked.remove(list.get(a));
                        } else {
                            //状态选中  是黑色  添加到存放已选中的集合中
                            circle.setBackground(getShape(Color.parseColor("#000000")));
                            blastholeChecked.add(list.get(a));
                        }

                        if (list.get(a).isChecked()) {
                            list.get(a).setChecked(false);
                        } else {
                            list.get(a).setChecked(true);
                        }
                    }
                }
            });
        }
    }


    //获取shape设置
    private GradientDrawable getShape(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setUseLevel(false);
        shape.setColor(color);
        return shape;
    }


    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    @OnClick(R.id.save_all)
    void SaveAll() {
        if (pkList.get(0).getAssayGroupId().equals("null")) {
            //当创建的组 大于 已经分好的组数时 不能直接上传  必须先将创建的组分好才能上传保存
            if (blastholeAtts.size() > yfzGroup && pkList.size() > blastholeGrouped.size()) {
                ShowToast.showShort(this, "请先将分好的组配置好炮孔信息");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.isSave);
                builder.setMessage(R.string.SaveMessage);
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存分组信息
                        SaveInfo();
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }
    }

    /**
     * 保存分组信息
     */
    private void SaveInfo() {
        OkGo.<String>get(PortIpAddress.SaveFzInfo())
                .tag(this)
                .params("projectid", projectId)
                .params("fzinfoList", setString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String mes = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                getGroupData(projectId);
                                ShowToast.showShort(BlastholeDetail.this, mes);
                            } else {
                                ShowToast.showShort(BlastholeDetail.this, mes);
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


//    @OnClick(R.id.upload_group)
//    void UpLoadGroup() {
//        if (blasthole_detail_groupingll.getChildCount() > 0) {
//            upLoadGroup();
//        } else {
//            ShowToast.showShort(this, "请先添加分组再进行上传");
//        }
//    }

    /**
     * 上传分组信息
     */
    private void upLoadGroup() {
        Gson gson = new Gson();
        String fzList = gson.toJson(blastholeAtts);

        OkGo.<String>get(PortIpAddress.upLoadGroupInfo())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, PortIpAddress.getUserId(this))
                .params("fzList", fzList)
                .params("projectid", projectId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String mes = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                ShowToast.showShort(BlastholeDetail.this, mes);
                            } else {
                                ShowToast.showShort(BlastholeDetail.this, mes);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(BlastholeDetail.this, R.string.connect_err);
                    }
                });
    }


    /**
     * 分组按钮
     */
    @OnClick(R.id.blasthole_detail_grouping)
    void Grouping() {
//        if (blasthole_detail_groupll.getChildCount() == 0) {
//            ShowToast.showToastNowait(this, "请选择炮孔后再进行分组操作");
//        } else {
//            groupList.add(blastholeAtts);
//            groupX.remove(removeX);
//            groupY.remove(removeY);
//
//            removeX.clear();
//            removeY.clear();
//            TextView tv = new TextView(this);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(20, 0, 20, 0);
//            tv.setLayoutParams(lp);
//            tv.setBackgroundColor(R.color.gray_deep);
//            tv.setGravity(Gravity.CENTER);
//            tv.setPadding(20, 20, 20, 20);
//            tv.setText(att.getGroupName());
//            blasthole_detail_groupingll.addView(tv);
//            blasthole_detail_groupll.removeAllViews();
//            groupId++;
//
//            //组号展示区点击事件
////            tv.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    blasthole_detail_groupingll.removeView(v);
////                    for (BlastholeAtt bean : blastholeAtts) {
////                        bean.getView().setEnabled(true);
////                        bean.getView().setBackgroundResource(R.drawable.blastholeimg);
////                        ((TextView) bean.getView()).setText("");
////                    }
////                    groupId--;
////                }
////            });
//        }----------------------------
//        final TextView tv = new TextView(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(20, 0, 20, 0);
//        tv.setLayoutParams(lp);
//        tv.setBackgroundColor(R.color.gray_deep);
//        tv.setGravity(Gravity.CENTER);
//        tv.setPadding(20, 20, 20, 20);
//        tv.setId(gid);
//        tv.setText("分组" + tv.getId());
//        tv.setTextColor(Color.WHITE);
//        gid++;
//        blasthole_detail_groupingll.addView(tv);
//
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (blastholeChecked.size() > 0) {
//                    ShowToast.showShort(BlastholeDetail.this, "点击的是" + tv.getId());
//                    //获得随机的颜色
//                    int color = StatusBarUtils.GetColor();
//                    tv.setEnabled(false);
//                    tv.setBackgroundColor(color);
//                    //分好组的炮孔
//                    blastholeGrouped.addAll(blastholeChecked);
//
//                    for (BlastholeAtt bean : blastholeChecked) {
//                        bean.getView().setEnabled(false);
//                        ((TextView) bean.getView()).setText("G" + tv.getId());
//                        ((TextView) bean.getView()).setTextColor(Color.WHITE);
//                        bean.getView().setBackground(getShape(color));
//                    }
//                    grouped++;
//                    groupId++;
//                    //这里存放组号和每组的炮孔信息
//                    saveMessage.put(tv, blastholeChecked);
//                    blastholeChecked.clear();
//                } else {
//                    ShowToast.showShort(BlastholeDetail.this, "请选择炮孔后再进行分组");
//                }
//            }
//        });


//        final TextView tv = new TextView(this);
//        lp.setMargins(20, 0, 20, 0);
//        tv.setLayoutParams(lp);
//        tv.setBackgroundColor(R.color.gray_deep);
//        tv.setGravity(Gravity.CENTER);
//        tv.setPadding(20, 20, 20, 20);
//        tv.setTextColor(Color.WHITE);
//        tv.setId(blastholeAtts.size() + fzid);
//        tv.setText("分组" + tv.getId());
//        fzid++;
//        blasthole_detail_groupingll.addView(tv);

//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (blastholeChecked.size() > 0) {
//                    ShowToast.showShort(BlastholeDetail.this, "点击的是" + tv.getId());
//                    //获得随机的颜色
//                    int color = StatusBarUtils.GetColor();
//                    tv.setEnabled(false);
//                    tv.setBackgroundColor(color);
//                    //分好组的炮孔
//                    blastholeGrouped.addAll(blastholeChecked);
//
//                    for (BlastholeAtt bean : blastholeChecked) {
//                        bean.getView().setEnabled(false);
//                        ((TextView) bean.getView()).setText("G" + tv.getId());
//                        ((TextView) bean.getView()).setTextColor(Color.WHITE);
//                        bean.getView().setBackground(getShape(color));
//                    }
//                    grouped++;
//                    groupId++;
//                    //这里存放组号和每组的炮孔信息
//                    saveMessage.put(tv, blastholeChecked);
//                    blastholeChecked.clear();
//                } else {
//                    ShowToast.showShort(BlastholeDetail.this, "请选择炮孔后再进行分组");
//                }
//            }
//        });
        if (pkList.get(0).getAssayGroupId().equals("null")) {
            crateGroup();
        }
    }

    /**
     * 创建分组
     */
    private void crateGroup() {
        OkGo.<String>get(PortIpAddress.upLoadGroupInfo())
                .tag(this)
                .params("projectid", projectId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String mes = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                ShowToast.showShort(BlastholeDetail.this, mes);
                                getGroupData(projectId);
                            } else {
                                ShowToast.showShort(BlastholeDetail.this, mes);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(BlastholeDetail.this, R.string.connect_err);
                    }
                });
    }


    @OnClick(R.id.clear_group)
    void ClearGroup() {
        if (pkList.get(0).getAssayGroupId().equals("null")) {
            if (blastholeGrouped.size() > 0) {
                saveMessage.clear();
                groupColor.clear();
                pkText.clear();

                for (BlastholeDetailBean.DataBean.PklistBean bean : blastholeGrouped) {
                    ((TextView) bean.getView()).setText("");
                    bean.getView().setBackground(getShape(Color.parseColor("#cccccc")));
                    bean.getView().setEnabled(true);
                    bean.setChecked(false);
                }


                for (int i = 0; i < blasthole_detail_groupingll.getChildCount(); i++) {
                    TextView tv = (TextView) blasthole_detail_groupingll.getChildAt(i);
                    tv.setEnabled(true);
                }
                blastholeGrouped.clear();
                ShowToast.showShort(this, "清空分组信息");


            } else {
                ShowToast.showShort(this, "当前没有分组信息");
            }
        }
    }


    /**
     * 拼接上传的json字符串
     *
     * @return
     */
    private String setString() {
        StringBuffer sf = new StringBuffer();
        sf.append("[");
        for (BlastholeDetailBean.DataBean.FzlistBean bean : saveMessage.keySet()) {
            sf.append("{");
            sf.append("\"fzuuid\":");
            sf.append(bean.getUuid() + ",");
            sf.append("\"pklist\":");
            sf.append("[");
            List<BlastholeDetailBean.DataBean.PklistBean> list = saveMessage.get(bean);
            for (int i = 0; i < list.size(); i++) {
                sf.append("\"");
                sf.append(list.get(i).getUuid());

                if (i != list.size() - 1) {
                    sf.append("\",");
                } else {
                    sf.append("\"");
                }
            }
            sf.append("]");
            sf.append("},");
        }

        //接收全部的炮孔集合
        List<BlastholeDetailBean.DataBean.PklistBean> wfzpkList = new ArrayList<>();
        wfzpkList.addAll(pkList);
        wfzpkList.removeAll(blastholeGrouped);

        for (int i = 0; i < wfzpkList.size(); i++) {
            sf.append("{");
            sf.append("\"fzuuid\":\"\",");
            sf.append("\"pklist\":");
            sf.append("[\"");
            sf.append(wfzpkList.get(i).getUuid());
            sf.append("\"]");
            sf.append("},");
        }
        String str = sf.toString();
        str.substring(0, str.length() - 1);
        str = str + "]";
        return str;
    }

}
