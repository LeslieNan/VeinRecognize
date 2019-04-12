package com.example.yourstory.jxd1500.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yourstory.jxd1500.bean.SignBean;
import com.example.yourstory.jxd1500.bean.UserBean;
import com.example.yourstory.jxd1500.bean.VeinBean;
import com.example.yourstory.jxd1500.db.ImgDao;
import com.example.yourstory.jxd1500.db.SignDao;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.db.VeinDBDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/4/11.
 */

public class WaitServerCollect extends IntentService {

    private UserDao userDao;
    private SignDao signDao;
    private VeinDBDao veinDBDao;

    public WaitServerCollect() {
        super("等待服务端连接Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userDao = new UserDao(this);
        signDao = new SignDao(this);
        veinDBDao=new VeinDBDao();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ServerSocket serverSocket;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(30000, 1);
            while (true) {
                socket = serverSocket.accept();
                // 读取客户端数据
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientInputStr = input.readLine();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                // 处理客户端数据
                Log.d("网络", "客户端发过来的内容:" + clientInputStr);
                String response = null;
                String body = parseClientBody(clientInputStr);
                switch (parseClientHead(clientInputStr)) {
                    case 0:
                        response = keepAlive();
                        break;
                    case 1:
                        response = synchUserInfo(body);
                        break;
                    case 2:
                        response = signInfo();
                        break;
                    case 3:
                        response=synchVeinSample(body);
                        break;
                    case 4:
                        response=synchScreenImg(body);
                        break;
                    default:
                        break;
                }
                // 向客户端回复信息
                PrintStream out = new PrintStream(socket.getOutputStream());
                // 发送返回的数据
                out.println(response);
                out.close();
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                Log.d("网络", "服务端 finally 异常:" + e.getMessage());
            }
        }
    }

    /**
     * 提取字符串第5位的数字
     *
     * @param clientStr
     * @return
     */
    private int parseClientHead(String clientStr) {
        return Integer.parseInt(String.valueOf(clientStr.charAt(5)));
    }

    //提取字符串中的body内容
    private String parseClientBody(String clientStr) {
        if (clientStr.length() > 12) {
            return clientStr.substring(12);
        }
        return String.valueOf(0);
    }

    private String keepAlive() {
        return String.valueOf(1);
    }

    private String synchUserInfo(String userJson) {
        Gson gson = new Gson();
        List<UserBean> userBeanList = gson.fromJson(userJson, new TypeToken<List<UserBean>>() {
        }.getType());
        userDao.insertAllUser(userBeanList);
        return String.valueOf(1);
    }

    private String signInfo() {
        List<SignBean> signs = new ArrayList<>();
        Cursor cursor = signDao.selectAllSign();
        if (cursor.moveToFirst()) {
            do {
                signs.add(new SignBean(cursor.getString(cursor.getColumnIndex("userid")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("type"))));
            } while (cursor.moveToNext());
            Gson gson = new Gson();
            return gson.toJson(signs);
        } else {
            return null;
        }
    }

    private String synchVeinSample(String veinJson) {
        Gson gson=new Gson();
        List<VeinBean> veins=gson.fromJson(veinJson,new TypeToken<List<VeinBean>>(){}.getType());
        return String.valueOf(veinDBDao.insertVein(veins));
    }

    private String synchScreenImg(String imgUrlJson) {
        Gson gson=new Gson();
        List<String> urls=gson.fromJson(imgUrlJson,new TypeToken<List<String>>(){}.getType());
        //存入数据库
        ImgDao imgDao=new ImgDao(this);
        imgDao.insertUrl(urls);
        return String.valueOf(1);
    }
}
