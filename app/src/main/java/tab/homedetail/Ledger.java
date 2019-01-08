package tab.homedetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.conchapp.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.homedetail.ledger.AuxiliaryLedger;
import tab.homedetail.ledger.BlastLedger;
import tab.homedetail.ledger.CrusherLedgerElectricity;
import tab.homedetail.ledger.CrusherLedgerProduction;
import tab.homedetail.ledger.ForkliftLedger;
import tab.homedetail.ledger.PerforationLedger;
import tab.homedetail.ledger.TransLedger;
import tab.homedetail.ledger.TruckLedger;
import utils.BaseActivity;
import utils.PortIpAddress;

public class Ledger extends BaseActivity {
    @BindView(R.id.perforation_ledger)
    LinearLayout perforation_ledger;
    @BindView(R.id.forklift_ledger)
    LinearLayout forklift_ledger;
    @BindView(R.id.truck_ledger)
    LinearLayout truck_ledger;
    @BindView(R.id.blast_ledger)
    LinearLayout blast_ledger;
    @BindView(R.id.crusher_ledger_production)
    LinearLayout crusher_ledger_production;
    @BindView(R.id.crusher_ledger_electricity)
    LinearLayout crusher_ledger_electricity;
    @BindView(R.id.transfer_ledger)
    LinearLayout transfer_ledger;
    private String teamId = "";
    //固定的工序id
    public static String ProcessId = "";
    public static Map<String, String> spinner_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        teamId = PortIpAddress.getLedgerName(this);
        if (teamId.equals("卡车和矿石转运")) {
            truck_ledger.setVisibility(View.VISIBLE);
            transfer_ledger.setVisibility(View.VISIBLE);
        } else if (teamId.equals("破碎台帐(生产)")) {
            crusher_ledger_production.setVisibility(View.VISIBLE);
        } else if (teamId.equals("破碎台帐(局控)")) {
            crusher_ledger_electricity.setVisibility(View.VISIBLE);
        } else if (teamId.equals("穿孔和爆破")) {
            perforation_ledger.setVisibility(View.VISIBLE);
            blast_ledger.setVisibility(View.VISIBLE);
        } else if (teamId.equals("铲装台帐")) {
            forklift_ledger.setVisibility(View.VISIBLE);
        }
        spinner_map = new HashMap<>();
        spinner_map.put("白班", "BC001");
        spinner_map.put("中班", "BC002");
        spinner_map.put("晚班", "BC003");

    }


    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 穿孔台账
     */
    @OnClick(R.id.perforation_ledger)
    void Perforation() {
        Intent intent = new Intent(this, PerforationLedger.class);
        ProcessId = "289e476b86af45b2800ae099d5477853";
        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }

    /**
     * 铲车台账
     */
    @OnClick(R.id.forklift_ledger)
    void Forklift() {
        Intent intent = new Intent(this, ForkliftLedger.class);
        ProcessId = "5e2b6e92a15747d39d314e0b245c6a6e";
        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }

    /**
     * 卡车运输台账
     */
    @OnClick(R.id.truck_ledger)
    void Truck() {
        Intent intent = new Intent(this, TruckLedger.class);
        ProcessId = "82087e58b3924c55b0e542b8ee87e6a2";
//        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }

    /**
     * 爆破台账
     */
    @OnClick(R.id.blast_ledger)
    void Blast() {
        Intent intent = new Intent(this, BlastLedger.class);
        ProcessId = "e8503e3acec849e287165824a182121c";
        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }


//    /**
//     * 转子台账
//     */
//    @OnClick(R.id.rotor_ledger)
//    void Rotor(){
//        startActivity(new Intent(this, RotorLedger.class));
//    }

    /**
     * 破碎机台账(生产)  只能签到 不能填
     */
    @OnClick(R.id.crusher_ledger_production)
    void Production() {
        Intent intent = new Intent(this, CrusherLedgerProduction.class);
        ProcessId = "f3554fc251d4408bba852eadb85ae2c2";
        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }


    /**
     * 破碎机台账(局控)  能填
     */
    @OnClick(R.id.crusher_ledger_electricity)
    void Electricity() {
        Intent intent = new Intent(this, CrusherLedgerElectricity.class);
        ProcessId = "f3554fc251d4408bba852eadb85ae2c2";
        intent.putExtra("ProcessId", ProcessId);
        startActivity(intent);
    }

    /**
     * 矿石转运台帐
     */
    @OnClick(R.id.transfer_ledger)
    void TransLedger() {
        startActivity(new Intent(this, TransLedger.class));
    }

    /**
     * 辅助产量台帐
     */
    @OnClick(R.id.auxiliary_ledger)
    void AuxiliaryLedger() {
        startActivity(new Intent(this, AuxiliaryLedger.class));
    }

}
