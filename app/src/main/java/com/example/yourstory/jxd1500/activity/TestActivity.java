package com.example.yourstory.jxd1500.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.constant.UserInfoConstant;
import com.example.yourstory.jxd1500.util.CodeUtil;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jx.vein.javajar.JXFVJavaInterface;

import static jx.vein.javajar.JXFVJavaInterface.groupIDLength;
import static jx.vein.javajar.JXFVJavaInterface.imgCols;
import static jx.vein.javajar.JXFVJavaInterface.imgRows;
import static jx.vein.javajar.JXFVJavaInterface.veinFeatureSize;
import static jx.vein.javajar.JXFVJavaInterface.veinIDLength;
import static jx.vein.javajar.JXFVJavaInterface.veinImgSize;
import static jx.vein.javajar.JXFVJavaInterface.veinSampleSize;

public class TestActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    //    private static final int BUFLENGH = 1024 * 1024 * 1024 * 1024;

    @BindView(R.id.btn_connected)
    Button btnConnected;
    @BindView(R.id.btn_disconnected)
    Button btnDisconnected;
    @BindView(R.id.iv_finger)
    ImageView ivFinger;
    @BindView(R.id.btn_tomenu)
    Button btnTomenu;


    private JXFVJavaInterface jxfvJavaInterface;    //设备API接口
    private long devHandler;                        //设备的唯一标识符
    private long dbHandler;                         //数据库连接句柄
    private byte[] groupID = new byte[groupIDLength]; //组名
    private byte[] userID = new byte[veinIDLength];   //用户名
    private String fileName;                            //数据库路径和Name


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        jxfvJavaInterface = new JXFVJavaInterface();
        devHandler = jxfvJavaInterface.jxInitUSBDriver();
        groupID = CodeUtil.strToBytes("firstGroup", groupIDLength);
        userID = CodeUtil.strToBytes("firstuser", veinIDLength);
        Log.d(TAG, "初始化静脉设备驱动,其句柄为:" + devHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jxfvJavaInterface.jxDeInitUSBDriver(devHandler);
        Log.d(TAG, "释放USB驱动，在系统退出时调用，重复调用无效。");
    }


    @OnClick({R.id.btn_connected, R.id.btn_disconnected, R.id.btn_tomenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_connected:
                connected();
                break;
            case R.id.btn_disconnected:
                disConnected();
                break;
            case R.id.btn_tomenu:
                Intent intent = new Intent(TestActivity.this, MainMenuActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 连接设备
     */
    private void connected() {
        int connected = jxfvJavaInterface.jxIsFVDConnected(devHandler);
        Log.d(TAG, "检测是否有设备连接到主机:" + connected);
        if (connected == 1) {
            //若有设备连接到主机，则连接设备
            int connectedTo = jxfvJavaInterface.jxConnFVD(devHandler);
            Log.d(TAG, "连接到静脉识别设备，在检测到设备已连接后调用:" + connectedTo);
            if (connectedTo == 0) {
                if (initDB()) {
                    //若数据库初始化成功
                    openChildThread();
                }
            }
        }
    }

    /**
     * 开启新线程，循环检测是否有手指放入识别模块
     */
    private void openChildThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int cover = jxfvJavaInterface.jxIsFingerDetected(devHandler);
                    if (cover == 1) {
                        getPhoto();
                        //检测到有遮挡物
//                        int finger = jxfvJavaInterface.jxIsFingerTouched(devHandler);       //在此不用
//                        if (finger == 1) {
//                            //感应到到手指
//                            getImg();
//
//                        } else if (finger == 0) {
//                            //未检测到手指
////                            Toast.makeText(MainActivity.this, "请正确放入手指", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "请正确放入手指");
//                        } else if (finger == -100) {
////                            Toast.makeText(MainActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "出现错误");
//                        }
                    } else if (cover == -100) {
                        //未知错误
                        Log.d(TAG, "未知错误");
                    }
                }
            }
        }).start();
    }

    /**
     * 获取手指图像
     */
    public void getPhoto() {
        if (jxfvJavaInterface.jxInitCapEnv(devHandler) == 0) {
            //初始化图像采集环境
            //采集图像，只用于显示
            int isOK = 0;
            byte[] img_buf = new byte[veinImgSize];
            while (isOK != 2 && isOK != 3) {
                try {
                    isOK = jxfvJavaInterface.jxIsVeinImgOK(devHandler, img_buf);
                    Log.d(TAG, "获取图像状态" + isOK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "结束");

            Log.d(TAG, img_buf.length + "," + imgRows * imgCols);
            Bitmap bitmap = null;
            bitmap = Bitmap.createBitmap(imgRows, imgCols, Bitmap.Config.ALPHA_8);

            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(img_buf));

            setImgBitmap(bitmap);

            resetEnviroment();
            //采集完成，获取信息包
            if (isOK == 2) {
                //用于存储信息包
                byte[] sample_buf = new byte[veinSampleSize];
                try {
                    jxfvJavaInterface.jxLoadVeinSample(devHandler, sample_buf);
                    rigister(sample_buf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    private void resetEnviroment() {
        //恢复图像采集环境
        int isRight = jxfvJavaInterface.jxDeInitCapEnv(devHandler);
        if (isRight == 0) {
            Log.d(TAG, "恢复图像采集环境完成");
        }
    }

    /**
     * 注册静脉信息
     */
    private void rigister(byte[] sample_buf) {
        try {
            //检测样本格式是否合格
            int isOK = jxfvJavaInterface.jxCheckVeinSampleQuality(sample_buf);
            if (isOK == 0) {
                Log.d(TAG, "样本质量合格");
                byte[] feat_buf = new byte[veinFeatureSize];   //静脉特征
                if (0 == jxfvJavaInterface.jxGrabVeinFeature(sample_buf, feat_buf)) {
                    //从静脉样本中提取出静脉特征,且合格
                    int repeat = jxfvJavaInterface.jxIsFeatureDuplicate(dbHandler, feat_buf);
                    if (0 == repeat) {
                        //该静脉特征与数据库中未出现重复
                        addToDB(feat_buf);
                    } else if (repeat == 1) {
                        Log.d(TAG, "静脉与数据库中出现重复,已存在");
                    } else {
                        Log.d(TAG, "查重出现错误，错误码：" + repeat);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将静脉特征加入到数据库中
     */
    private void addToDB(byte[] feat_buf) {

        try {
            int i = jxfvJavaInterface.jxAddOneVeinFeature(dbHandler, feat_buf, userID, groupID);
            if (0 == i) {
                //添加成功
                Log.d(TAG, "注册成功");
                int n = jxfvJavaInterface.jxCountGroupFeatures(dbHandler, groupID);
                Log.d(TAG, "数据库中有" + n + "条数据");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * 初始化数据库
     */
    private boolean initDB() {
        String fileDirectory = getExternalFilesDir("").getAbsolutePath();    //获取外部存储的file路径
        fileName = fileDirectory + "/" + UserInfoConstant.DB_NAME;
        if (0 == jxfvJavaInterface.jxIsVeinDBExist(fileName)) {
            //若数据库不存在,则创建
            if (0 == jxfvJavaInterface.jxCreateVeinDatabase(fileName)) {
                //创建成功
            }
        }
        if ((dbHandler = jxfvJavaInterface.jxInitVeinDatabase(fileName)) != 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 设置imageView的图像
     *
     * @param bitmap
     */

    private void setImgBitmap(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, bitmap.toString());
                ivFinger.setImageBitmap(bitmap);
            }
        });
    }


    /**
     * 断开连接
     */
    private void disConnected() {
        jxfvJavaInterface.jxDisConnFVD(devHandler);
        Log.d(TAG, "断开设备连接");
    }

}
