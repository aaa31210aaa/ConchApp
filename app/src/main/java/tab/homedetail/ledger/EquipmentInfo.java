package tab.homedetail.ledger;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.conchapp.R;

import utils.BaseFragment;

/**
 * 设备信息
 */
public class EquipmentInfo extends BaseFragment {


    public EquipmentInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equipment_info, container, false);
    }

    @Override
    public View makeView() {
        return null;
    }

    @Override
    protected void loadData() {

    }

}
