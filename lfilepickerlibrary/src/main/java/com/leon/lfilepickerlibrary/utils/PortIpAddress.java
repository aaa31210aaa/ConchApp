package com.leon.lfilepickerlibrary.utils;


import android.content.Context;

public class PortIpAddress {
    public static String SUCCESS_CODE = "0000";
    public static String ERR_CODE = "0001";
    public static String CODE = "code";
    public static String MESSAGE = "message";
    public static String JsonArrName = "data";
    public static String TOKEN_KEY = "access_token";
    public static String USERID_KEY = "userid";

    //现场
//    public static String host = "http://10.164.40.100:9091/mes/mobile/";
    //文
//    public static String host = "http://192.168.5.103:8080/CONCH-MES/mobile/";
    //228
    public static String host = "http://192.168.5.228:9687/CONCH-MES/mobile/";
    //218外网
//    public static String host = "http://218.76.35.118:8884/CONCH-MES/mobile/";
    //飞
//    public static String host = "http://192.168.5.33:8080/CONCH-MES/mobile/";

    //获取token
    public static String getToken(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "user_token", "");
    }

    //获取用户id
    public static String getUserId(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "userid", "");
    }

    //登陆
    public static String LoginAddress() {
        return host + "login.action";
    }

    //待办
    public static String ToDo() {
        return host + "index/findMatterByuser.action";
    }

    //预警
    public static String Alarm() {
        return host + "index/findAlarmByuser.action";
    }

    //告警
    public static String Warning() {
        return host + "index/findAlarmByuserBYwarning.action";
    }

    //通知公告列表
    public static String NoticeMessage() {
        return host + "index/findMassage.action";
    }

    //消息数量展示
    public static String GetTomatter() {
        return host + "index/countTomatter.action";
    }

    //已读
    public static String MessageState() {
        return host + "message/updateMessageState.action";
    }

    //查询台阶接口
    public static String FindTj() {
        return host + "index/findTJ.action";
    }

    public static String FindTJDate() {
        return host + "index/findTJDate.action";
    }


    //年计划
    public static String Yplan() {
        return host + "plan/findPlandataY.action";
    }

    //季度计划
    public static String Qplan() {
        return host + "plan/findPlandataQ.action";
    }

    //月计划
    public static String Mplan() {
        return host + "plan/findPlandataM.action";
    }

    //计划详情
    public static String PlanDetail() {
        return host + "plan/findProct.action";
    }

    //消耗日报
    public static String ConsumeByDay() {
        return host + "report/countConsumeByDay.action";
    }

    //消耗周报
    public static String ConsumeByWeek() {
        return host + "report/countConsumeByWeek.action";
    }

    //消耗月报
    public static String ConsumeByMonth() {
        return host + "report/countConsumeByMonth.action";
    }

    //生产报表
    public static String CountProduction() {
        return host + "report/countProduction.action";
    }

    //设备报表
    public static String countDevice() {
        return host + "report/countDevice.action";
    }

    //获得作业地点
    public static String getPlace() {
        return host + "hole/getProjectinfoData.action";
    }

    //炮孔信息文件上传
    public static String upLoadProjectinfoData() {
        return host + "hole/savePkInfo.action";
    }

    //获取炮孔列表数据
    public static String getBlasthole() {
        return host + "hole/selectPkList.action";
    }

    //修改密码
    public static String modifyPwd() {
        return host + "password/change.action";
    }

    //修改头像
    public static String updateHeadImage() {
        return host + "user/updateHeadportrait.action";
    }
}
