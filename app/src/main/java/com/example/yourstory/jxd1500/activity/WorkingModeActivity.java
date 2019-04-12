package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yourstory.jxd1500.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkingModeActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_workingmode_local)
    Button btnWorkingmodeLocal;
    @BindView(R.id.btn_workingmode_net)
    Button btnWorkingmodeNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_mode);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.btn_workingmode_local, R.id.btn_workingmode_net})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_workingmode_local:
                break;
            case R.id.btn_workingmode_net:
                break;
        }
    }
}
