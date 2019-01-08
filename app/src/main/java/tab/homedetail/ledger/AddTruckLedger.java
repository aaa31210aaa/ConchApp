package tab.homedetail.ledger;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.conchapp.R;
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

import bean.DeviceBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;

public class AddTruckLedger extends BaseActivity {
    @BindView(R.id.add_truck_ledger_equipment)
    TextView add_truck_ledger_equipment;
    @BindView(R.id.add_truck_ledger_runningtime)
    EditText add_truck_ledger_runningtime;
    @BindView(R.id.add_truck_ledger_failuretime)
    EditText add_truck_ledger_failuretime;
    @BindView(R.id.add_truck_ledger_dieselconsumption)
    EditText add_truck_ledger_dieselconsumption;
    @BindView(R.id.title_name)
    TextView title_name;
    /**
     * popup窗口里的ListView
     */
    private ListView mTypeLv;
    /**
     * popup窗口
     */
    private PopupWindow typeSelectPopup;
    /**
     * 设备数据集
     */
    private List<String> deviceData;
    /**
     * 设备数据map
     */
    private Map<String, String> deviceMap;

    /**
     * 数据适配器
     */
    private ArrayAdapter<String> testDataAdapter;
    private DisplayMetrics metrics;
    private String tag = "";
    private String bookid = "";   //台帐id
    private String workdate = ""; //日期
    private String deviceid = ""; //设备
    private String driver = "";  //用户id
    private String teamgroupid = ""; //班组id
    private String classes = ""; //班次
    private String runningtime = "0"; //运行时间
    private String faulttime = "0"; //故障时间
    private String useamount = "0"; //柴油消耗
    private String useid = ""; //柴油消耗id
    private String procid = ""; //工序id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck_ledger);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        tag = intent.getStringExtra("titleName");
        title_name.setText(tag);
        workdate = intent.getStringExtra("workdate");
        driver = intent.getStringExtra("driver");
        teamgroupid = intent.getStringExtra("teamgroupid");
        classes = intent.getStringExtra("classes");

        if (tag.equals("修改")){
            add_truck_ledger_equipment.setText(intent.getStringExtra("deviceid"));
            add_truck_ledger_runningtime.setText(intent.getStringExtra("runningtime"));
            add_truck_ledger_failuretime.setText(intent.getStringExtra("failuretime"));
            add_truck_ledger_dieselconsumption.setText(intent.getStringExtra("useamount"));
        }


        procid = intent.getStringExtra("procid");
        mTypeLv = new ListView(this);
        //获取设备数据
        Data();
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.popup_text_item, deviceData);
        mTypeLv.setAdapter(testDataAdapter);
        //获取屏幕宽度
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 新增
     */
    @OnClick(R.id.submit)
    void SubmitAdd() {
        if (add_truck_ledger_runningtime.getText().toString().equals("")) {
            ShowToast.showShort(this, "请填写运行时间");
        } else if (add_truck_ledger_failuretime.getText().toString().equals("")) {
            ShowToast.showShort(this, "请填写故障时间");
        } else {
            OkGo.<String>get(PortIpAddress.SaveTruckDeviceList())
                    .tag(TAG)
                    .params("bean.bookid", bookid)
                    .params("bean.workdate",workdate)
                    .params("bean.deviceid", deviceMap.get(add_truck_ledger_equipment.getText().toString()))
                    .params("bean.driver", driver)
                    .params("bean.teamgroupid", teamgroupid)
                    .params("bean.classes", classes)
                    .params("bean.runningtime", add_truck_ledger_runningtime.getText().toString())
                    .params("bean.faulttime", add_truck_ledger_failuretime.getText().toString())
                    .params("bean.useamount", add_truck_ledger_dieselconsumption.getText().toString())
                    .params("bean.useid", useid)
                    .params("bean.procid", procid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e(TAG, response.body().toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                                String err = jsonObject.getString(MESSAGE);

                                if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                    ShowToast.showShort(AddTruckLedger.this, "新增成功");
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    ShowToast.showShort(AddTruckLedger.this, err);
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

    /**
     * 设备选择
     */
    @OnClick(R.id.add_truck_ledger_equipment)
    void Equipment() {
        initSelectPopup();
        // 使用isShowing()检查popup窗口是否在显示状态
        if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                int[] location = new int[2];
                add_truck_ledger_equipment.getLocationOnScreen(location);
                int y = location[1];
                typeSelectPopup.showAtLocation(add_truck_ledger_equipment, Gravity.NO_GRAVITY, (int) ((metrics.widthPixels / 1) * 0.3) + 10, y + (int) getResources().getDimension(R.dimen.x30));
            } else {
                typeSelectPopup.showAsDropDown(add_truck_ledger_equipment);
            }
        }
    }


    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = deviceData.get(position);
                // 把选择的数据展示对应的TextView上
                add_truck_ledger_equipment.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, add_truck_ledger_equipment.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.editext_yb);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }


    /**
     * 设备数据
     */
    private void Data() {
        deviceData = new ArrayList<>();
        deviceMap = new HashMap<>();
        dialog = DialogUtil.createLoadingDialog(AddTruckLedger.this, R.string.loading);

        OkGo.<String>get(PortIpAddress.TruckDeviceList())
                .tag(TAG)
                .params("teamgroupid", PortIpAddress.getTeamId(this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                DeviceBean bean = new DeviceBean();
                                bean.setDeviceid(jsonArray.optJSONObject(i).getString("deviceid"));
                                bean.setDevname(jsonArray.optJSONObject(i).getString("devname"));
                                deviceMap.put(bean.getDevname(), bean.getDeviceid());
                                deviceData.add(bean.getDevname());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (tag.equals("新增")) {
                            add_truck_ledger_equipment.setText(deviceData.get(0));
                        } else {

                        }

                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(AddTruckLedger.this, R.string.connect_dateErr);
                    }
                });

    }

}
