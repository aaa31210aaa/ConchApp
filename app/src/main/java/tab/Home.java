package tab;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.HomeRvAdapter;
import bean.HomeRvBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.homedetail.EarlyWarning;
import tab.homedetail.Notice;
import tab.homedetail.Todo;
import tab.homedetail.Warning;
import utils.BaseFragment;
import utils.DateUtils;
import utils.DialogUtil;
import utils.DisplayUtil;
import utils.DividerItemDecoration;
import utils.LocalImageHolderView;
import utils.PortIpAddress;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends BaseFragment implements View.OnClickListener {
    private View view;
    @BindView(R.id.homeBanner)
    ConvenientBanner homeBanner;
    @BindView(R.id.homeRv)
    RecyclerView homeRv;
    @BindView(R.id.fragment_home_date_before)
    ImageButton fragment_home_date_before;
    @BindView(R.id.fragment_home_date_next)
    ImageButton fragment_home_date_next;
    @BindView(R.id.fragment_home_date)
    TextView fragment_home_date;
    //    @BindView(R.id.spinner_test)
//    Spinner spinner_test;
    @BindView(R.id.home_tj)
    RadioGroup home_tj;
    @BindView(R.id.home_ll)
    LinearLayout home_ll;
    @BindView(R.id.home_explosion_volume)
    TextView home_explosion_volume;
    @BindView(R.id.home_ore_storage_capacity)
    TextView home_ore_storage_capacity;

    public int height;
    public int width;

    private List<String> list = new ArrayList<>();
    //    private List<String> contentList = new ArrayList<>();
    private Map<String, String> tjMap = new HashMap<>();


    private Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int day;
    private SimpleDateFormat formatter;
    //点击次数
    private int num = 0;


    private List<HomeRvBean> mDatas;
    //首页模块跳转的activity
//    private Class<?>[] ACTIVITY = {Todo.class, EarlyWarning.class, Warning.class, Notice.class, Ledger.class};
    private Class<?>[] ACTIVITY = {Todo.class, EarlyWarning.class, Warning.class, Notice.class};
//    private String[] tvs = {"待办事宜", "预警信息", "告警信息", "通知公告","台帐信息"};
    private String[] tvs = {"待办事宜", "预警信息", "告警信息", "通知公告"};
//    private int[] images = {R.drawable.todo, R.drawable.early_warning, R.drawable.after_warning, R.drawable.notification,R.drawable.ledger};
    private int[] images = {R.drawable.todo, R.drawable.early_warning, R.drawable.after_warning, R.drawable.notification};

    //banner加载的图片集
    private ArrayList<Integer> localImages;
    //翻页效果集
    private ArrayList<String> transformerList;
    private Class cls;
    private ABaseTransformer transforemer;
    private String transforemerName;

    //消息数量展示数组
    private List<String> message;
    private HomeRvAdapter adapter;
    public static boolean isGet = false;
    private List<HomeRvBean> mList;
    //初始化第一个台阶id
    private String tjid = "";
    //保存选中的台阶id
    private String tjCheckedId = "";
    private String token;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_home, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        initData();
    }

    private void initData() {
        token = PortIpAddress.getToken(getActivity());
        homeRv.setLayoutManager(new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
        homeRv.addItemDecoration(new DividerItemDecoration(getActivity()));
        homeRv.setHasFixedSize(true);
        mDatas = new ArrayList<>();
        message = new ArrayList<>();
        mList = new ArrayList<>();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        setHomeBanner();
        setHomeRv();
        //设置初始日期
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = DateUtils.getStringDateShort();
        fragment_home_date.setText(nowdate);

        year = calendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = calendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = calendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

//      SetSpinner();
        getTj();
    }


    /**
     * 获取台阶数据
     */
    private void getTj() {
        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading_write);
        OkGo.<String>get(PortIpAddress.FindTj())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        list.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                for (int i = 0; i < data.length(); i++) {
                                    list.add(data.optJSONObject(i).getString("projectname"));
                                    tjMap.put(data.optJSONObject(i).getString("projectname"), data.optJSONObject(i).getString("projectid"));

                                    if (i == 0) {
                                        tjid = data.optJSONObject(0).getString("projectid");
                                    }
                                }
                                //初始化台阶
                                initTextView(list);
                                initContent(fragment_home_date.getText().toString(), tjid);
                                //设置默认选中台阶id
                                tjCheckedId = tjid;
                            } else {
                                ShowToast.showShort(getActivity(), err);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    //获取台阶详情
//    private void getTjContent(String reportdate, String projectid) {
//        OkGo.<String>get(PortIpAddress.FindTj())
//                .tag(this)
//                .params("access_token", PortIpAddress.getToken(getActivity()))
//                .params("userid", PortIpAddress.getUserId(getActivity()))
//                .params("reportdate", reportdate)
//                .params("projectid", projectid)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e(TAG, response.body().toString());
////                        contentList.clear();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            String err = jsonObject.getString(MESSAGE);
//
//                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
//                                home_explosion_volume.setText(jsonObject.getString("blastingoff"));
//                                home_ore_storage_capacity.setText(jsonObject.getString("oredeposit"));
//                            } else {
//                                ShowToast.showShort(getActivity(), err);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//    }

    private void initTextView(final List<String> tjList) {
        if (home_tj != null) {
            home_tj.removeAllViews();
        }

        for (int i = 0; i < tjList.size(); i++) {
            final int index = i;
            final RadioButton radio = new RadioButton(getActivity());
            radio.setId(i);
            radio.setGravity(Gravity.CENTER);
            if (tjList.size() < 4) {
                radio.setWidth(width / tjList.size());
            } else {
                radio.setWidth(width / 4);
            }

            radio.setButtonDrawable(android.R.color.transparent);

            radio.setPadding(0, (int) getResources().getDimension(R.dimen.x10), 0, (int) getResources().getDimension(R.dimen.x10));
            radio.setText(tjList.get(i));

            radio.setTextSize(DisplayUtil.px2sp(getActivity(), getResources().getDimension(R.dimen.x12)));
//            radio.setTextSize(16);


            radio.setBackgroundResource(R.drawable.home_radio);
            radio.setTextColor(Color.WHITE);
//            tv.setBackgroundColor(Color.parseColor("#1195db"));
            home_tj.addView(radio);

            if (i == 0) {
                home_tj.check(radio.getId());
            }

            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tjCheckedId = tjMap.get(tjList.get(index));
                    initContent(fragment_home_date.getText().toString(), tjCheckedId);
                }
            });
        }
    }

    private void initContent(String reportdate, String projectid) {
        OkGo.<String>get(PortIpAddress.FindTJDate())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .params("reportdate", reportdate)
                .params("projectid", projectid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String err = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                home_explosion_volume.setText(jsonObject.getString("blastingoff"));
                                home_ore_storage_capacity.setText(jsonObject.getString("oredeposit"));
                            } else {
                                ShowToast.showShort(getActivity(), err);
                            }

                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    /**
     * 日生产情况
     */
//    private void initContent(List<String> contentList) {
//        for (int i = 0; i < contentList.size(); i++) {
//            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
//            LinearLayout.LayoutParams linps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
//            LinearLayout ll = new LinearLayout(getActivity());
//            ll.setOrientation(LinearLayout.HORIZONTAL);
//            ll.setLayoutParams(lps);
//            for (int j = 0; j < 2; j++) {
//                TextView tv = new TextView(getActivity());
//                tv.setGravity(Gravity.CENTER);
//                tv.setPadding(5, 5, 5, 5);
//                tv.setText(contentList.get(i));
//                tv.setTextSize(16);
//                tv.setLayoutParams(linps);
//                ll.addView(tv);
//            }
//            home_ll.addView(ll);
//        }
//    }

//    private void SetSpinner() {
//        str = getResources().getStringArray(R.array.Test1);
//        // 建立Adapter并且绑定数据源
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, str);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //绑定 Adapter到控件
//        spinner_test .setAdapter(adapter);
//        spinner_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ShowToast.showShort(getActivity(), "你点击的是" + str[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

    //前一天
    @OnClick(R.id.fragment_home_date_before)
    public void Before() {
        try {
            dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading_write);
            num--;
            fragment_home_date.setText(DateUtils.mtime(num, fragment_home_date.getText().toString()));
            num = 0;
            Date date = formatter.parse(fragment_home_date.getText().toString());
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            initContent(fragment_home_date.getText().toString(), tjCheckedId);
//            home_explosion_volume.setText("测试内容" + random);
//            home_ore_storage_capacity.setText("测试内容" + random);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //后一天
    @OnClick(R.id.fragment_home_date_next)
    public void Next() {
        try {
            dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading_write);
            num++;
            fragment_home_date.setText(DateUtils.mtime(num, fragment_home_date.getText().toString()));
            num = 0;
            Date date = formatter.parse(fragment_home_date.getText().toString());
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            initContent(fragment_home_date.getText().toString(), tjCheckedId);
//            home_explosion_volume.setText("测试内容" + random);
//            home_ore_storage_capacity.setText("测试内容" + random);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //设置日期
    @OnClick(R.id.fragment_home_date)
    public void SetDate() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), Datelistener, year, month, day);
        dialog.show();
    }


    @Override
    public void onClick(View v) {

    }

    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            //更新日期
            updateDate();
        }

        //更新选择后的日期
        private void updateDate() {
            try {
                String select_time = year + "-" + (month + 1) + "-" + day;
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(select_time);
                String select_date = formatter.format(date);
                fragment_home_date.setText(select_date);
                initContent(select_date, tjCheckedId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 设置轮播
     */
    private void setHomeBanner() {
        localImages = new ArrayList<Integer>();
        transformerList = new ArrayList<String>();

        for (int position = 1; position < 5; position++) {
            localImages.add(getResId("banner" + position, R.drawable.class));
        }

        //自定义Holder
        homeBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        // 设置翻页的效果，不需要翻页效果可用不设

//                .setPageTransformer(Transformer.CubeIn);
//        convenientBanner.setManualPageable(false);//设置不能手动影响


        //加载网络图片
//        networkImages= Arrays.asList(imageUrls);
//        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);

        //各种翻页效果
        transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        transforemerName = transformerList.get(13);
        try {
            cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
            transforemer = (ABaseTransformer) cls.newInstance();
            homeBanner.getViewPager().setPageTransformer(true, transforemer);

            if (transforemerName.equals("StackTransformer")) {
                homeBanner.setScrollDuration(4000);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获得消息数量
     */
    private void getMessage() {
        OkGo.<String>get(PortIpAddress.GetTomatter())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String err = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                message.add(jsonObject.getString("Matter"));
                                message.add(jsonObject.getString("alarm"));
                                message.add(jsonObject.getString("warning"));
                                message.add(jsonObject.getString("message"));
                                Log.e(TAG, message.size() + "");

                                for (int i = 0; i < mDatas.size(); i++) {
                                    HomeRvBean bean = mDatas.get(i);
                                    if (i<mDatas.size()-1){
                                        bean.setMsgNum(message.get(i));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                ShowToast.showShort(getActivity(), err);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    /**
     * 设置首页模块内容
     */
    private void setHomeRv() {
        //获得消息数量
        getMessage();

        for (int i = 0; i < tvs.length; i++) {
            HomeRvBean bean = new HomeRvBean();
            bean.setImage(images[i]);
            bean.setTvName(tvs[i]);
            bean.setMsgNum("0");
            mDatas.add(bean);
        }

        if (adapter == null) {
            adapter = new HomeRvAdapter(R.layout.home_rvitem, mDatas);
            homeRv.setAdapter(adapter);
        } else {
            adapter.setNewData(mDatas);
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivityForResult(new Intent(getActivity(), ACTIVITY[position]), 10);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        homeBanner.startTurning(4000);
        getMessage();
    }

    // 停止自动翻页
    @Override
    public void onStop() {
        super.onStop();
        //停止翻页
        homeBanner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
