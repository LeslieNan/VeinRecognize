package com.example.yourstory.jxd1500.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.yourstory.jxd1500.MyApplication;
import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.bean.FingerBuf;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jx.vein.javajar.JXFVJavaInterface;

import static jx.vein.javajar.JXFVJavaInterface.veinFeatureSize;
import static jx.vein.javajar.JXFVJavaInterface.veinImgSize;
import static jx.vein.javajar.JXFVJavaInterface.veinSampleSize;

/**
 * Created by lenovo on 2019/3/23.
 */

public class InputVeinDialog extends Dialog {


    private static final String TAG = "InputVeinDialog";
    @BindView(R.id.tv_dialog_text)
    TextView tvDialogText;
    @BindView(R.id.btn_dialog_cancle)
    Button btnDialogCancle;

    private Activity activity;

    private JXFVJavaInterface jxfvJavaInterface;    //设备API接口
    private long devHandler;                        //设备的唯一标识符
    private long dbHandler;                         //数据库连接句柄
    private int times = 0;                          //识别次数
    private FingerBuf fingerBuf = new FingerBuf();            //指静脉字节数组缓存
    private FingerBuf[] fingerBufs;

    private boolean childThreadRun = true;

    public InputVeinDialog(@NonNull Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    public InputVeinDialog(@NonNull Context context, int themeResId, OnCancelListener cancelListener, FingerBuf[] fingerBufs) {
        super(context, themeResId);
        this.activity = (Activity) context;
        setOnCancelListener(cancelListener);
        this.fingerBufs = fingerBufs;
    }

    public InputVeinDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, FingerBuf[] fingerBufs) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
        this.fingerBufs = fingerBufs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_finger_dialog);
        ButterKnife.bind(this);

        /*
        * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
        * 对象,这样这可以以同样的方式改变这个Activity的属性.
        */
        Window dialogWindow = this.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        setCanceledOnTouchOutside(false);       //设置外部点击不消失

        startGetFinger();
    }

    private void startGetFinger() {
        jxfvJavaInterface = MyApplication.getjxfvJavaInterface();
        devHandler = MyApplication.getDvHandler();
        dbHandler = MyApplication.getDbHandler();
        connected();
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
                openChildThread();
            }
        }
    }

    /**
     * 开启新线程，循环检测是否有手指放入识别模块
     */
    private void openChildThread() {
        //开启子线程进行认证
        new Thread(new Runnable() {
            @Override
            public void run() {
                //当设备工作正常
                while (childThreadRun) {
                    showText(times);
                    int cover = jxfvJavaInterface.jxIsFingerDetected(devHandler);
                    if (cover == 1) {
                        //感应到手指
                        showText(6);
                        getImg();
                    }
                    if (cover == 0) {
                        //未检测到手指
//                        showText(times);
                    } else if (cover == -100) {
                        //未知错误
                        Log.d(TAG, "未知错误");
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
                    register(sample_buf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 注册静脉信息
     */
    private void register(byte[] sample_buf) {
        try {
            //检测样本格式是否合格
            int isOK = jxfvJavaInterface.jxCheckVeinSampleQuality(sample_buf);
            if (isOK == 0) {
                Log.d(TAG, "样本质量合格");
                byte[] feat_buf = new byte[veinFeatureSize];   //静脉特征
                if (0 == jxfvJavaInterface.jxGrabVeinFeature(sample_buf, feat_buf)) {
                    //从静脉样本中提取出静脉特征,且合格
                    if (times == 0) {
                        //当第一次采集,检测数据库中是否有重复的样本
                        int repeat = jxfvJavaInterface.jxIsFeatureDuplicate(dbHandler,
                                feat_buf);
                        if (0 == repeat) {
                            //该静脉特征与数据库中未出现重复
                            fingerBuf.feat_buf1 = feat_buf;
                            showText(1);
                            times++;
                        } else if (repeat == 1) {
                            Log.d(TAG, "静脉与数据库中出现重复,已存在");
                            showText(5);
                        } else {
                            Log.d(TAG, "查重匹配出现错误，错误码：" + repeat);
                        }
                    } else if (times == 1) {
                        fingerBuf.feat_buf2 = feat_buf;
                        showText(2);
                        times++;
                    } else if (times == 2) {
                        fingerBuf.feat_buf3 = feat_buf;
                        times++;
                        //根据当前是第几个手指，判断加入到哪个数组中
                        for (int i = 0; i < fingerBufs.length; i++) {
                            if (fingerBufs[i] == null) {
                                fingerBufs[i] = fingerBuf;
                                break;
                            }
                        }
                        showText(3);
                    }
                }
            }
            //每次检测成功后,手指放开后再进行识别
            while (jxfvJavaInterface.jxIsFingerDetected(devHandler) != 0) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showText(final int isOK) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (isOK) {
                    case 0:
                        tvDialogText.setText("第一次采集，请放入手指");
                        break;
                    case 1:
                        tvDialogText.setText("第二次采集，请放入手指");
                        break;
                    case 2:
                        tvDialogText.setText("第三次采集，请放入手指");
                        break;
                    case 3:
                        tvDialogText.setText("采集成功");
                        btnDialogCancle.setText("完成");
                        break;
                    case 4:
                        tvDialogText.setText("正在采集，请稍后");
                        break;
                    case 5:
                        tvDialogText.setText("该指静脉已注册");
                        break;
                    case 6:
                        tvDialogText.setText("正在采集，请稍后");
                        break;
                }
            }
        });
    }


    /**
     * 点击取消dialog,
     */
    @OnClick(R.id.btn_dialog_cancle)
    public void onViewClicked() {
        childThreadRun = false;           //让子线程退出循环
        jxfvJavaInterface.jxDisConnFVD(devHandler);
        this.cancel();
    }


}
