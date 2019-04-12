package com.example.yourstory.jxd1500.util;

import android.database.Cursor;
import android.util.Log;

import com.example.yourstory.jxd1500.MyApplication;
import com.example.yourstory.jxd1500.bean.SignBean;
import com.example.yourstory.jxd1500.db.SignDao;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/4/8.
 */

public class NetUtil {

    private String serverAddress = "";
    private int serverPort = 1;


    public static void getNet() {

    }

    /**
     * 要发送到服务端的ip
     * 1.创建socket并指定ip和端口号
     * 2.获取输出流，写数据
     * 3.释放资源
     * 4.Tcp一定要先开接收端
     */
    public void send_tcp(String message) {
        try {
            Socket s = new Socket(serverAddress, serverPort);
            //为了发送数据，应该获得socket流中的输出流
            OutputStream out = s.getOutputStream();
            out.write(message.getBytes());
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取考勤数据
     *
     * @return json格式的考勤数据
     */
    public static String getSign() {
        SignDao dao = new SignDao(MyApplication.getInstance());
        Cursor cursor = dao.selectAllSign();
        List<SignBean> signList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                signList.add(new SignBean(cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("type"))));
            } while (cursor.moveToNext());
        }
        Gson gson = new Gson();
        return gson.toJson(signList);
    }


    public static void setStaticIP(){

    }


}
