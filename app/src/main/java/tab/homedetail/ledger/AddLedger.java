package tab.homedetail.ledger;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.conchapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;

public class AddLedger extends BaseActivity {
    @BindView(R.id.add_ledger_equipment)
    TextView add_ledger_equipment;
    /**
     * popup窗口里的ListView
     */
    private ListView mTypeLv;
    /**
     * popup窗口
     */
    private PopupWindow typeSelectPopup;
    /**
     * 模拟的假数据
     */
    private List<String> testData;
    /**
     * 数据适配器
     */
    private ArrayAdapter<String> testDataAdapter;
    private DisplayMetrics metrics;
    @BindView(R.id.add_ledger_runtime)
    EditText add_ledger_runtime;
    @BindView(R.id.add_ledger_faulttime)
    EditText add_ledger_faulttime;
    @BindView(R.id.add_ledger_oilconsume)
    EditText add_ledger_oilconsume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ledger);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    protected void initData() {
        mTypeLv = new ListView(this);
        TestData();
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.popup_text_item, testData);
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
     * 设备选择
     */
    @OnClick(R.id.add_ledger_equipment)
    void Equipment() {
        initSelectPopup();
        // 使用isShowing()检查popup窗口是否在显示状态
        if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                int[] location = new int[2];
                add_ledger_equipment.getLocationOnScreen(location);
                int y = location[1];
                typeSelectPopup.showAtLocation(
                        add_ledger_equipment, Gravity.NO_GRAVITY, (int) ((metrics.widthPixels / 1) * 0.3) + 10, y + (int) getResources().getDimension(R.dimen.x30));
            } else {
                typeSelectPopup.showAsDropDown(add_ledger_equipment);
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
                String value = testData.get(position);
                // 把选择的数据展示对应的TextView上
                add_ledger_equipment.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, add_ledger_equipment.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
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
     * 模拟假数据
     */
    private void TestData() {
        testData = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            String str = new String("数据" + i);
            testData.add(str);
        }
        add_ledger_equipment.setText(testData.get(0));
    }
}
