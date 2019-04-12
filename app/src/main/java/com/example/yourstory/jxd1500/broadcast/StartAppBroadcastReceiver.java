package com.example.yourstory.jxd1500.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.yourstory.jxd1500.MainActivity;

/**
 * Created by lenovo on 2019/3/26.
 * 开机启动本应用
 */


public class StartAppBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startMainActivity=new Intent(context, MainActivity.class);
        //若返回栈不存在则创建新栈
        startMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMainActivity);
    }
}
