package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetSetActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_local_ip)
    EditText etLocalIp;
    @BindView(R.id.et_local_yanma)
    EditText etLocalYanma;
    @BindView(R.id.et_local_wangguan)
    EditText etLocalWangguan;
    @BindView(R.id.sw_dhcp)
    Switch swDhcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_set);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //活动销毁时保存设置的信息
        SystemSettingUtil.setLocalDHCP(swDhcp.isChecked());
        SystemSettingUtil.setLocalIP(etLocalIp.getText().toString());
        SystemSettingUtil.setLocalMask(etLocalYanma.getText().toString());
        SystemSettingUtil.setLocalGateway(etLocalWangguan.getText().toString());
    }

    private void initView() {
        swDhcp.setChecked(SystemSettingUtil.getLocalDHCP());
        etLocalIp.setText(SystemSettingUtil.getLocalIP());
        etLocalYanma.setText(SystemSettingUtil.getLocalMask());
        etLocalWangguan.setText(SystemSettingUtil.getLocalGateway());
        if (!swDhcp.isChecked()) {
            etLocalIp.setEnabled(false);
            etLocalYanma.setEnabled(false);
            etLocalWangguan.setEnabled(false);
        }
        //根据switch设置控件是否可用
        swDhcp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etLocalIp.setEnabled(true);
                    etLocalYanma.setEnabled(true);
                    etLocalWangguan.setEnabled(true);
                } else {
                    etLocalIp.setEnabled(false);
                    etLocalYanma.setEnabled(false);
                    etLocalWangguan.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
