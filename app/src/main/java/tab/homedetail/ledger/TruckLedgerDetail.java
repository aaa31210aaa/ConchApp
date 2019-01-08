package tab.homedetail.ledger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.conchapp.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;

import static tab.homedetail.ledger.PerforationLedger.REQUEST_CODE;

public class TruckLedgerDetail extends BaseActivity {
    @BindView(R.id.search_edittext)
    EditText search_edittext;
    @BindView(R.id.search_clear)
    ImageView search_clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truckledger_detail);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    protected void initData() {

    }

    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 添加卡车作业量
     */
    @OnClick(R.id.add_fbt)
    void AddTruckWork() {
        startActivityForResult(new Intent(this, AddTruckWork.class), REQUEST_CODE);
    }


}
