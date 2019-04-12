package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvancedSettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rg_advance_time)
    RadioGroup rgAdvanceTime;
    @BindView(R.id.sw_face)
    Switch swFace;
    @BindView(R.id.rb_advance_30s)
    RadioButton rbAdvance30s;
    @BindView(R.id.rb_advance_1min)
    RadioButton rbAdvance1min;
    @BindView(R.id.rb_advance_3min)
    RadioButton rbAdvance3min;
    @BindView(R.id.rb_advance_5min)
    RadioButton rbAdvance5min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        switch (SystemSettingUtil.getScreenTime()) {
            case 30:
                rbAdvance30s.setChecked(true);
                break;
            case 60:
                rbAdvance1min.setChecked(true);
                break;
            case 180:
                rbAdvance3min.setChecked(true);
                break;
            case 300:
                rbAdvance5min.setChecked(true);
                break;
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int time = 30;
        switch (rgAdvanceTime.getCheckedRadioButtonId()) {
            case R.id.rb_advance_30s:
                time = 30;
                break;
            case R.id.rb_advance_1min:
                time = 60;
                break;
            case R.id.rb_advance_3min:
                time = 180;
                break;
            case R.id.rb_advance_5min:
                time = 300;
                break;
        }
        SystemSettingUtil.setScreenTime(time);
    }
}
