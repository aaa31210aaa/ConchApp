package utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.administrator.conchapp.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bean.DateBean;

public class CustomDatePicker {
    //年选择器
    private static TimePickerView yearOption;
    //日报选择器
    private static TimePickerView dayOption;
    //季度选择器
    private static OptionsPickerView qOption;
    //周报选择器
    private static OptionsPickerView weekOption;
    //月报选择器
    private static TimePickerView monthOption;

    //年数据
    private static ArrayList<DateBean> dateItem = new ArrayList<>();
    //月数据
    private static ArrayList<ArrayList<String>> mdateItem = new ArrayList<>();
    //周数据
    private static ArrayList<ArrayList<ArrayList<String>>> wdateItem = new ArrayList<>();
    //季度数据
    private static ArrayList<ArrayList<String>> qdateItem = new ArrayList<>();

    private static int yindex = 0;
    private static int qindex = 0;
    private static int mindex = 0;
    private static int windex = 0;
    //初始年份
    private static int initYear = 2005;
    //取多少年
    private static int years = 40;
    //初始化的年，季度位置
    private static int yearIndex;
    private static int quarterIndex;


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

    private static Calendar calendarDate = Calendar.getInstance();


    private int mYear;
    private int mMonth;
    private int mDay;
    private SimpleDateFormat formatter;
    private String select_date ="";

    public CustomDatePicker() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 初始化年数据
     */
    public static void getYearCardData(TextView textView) {
        for (int i = 0; i <= years; i++) {
            ++initYear;
            String str = String.valueOf(initYear);
            dateItem.add(new DateBean(i, str));
            String st = textView.getText().toString();

            if (str.equals(st.split("-")[0])) {
                yindex = i;
                yearIndex = i;
            }
        }
    }

    /**
     * 初始化季度数据
     */
    public static void getQCardData(TextView textView) {
        String st = textView.getText().toString();
        for (int i = 0; i <= years; i++) {
            ArrayList<String> qdateItemArr = new ArrayList<>();
            for (int j = 1; j <= 4; j++) {
                int index = j;
                qdateItemArr.add(j + "");

                if (qdateItemArr.get((j - 1)).equals(st.split("-")[1])) {
                    qindex = j;
                    quarterIndex = j;
                }
            }
            qdateItem.add(qdateItemArr);
        }
    }

    /**
     * 初始化周数据
     */
    public static void getWeekCardData() {
        for (int i = 0; i <= years; i++) {
            ArrayList<ArrayList<String>> wdateItemArr = new ArrayList<>();

            for (int j = 0; j < 12; j++) {
                ArrayList<String> listItem = new ArrayList<>();

                int year = Integer.parseInt(dateItem.get(i).getPickerViewText());
                int month = Integer.parseInt(mdateItem.get(i).get(j));

                for (int k = 1; k <= DateUtils.getWeekNum(year, month); k++) {
                    listItem.add(k + "");
                }
                wdateItemArr.add(listItem);
            }
            wdateItem.add(wdateItemArr);
        }
    }

    /**
     * 初始化月数据
     */
    public static void getMonthCardData(TextView textView) {
        String st = textView.getText().toString();
        for (int i = 0; i <= years; i++) {
            ArrayList<String> mdateItemArr = new ArrayList<>();
            for (int j = 1; j <= 12; j++) {
//            mdateItem.add(String.valueOf(index));
                mdateItemArr.add(String.valueOf(j));
            }

            for (int k = 0; k < mdateItemArr.size(); k++) {
                //判断我当前月在集合的第几位
                if (Integer.parseInt(mdateItemArr.get(k)) == Integer.parseInt(st.split("-")[1])) {
                    mindex = k;
                }
            }
            mdateItem.add(mdateItemArr);
        }
    }


    private static String getMonthTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    private static String getYMDTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private static String getYTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }


    /**
     * 年dialog
     */
    public static void initYearPicker(Context context, final TextView textView, final SmartRefreshLayout refreshLayout) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        final Calendar selectedYearDate;

        if (textView.getTag() == null) {
            selectedYearDate = (Calendar) calendarDate.clone();
        } else {
            selectedYearDate = (Calendar) textView.getTag();
        }
        Calendar startDate = (Calendar) selectedYearDate.clone();
        startDate.set(2006, 1, 1);
        Calendar endDate = (Calendar) selectedYearDate.clone();
        endDate.set(2046, 1, 1);


        //时间选择器
        yearOption = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                textView.setText(getYTime(date));
                refreshLayout.autoRefresh();
                try {
                    date = sdf.parse(textView.getText().toString());
                    selectedYearDate.setTime(date);
                    textView.setTag(selectedYearDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, false, false, false, false, false})
                .setLabel("年", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedYearDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        yearOption.show();
    }


    /**
     * 年月日dialog
     */
    //初始化日报选择器
    public static void initDayPicker(Context context, final TextView textView, final SmartRefreshLayout refreshLayout) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        final Calendar selectedDayDate;

        if (textView.getTag() == null) {
            selectedDayDate = (Calendar) calendarDate.clone();
        } else {
            selectedDayDate = (Calendar) textView.getTag();
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(2006, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2046, 11, 31);
        //时间选择器
        dayOption = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                textView.setText(getYMDTime(date));
                refreshLayout.autoRefresh();

                try {
                    date = sdfDay.parse(textView.getText().toString());
                    selectedDayDate.setTime(date);
                    textView.setTag(selectedDayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDayDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        dayOption.show();
    }


    /**
     * 初始化季度选择器
     */
    public static void initQuarterPicker(Context context, final TextView textView, final SmartRefreshLayout refreshLayout) {

        if (textView.getTag() == null) {
            yindex = yearIndex;
            qindex = quarterIndex;
        }


        qOption = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str1 = dateItem.get(options1).getPickerViewText();
                String str2 = qdateItem.get(options1).get(options2);
                yindex = options1;
                qindex = options2;
                String mindex = yindex + "," + qindex;
                textView.setText(str1 + "-" + str2);
                textView.setTag(mindex);
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//              final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qOption.returnData();
                        qOption.dismiss();
                        refreshLayout.autoRefresh();
                    }
                });

                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qOption.dismiss();
                    }
                });
            }
        })
                .setLabels("年", "季度", "")
                .setSelectOptions(yindex, qindex)
                .build();
        qOption.setPicker(dateItem, qdateItem);
        qOption.show();
    }

    /**
     * 周dialog
     */
    //初始化周报选择器
    public static void initWeekPicker(Context context, final TextView textView, final SmartRefreshLayout refreshLayout) {
        if (textView.getTag() == null) {
            yindex = yearIndex;
            qindex = quarterIndex;
            windex = 0;
        }

        weekOption = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str1 = dateItem.get(options1).getPickerViewText();
                String str2 = mdateItem.get(options1).get(options2);
                String str3 = wdateItem.get(options1).get(options2).get(options3);
                if (Integer.parseInt(str2) < 10) {
                    str2 = "0" + str2;
                }
                yindex = options1;
                mindex = options2;
                windex = options3;
                textView.setText(str1 + "-" + str2 + "-" + str3);
                textView.setTag(yindex + "," + qindex + "," + windex);
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//              final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weekOption.returnData();
                        weekOption.dismiss();
                        refreshLayout.autoRefresh();
                    }
                });

                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weekOption.dismiss();
                    }
                });
            }
        })
                .setLabels("年", "月", "周")
                .setSelectOptions(yindex, mindex, windex)
                .build();
        weekOption.setPicker(dateItem, mdateItem, wdateItem);
        weekOption.show();
    }


    /**
     * 年月dialog
     */
    //初始化月报选择器
    public static void initMonthPicker(final Context context, final TextView textView, final SmartRefreshLayout refreshLayout) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        final Calendar selectedMonthDate;

        if (textView.getTag() == null) {
            selectedMonthDate = (Calendar) calendarDate.clone();
        } else {
            selectedMonthDate = (Calendar) textView.getTag();
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(2006, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2046, 11, 31);
        //时间选择器
        monthOption = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                textView.setText(getMonthTime(date));
                refreshLayout.autoRefresh();

                try {
                    date = sdfMonth.parse(textView.getText().toString());
                    selectedMonthDate.setTime(date);
                    textView.setTag(selectedMonthDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedMonthDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        monthOption.show();
    }


    /**
     * 系统日期选择器
     */
    public void CustomDatePickDialog(Context context, TextView textView,SmartRefreshLayout refreshLayout) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, null,
                mYear,
                mMonth,
                mDay);
        datePickerDialog.setCancelable(true);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //确定的逻辑代码在监听中实现
                DatePicker picker = datePickerDialog.getDatePicker();
                String select_time = picker.getYear() + "-" + (picker.getMonth() + 1) + "-" + picker.getDayOfMonth();
                try {
                    Date date = formatter.parse(select_time);
                    select_date = formatter.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mYear = picker.getYear();
                mMonth = picker.getMonth();
                mDay = picker.getDayOfMonth();
                textView.setText(select_date);
                refreshLayout.autoRefresh();
            }
        });

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        datePickerDialog.show();
    }


}
