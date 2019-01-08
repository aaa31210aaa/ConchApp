package tab;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.administrator.conchapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.formdetail.FormDetail;
import tab.formdetail.SbyxFormDetail;
import tab.formdetail.XhtjFormDetail;
import utils.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Form extends BaseFragment {
    private View view;
    private Intent intent;
    public static String SCTJDAY = "sctjday";
    public static String SCTJWEEK = "sctjweek";
    public static String SCTJMONTH = "sctjmonth";
    public static String SBYXDAY = "sbyxday";
    public static String SBYXWEEK = "sbyxweek";
    public static String SBYXMONTH = "sbyxmonth";
    public static String XHTJDAY = "xhtjday";
    public static String XHTJWEEK = "xhtjweek";
    public static String XHTJMONTH = "xhtjmonth";

    public Form() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_form, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {

    }

    //生产统计日报
    @OnClick(R.id.production_dayform)
    void ProDayForm() {
        intent = new Intent(getActivity(), FormDetail.class);
        intent.putExtra("type", SCTJDAY);
        startActivity(intent);
    }

    //生产统计周报
    @OnClick(R.id.production_weekform)
    void ProWeekForm() {
        intent = new Intent(getActivity(), FormDetail.class);
        intent.putExtra("type", SCTJWEEK);
        startActivity(intent);
    }

    //生产统计月报
    @OnClick(R.id.production_monthform)
    void ProMonthForm() {
        intent = new Intent(getActivity(), FormDetail.class);
        intent.putExtra("type", SCTJMONTH);
        startActivity(intent);
    }

    //设备运行统计日报
    @OnClick(R.id.equipment_dayform)
    void EquiDayForm() {
        intent = new Intent(getActivity(), SbyxFormDetail.class);
        intent.putExtra("type", SBYXDAY);
        startActivity(intent);
    }

    //设备运行统计周报
    @OnClick(R.id.equipment_weekform)
    void EquiWeekForm() {
        intent = new Intent(getActivity(), SbyxFormDetail.class);
        intent.putExtra("type", SBYXWEEK);
        startActivity(intent);
    }

    //设备运行统计月报
    @OnClick(R.id.equipment_monthform)
    void EquiMonthForm() {
        intent = new Intent(getActivity(), SbyxFormDetail.class);
        intent.putExtra("type", SBYXMONTH);
        startActivity(intent);
    }

    //消耗统计日报
    @OnClick(R.id.consume_dayform)
    void ConsDayForm() {
        intent = new Intent(getActivity(), XhtjFormDetail.class);
        intent.putExtra("type", XHTJDAY);
        startActivity(intent);
    }

    //消耗统计周报
    @OnClick(R.id.consume_weekform)
    void ConsWeekForm() {
        intent = new Intent(getActivity(), XhtjFormDetail.class);
        intent.putExtra("type", XHTJWEEK);
        startActivity(intent);
    }

    //消耗统计月报
    @OnClick(R.id.consume_monthform)
    void ConsMonthForm() {
        intent = new Intent(getActivity(), XhtjFormDetail.class);
        intent.putExtra("type", XHTJMONTH);
        startActivity(intent);
    }


}
