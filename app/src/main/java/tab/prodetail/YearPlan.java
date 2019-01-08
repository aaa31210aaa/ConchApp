package tab.prodetail;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
//import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.BaseFragment;
import utils.DialogUtil;
import utils.MyData;
import utils.PortIpAddress;
import utils.ShowToast;

import static tab.Production.CJID;
import static tab.Production.KCID;
import static tab.Production.SLPHID;
import static tab.Production.ZLPHID;
import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class YearPlan extends BaseFragment {
    private View view;
    private LinearLayout.LayoutParams lps;
    private LinearLayout.LayoutParams tableLps;
    private LinearLayout.LayoutParams layoutps;
//    private LinearLayout.LayoutParams tvLps;
//    private LinearLayout.LayoutParams nameLps;
    @BindView(R.id.production_yplan)
    LinearLayout production_yplan;
    @BindView(R.id.year_plan_refresh)
    SmartRefreshLayout year_plan_refresh;
    private String token;

    public YearPlan() {
        // Required empty public constructor
    }


    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_year_plan, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        token = PortIpAddress.getToken(getActivity());
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tableLps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        nameLps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutps.setMargins(10, 0, 10, 0);
//        tvLps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        dialog = DialogUtil.createLoadingDialog(getActivity(), R.string.loading_write);
        getYearPlan();
        initRefresh();
    }

    private void getYearPlan() {
        OkGo.<String>get(PortIpAddress.Yplan())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e(TAG, response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray data = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String err = jsonObject.getString(MESSAGE);

                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                production_yplan.removeAllViews();
                                LinearLayout.LayoutParams tvlps = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
                                for (int i = 0; i < data.length(); i++) {
                                    final String techid = data.optJSONObject(i).getString("techid");
                                    final String procid = data.optJSONObject(i).getString("procid");

                                    if (procid.equals(KCID) || procid.equals(CJID) || procid.equals(ZLPHID)) {
//                                        LinearLayout llh = new LinearLayout(getActivity());
//                                        llh.setOrientation(LinearLayout.VERTICAL);
//                                        llh.setLayoutParams(lps);
//                                        LinearLayout llv = new LinearLayout(getActivity());
//                                        llv.setPadding(10, 10, 10, 10);
//                                        llv.setOrientation(LinearLayout.HORIZONTAL);
//                                        llv.setLayoutParams(lps);
//                                        llv.setGravity(Gravity.CENTER_VERTICAL);
//
//
//                                        LinearLayout.LayoutParams layoutps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                        layoutps.setMargins(10, 0, 10, 0);
//                                        HorizontalScrollView htv = new HorizontalScrollView(getActivity());
//                                        htv.setLayoutParams(layoutps);
//                                        LinearLayout llv2 = new LinearLayout(getActivity());
//                                        llv2.setOrientation(LinearLayout.VERTICAL);
//                                        llv2.setLayoutParams(lps);
//                                        llv2.setGravity(Gravity.CENTER);
//
//
//                                        LinearLayout llv2Top = new LinearLayout(getActivity());
//                                        llv2Top.setOrientation(LinearLayout.HORIZONTAL);
//                                        llv2Top.setGravity(Gravity.CENTER);
//                                        llv2Top.setLayoutParams(lps);
//                                        LinearLayout llv2Down = new LinearLayout(getActivity());
//                                        llv2Down.setOrientation(LinearLayout.HORIZONTAL);
//                                        llv2Down.setGravity(Gravity.CENTER);
//                                        llv2Down.setLayoutParams(lps);
//
//                                        ImageView image = new ImageView(getActivity());
//                                        TextView tv = new TextView(getActivity());
//
//                                        image.setLayoutParams(lps);
//                                        tv.setLayoutParams(lps);
//                                        if (procid.equals(KCID)) {
//                                            image.setBackgroundResource(R.mipmap.ic_launcher);
//                                            tv.setText("开采");
//                                        } else if (procid.equals(CJID)) {
//                                            image.setBackgroundResource(R.mipmap.ic_launcher);
//                                            tv.setText("采掘");
//                                        } else if (procid.equals(ZLPHID)) {
//                                            image.setBackgroundResource(R.mipmap.ic_launcher);
//                                            tv.setText("质量平衡");
//                                        }
//                                        //添加图案文字标题
//                                        llv.addView(image);
//                                        llv.addView(tv);
//
//                                        JSONArray index = jsonArray.optJSONObject(i).getJSONArray("index");
//
//                                        for (int j = 0; j < index.length(); j++) {
//                                            TextView tvName = new TextView(getActivity());
//                                            TextView tvValue = new TextView(getActivity());
//                                            tvName.setLayoutParams(tvlps);
//                                            tvName.setPadding(10, 10, 10, 10);
//                                            tvName.setGravity(Gravity.CENTER);
//                                            tvName.setText(index.optJSONObject(j).getString("name"));
//                                            tvValue.setLayoutParams(tvlps);
//                                            tvValue.setPadding(10, 10, 10, 10);
//                                            tvValue.setGravity(Gravity.CENTER);
//                                            tvValue.setText(index.optJSONObject(j).getString("value"));
//                                            tvName.setBackgroundResource(R.drawable.editext_yb);
//                                            tvValue.setBackgroundResource(R.drawable.editext_yb);
//                                            llv2Top.addView(tvName);
//                                            llv2Down.addView(tvValue);
//                                        }
//                                        llv2.addView(llv2Top);
//                                        llv2.addView(llv2Down);
//                                        htv.addView(llv2);
//                                        llh.addView(llv);
//                                        llh.addView(htv);
//
//                                        llh.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                if (procid.equals(KCID)) {
//                                                    Intent intent = new Intent(getActivity(), KcjhDetail.class);
//                                                    intent.putExtra("type", MyData.YEAR);
//                                                    intent.putExtra("techid", techid);
//                                                    intent.putExtra("procid", procid);
//                                                    startActivity(intent);
//                                                } else if (procid.equals(CJID)) {
//                                                    Intent intent = new Intent(getActivity(), CjjhDetail.class);
//                                                    intent.putExtra("type", MyData.YEAR);
//                                                    intent.putExtra("techid", techid);
//                                                    intent.putExtra("procid", procid);
//                                                    startActivity(intent);
//                                                } else if (procid.equals(ZLPHID)) {
//                                                    Intent intent = new Intent(getActivity(), ZlphDetail.class);
//                                                    intent.putExtra("type", MyData.YEAR);
//                                                    intent.putExtra("techid", techid);
//                                                    intent.putExtra("procid", procid);
//                                                    startActivity(intent);
//                                                }
//                                            }
//                                        });
//                                        production_yplan.addView(llh);


                                        //外层的ll
                                        LinearLayout llw = new LinearLayout(getActivity());
                                        llw.setOrientation(LinearLayout.VERTICAL);
                                        llw.setLayoutParams(lps);

                                        //图片+文字的ll
                                        LinearLayout llv = new LinearLayout(getActivity());
                                        llv.setPadding(10, 10, 10, 10);
                                        llv.setOrientation(LinearLayout.HORIZONTAL);
                                        llv.setLayoutParams(lps);
                                        llv.setGravity(Gravity.CENTER_VERTICAL);

                                        ImageView image = new ImageView(getActivity());
                                        TextView tv = new TextView(getActivity());

                                        image.setLayoutParams(lps);
                                        tv.setLayoutParams(lps);
                                        if (procid.equals(KCID)) {
                                            image.setBackgroundResource(R.drawable.kcimg);
                                            tv.setText("开采");
                                        } else if (procid.equals(CJID)) {
                                            image.setBackgroundResource(R.drawable.cjimg);
                                            tv.setText("采掘");
                                        } else if (procid.equals(ZLPHID)) {
                                            image.setBackgroundResource(R.drawable.zlphimg);
                                            tv.setText("质量平衡");
                                        }
                                        //添加图案文字标题
                                        llv.addView(image);
                                        llv.addView(tv);
                                        llw.addView(llv);

                                        //创建内容TableLayout
//                                        TableLayout tableLayout = new TableLayout(getActivity());
//                                        tableLayout.setLayoutParams(lps);
////                                        tableLayout.setStretchAllColumns(true);
//
//                                        JSONArray index = data.optJSONObject(i).getJSONArray("index");
//                                        for (int j = 0; j < index.length(); j++) {
//                                            TableRow row = new TableRow(getActivity());
//                                            row.setLayoutParams(tvLps);
//                                            for (int k = 0; k < 2; k++) {
//                                                TextView textview = new TextView(getActivity());
//                                                textview.setLayoutParams(tvLps);
//                                                textview.setPadding(10, 10, 10, 10);
//                                                textview.setBackgroundResource(R.drawable.editext_yb);
//                                                if (k == 0) {
//                                                    textview.setText(index.optJSONObject(j).getString("name"));
//                                                } else if (k == 1) {
//                                                    textview.setText(index.optJSONObject(j).getString("value"));
//                                                }
//                                                //添加到Row
//                                                row.addView(textview);
//                                            }
//                                            //将一行添加到tableLayout
//                                            tableLayout.addView(row);
//                                        }
//                                        llw.addView(tableLayout);
//                                        production_yplan.addView(llw);

                                        //动态创建表格
                                        TableLayout tableLayout = new TableLayout(getActivity());
                                        tableLayout.setLayoutParams(lps);
                                        tableLayout.setStretchAllColumns(true);

                                        JSONArray index = data.optJSONObject(i).getJSONArray("index");
                                        for (int j = 0; j < index.length(); j++) {
                                            // 创建一行
                                            TableRow row = new TableRow(getActivity());
                                            row.setLayoutParams(tableLps);
                                            for (int k = 0; k < 2; k++) {
                                                //创建显示的内容,这里创建的是一列
                                                TextView text = new TextView(getActivity());
                                                text.setBackgroundResource(R.drawable.editext_yb); //背景色
                                                text.setPadding(10, 10, 10, 10);
                                                text.setGravity(Gravity.CENTER); //居中显示
                                                if (k == 0) {
                                                    text.setText(index.optJSONObject(j).getString("name"));
                                                } else {
                                                    if (index.optJSONObject(j).getString("name").contains("含量")){
                                                        if (index.optJSONObject(j).getString("name").contains("总")){
                                                            text.setText(index.optJSONObject(j).getString("value")+"(t)");
                                                        }else{
                                                            text.setText(index.optJSONObject(j).getString("value")+"(%)");
                                                        }
                                                    }else{
                                                        text.setText(index.optJSONObject(j).getString("value")+"(t)");
                                                    }
                                                }
                                                if (!index.optJSONObject(j).getString("name").contains("矿种")) {
                                                    //添加到Row
                                                    row.addView(text);
                                                }
                                            }
                                            //将一行数据添加到表格中
                                            tableLayout.addView(row);
                                        }
                                        llw.addView(tableLayout);


                                        llw.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (procid.equals(KCID)) {
                                                    Intent intent = new Intent(getActivity(), KcjhDetail.class);
                                                    intent.putExtra("type", MyData.YEAR);
                                                    intent.putExtra("techid", techid);
                                                    intent.putExtra("procid", procid);
                                                    startActivity(intent);
                                                } else if (procid.equals(CJID)) {
                                                    Intent intent = new Intent(getActivity(), CjjhDetail.class);
                                                    intent.putExtra("type", MyData.YEAR);
                                                    intent.putExtra("techid", techid);
                                                    intent.putExtra("procid", procid);
                                                    startActivity(intent);
                                                } else if (procid.equals(ZLPHID)) {
                                                    Intent intent = new Intent(getActivity(), ZlphDetail.class);
                                                    intent.putExtra("type", MyData.YEAR);
                                                    intent.putExtra("techid", techid);
                                                    intent.putExtra("procid", procid);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                        production_yplan.addView(llw);
                                    } else if (procid.equals(SLPHID)) {
                                        String ncbyl = data.getJSONObject(i).getString("ncbyl");
                                        String ncbylqx = data.getJSONObject(i).getString("ncbylqx");
                                        String ncczl = data.getJSONObject(i).getString("ncczl");
                                        String ncczlqx = data.getJSONObject(i).getString("ncczlqx");
                                        String nckzl = data.getJSONObject(i).getString("nckzl");
                                        String nckclqx = data.getJSONObject(i).getString("nckclqx");
                                        String nmbyl = data.getJSONObject(i).getString("nmbyl");
                                        String nmbylqx = data.getJSONObject(i).getString("nmbylqx");
                                        String nmczl = data.getJSONObject(i).getString("nmczl");
                                        String nmczlqx = data.getJSONObject(i).getString("nmczlqx");
                                        String nmkzl = data.getJSONObject(i).getString("nmkzl");
                                        String nmkzlqx = data.getJSONObject(i).getString("nmkzlqx");

                                        LinearLayout llh = new LinearLayout(getActivity());
                                        llh.setOrientation(LinearLayout.VERTICAL);
                                        llh.setLayoutParams(lps);

                                        LinearLayout llv = new LinearLayout(getActivity());
                                        llv.setOrientation(LinearLayout.HORIZONTAL);
                                        llv.setPadding(10, 10, 10, 10);
                                        llv.setLayoutParams(lps);
                                        llv.setGravity(Gravity.CENTER_VERTICAL);

                                        ImageView image = new ImageView(getActivity());
                                        TextView tv = new TextView(getActivity());
                                        image.setLayoutParams(lps);
                                        tv.setLayoutParams(lps);
                                        image.setBackgroundResource(R.drawable.slphimg);
                                        tv.setText("三量平衡");
                                        llv.addView(image);
                                        llv.addView(tv);

                                        //动态创建三量平衡表格
                                        TableLayout tableLayout = new TableLayout(getActivity());
                                        tableLayout.setLayoutParams(lps);
                                        tableLayout.setStretchAllColumns(true);

                                        for (int j = 0; j < 5; j++) {
                                            // 创建一行
                                            TableRow row = new TableRow(getActivity());
                                            row.setLayoutParams(tableLps);
                                            for (int k = 0; k < 4; k++) {
                                                //创建显示的内容,这里创建的是一列
                                                TextView text = new TextView(getActivity());
                                                //设置显示内容
                                                if (j == 0 && k == 0) {
                                                    text.setText("");
                                                } else if (j == 0 && k == 1) {
                                                    text.setText("保有矿量");
                                                } else if (j == 0 && k == 2) {
                                                    text.setText("采准矿量");
                                                } else if (j == 0 && k == 3) {
                                                    text.setText("可采矿量");
                                                } else if (j == 1 && k == 0) {
                                                    text.setText("年初矿量");
                                                } else if (j == 1 && k == 1) {
                                                    text.setText(ncbyl);
                                                } else if (j == 1 && k == 2) {
                                                    text.setText(ncczl);
                                                } else if (j == 1 && k == 3) {
                                                    text.setText(nckzl);
                                                } else if (j == 2 && k == 0) {
                                                    text.setText("保有期限");
                                                } else if (j == 2 && k == 1) {
                                                    text.setText(ncbylqx);
                                                } else if (j == 2 && k == 2) {
                                                    text.setText(ncczlqx);
                                                } else if (j == 2 && k == 3) {
                                                    text.setText(nckclqx);
                                                } else if (j == 3 && k == 0) {
                                                    text.setText("年底矿量");
                                                } else if (j == 3 && k == 1) {
                                                    text.setText(nmbyl);
                                                } else if (j == 3 && k == 2) {
                                                    text.setText(nmczl);
                                                } else if (j == 3 && k == 3) {
                                                    text.setText(nmkzl);
                                                } else if (j == 4 && k == 0) {
                                                    text.setText("保有期限");
                                                } else if (j == 4 && k == 1) {
                                                    text.setText(nmbylqx);
                                                } else if (j == 4 && k == 2) {
                                                    text.setText(nmczlqx);
                                                } else if (j == 4 && k == 3) {
                                                    text.setText(nmkzlqx);
                                                }

                                                text.setBackgroundResource(R.drawable.editext_yb); //背景色
                                                text.setPadding(10, 10, 10, 10);
                                                text.setGravity(Gravity.CENTER); //居中显示
                                                //添加到Row
                                                row.addView(text);
                                            }
                                            //将一行数据添加到表格中
                                            tableLayout.addView(row);
                                        }
                                        llh.addView(llv);
                                        llh.addView(tableLayout);
                                        production_yplan.addView(llh);

                                        llh.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), SlphjhDetail.class);
                                                intent.putExtra("techid", techid);
                                                intent.putExtra("procid", procid);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                ShowToast.showShort(getActivity(), err);
                            }
                            year_plan_refresh.finishRefresh();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        year_plan_refresh.finishRefresh();
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }

    private void initRefresh() {
        //刷新
        year_plan_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(1, ShowToast.refreshTime);
            }
        });

        //加载
        year_plan_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                handler.sendEmptyMessageDelayed(0, ShowToast.refreshTime);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    ShowToast.showShort(getActivity(), R.string.loadSuccess);
                    year_plan_refresh.finishLoadmore();
                    break;
                case 1:
//                    ShowToast.showShort(getActivity(), R.string.refreshSuccess);
                    getYearPlan();
                    break;
                default:
                    break;
            }
        }
    };
}
