package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.dialog.AlterLxlpDialog;
import com.example.yourstory.jxd1500.dialog.AlterPswDialog;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PswSetActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_psw_info)
    TextView tvPswInfo;
    @BindView(R.id.rb_pswset_lxlp)
    RadioButton rbPswsetLxlp;
    @BindView(R.id.rb_pswset_gdpsw)
    RadioButton rbPswsetGdpsw;
    private AlterLxlpDialog lxlpDialog;
    private AlterPswDialog pswDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_set);
        ButterKnife.bind(this);
        getNowSet();

    }

    private void getNowSet() {
        if (SystemSettingUtil.getNowPswMode()) {
            //使用的是灵犀令牌
            rbPswsetLxlp.setChecked(true);
        } else {
            rbPswsetGdpsw.setChecked(true);
        }
    }

    //获取当前选定的button项，根据这个判断弹出哪个dialog


    @OnClick({R.id.iv_back, R.id.tv_psw_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_psw_info:
                //判断当前选定的当前密码模式
                if (rbPswsetLxlp.isChecked()) {
                    showLxlpEditDialog();
                } else {
                    showPswEditDialog();
                }
                break;
        }
    }

    private void showPswEditDialog() {
        if (pswDialog != null) {
            pswDialog = null;
        }
        pswDialog = new AlterPswDialog(this, R.style.MyDialog);
        pswDialog.show();
    }

    /**
     * 弹出修改灵犀令牌密钥的dialog
     */
    private void showLxlpEditDialog() {
        if (lxlpDialog != null) {
            lxlpDialog = null;
        }
        lxlpDialog = new AlterLxlpDialog(this, R.style.MyDialog);
        lxlpDialog.show();
    }

    /**
     * 销毁前将当前密码模式保存下来
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rbPswsetLxlp.isChecked()) {
            SystemSettingUtil.setNowPswMode(true);
        } else {
            SystemSettingUtil.setNowPswMode(false);
        }
    }
}
