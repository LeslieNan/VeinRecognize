package com.example.yourstory.jxd1500;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourstory.jxd1500.activity.AdminPasswordActivity;
import com.example.yourstory.jxd1500.broadcast.PassCheckEvent;
import com.example.yourstory.jxd1500.constant.UserInfoConstant;
import com.example.yourstory.jxd1500.constant.WhoSetConstant;
import com.example.yourstory.jxd1500.db.SignDao;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.dialog.ScreenDialog;
import com.example.yourstory.jxd1500.util.CodeUtil;
import com.example.yourstory.jxd1500.util.Screensaver;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;
import com.example.yourstory.jxd1500.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jx.vein.javajar.JXFVJavaInterface;

import static jx.vein.javajar.JXFVJavaInterface.veinFeatureSize;
import static jx.vein.javajar.JXFVJavaInterface.veinImgSize;
import static jx.vein.javajar.JXFVJavaInterface.veinSampleSize;

public class MainActivity extends AppCompatActivity implements Screensaver.OnTimeOutListener {


    private static final String TAG = "MainActivity";


    @BindView(R.id.iv_main_wifi)
    ImageView ivMainWifi;
    @BindView(R.id.tv_main_time)
    TextView tvMainTime;
    @BindView(R.id.tv_main_date)
    TextView tvMainDate;
    @BindView(R.id.tv_main_name)
    TextView tvMainName;
    @BindView(R.id.tv_main_number)
    TextView tvMainNumber;
    @BindView(R.id.tv_main_sign)
    TextView tvMainSign;
    @BindView(R.id.iv_main_circle)
    ImageView ivMainCircle;
    @BindView(R.id.iv_main_guanli)
    ImageView ivMainGuanli;


    private JXFVJavaInterface jxfvJavaInterface;        //设备API接口
    private long devHandler;                            //设备的唯一标识符
    private long dbHandler;                             //数据库连接句柄
    private UserDao userDao;
    private SignDao signDao;

    private boolean childThreadRun = true;                //控制1ToN识别子线程是否继续运行
    private int nowUIState = 0;                           //记录当前UI的状态，避免TextView重复设置Text


    private String userName = null;
    private String userID = null;

    private Screensaver mScreensaver;                   //锁屏的计时服务类
    private ScreenDialog screenDialog;                  //锁屏dialog
    //主界面时间显示
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Calendar calendar = Calendar.getInstance();
                    tvMainDate.setText(TimeUtil.datechange(calendar.getTime(), "MM月dd日"));
                    tvMainTime.setText(TimeUtil.datechange(calendar.getTime(), "HH:mm"));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jxfvJavaInterface = MyApplication.getjxfvJavaInterface();
        devHandler = MyApplication.getDvHandler();
        dbHandler = MyApplication.getDbHandler();
        userDao = new UserDao(this);
        signDao = new SignDao(this);
        EventBus.getDefault().register(this);
    }

    /**
     * 每一分钟更新一次时间TextView
     */
    private void setTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (childThreadRun) {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        childThreadRun = true;
        setTime();
        initIDBtn();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //使得1对1验证的信息显示1秒,再进行后续验证
                    Thread.sleep(1000);
                    showUserInfo(0);
                    //当1:N认证开启时再进行认证
                    if (SystemSettingUtil.getCertification1toN()) {
                        //初始化连接
                        int connected = jxfvJavaInterface.jxIsFVDConnected(devHandler);
                        Log.d(TAG, "检测是否有设备连接到主机:" + connected);
                        if (connected == 1) {
                            //若有设备连接到主机，则连接设备
                            int connectedTo = jxfvJavaInterface.jxConnFVD(devHandler);
                            Log.d(TAG, "连接到静脉识别设备，在检测到设备已连接后调用:" + connectedTo);
                            if (connectedTo == 0) {
                                open1ToNRecognize();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        //开始计时锁屏
        int ScreenTime = SystemSettingUtil.getScreenTime();
        mScreensaver = new Screensaver(ScreenTime * 1000); //定时
        mScreensaver.setOnTimeOutListener(this); //监听
        mScreensaver.start(); //开始计时
    }

    private void initIDBtn() {
        //若密码认证与1:1认证都未开启，则解锁按钮不可用
        if (!SystemSettingUtil.getCertificationPsw() && !SystemSettingUtil.getCertification1to1()) {
            ivMainCircle.setEnabled(false);
        } else {
            ivMainCircle.setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jxfvJavaInterface.jxDisConnFVD(devHandler);
        childThreadRun = false;                       //暂停子线程的识别
        mScreensaver.stop();            //停止锁屏计时
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开始1对N认证
     */
    private void open1ToNRecognize() {
        //开启子线程进行认证
        new Thread(new Runnable() {
            @Override
            public void run() {
                //当设备工作正常
                while (childThreadRun) {
                    int cover = jxfvJavaInterface.jxIsFingerDetected(devHandler);
                    if (cover == 1) {
                        //感应到手指
                        getImg();
                        //每次检测成功后,手指放开后再进行识别
                        while (jxfvJavaInterface.jxIsFingerDetected(devHandler) != 0) ;
                    }
                    if (cover == 0) {
                        //未检测到手指
                        showUserInfo(0);
                    } else if (cover == -100) {
                        //未知错误
                        Log.d(TAG, "连接出现未知错误");
                    }
                }
            }
        }).start();
    }


    public void getImg() {
        if (jxfvJavaInterface.jxInitCapEnv(devHandler) == 0) {
            //初始化图像采集环境
            //采集图像，只用于显示
            int isOK = 0;
            byte[] img_buf = new byte[veinImgSize];
            while (isOK != 2 && isOK != 3) {
                try {
                    isOK = jxfvJavaInterface.jxIsVeinImgOK(devHandler, img_buf);
//                    showFingerState(isOK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "图像获取结束");

            //采集完成，获取信息包
            if (isOK == 2) {
                //用于存储信息包
                byte[] sample_buf = new byte[veinSampleSize];
                try {
                    jxfvJavaInterface.jxLoadVeinSample(devHandler, sample_buf);
                    recognize(sample_buf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 静脉认证对比
     */
    private void recognize(byte[] sample_buf) {
        //检测样本格式是否合格
        try {
            int isOK = jxfvJavaInterface.jxCheckVeinSampleQuality(sample_buf);
            if (isOK == 0) {
                Log.d(TAG, "样本质量合格");
                byte[] feat_buf = new byte[veinFeatureSize];   //静脉特征
                if (0 == jxfvJavaInterface.jxGrabVeinFeature(sample_buf, feat_buf)) {
                    //从静脉样本中提取出静脉特征,且合格
                    try {
                        find(feat_buf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "样本质量不合格");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param feat_buf
     * @throws Exception
     */
    private void find(byte[] feat_buf) throws Exception {
        byte[] veinIDs = new byte[250];
        int exist = jxfvJavaInterface.jxRecognizeVeinFeature(dbHandler, feat_buf, veinIDs);
        if (exist > 0) {
            //说明数据库中有该样本,现从veinIDs中提取user的id
            userID = CodeUtil.findUserFromVeinID(veinIDs);
            userName = userDao.selectNameByID(userID);
            Log.d(TAG, "样本的数量长度" + veinIDs.length);
            Log.d(TAG, "样本的用户名" + new String(veinIDs));
            //添加签到记录
            signDao.addSign(userID, TimeUtil.getNowTime(), UserInfoConstant.SIGN_TYPE_1ToN);
            showUserInfo(1);
        } else if (exist == 0) {
            showUserInfo(2);
        }
    }

//    /**
//     * 显示指纹识别状态
//     *
//     * @param state
//     */
//    private void showFingerState(int state) {
//        switch (state) {
//            case 0:
//                Log.d(TAG, "正在调节光强");
//                break;
//            case 1:
//                Log.d(TAG, "正在等待手指放置稳定");
//                break;
//            case 2:
//                Log.d(TAG, "采集完成");
//                break;
//            case 3:
//                Log.d(TAG, "手指已离开");
//                break;
//            case -100:
//                Log.d(TAG, "未知错误");
//                break;
//        }
//    }


    /**
     * 展示是否认证成功和用户名
     */
    private void showUserInfo(final int isOK) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (isOK) {
                    case 0:
                        if (nowUIState != 0) {
                            tvMainSign.setText("欢迎使用");
                            tvMainName.setText("");
                            tvMainNumber.setText("");
                            nowUIState = 0;
                        }
                        break;
                    case 1:
                        if (nowUIState != 1) {
                            tvMainSign.setText("签到成功");
                            tvMainName.setText("用户：" + userName);
                            tvMainNumber.setText("工号：" + userID);
                            nowUIState = 1;
                        }
                        break;
                    case 2:
                        if (nowUIState != 2) {
                            tvMainSign.setText("未找到匹配项");
                            tvMainName.setText("");
                            tvMainNumber.setText("");
                            nowUIState = 2;
                        }
                        break;
                }

            }
        });
    }

    @OnClick({R.id.iv_main_circle, R.id.iv_main_guanli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_main_circle:
                Intent intentToAdmin = new Intent(MainActivity.this, AdminPasswordActivity.class);
                intentToAdmin.putExtra(WhoSetConstant.NAME, WhoSetConstant.USER_ID);
                startActivity(intentToAdmin);
                break;
            case R.id.iv_main_guanli:
                Intent intent = new Intent(MainActivity.this, AdminPasswordActivity.class);
                intent.putExtra(WhoSetConstant.NAME, WhoSetConstant.ADMIN);
                startActivity(intent);
                break;
        }
    }

    /**
     * 监听1对1验证通过时刷新主界面的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void passCheck1To1(PassCheckEvent event) {
        tvMainName.setText("用户：" + event.userName);
        tvMainNumber.setText("工号：" + event.userId);
        tvMainSign.setText("签到成功");
        nowUIState = 4;
    }


    /**
     * 当触摸就会执行此方法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mScreensaver.resetTime(); //重置时间
        return super.dispatchTouchEvent(ev);
    }

    //当时间到会执行此方法
    @Override
    public void onTimeOut(Screensaver screensaver) {
        if (screenDialog == null) {
            screenDialog = new ScreenDialog(this, R.style.FullscreenTheme, cancelListener);
            screenDialog.show();
        }
    }

    Dialog.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            screenDialog = null;
            mScreensaver.resetTime(); //重置时间
        }
    };
}
