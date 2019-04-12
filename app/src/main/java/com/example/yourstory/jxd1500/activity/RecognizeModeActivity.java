package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecognizeModeActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_certification_select_1to1)
    ImageView ivCertificationSelect1to1;
    @BindView(R.id.iv_certification_select_1ton)
    ImageView ivCertificationSelect1ton;
    @BindView(R.id.iv_certification_select_psw)
    ImageView ivCertificationSelectPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemSettingUtil.setCertification1to1(ivCertificationSelect1to1.isSelected());
        SystemSettingUtil.setCertification1toN(ivCertificationSelect1ton.isSelected());
        SystemSettingUtil.setCertificationPsw(ivCertificationSelectPsw.isSelected());
    }

    private void initView() {
        ivCertificationSelect1to1.setSelected(SystemSettingUtil.getCertification1to1());
        ivCertificationSelect1ton.setSelected(SystemSettingUtil.getCertification1toN());
        ivCertificationSelectPsw.setSelected(SystemSettingUtil.getCertificationPsw());
    }

    @OnClick({R.id.iv_back, R.id.iv_certification_select_1to1, R.id.iv_certification_select_1ton, R.id.iv_certification_select_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_certification_select_1to1:
                if (ivCertificationSelect1to1.isSelected()) {
                    ivCertificationSelect1to1.setSelected(false);
                } else {
                    ivCertificationSelect1to1.setSelected(true);
                }
                break;
            case R.id.iv_certification_select_1ton:
                if (ivCertificationSelect1ton.isSelected()) {
                    ivCertificationSelect1ton.setSelected(false);
                } else {
                    ivCertificationSelect1ton.setSelected(true);
                }
                break;
            case R.id.iv_certification_select_psw:
                if (ivCertificationSelectPsw.isSelected()) {
                    ivCertificationSelectPsw.setSelected(false);
                } else {
                    ivCertificationSelectPsw.setSelected(true);
                }
                break;
        }
    }
}
