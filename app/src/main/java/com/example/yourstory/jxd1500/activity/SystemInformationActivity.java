package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemInformationActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_system_name)
    EditText etSystemName;
    @BindView(R.id.tv_system_model)
    TextView tvSystemModel;
    @BindView(R.id.tv_system_version)
    TextView tvSystemVersion;
    @BindView(R.id.tv_system_serial)
    TextView tvSystemSerial;
    @BindView(R.id.tv_system_android)
    TextView tvSystemAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_information);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemSettingUtil.setDeviceName(etSystemName.getText().toString());
    }

    private void initView() {
        //初始化设备名称
        etSystemName.setText(SystemSettingUtil.getDevicename());
        tvSystemModel.setText(android.os.Build.MODEL);
        tvSystemVersion.setText(android.os.Build.BOOTLOADER);
        tvSystemSerial.setText(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        tvSystemAndroid.setText(android.os.Build.VERSION.RELEASE);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
