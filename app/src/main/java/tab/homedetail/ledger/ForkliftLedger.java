package tab.homedetail.ledger;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.conchapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.BaseActivity;


/**
 * 铲车台账
 */
public class ForkliftLedger extends BaseActivity {
    //生产日期
    @BindView(R.id.production_date)
    TextView production_date;
    //班次
    @BindView(R.id.shift)
    TextView shift;
    //生产工序
    @BindView(R.id.production_process)
    TextView production_process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forklift_ledger);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {

    }
}
