package tab.homedetail.earlywarningdetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.conchapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;

public class AlarmDetail extends BaseActivity {
    @BindView(R.id.alarm_detail_title)
    TextView alarm_detail_title;
    @BindView(R.id.alarm_detail_tilte)
    TextView alarm_detail_tilte;
    @BindView(R.id.alarm_detail_lv)
    TextView alarm_detail_lv;
    @BindView(R.id.alarm_detail_time)
    TextView alarm_detail_time;
    @BindView(R.id.alarm_detail_memo)
    TextView alarm_detail_memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String ym = intent.getStringExtra("ym");
        if (ym.equals("EarlyWarning")) {
            alarm_detail_title.setText("预警详情");
        } else {
            alarm_detail_title.setText("告警详情");
        }
        alarm_detail_tilte.setText(intent.getStringExtra("alarmtitle"));
        alarm_detail_lv.setText(intent.getStringExtra("alarmlevelname"));
        alarm_detail_time.setText("发布时间:" + intent.getStringExtra("addtime"));
        alarm_detail_memo.setText(intent.getStringExtra("memo"));


    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

}
