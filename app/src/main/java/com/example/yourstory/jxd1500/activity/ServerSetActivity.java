package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServerSetActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_server_ip)
    EditText etServerIp;
    @BindView(R.id.et_server_mask)
    EditText etServerMask;
    @BindView(R.id.et_server_port)
    EditText etServerPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_certification);
        ButterKnife.bind(this);
        setViewData();
    }

    private void setViewData() {
        etServerIp.setText(SystemSettingUtil.getServerIP());
        etServerMask.setText(SystemSettingUtil.getServerMask());
        etServerPort.setText(SystemSettingUtil.getServerPort());
    }

    /**
     * 设置服务器的ip
     */
    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        SystemSettingUtil.setServerIP(etServerIp.getText().toString());
        SystemSettingUtil.setServerMask(etServerMask.getText().toString());
        SystemSettingUtil.setServerPort(etServerPort.getText().toString());
        finish();
    }
}
