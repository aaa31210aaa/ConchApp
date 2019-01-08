package tab;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.conchapp.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adpter.ViewPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import tab.prodetail.MonthPlan;
import tab.prodetail.QuarterPlan;
import tab.prodetail.YearPlan;
import utils.BaseFragment;
import utils.ColorFlipPagerTitleView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Production extends BaseFragment {
    private static final String[] CHANNELS = new String[]{"年计划", "季度计划", "月计划"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private View view;
    @BindView(R.id.production_indicator)
    MagicIndicator production_indicator;
    @BindView(R.id.production_viewpager)
    ViewPager production_viewpager;

    public static final String KCID = "164ee6b76c584e5fa56455551088c643";
    public static final String CJID = "840011789c17406d80cb80803d434563";
    public static final String SLPHID = "787a6765f4c5462bb869b0b5a5cb32c3";
    public static final String ZLPHID = "3e554a41354d4a2780f61d03ea51343b";


    //年计划
//    @BindView(R.id.production_yplan)
//    LinearLayout production_yplan;
//    //季度计划
//    @BindView(R.id.production_qplan)
//    LinearLayout production_qplan;
//    //月计划
//    @BindView(R.id.production_mplan)
//    LinearLayout production_mplan;
    private LinearLayout.LayoutParams lps;
    private LinearLayout.LayoutParams lps2;

    public Production() {

    }


    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_production, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new YearPlan());
        fragments.add(new QuarterPlan());
        fragments.add(new MonthPlan());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), fragments);
        production_viewpager.setOffscreenPageLimit(2);
        production_viewpager.setAdapter(adapter);
        initMagicIndicator();
        lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading);
//        getYplan();
//        getQplan();
//        getMplan();

    }

    /**
     * 获取年计划
     */
//    private void getYplan() {
//        OkGo.<String>get(PortIpAddress.Yplan())
//                .tag(this)
//                .params("access_token", PortIpAddress.getToken(getActivity()))
//                .params("userid", PortIpAddress.getUserId(getActivity()))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        try {
//                            Log.e(TAG, response.body().toString());
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
//                            LinearLayout.LayoutParams tvlps = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                final String techid = jsonArray.optJSONObject(i).getString("techid");
//                                final String procid = jsonArray.optJSONObject(i).getString("procid");
////                                if (type.equals(KCID)||type.equals(CJID)||type.equals(ZLPHID)){
//
//                                if (procid.equals(KCID) || procid.equals(CJID) || procid.equals(ZLPHID)) {
//                                    LinearLayout llh = new LinearLayout(getActivity());
//                                    llh.setOrientation(LinearLayout.HORIZONTAL);
//                                    llh.setLayoutParams(lps);
//                                    llh.setGravity(Gravity.CENTER);
//                                    LinearLayout llv = new LinearLayout(getActivity());
//                                    llv.setPadding(10, 10, 10, 10);
//                                    llv.setOrientation(LinearLayout.VERTICAL);
//                                    llv.setLayoutParams(lps);
//                                    llv.setGravity(Gravity.CENTER);
//
//                                    LinearLayout.LayoutParams layoutps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    layoutps.setMargins(10, 0, 10, 0);
//                                    HorizontalScrollView htv = new HorizontalScrollView(getActivity());
//                                    htv.setLayoutParams(layoutps);
//                                    LinearLayout llv2 = new LinearLayout(getActivity());
//                                    llv2.setOrientation(LinearLayout.VERTICAL);
//                                    llv2.setLayoutParams(lps);
//                                    llv2.setGravity(Gravity.CENTER);
//
//
//                                    LinearLayout llv2Top = new LinearLayout(getActivity());
//                                    llv2Top.setOrientation(LinearLayout.HORIZONTAL);
//                                    llv2Top.setGravity(Gravity.CENTER);
//                                    llv2Top.setLayoutParams(lps);
//                                    LinearLayout llv2Down = new LinearLayout(getActivity());
//                                    llv2Down.setOrientation(LinearLayout.HORIZONTAL);
//                                    llv2Down.setGravity(Gravity.CENTER);
//                                    llv2Down.setLayoutParams(lps);
//
//                                    ImageView image = new ImageView(getActivity());
//                                    TextView tv = new TextView(getActivity());
//
//                                    image.setLayoutParams(lps);
//                                    tv.setLayoutParams(lps);
//                                    if (procid.equals(KCID)) {
//                                        image.setBackgroundResource(R.mipmap.ic_launcher);
//                                        tv.setText("开采");
//                                    } else if (procid.equals(CJID)) {
//                                        image.setBackgroundResource(R.mipmap.ic_launcher);
//                                        tv.setText("采掘");
//                                    } else if (procid.equals(ZLPHID)) {
//                                        image.setBackgroundResource(R.mipmap.ic_launcher);
//                                        tv.setText("质量平衡");
//                                    }
//                                    llv.addView(image);
//                                    llv.addView(tv);
//
//                                    JSONArray index = jsonArray.optJSONObject(i).getJSONArray("index");
//                                    for (int j = 0; j < index.length(); j++) {
//                                        TextView tvName = new TextView(getActivity());
//                                        TextView tvValue = new TextView(getActivity());
//                                        tvName.setLayoutParams(tvlps);
//                                        tvName.setPadding(10, 10, 10, 10);
//                                        tvName.setGravity(Gravity.CENTER);
//                                        tvName.setText(index.optJSONObject(j).getString("name"));
//                                        tvValue.setLayoutParams(tvlps);
//                                        tvValue.setPadding(10, 10, 10, 10);
//                                        tvValue.setGravity(Gravity.CENTER);
//                                        tvValue.setText(index.optJSONObject(j).getString("value"));
//                                        tvName.setBackgroundResource(R.drawable.editext_yb);
//                                        tvValue.setBackgroundResource(R.drawable.editext_yb);
//                                        llv2Top.addView(tvName);
//                                        llv2Down.addView(tvValue);
//                                    }
//                                    llv2.addView(llv2Top);
//                                    llv2.addView(llv2Down);
//                                    htv.addView(llv2);
//                                    llh.addView(llv);
//                                    llh.addView(htv);
//
//                                    llh.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (procid.equals(KCID)) {
//                                                Intent intent = new Intent(getActivity(), KcjhDetail.class);
//                                                intent.putExtra("type", "year");
//                                                intent.putExtra("techid", techid);
//                                                intent.putExtra("procid", procid);
//                                                startActivity(intent);
//                                            } else if (procid.equals(CJID)) {
//                                                Intent intent = new Intent(getActivity(), CjjhDetail.class);
//                                                intent.putExtra("type", "year");
//                                                intent.putExtra("techid", techid);
//                                                intent.putExtra("procid", procid);
//                                                startActivity(intent);
//                                            } else if (procid.equals(ZLPHID)) {
//                                                Intent intent = new Intent(getActivity(), ZlphDetail.class);
//                                                intent.putExtra("type", "year");
//                                                intent.putExtra("techid", techid);
//                                                intent.putExtra("procid", procid);
//                                                startActivity(intent);
//                                            }
//                                        }
//                                    });
//                                    production_yplan.addView(llh);
//                                } else if (procid.equals(SLPHID)) {
//                                    //动态创建三两平衡表格
//                                    TableLayout tableLayout = new TableLayout(getActivity());
//                                    tableLayout.setLayoutParams(lps);
////                                    tableLayout.setStretchAllColumns(true);
//
//                                }
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        ShowToast.showShort(getActivity(), R.string.connect_err);
//                    }
//                });
//    }

    /**
     * 获取季度计划
     */
//    private void getQplan() {
//        OkGo.<String>get(PortIpAddress.Qplan())
//                .tag(this)
//                .params("access_token", PortIpAddress.getToken(getActivity()))
//                .params("userid", PortIpAddress.getUserId(getActivity()))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        try {
//                            Log.e(TAG, response.body().toString());
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        ShowToast.showShort(getActivity(), R.string.connect_err);
//                    }
//                });
//    }

    /**
     * 获取月计划
     */
//    private void getMplan() {
//        OkGo.<String>get(PortIpAddress.Mplan())
//                .tag(this)
//                .params("access_token", PortIpAddress.getToken(getActivity()))
//                .params("userid", PortIpAddress.getUserId(getActivity()))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        try {
//                            dialog.dismiss();
//                            Log.e(TAG, response.body().toString());
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        dialog.dismiss();
//                        ShowToast.showShort(getActivity(), R.string.connect_err);
//                    }
//                });
//    }

//    /**
//     * 年计划点击事件
//     */
//    @OnClick(R.id.yzlph_ll)
//    void YZlphClick() {
//        startActivity(new Intent(getActivity(), ZlphDetail.class));
//    }
//
//    @OnClick(R.id.yplan_cjjh)
//    void YCjjhClick() {
//        startActivity(new Intent(getActivity(), CjjhDetail.class));
//    }
//
//    /**
//     * 季度计划点击事件
//     */
//    @OnClick(R.id.qzlph_ll)
//    void QZlphClick() {
//        startActivity(new Intent(getActivity(), ZlphDetail.class));
//    }
//
//    @OnClick(R.id.qplan_cjjh)
//    void QCjjhClick() {
//        startActivity(new Intent(getActivity(), CjjhDetail.class));
//    }
//
//    /**
//     * 月计划点击事件
//     */
//    @OnClick(R.id.mzlph_ll)
//    void MZlphClick() {
//        startActivity(new Intent(getActivity(), ZlphDetail.class));
//    }
//
//    @OnClick(R.id.mplan_cjjh)
//    void MCjjhClick() {
//        startActivity(new Intent(getActivity(), CjjhDetail.class));
//    }


    /**
     * 设置tablayout
     */
    private void initMagicIndicator() {
        production_indicator.setBackgroundColor(Color.parseColor("#455a64"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        production_viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                indicator.setLineHeight(UIUtil.dip2px(context, 6));
//                indicator.setLineWidth(UIUtil.dip2px(context, 10));
//                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }


            /**
             * 可以改变某个tab的长度
             * @param context
             * @param index
             * @return
             */
//            @Override
//            public float getTitleWeight(Context context, int index) {
//                if (index == 0) {
//                    return 2.0f;
//                } else if (index == 1) {
//                    return 1.2f;
//                } else {
//                    return 1.0f;
//                }
//            }
        });

        production_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(production_indicator, production_viewpager);
    }


}
