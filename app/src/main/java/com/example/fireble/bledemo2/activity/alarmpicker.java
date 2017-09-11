package com.example.fireble.bledemo2.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fireble.bledemo2.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/10.
 */
public class alarmpicker extends Activity {
    TimePicker timepicker; // 时间拾取器
    Calendar c; // 日历对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        c = Calendar.getInstance(); // 获取日历对象
        c.setTimeInMillis(System.currentTimeMillis()); // 设置当前时间
        timepicker = (TimePicker) findViewById(R.id.timePicker1); // 获取时间拾取组件
        timepicker.setIs24HourView(true); // 设置使用24小时制
        timepicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY)); // 设置当前小时数
        timepicker.setCurrentMinute(c.get(Calendar.MINUTE)); // 设置当前分钟数
        ImageButton button1 = (ImageButton) findViewById(R.id.button1); // 获取“设置闹钟”按钮
        // 为“设置闹钟”按钮添加单击事件监听器
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(alarmpicker.this,
                        AlarmActivity.class); // 创建一个Intent对象
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        alarmpicker.this, 0, intent, 0); // 获取显示闹钟的PendingIntent对象
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // 获取AlarmManager对象
                c.set(Calendar.HOUR_OF_DAY, timepicker.getCurrentHour()); // 设置闹钟的小时数
                c.set(Calendar.MINUTE, timepicker.getCurrentMinute()); // 设置闹钟的分钟数
                alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                        pendingIntent); // 设置一个闹钟
                Toast.makeText(alarmpicker.this, "闹钟设置成功", Toast.LENGTH_SHORT)
                        .show(); // 显示一个消息提示
            }
        });
    }
}