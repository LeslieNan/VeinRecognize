package com.example.yourstory.jxd1500.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourstory.jxd1500.MyApplication;
import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.bean.FingerBuf;
import com.example.yourstory.jxd1500.constant.UserInfoConstant;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.dialog.InputVeinDialog;
import com.example.yourstory.jxd1500.util.CodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jx.vein.javajar.JXFVJavaInterface;

import static jx.vein.javajar.JXFVJavaInterface.groupIDLength;
import static jx.vein.javajar.JXFVJavaInterface.veinIDLength;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_id)
    EditText etRegisterId;
    @BindView(R.id.et_register_group)
    EditText etRegisterGroup;
    @BindView(R.id.et_register_psw)
    EditText etRegisterPsw;
    @BindView(R.id.et_register_agpsw)
    EditText etRegisterAgpsw;
    @BindView(R.id.iv_register_yanid)
    ImageView ivRegisterYanid;
    @BindView(R.id.iv_register_yanpsw)
    ImageView ivRegisterYanpsw;
    @BindView(R.id.btn_register_caiji)
    Button btnRegisterCaiji;
    @BindView(R.id.btn_register_finger1)
    TextView btnRegisterFinger1;
    @BindView(R.id.iv_register_finger1)
    ImageView ivRegisterFinger1;
    @BindView(R.id.btn_register_finger2)
    TextView btnRegisterFinger2;
    @BindView(R.id.iv_register_finger2)
    ImageView ivRegisterFinger2;
    @BindView(R.id.btn_register_finger3)
    TextView btnRegisterFinger3;
    @BindView(R.id.iv_register_finger3)
    ImageView ivRegisterFinger3;
    @BindView(R.id.btn_register_caijiface)
    Button btnRegisterCaijiface;
    @BindView(R.id.btn_register_face)
    TextView btnRegisterFace;
    @BindView(R.id.iv_register_face)
    ImageView ivRegisterFace;
    @BindView(R.id.btn_register_true)
    Button btnRegisterTrue;
    @BindView(R.id.btn_register_false)
    Button btnRegisterFalse;
    @BindView(R.id.tv_warn_id)
    TextView tvWarnId;
    @BindView(R.id.tv_warn_psw)
    TextView tvWarnPsw;


    private JXFVJavaInterface jxfvJavaInterface;    //设备API接口
    private long dbHandler;                         //数据库连接句柄
    private FingerBuf[] fingerBufs = new FingerBuf[3];  //用于存储三个指头的静脉指纹
    private int fingerCount = 0;                        //用于记录注册的手指数

    private TextView[] fingerTextview;     //三个textView,用于设置visible
    private ImageView[] fingerImageview;   //三个image

    private byte[] groupID = new byte[groupIDLength];   //组名
    private InputVeinDialog dialog;                   //弹出窗
    private String veinID;
    private UserDao userDao = new UserDao(this);

    private boolean isIDOK = false;                     //id验证是否合格


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initAPI();
        initViewSetting();
    }


    /**
     * 控件的监听事件
     */
    private void initViewSetting() {
        //验证id
        etRegisterId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //当失去焦点时
                    String id = etRegisterId.getText().toString();
                    ivRegisterYanid.setVisibility(View.INVISIBLE);
                    tvWarnId.setVisibility(View.INVISIBLE);
                    isIDOK = false;
                    if (id.length() != UserInfoConstant.PASSWORD_LENGTH) {
                        tvWarnId.setVisibility(View.VISIBLE);
                        tvWarnId.setText("*id为6位数字");
                    } else {
                        if (userDao.recheckByID(id)) {
                            //若数据库中有该id
                            tvWarnId.setVisibility(View.VISIBLE);
                            tvWarnId.setText("*id不可重复");
                        } else {
                            //若数据库中无此id
                            tvWarnId.setVisibility(View.INVISIBLE);
                            ivRegisterYanid.setVisibility(View.VISIBLE);
                            isIDOK = true;
                        }
                    }
                }
            }
        });
        //验证密码
        etRegisterAgpsw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    //当失去焦点时
                    String psw = etRegisterPsw.getText().toString();
                    String agPsw = etRegisterAgpsw.getText().toString();
                    ivRegisterYanpsw.setVisibility(View.INVISIBLE);
                    tvWarnPsw.setVisibility(View.INVISIBLE);
                    if (psw.equals(agPsw) && !psw.equals("")) {
                        ivRegisterYanpsw.setVisibility(View.VISIBLE);
                        tvWarnPsw.setVisibility(View.INVISIBLE);
                    } else {
                        tvWarnPsw.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initAPI() {
        jxfvJavaInterface = MyApplication.getjxfvJavaInterface();
        dbHandler = MyApplication.getDbHandler();
        fingerTextview = new TextView[]{btnRegisterFinger1, btnRegisterFinger2, btnRegisterFinger3};
        fingerImageview = new ImageView[]{ivRegisterFinger1, ivRegisterFinger2, ivRegisterFinger3};
    }


    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.iv_back, R.id.btn_register_caiji, R.id.btn_register_caijiface, R.id.btn_register_true, R.id.btn_register_false,
            R.id.iv_register_finger1, R.id.iv_register_finger2, R.id.iv_register_finger3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_register_caiji:
                showEditDialog();
                break;
            case R.id.btn_register_caijiface:
                break;
            case R.id.btn_register_true:
                //当所有输入框不为空并有指纹录入时
                if (!etRegisterUsername.getText().toString().equals("") &&
                        !etRegisterId.getText().toString().equals("") &&
                        !etRegisterAgpsw.getText().toString().equals("") &&
                        !etRegisterPsw.getText().toString().equals("") &&
                        !etRegisterGroup.getText().toString().equals("") &&
                        !CodeUtil.isArrayNull(fingerBufs) && isIDOK) {
                    getInputMessage();
                } else {
                    Toast.makeText(this, "请填写所有信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register_false:
                finish();
                break;
            case R.id.iv_register_finger1:
                fingerBufs[0] = null;
                fingerTextview[0].setVisibility(View.INVISIBLE);
                fingerImageview[0].setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_register_finger2:
                fingerBufs[1] = null;
                fingerTextview[1].setVisibility(View.INVISIBLE);
                fingerImageview[1].setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_register_finger3:
                fingerBufs[2] = null;
                fingerTextview[2].setVisibility(View.INVISIBLE);
                fingerImageview[2].setVisibility(View.INVISIBLE);
                break;
        }
    }


    /**
     * 检查输入数据的格式
     */
    private void getInputMessage() {
        veinID = etRegisterId.getText().toString();
        String name = etRegisterUsername.getText().toString();
        String group = etRegisterGroup.getText().toString();
        String psw = etRegisterPsw.getText().toString();
        String agPsw = etRegisterAgpsw.getText().toString();
        if (!agPsw.equals(psw)) {
            Toast.makeText(this, "两次密码不同", Toast.LENGTH_SHORT).show();
        } else if (!name.equals("") && !group.equals("")) {
            groupID = CodeUtil.strToBytes(group, groupIDLength);
            register(veinID, name, group, psw);
        }
    }


    /**
     * 注册到本地数据库
     */
    private void register(String id, String name, String group, String psw) {
        try {
            for (int i = 0; i < fingerBufs.length; i++) {
                if (fingerBufs[i] != null) {
                    //存储的veinID是组合形式，组合形式为：userID+fingerCount,fingerCount指第几个手指
                    int isOK = jxfvJavaInterface.jxAddThreeVeinFeature(dbHandler
                            , fingerBufs[i].feat_buf1
                            , fingerBufs[i].feat_buf2
                            , fingerBufs[i].feat_buf3
                            , CodeUtil.strToBytes(veinID + fingerCount, veinIDLength), groupID);
                    if (0 == isOK) {
                        //添加成功
                        Log.d(TAG, "注册一个手指成功");
                        fingerCount++;
                    }
                }
            }
            //添加到本地数据库
            userDao.addUserWithID(id, name, group, psw, fingerCount);
            popToast();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fingerBufs = null;
        fingerTextview = null;
        fingerImageview = null;
    }


    private void popToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 弹出收集指静脉的dialog
     */
    private void showEditDialog() {
        if (dialog != null) {
            dialog = null;
        }
        dialog = new InputVeinDialog(this, R.style.MyDialog, dialogListener, fingerBufs);
        dialog.show();
    }

    //dialog消失后的监听，刷新当前UI
    Dialog.OnCancelListener dialogListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            refreshUI();
        }
    };


    /**
     * 刷新几个Button的UI
     */
    public void refreshUI() {
        //每次都检查手指采集情况，并更新三个Button的UI
        for (int i = 0; i < fingerBufs.length; i++) {
            if (fingerBufs[i] == null) {
                fingerTextview[i].setVisibility(View.INVISIBLE);
                fingerImageview[i].setVisibility(View.INVISIBLE);
            } else {
                fingerTextview[i].setVisibility(View.VISIBLE);
                fingerImageview[i].setVisibility(View.VISIBLE);
            }
        }
    }


}
