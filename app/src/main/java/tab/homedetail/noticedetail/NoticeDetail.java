package tab.homedetail.noticedetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.conchapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;

public class NoticeDetail extends BaseActivity {
    @BindView(R.id.notice_detail_tilte)
    TextView notice_detail_tilte;
    @BindView(R.id.notice_detail_time)
    TextView notice_detail_time;
    @BindView(R.id.notice_detail_content)
    TextView notice_detail_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        notice_detail_tilte.setText(intent.getStringExtra("messagetitle"));
        notice_detail_time.setText(intent.getStringExtra("mestime"));
        notice_detail_content.setText(intent.getStringExtra("mescontent"));
    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }
}
