package com.example.fireble.bledemo2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.example.fireble.bledemo2.R;
import com.example.fireble.bledemo2.app.Application;

public class AlarmActivity extends Activity {
//	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mp=MediaPlayer.create(this, Uri.parse("file:///sdcard/music/chimes.wav"));
//		mp.setLooping(true);	//设置循环播放音乐
//		mp.start();		//开始播放音乐
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setIcon(R.drawable.clockicon);							//设置对话框的图标
		alert.setTitle("闹钟：");									//设置对话框的标题
		alert.setMessage("设置时间到了...");		//设置要显示的内容
		//添加确定按钮
		alert.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				mp.stop();
				finish();
			}
		});
		alert.show();  									// 显示对话框
		if(Application.getInstance().getIs_noti()) {
			Bitmap btm = BitmapFactory.decodeResource(getResources(),
					R.mipmap.ic_launcher);
			Intent intent = new Intent(AlarmActivity.this,
					MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(
					AlarmActivity.this, 0, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			Notification noti = new NotificationCompat.Builder(
					AlarmActivity.this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle("早安提醒")
					.setLargeIcon(btm)
					.setNumber(13)
					.setDefaults(Notification.DEFAULT_ALL)
					.setContentIntent(pendingIntent)
					.setStyle(
							new NotificationCompat.InboxStyle()
									.addLine(
											"早上好")
									.addLine("祝你一天顺利")
									.addLine("一日之计在于晨")
									.addLine("早起的鸟儿有虫吃")
									.setBigContentTitle("3 new message")
									.setSummaryText("zhanghuaming@android.com"))
					.build();
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(0, noti);
		}
	}
}
