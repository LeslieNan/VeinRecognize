package com.example.yourstory.jxd1500.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourstory.jxd1500.MainActivity;
import com.example.yourstory.jxd1500.MyApplication;
import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.broadcast.PassCheckEvent;
import com.example.yourstory.jxd1500.constant.UserInfoConstant;
import com.example.yourstory.jxd1500.constant.WhoSetConstant;
import com.example.yourstory.jxd1500.db.SignDao;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.totp.PasscodeGenerator;
import com.example.yourstory.jxd1500.util.CodeUtil;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;
import com.example.yourstory.jxd1500.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jx.vein.javajar.JXFVJavaInterface;

import static android.widget.Toast.LENGTH_SHORT;
import static jx.vein.javajar.JXFVJavaInterface.veinFeatureSize;
import static jx.vein.javajar.JXFVJavaInterface.veinImgSize;
import static jx.vein.javajar.JXFVJavaInterface.veinSampleSize;

public class AdminPasswordActivity extends AppCompatActivity {


    private static final String TAG = "AdminPasswordActivity";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_num_title)
    TextView tvNumTitle;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.num1)
    ImageView num1;
    @BindView(R.id.num2)
    ImageView num2;
    @BindView(R.id.num3)
    ImageView num3;
    @BindView(R.id.num4)
    ImageView num4;
    @BindView(R.id.num5)
    ImageView num5;
    @BindView(R.id.num6)
    ImageView num6;
    @BindView(R.id.num7)
    ImageView num7;
    @BindView(R.id.num8)
    ImageView num8;
    @BindView(R.id.num9)
    ImageView num9;
    @BindView(R.id.num10)
    ImageView num10;
    @BindView(R.id.num11)
    ImageView num11;
    @BindView(R.id.num12)
    ImageView num12;

    private StringBuilder password = new StringBuilder("");
    private int identity;
    private JXFVJavaInterface jxfvJavaInterface;
    private long devHandler;
    private long dbHandler;
    private UserDao userDao;
    private String id;
    private boolean childThreadRun = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_password);
        ButterKnife.bind(this);
        userDao = new UserDao(this);
        Intent intent = getIntent();
        identity = intent.getIntExtra(WhoSetConstant.NAME, WhoSetConstant.USER_ID);
        switch (identity) {
            case WhoSetConstant.ADMIN:
                tvNumTitle.setText(WhoSetConstant.ADMIN_TITLE);
                break;
            case WhoSetConstant.USER_ID:
                tvNumTitle.setText(WhoSetConstant.USER_TITLE);
                break;
            case WhoSetConstant.INPUTPW:
                tvNumTitle.setText(WhoSetConstant.INPUT_TITLE);
                //若认证模式开启了1:1认证，则开始认证
                id = getIntent().getStringExtra("id");
                if (SystemSettingUtil.getCertification1to1()) {
                    initVeinAPI();
                    initVeinConnetced();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //因此活动有多个实例，有的实例可能会不初始化AIPI
        if (jxfvJavaInterface != null) {
            jxfvJavaInterface.jxDisConnFVD(devHandler); //解除设备的连接
            childThreadRun = false;                       //暂停子线程的识别
        }
    }

    private void initVeinConnetced() {
        int connected = jxfvJavaInterface.jxIsFVDConnected(devHandler);
        Log.d(TAG, "检测是否有设备连接到主机:" + connected);
        if (connected == 1) {
            //若有设备连接到主机，则连接设备
            int connectedTo = jxfvJavaInterface.jxConnFVD(devHandler);
            Log.d(TAG, "连接到静脉识别设备，在检测到设备已连接后调用:" + connectedTo);
            if (connectedTo == 0) {
                open1To1Renzheng();
            }
        }
    }

    /**
     * 开启1对1识别
     */
    private void open1To1Renzheng() {
        //开启子线程进行静脉识别
        childThreadRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //当设备工作正常
                while (childThreadRun) {
                    int cover = jxfvJavaInterface.jxIsFingerDetected(devHandler);
                    if (cover == 1) {
                        //感应到手指
                        getImg();
                    }
                    if (cover == 0) {
                        //未检测到手指
                    } else if (cover == -100) {
                        //未知错误
                        Log.d(TAG, "未知错误");
                    }
                }
            }
        }).start();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, R.id.num6,
            R.id.num7, R.id.num8, R.id.num9, R.id.num10, R.id.num11, R.id.num12})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.num1:
                password.append(1);
                break;
            case R.id.num2:
                password.append(2);
                break;
            case R.id.num3:
                password.append(3);
                break;
            case R.id.num4:
                password.append(4);
                break;
            case R.id.num5:
                password.append(5);
                break;
            case R.id.num6:
                password.append(6);
                break;
            case R.id.num7:
                password.append(7);
                break;
            case R.id.num8:
                password.append(8);
                break;
            case R.id.num9:
                password.append(9);
                break;
            case R.id.num10:
                //删除最后一个字符
                if (password.length() > 0) {
                    password.deleteCharAt(password.length() - 1);
                }
                break;
            case R.id.num11:
                password.append(0);
                break;
            case R.id.num12:
                if (!etPassword.getText().toString().equals("")) {
                    checkInput();
                }
                break;
        }
        etPassword.setText(password);
    }

    /**
     * 验证密码
     */
    private void checkInput() {
        switch (identity) {
            case WhoSetConstant.ADMIN:
                checkAdmin();
                break;
            case WhoSetConstant.USER_ID:
                checkID();
                break;
            case WhoSetConstant.INPUTPW:
                if (SystemSettingUtil.getCertificationPsw()) {
                    checkIDAndPsw();
                } else {
                    Toast.makeText(this, "密码认证未开启", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void checkIDAndPsw() {
        String psw = etPassword.getText().toString();
        if (!psw.equals("")) {
            String username;
            if ((username = userDao.checkIDAndPsw(id, psw)) != null) {
                //验证成功
                //加入到签到数据库里
                SignDao signDao = new SignDao(this);
                signDao.addSign(id, TimeUtil.getNowTime(), UserInfoConstant.SIGN_TYPE_PSW);
                EventBus.getDefault().post(new PassCheckEvent(username, id));
                //启动活动
                Intent intent = new Intent(AdminPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "密码错误", LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化指静脉认证API
     */
    private void initVeinAPI() {
        jxfvJavaInterface = MyApplication.getjxfvJavaInterface();
        devHandler = MyApplication.getDvHandler();
        dbHandler = MyApplication.getDbHandler();
    }

    /**
     * 验证用户ID
     */
    private void checkID() {
        if (userDao.recheckByID(etPassword.getText().toString())) {
            Intent intent = new Intent(AdminPasswordActivity.this, AdminPasswordActivity.class);
            intent.putExtra(WhoSetConstant.NAME, WhoSetConstant.INPUTPW);
            intent.putExtra("id", etPassword.getText().toString());
            startActivity(intent);
        } else {
            //该id不存在
            Toast.makeText(AdminPasswordActivity.this, "该ID不存在", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 验证管理员密码,默认：123456
     */
    private void checkAdmin() {
        boolean islxlp = SystemSettingUtil.getNowPswMode();
        if (islxlp) {
            //若当前设置的是灵犀令牌密码
            Calendar calendar = Calendar.getInstance();
            long time = calendar.get(Calendar.YEAR) +
                    calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH);
            String adminPsw = PasscodeGenerator.generateTotpNum(time);
            Log.d("灵犀密钥", adminPsw);
            if (adminPsw.equals(etPassword.getText().toString())) {
                Intent intent = new Intent(AdminPasswordActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "请正确输入灵犀令牌的动态密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            //若是固定管理员密码
            String adminPsw = SystemSettingUtil.getPswKey();
            if (adminPsw.equals(etPassword.getText().toString())) {
                Intent intent = new Intent(AdminPasswordActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "管理员密码错误", Toast.LENGTH_SHORT).show();

            }
        }

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
            String userID = CodeUtil.findUserFromVeinID(veinIDs);
            if (userID.equals(id)) {
                //说明该指纹的id和输入的id相同，验证通过
                String userName = userDao.selectNameByID(userID);
                EventBus.getDefault().post(new PassCheckEvent(userName, id));
                //加入到签到数据库里
                SignDao signDao = new SignDao(this);
                signDao.addSign(id, TimeUtil.getNowTime(), UserInfoConstant.SIGN_TYPE_1To1);
                Intent mainActivityIntnet = new Intent(AdminPasswordActivity.this, MainActivity.class);
                startActivity(mainActivityIntnet);
            } else {
                showToast(1);
            }
            Log.d(TAG, "样本的数量长度" + veinIDs.length);
            Log.d(TAG, "样本的用户名" + new String(veinIDs));
        } else if (exist == 0) {
            Log.d(TAG, "该指纹和id不匹配");
            showToast(0);
        }
    }


    private void showToast(final int what) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (what) {
                    case 1:
                        Toast.makeText(AdminPasswordActivity.this, "指纹不匹配", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(AdminPasswordActivity.this, "指纹不匹配", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
