package com.example.yourstory.jxd1500.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.yourstory.jxd1500.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WgActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rb_wgset_26)
    RadioButton rbWgset26;
    @BindView(R.id.rb_wgset_34)
    RadioButton rbWgset34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wg);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
