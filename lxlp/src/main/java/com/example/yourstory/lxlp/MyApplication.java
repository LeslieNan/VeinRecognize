package com.example.yourstory.lxlp;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2019/3/20.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getInstance() {
        return mContext;
    }
}
