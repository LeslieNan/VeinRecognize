package com.example.yourstory.jxd1500;

import android.app.Application;
import android.content.Context;

import com.example.yourstory.jxd1500.util.CloseBarUtil;
import com.example.yourstory.jxd1500.util.GetUSBPermission;

import jx.vein.javajar.JXFVJavaInterface;

/**
 * Created by lenovo on 2019/3/21.
 *
 */

public class MyApplication extends Application {
    private static Context mContext;

    private static JXFVJavaInterface jxfvJavaInterface;        //设备API接口
    private static long devHandler;                            //设备的唯一标识符
    private static long dbHandler;                              //数据库的句柄
    private static final String FILE_NAME = "myDB";             //数据库名
    private String fileName;                            //数据库路径和Name

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        new GetUSBPermission();                                 //获取usb外接权限
//        CloseBarUtil.closeBar();                                //禁用系统的虚拟按键和通知栏
        jxfvJavaInterface = new JXFVJavaInterface();            //初始化接口
        devHandler = jxfvJavaInterface.jxInitUSBDriver();         //初始化USB接口
        initDB();                                               //初始化数据库
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CloseBarUtil.showBar();
        jxfvJavaInterface.jxDeInitUSBDriver(devHandler);        //释放USB驱动
    }

    public static JXFVJavaInterface getjxfvJavaInterface() {
        return jxfvJavaInterface;
    }

    public static long getDvHandler() {
        return devHandler;
    }

    public static Context getInstance() {
        return mContext;
    }

    public static long getDbHandler() {
        return dbHandler;
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        String fileDirectory = getExternalFilesDir("").getAbsolutePath();    //获取外部存储的file路径
        fileName = fileDirectory + "/" + FILE_NAME;
        if (0 == jxfvJavaInterface.jxIsVeinDBExist(fileName)) {
            //若数据库不存在,则创建
            if (0 == jxfvJavaInterface.jxCreateVeinDatabase(fileName)) {
                //创建成功
            }
        }
        //初始化数据库接口
        dbHandler = jxfvJavaInterface.jxInitVeinDatabase(fileName);

    }
}
