package com.example.yourstory.lxlp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.yourstory.lxlp.totp.PasscodeGenerator;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    private TextView tvshowTime;
    private TextView tvChange;
    private TextView tvShow;

    //用来获取当前日期时间
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        long time = calendar.get(Calendar.YEAR) +
                calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH);
        String password = PasscodeGenerator.generateTotpNum(time);
        tvShow.setText(password);
    }

    /**
     * 初始化View
     */
    @SuppressLint("SetTextI18n")
    private void initView() {
        tvshowTime = findViewById(R.id.tv_show_time);
        tvChange = findViewById(R.id.tv_change);
        tvShow = findViewById(R.id.tv_password);
        //获取时间类
        calendar = Calendar.getInstance();
        tvshowTime.setText(calendar.get(Calendar.YEAR) + "年" +
                (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        tvshowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeKey();
            }
        });
    }

    /**
     * 更改密钥
     */
    private void changeKey() {
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }


    /**
     * 弹出日期选择框
     */
    @SuppressLint("ResourceType")
    private void showDatePickerDialog() {
        new DatePickerDialog(this, 4, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                long time = year + monthOfYear + dayOfMonth;
                String password = PasscodeGenerator.generateTotpNum(time);
                tvshowTime.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                tvShow.setText(password);
            }
        }// 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


}
