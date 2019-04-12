package com.example.yourstory.jxd1500.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yourstory.jxd1500.MyApplication;

import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.CTF_1TO1_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.CTF_1TON_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.CTF_PSW_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.DEVICE_NAME_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.LOCAL_DHCP_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.LOCAL_IP_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.LOCAL_GATEWAY_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.LOCAL_MASK_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.NTP_TIME_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.SCREEN_TIME_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.SERVER_PORT_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.PSW_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.PSW_MODE;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.SERVER_IP_KEY;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.SYSTEM_SET_FILE;
import static com.example.yourstory.jxd1500.constant.SystemSettingConstant.SERVER_MASK_KEY;

/**
 * Created by lenovo on 2019/4/1.
 */

public class SystemSettingUtil {


    private static SharedPreferences sharedPreferences =
            MyApplication.getInstance().getSharedPreferences(SYSTEM_SET_FILE, Context.MODE_PRIVATE);


    public static boolean getNowPswMode() {
        return sharedPreferences.getBoolean(PSW_MODE, false);
    }

    public static void setNowPswMode(boolean isLxlp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PSW_MODE, isLxlp);
        editor.apply();
    }


    public static void setPswKey(String psw) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PSW_KEY, psw);
        editor.apply();
    }

    public static String getPswKey() {
        return sharedPreferences.getString(PSW_KEY, "123456");
    }


    public static void setServerIP(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_IP_KEY, ip);
        editor.apply();
    }

    public static String getServerIP() {
        return sharedPreferences.getString(SERVER_IP_KEY, "");
    }

    public static void setServerMask(String yanma) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_MASK_KEY, yanma);
        editor.apply();
    }

    public static String getServerMask() {
        return sharedPreferences.getString(SERVER_MASK_KEY, "");
    }

    public static void setServerPort(String duankou) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_PORT_KEY, duankou);
        editor.apply();
    }

    public static String getServerPort() {
        return sharedPreferences.getString(SERVER_PORT_KEY, "");
    }

    public static void setLocalDHCP(boolean dhcp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOCAL_DHCP_KEY, dhcp);
        editor.apply();
    }

    public static boolean getLocalDHCP() {
        return sharedPreferences.getBoolean(LOCAL_DHCP_KEY, true);
    }


    public static void setLocalIP(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCAL_IP_KEY, ip);
        editor.apply();
    }

    public static String getLocalIP() {
        return sharedPreferences.getString(LOCAL_IP_KEY, "");
    }

    public static void setLocalMask(String yanma) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCAL_MASK_KEY, yanma);
        editor.apply();
    }

    public static String getLocalMask() {
        return sharedPreferences.getString(LOCAL_MASK_KEY, "");
    }

    public static void setLocalGateway(String wangguan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOCAL_GATEWAY_KEY, wangguan);
        editor.apply();
    }

    public static String getLocalGateway() {
        return sharedPreferences.getString(LOCAL_GATEWAY_KEY, "");
    }

    public static void setDeviceName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEVICE_NAME_KEY, name);
        editor.apply();
    }

    public static String getDevicename() {
        return sharedPreferences.getString(DEVICE_NAME_KEY, android.os.Build.BOARD);
    }

    public static void setScreenTime(int time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCREEN_TIME_KEY, time);
        editor.apply();
    }

    public static int getScreenTime() {
        return sharedPreferences.getInt(SCREEN_TIME_KEY, 30);
    }

    public static void setNTPTime(boolean isSelected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NTP_TIME_KEY, isSelected);
        editor.apply();
    }

    public static boolean getNTPTime() {
        return sharedPreferences.getBoolean(NTP_TIME_KEY, true);
    }

    public static void setCertification1to1(boolean isSelected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CTF_1TO1_KEY, isSelected);
        editor.apply();
    }

    public static boolean getCertification1to1() {
        return sharedPreferences.getBoolean(CTF_1TO1_KEY, true);
    }

    public static void setCertification1toN(boolean isSelected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CTF_1TON_KEY, isSelected);
        editor.apply();
    }

    public static boolean getCertification1toN() {
        return sharedPreferences.getBoolean(CTF_1TON_KEY, true);
    }

    public static void setCertificationPsw(boolean isSelected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CTF_PSW_KEY, isSelected);
        editor.apply();
    }

    public static boolean getCertificationPsw() {
        return sharedPreferences.getBoolean(CTF_PSW_KEY, true);
    }


}
