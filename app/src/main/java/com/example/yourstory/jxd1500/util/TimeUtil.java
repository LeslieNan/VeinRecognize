package com.example.yourstory.jxd1500.util;

import android.app.AlarmManager;
import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;

import com.example.yourstory.jxd1500.MyApplication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lenovo on 2019/4/2.
 */

public class TimeUtil {


    public static String datechange(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String demo = sdf.format(date);
        return demo;
    }

    /**
     * 更新系统时间,需要root
     */
    public static void updateSysTime(Calendar calendar) {
        String time = datechange(calendar.getTime(), "yyyyMMdd.HHmmss");
        try {
            Process process = Runtime.getRuntime().exec("su");
            //String datetime="20131023.112800"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】  可能存在时区问题
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + time + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNowTime() {
        Calendar calender = Calendar.getInstance();
        return datechange(calender.getTime(), "yyyyMMdd.HHmmss");
    }

    //获取网络时间
    public static Date getNetTime() {
        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            Date date = new Date(correctTime);
            return date;
        } catch (Exception e) {
            return new Date();
        }
    }


    //获取网络数据并更新到系统时间
    public static void openNTPTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;//取得资源对象
                try {
                    url = new URL("http://www.baidu.com"); //中国科学院国家授时中心
                    URLConnection baidu = url.openConnection();//生成连接对象
                    baidu.connect(); //发出连接
                    long time = baidu.getDate(); //取得网站日期时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    String currentTime = sdf.format(new Date(time));
                    //更改系统时间
                    Process process = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());
                    os.writeBytes("setprop persist.sys.timezone GMT\n");
                    os.writeBytes("/system/bin/date -s " + currentTime + "\n");
                    os.writeBytes("clock -w\n");
                    os.writeBytes("exit\n");
                    os.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置系统自动确定日期时间
     *
     * @param checked
     */
    public static void setAutoDateTime(int checked) {
        android.provider.Settings.Global.putInt(MyApplication.getInstance().getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME, checked);
    }

    public static void setAutoTimeZone(int checked) {
        android.provider.Settings.Global.putInt(MyApplication.getInstance().getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME_ZONE, checked);
    }


    public static boolean isTimeZoneAuto() {
        try {
            return android.provider.Settings.Global.getInt(MyApplication.getInstance().getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME_ZONE) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDateTimeAuto() {
        try {
            return android.provider.Settings.Global.getInt(MyApplication.getInstance().getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //设置系统时区为北京
    public static void setTimeZone() {
        AlarmManager mAlarmManager = (AlarmManager) MyApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("GMT+08:00");
    }


}
