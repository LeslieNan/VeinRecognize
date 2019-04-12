package com.example.yourstory.jxd1500.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.util.SystemSettingUtil;
import com.example.yourstory.jxd1500.util.TimeUtil;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSettingActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_timesetting_nowtime)
    TextView tvTimesettingNowtime;
    @BindView(R.id.sw_timesystem)
    Switch swTimesystem;
    @BindView(R.id.tv_timesetting_showtime)
    TextView tvTimesettingShowtime;
    @BindView(R.id.sw_time_auto)
    Switch swTimeAuto;
    @BindView(R.id.tv_timesetting_ip)
    TextView tvTimesettingIp;
    @BindView(R.id.tv_timesetting_showdate)
    TextView tvTimesettingShowdate;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar timeBufferdCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //设置系统时区和calendar的时区
        TimeUtil.setTimeZone();
        timeBufferdCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if (DateFormat.is24HourFormat(this)) {
            //若当前时24小时制
            swTimesystem.setChecked(true);
        }
        if (TimeUtil.isDateTimeAuto()) {
            //当前系统时间是自动获取的
            swTimeAuto.setChecked(true);
            tvTimesettingShowdate.setEnabled(false);
            tvTimesettingShowtime.setEnabled(false);
        }
        //获取当前系统时间
        updateShowTime();
        //设置系统小时制
        swTimesystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Settings.System.putString(getContentResolver(),
                            Settings.System.TIME_12_24, "24");
                } else {
                    Settings.System.putString(getContentResolver(),
                            Settings.System.TIME_12_24, "12");
                }
            }
        });
        swTimeAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    TimeUtil.setAutoDateTime(1);
                    TimeUtil.setAutoTimeZone(1);
                    tvTimesettingShowdate.setEnabled(false);
                    tvTimesettingShowtime.setEnabled(false);
                    timeBufferdCalendar = Calendar.getInstance();
                    updateShowTime();
                } else {
                    TimeUtil.setAutoDateTime(0);
                    TimeUtil.setAutoTimeZone(0);
                    tvTimesettingShowdate.setEnabled(true);
                    tvTimesettingShowtime.setEnabled(true);
                    updateShowTime();
                }
            }
        });
    }

    private void updateShowTime() {
        tvTimesettingNowtime.setText(TimeUtil.datechange(timeBufferdCalendar.getTime(),
                "yyyy-MM-dd  HH:mm:ss"));
        tvTimesettingShowdate.setText(TimeUtil.datechange(timeBufferdCalendar.getTime(),
                "yyyy年MM月dd日"));
        tvTimesettingShowtime.setText(TimeUtil.datechange(timeBufferdCalendar.getTime(),
                "HH:mm:ss"));
    }


    //设置时间到calendar
    public void setBufferdDate(int year, int month, int day) {
        timeBufferdCalendar.set(Calendar.YEAR, year);
        timeBufferdCalendar.set(Calendar.MONTH, month);
        timeBufferdCalendar.set(Calendar.DAY_OF_MONTH, day);
        updateShowTime();
    }

    public void setBufferdTime(int hour, int minute) {
        timeBufferdCalendar.set(Calendar.HOUR_OF_DAY, hour);
        timeBufferdCalendar.set(Calendar.MINUTE, minute);
        timeBufferdCalendar.set(Calendar.SECOND, 0);
        timeBufferdCalendar.set(Calendar.MILLISECOND, 0);
        updateShowTime();
    }


    private void displayDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                tvTimesettingShowdate.setText(years + "年" + (months + 1) + "月" + days + "日");
                setBufferdDate(years, months, days);
                datePickerDialog.cancel();
            }
        };
        int year = timeBufferdCalendar.get(Calendar.YEAR);
        int month = timeBufferdCalendar.get(Calendar.MONTH);
        int day = timeBufferdCalendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void displayTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                tvTimesettingShowtime.setText(hourOfDay + ":" + minute + ":00");
                setBufferdTime(hourOfDay, minute);
                timePickerDialog.cancel();
            }
        };
        int hourOfDay = timeBufferdCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = timeBufferdCalendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                timeSetListener, hourOfDay, minute, true);
        timePickerDialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_timesetting_showdate, R.id.tv_timesetting_showtime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_timesetting_showdate:
                displayDatePickerDialog();
                break;
            case R.id.tv_timesetting_showtime:
                displayTimePickerDialog();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存NTP选定状态
        if (!swTimeAuto.isChecked()) {
            TimeUtil.updateSysTime(timeBufferdCalendar);
        }
    }

}
