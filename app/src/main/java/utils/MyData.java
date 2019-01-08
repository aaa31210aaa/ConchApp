package utils;


import java.util.List;

import bean.BlastholeDetailBean;

public class MyData {
    public static String YEAR = "year";
    public static String QUARTER = "quarter";
    public static String MONTH = "month";


    //将list转换为带有 ， 的字符串
    public static String listToString(List<BlastholeDetailBean.DataBean.FzlistBean> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
}
