package com.example.fireble.bledemo2.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.fireble.bledemo2.R;
import com.example.fireble.bledemo2.app.Application;
import com.example.fireble.bledemo2.ble.BleManager;
import com.example.fireble.bledemo2.db.DBHelper;
import com.example.fireble.bledemo2.db.light_setting;
import com.example.fireble.bledemo2.myweather.ApiClient;
import com.example.fireble.bledemo2.myweather.WeatherInfo;
import com.example.fireble.bledemo2.myweather.XmlPullParseUtil;
import com.example.fireble.bledemo2.service.CurrentTimeService;
import com.example.fireble.bledemo2.utils.PushSlideSwitchView;
import com.example.fireble.bledemo2.utils.PushSlideSwitchView.OnSwitchChangedListener;
import com.example.fireble.bledemo2.utils.RotateImageView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity implements View.OnClickListener {


    private Button btn_scan;
    private Button btn_connect;
    private Button no_connect;
    private float env_light_value;
    private View sellayoutView;
    private View mSplashView;
    private View color_c;
    private light_setting now_light_setting;
    private TextView s_color;
    private TextView s_brightness;
    private TextView s_state;
    private TextView s_modo;
    private TextView title_name;
    private TextView tempnow;
    private TextView wind;
    private ImageView more;
    private ImageView up100;
    private ImageView down0;
    private ImageView open_color;
    private SeekBar light_sb;
    private PushSlideSwitchView all_switch;
    private RotateImageView updata;
    private int[] imageId = new int[]{R.drawable.auto,
            R.drawable.sleep,
            R.drawable.computer, R.drawable.movie, R.drawable.music, R.drawable.child, R.drawable.strong, R.drawable.sun,
            R.drawable.moon, R.drawable.book, R.drawable.handle, R.drawable.livingroom}; // 定义并初始化保存图片id的数组
    private ImageSwitcher imageSwitcher; // 声明一个图像切换器对象
    private ArrayList<light_setting> setting_list = new ArrayList<light_setting>();
    private Button btn_red, btn_white, btn_green, btn_blue, btn_yellow;

    private DBHelper helper;
    private InputStream is;
    private Scanner scanner;
    private boolean is_colorful = false;
    private boolean is_rainbow = false;
    private boolean is_child = false;
    private boolean is_choose = true;
    private String mydevicer;
    private boolean is_open = false;
    private boolean is_auto = false;
    private boolean is_random = false;
    private ListView listview_devices;
    //感应器管理器
    private SensorManager sensorManager;


    private int cur_Index = 0;

    private ArrayList<String> preDatas = new ArrayList<String>();// 存放分段发送的消息
    public static final String SEND_COMPLETED = "SEND_COMPLETED";
    private Iterator<String> dataIterator;// 消息的迭代器


    private ArrayList<BluetoothGattService> services = new ArrayList<BluetoothGattService>();


    protected static String uuidQppService = "0000ff92-0000-1000-8000-00805f9b34fb";// 读写服务
    protected static String uuidQppCharWrite = "00009600-0000-1000-8000-00805f9b34fb"; // 写特征
    protected static String uuidQppCharRead = "00009601-0000-1000-8000-00805f9b34fb";// 读特征

    protected static String uuidATService = "0000cc03-0000-1000-8000-00805f9b34fb";// AT命令
    protected static String uuidATCharWrite = "0000ec00-0000-1000-8000-00805f9b34fb";// 写特征
    protected static String uuidATCharRead = "0000eb00-0000-1000-8000-00805f9b34fb"; // 读特征


    private Context ctx = MainActivity.this;
    private SharedPreferences sp;

    private SeekBar w;
    private SeekBar y;
    private SeekBar r;
    private SeekBar b;
    private SeekBar g;

    private ImageView w0, w100;
    private ImageView y0, y100;
    private ImageView r0, r100;
    private ImageView b0, b100;
    private ImageView g0, g100;


    private String cityName = "中山";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread R_light = new Thread(new Runnable() {
            @Override
            public void run() {
                BleManager.getInstance().write("######" + "80" + "50" + new Random().nextInt(100) + new Random().nextInt(100) + new Random().nextInt(100));
            }
        });
        init_data();
        init_view();
        sp = ctx.getSharedPreferences("set", MODE_PRIVATE);
        Application.getInstance().setIs_mess(sp.getBoolean("mess_Value", true));
        Application.getInstance().setIs_sound(sp.getBoolean("sound_Value", true));
        Application.getInstance().setIs_chose(sp.getBoolean("chose_Value", true));
        Application.getInstance().setIs_conn(sp.getBoolean("conn_Value", true));
        Application.getInstance().setIs_key(sp.getBoolean("key_Value", true));
        Application.getInstance().setIs_noti(sp.getBoolean("noti_Value", true));
        Application.getInstance().setIs_QR(sp.getBoolean("load_way_Value", true));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);  //获得感应器服务
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);        //获得光线感应器
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);        //注册监听器


        new GetWeatherTask(cityName).execute();
        BleManager.getInstance().init(MainActivity.this);    //打开蓝牙
        listview_devices.setAdapter(BleManager.getInstance().getmLeDeviceListAdapter());
        listview_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cur_Index = i;
                BleManager.getInstance().prepareBLEDevice(uuidQppService, BleManager.getInstance());
                BleManager.getInstance().setDevice(cur_Index);
                BleManager.getInstance().connect();
                if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTING) {
                    BleManager.getInstance().onBLEDeviceConnectionChange(BleManager.getInstance().getmBLEDevice(), BleManager.STATE_CONNECTING, BleManager.STATE_CONNECTED);

                    color_button_disvistible();
                    mydevicer = BleManager.getInstance().getDeviceName();
                    title_name.setText(mydevicer);
                    list_go();
                    color_hide();
                    updata.startAnim();
                } else if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTED) {


                }
            }
        });


        light_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub


                now_light_setting.set_brightness(light_sb.getProgress());
                s_brightness.setText("亮度为：" + now_light_setting.get_brightness());
                s_modo.setText("模式为：" + now_light_setting.get_name());
                s_color.setText("颜色为：" + now_light_setting.get_color());
                s_state.setText("状态为：" + BleManager.getInstance().getConnectionState());
                send_Data1();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);

                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        w.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                now_light_setting.set_brightness(progress);
                send_color();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);

                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        y.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                now_light_setting.set_temperature(progress);
                send_color();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);

                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                now_light_setting.setColor_R(progress);
                send_color();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);

                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                now_light_setting.setColor_B(progress);
                send_color();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);

                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                now_light_setting.setColor_G(progress);
                send_color();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
                if (is_child == true) {
                    is_child = false;
                    Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                    light_sb.setMax(99);
                }
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
        all_switch.setOnChangeListener(new OnSwitchChangedListener() {

            @Override
            public void onSwitchChange(PushSlideSwitchView switchView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked == true && is_rainbow != true) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;

                } else if (isChecked == false && is_rainbow != true) {
                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
                    is_open = false;
                    BleManager.getInstance().write("######0000000000");
                }
            }
        });

        Gallery gallery = (Gallery) findViewById(R.id.gallery1); // 获取Gallery组件
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1); // 获取图像切换器
        // 设置动画效果
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in)); // 设置淡入动画
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out)); // 设置淡出动画
        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_rainbow) color_hide();
            }
        });
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this); // 实例化一个ImageView类的对象
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // 设置保持纵横比居中缩放图像
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT));
                return imageView; // 返回imageView对象
            }

        });

        /********************** 使用BaseAdapter指定要显示的内容 *****************************/
        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageview; // 声明ImageView的对象
                if (convertView == null) {
                    imageview = new ImageView(MainActivity.this); // 实例化ImageView的对象
                    imageview.setScaleType(ImageView.ScaleType.FIT_XY); // 设置缩放方式
                    imageview
                            .setLayoutParams(new Gallery.LayoutParams(220, 220));
                    TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
                    // imageview.setBackgroundResource(typedArray.getResourceId(
                    // R.styleable.Gallery_android_galleryItemBackground,
                    // 0));
                    imageview.setPadding(5, 0, 5, 0); // 设置ImageView的内边距
                } else {
                    imageview = (ImageView) convertView;
                }
                imageview.setImageResource(imageId[position]); // 为ImageView设置要显示的图片
                return imageview; // 返回ImageView
            }

            /*
             * 功能：获得当前选项的ID (non-Javadoc)
             *
             * @see android.widget.Adapter#getItemId(int)
             */
            @Override
            public long getItemId(int position) {
                return position;
            }

            /*
             * 功能：获得当前选项 (non-Javadoc)
             *
             * @see android.widget.Adapter#getItem(int)
             */
            @Override
            public Object getItem(int position) {
                return position;
            }

            /*
             * 获得数量 (non-Javadoc)
             *
             * @see android.widget.Adapter#getCount()
             */
            @Override
            public int getCount() {
                return imageId.length;
            }
        };
        gallery.setAdapter(adapter); // 将适配器与Gallery关联
        /*********************************************************************************/
        gallery.setSelection(imageId.length / 2); // 让中间的图片选中
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (is_open == false) {
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                    send_Data1();
                    is_open = true;
                    all_switch.setChecked(true);
                }
                if (is_rainbow != true) {
                    imageSwitcher.setImageResource(imageId[position]);// 显示选中的图片
                    now_light_setting = setting_list.get(position);
                    s_brightness.setText("亮度为：" + now_light_setting.get_brightness());
                    s_modo.setText("模式为：" + now_light_setting.get_name());
                    s_color.setText("颜色为：" + now_light_setting.get_color());
                    s_state.setText("状态为：" + BleManager.getInstance().getConnectionState());
                    light_sb.setProgress(now_light_setting.get_brightness());
                    if (position == 0) {
                        is_auto = true;
                        CurrentTimeService t = new CurrentTimeService();
                        int c = t.getClock();
                        if (Application.getInstance().get_is_mess()) {
                            if (c <= 18 && c >= 6) {
                                Toast.makeText(MainActivity.this, "现在是白天,亮度为" + env_light_value, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "现在是晚上，亮度为" + env_light_value, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                    if (is_child == true) {
                        is_child = false;
                        if (Application.getInstance().get_is_mess())
                            Toast.makeText(MainActivity.this, "已退出儿童模式", Toast.LENGTH_SHORT).show();
                        light_sb.setMax(99);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });


        startService(new Intent(MainActivity.this, CurrentTimeService.class));
        startScanAndBind();
        color_button_disvistible();
        CountDownTimer tt = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {  //倒计时结束。

                is_open = false;
                all_switch.setChecked(false);
                BleManager.getInstance().write("######0000000000");
                splash_go();

                if (Application.getInstance().getIs_QR()) {
                    mysanner();
                }

            }
        };
        tt.start();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private OnSwitchChangedListener OnSwitchChangedListener() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_VOLUME_UP && Application.getInstance().getIs_key()) {
            if (light_sb.getProgress() >= light_sb.getMax()) {

            } else {
                light_sb.setProgress(light_sb.getProgress() + 1);
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
            }
            return true;
        } else if (keyCode == event.KEYCODE_VOLUME_DOWN && Application.getInstance().getIs_key()) {
            if (light_sb.getProgress() <= 0) {

            } else {
                light_sb.setProgress(light_sb.getProgress() - 1);
                imageSwitcher.setImageResource(imageId[10]);// 显示选中的图片
                now_light_setting = setting_list.get(10);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        if (is_rainbow) {
            color_hide();
        } else if (is_choose) {
            list_go();
            color_hide();
        } else {
            if (System.currentTimeMillis() - firstTime < 3000) {
                finish();
            } else {
                firstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次返回", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void init_view() {
        mSplashView = (View) findViewById(R.id.splash_view);
        sellayoutView = (View) findViewById(R.id.sellayout_id);
        color_c = (View) findViewById(R.id.color_c);
        s_color = (TextView) findViewById(R.id.show_color);
        s_color.setText("颜色为：" + now_light_setting.get_color());
        s_brightness = (TextView) findViewById(R.id.show_brightness);
        s_brightness.setText("" + now_light_setting.get_brightness() + "K");
        s_modo = (TextView) findViewById(R.id.show_modo);
        s_state = (TextView) findViewById(R.id.state);
        title_name = (TextView) findViewById(R.id.title_light_name);
        more = (ImageView) findViewById(R.id.more);
        light_sb = (SeekBar) findViewById(R.id.seekBar_brightness);
        all_switch = (PushSlideSwitchView) findViewById(R.id.all_pushSlideSwitchView);
        updata = (RotateImageView) findViewById(R.id.title_update_progress);
        up100 = (ImageView) findViewById(R.id.up_to100);
        down0 = (ImageView) findViewById(R.id.down_to0);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        no_connect = (Button) findViewById(R.id.no_connect);
        listview_devices = (ListView) findViewById(R.id.listViewitems);
        open_color = (ImageView) findViewById(R.id.chose_color);
        btn_red = (Button) findViewById(R.id.btn_red);
        btn_white = (Button) findViewById(R.id.btn_white);
        btn_green = (Button) findViewById(R.id.btn_green);
        btn_blue = (Button) findViewById(R.id.btn_blue);
        btn_yellow = (Button) findViewById(R.id.btn_yellow);
        wind = (TextView) findViewById(R.id.wind);
        tempnow = (TextView) findViewById(R.id.tempnow);

        up100.setOnClickListener(this);
        down0.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
        btn_connect.setOnClickListener(this);
        no_connect.setOnClickListener(this);
        more.setOnClickListener(this);
        open_color.setOnClickListener(this);
        btn_red.setOnClickListener(this);
        btn_white.setOnClickListener(this);
        btn_green.setOnClickListener(this);
        btn_blue.setOnClickListener(this);
        btn_yellow.setOnClickListener(this);


        w = (SeekBar) findViewById(R.id.wsb);
        y = (SeekBar) findViewById(R.id.ysb);
        r = (SeekBar) findViewById(R.id.rsb);
        b = (SeekBar) findViewById(R.id.bsb);
        g = (SeekBar) findViewById(R.id.gsb);


        w0 = (ImageView) findViewById(R.id.w0);
        w100 = (ImageView) findViewById(R.id.w100);
        y0 = (ImageView) findViewById(R.id.y0);
        y100 = (ImageView) findViewById(R.id.y100);
        r0 = (ImageView) findViewById(R.id.r0);
        r100 = (ImageView) findViewById(R.id.r100);
        b0 = (ImageView) findViewById(R.id.b0);
        b100 = (ImageView) findViewById(R.id.b100);
        g0 = (ImageView) findViewById(R.id.g0);
        g100 = (ImageView) findViewById(R.id.g100);
        w0.setOnClickListener(this);
        w100.setOnClickListener(this);
        y0.setOnClickListener(this);
        y100.setOnClickListener(this);
        r0.setOnClickListener(this);
        r100.setOnClickListener(this);
        b0.setOnClickListener(this);
        b100.setOnClickListener(this);
        g0.setOnClickListener(this);
        g100.setOnClickListener(this);
        updata.setOnClickListener(this);
        title_name.setOnClickListener(this);


    }

    void init_data() {
        now_light_setting = new light_setting(false, "now", 50, 5, "white");
        setting_list.add(new light_setting(false, "自动", 50, 50, "white"));
        setting_list.add(new light_setting(false, "睡眠", 10, 50, "white"));
        setting_list.add(new light_setting(false, "儿童", 40, 50, "white"));
        setting_list.add(new light_setting(false, "电脑", 60, 20, "white"));
        setting_list.add(new light_setting(false, "电影", 20, 20, "white"));
        setting_list.add(new light_setting(false, "音乐", 50, 20, "white"));
        setting_list.add(new light_setting(false, "增强", 99, 99, "white"));
        setting_list.add(new light_setting(false, "白天", 50, 0, "white"));
        setting_list.add(new light_setting(false, "深夜", 80, 60, "white"));
        setting_list.add(new light_setting(false, "看书", 60, 40, "white"));
        setting_list.add(new light_setting(false, "手动", 50, 30, "white"));
        setting_list.add(new light_setting(false, "客厅", 90, 60, "white"));
        mydevicer = new String();


    }

    @Override
    public void onClick(View view) {
        if (is_rainbow) {
            switch (view.getId()) {
                case R.id.w0:
                    w.setProgress(0);
                    now_light_setting.set_brightness(0);
                    send_color();
                    break;
                case R.id.w100:
                    w.setProgress(w.getMax());
                    now_light_setting.set_brightness(w.getMax());
                    send_color();
                    break;
                case R.id.y0:
                    y.setProgress(0);
                    now_light_setting.set_temperature(0);
                    send_color();
                    break;
                case R.id.y100:
                    y.setProgress(y.getMax());
                    now_light_setting.set_temperature(y.getMax());
                    send_color();
                    break;
                case R.id.r0:
                    r.setProgress(0);
                    now_light_setting.setColor_R(0);
                    send_color();
                    break;
                case R.id.r100:
                    r.setProgress(r.getMax());
                    now_light_setting.setColor_R(r.getMax());
                    send_color();
                    break;
                case R.id.b0:
                    b.setProgress(0);
                    now_light_setting.setColor_B(0);
                    send_color();
                    break;
                case R.id.b100:
                    b.setProgress(b.getMax());
                    now_light_setting.setColor_B(b.getMax());
                    send_color();
                    break;
                case R.id.g0:
                    g.setProgress(0);
                    now_light_setting.setColor_G(0);
                    send_color();
                    break;
                case R.id.g100:
                    g.setProgress(g.getMax());
                    now_light_setting.setColor_G(g.getMax());
                    send_color();
                    break;
                default:
                    //  color_hide();
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.btn_scan:
                    Toast.makeText(this, BleManager.getInstance().getDeviceCount() + "", Toast.LENGTH_SHORT).show();
                    stopScanForBind();
                    BleManager.getInstance().getmLeDeviceListAdapter().clear();
                    startScanAndBind();
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "正在搜索设备", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_connect:
                    BleManager.getInstance().prepareBLEDevice(uuidQppService, BleManager.getInstance());
                    BleManager.getInstance().setDevice(cur_Index);
                    BleManager.getInstance().connect();
                    if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTING) {
                        BleManager.getInstance().onBLEDeviceConnectionChange(BleManager.getInstance().getmBLEDevice(), BleManager.STATE_CONNECTING, BleManager.STATE_CONNECTED);
                        color_button_disvistible();
                        mydevicer = BleManager.getInstance().getDeviceName();
                        title_name.setText(mydevicer);
                        list_go();
                        color_hide();
                        updata.startAnim();
                    } else if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTED) {


                    }

                    break;
                case R.id.no_connect:
                    list_go();
                    if (Application.getInstance().get_is_mess())
                        Snackbar.make(view, "已跳过搜索", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    color_hide();

                case R.id.up_to100:
                    light_sb.setProgress(99);
                    now_light_setting.set_brightness(99);
                    send_Data1();
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "已设置亮度为最高", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.down_to0:
                    light_sb.setProgress(0);
                    send_Data1();
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "已设置亮度为最低", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.more:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.drawable.ic_launcher);
                    builder.setTitle("选择");
                    //    指定下拉列表的显示数据
                    final String[] cities = {
                            "设置", "儿童模式", "早安唤醒","小夜灯", "彩虹灯", "重新连接", "定时", "随机", "关于我们"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent set = new Intent();
                                    set.setClass(MainActivity.this, setting.class);
                                    startActivity(set);
                                    break;

                                case 1:
                                    if (!is_open) {
                                        Toast.makeText(MainActivity.this, "请打开开关！！", Toast.LENGTH_SHORT).show();
                                    } else if (is_child && is_open) {
                                        is_child = false;
                                        if (Application.getInstance().get_is_mess())
                                            Toast.makeText(MainActivity.this, "已关闭儿童模式", Toast.LENGTH_SHORT).show();
                                        light_sb.setMax(99);

                                    } else if (!is_child && is_open) {
                                        is_child = true;
                                        if (Application.getInstance().get_is_mess())
                                            Toast.makeText(MainActivity.this, "已开启儿童模式", Toast.LENGTH_SHORT).show();
                                        light_sb.setMax(80);
                                        open_color.setVisibility(View.INVISIBLE);
                                        color_button_disvistible();
                                        now_light_setting.set_color("white");
                                        now_light_setting.set_brightness(60);
                                        now_light_setting.set_temperature(30);
                                        s_modo.setText("儿童模式");
                                        s_brightness.setText("60k");
                                        s_color.setText("颜色为：" + now_light_setting.get_color());
                                        imageSwitcher.setImageResource(imageId[5]);
                                        BleManager.getInstance().write("######4200000000");
                                    }

                                    break;
                                case 2:
                                    Intent alarm = new Intent();
                                    alarm.setClass(MainActivity.this, alarmpicker.class);
                                    startActivity(alarm);
                                    break;
                                case 3:
                                    if (is_open) {
                                        now_light_setting.set_brightness(30);
                                        now_light_setting.set_color("white");
                                        now_light_setting.set_temperature(5);

                                        light_sb.setProgress(1000);
                                        s_modo.setText("小夜灯");
                                        s_brightness.setText("20k");
                                        if (Application.getInstance().get_is_mess())
                                            Toast.makeText(MainActivity.this, "已开启小夜灯功能", Toast.LENGTH_SHORT).show();
                                        CountDownTimer tt = new CountDownTimer(60 * 60000, 1000) {
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            public void onFinish() {  //倒计时结束。

                                                is_open = false;
                                                all_switch.setChecked(false);
                                                BleManager.getInstance().write("######0000000000");
                                            }
                                        };
                                        tt.start();
                                    }
                                    break;
                                case 4:
                                    color_show();
                                    break;

                                case 5:
                                    BleManager.getInstance().disconnect();
                                    if (Application.getInstance().getIs_QR())
                                        mysanner();
                                    else
                                        list_come();
                                    mydevicer = null;
                                    updata.stopAnim();
                                    title_name.setText("NOT ...");
                                    break;
                                case 6:
                                    buildTimerEditDialog();

                                    break;
                                case 7:
                                    if (is_random) {
                                        is_random = false;
                                    } else {
                                        is_random = true;

                                    }

                                    break;
                                case 8:
                                    Dialog alertDialog = new AlertDialog.Builder(MainActivity.this).
                                            setTitle("关于我们：").
                                            setIcon(android.R.drawable.ic_menu_upload_you_tube).
                                            setMessage("Firefly是天启科技于2014年3月成立的开源团队。致力于开源硬件的设计、生产和销售，以及开源文化和知识的推广。同时也提供软硬件定制、产品生产和技术支持等服务。\n" +
                                                    "\n" +
                                                    "Firefly开源团队由超过40人的专业成员组成，我们擅长Android、Linux的系统级开发、多平台应用开发和云服务支持，以及硬件的电路设计和工业设计。\n" +
                                                    "\n" +
                                                    "\"Light up your ideas\" 是Firefly的理念，我们希望Firefly对技术的热情和执着能帮助实现你的创意和梦想。\n").
                                            setNegativeButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    // TODO Auto-generated method stub
                                                }
                                            }).
                                            create();
                                    alertDialog.show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.show();

                    break;
                case R.id.chose_color:
                    if (is_colorful) {
                        is_colorful = false;
                        color_button_disvistible();
                    } else {
                        is_colorful = true;
                        color_button_vistible();
                    }
                    break;
                case R.id.btn_red:
                    if (is_open) {
                        color_button_disvistible();
                        now_light_setting.set_color("red");
                        //       open_color.setBackgroundColor(Color.RED);
                        s_color.setText("颜色为：" + now_light_setting.get_color());
                        BleManager.getInstance().write("######0000" + now_light_setting.get_brightness() + "0000");
                    }
                    break;
                case R.id.btn_blue:
                    if (is_open) {
                        color_button_disvistible();
                        now_light_setting.set_color("blue");
                        //    open_color.setBackgroundColor(Color.BLUE);
                        s_color.setText("颜色为：" + now_light_setting.get_color());
                        BleManager.getInstance().write("######00000000" + now_light_setting.get_brightness());
                    }
                    break;
                case R.id.btn_green:
                    if (is_open) {
                        color_button_disvistible();
                        now_light_setting.set_color("green");
                        //   open_color.setBackgroundColor(Color.GREEN);
                        s_color.setText("颜色为：" + now_light_setting.get_color());
                        BleManager.getInstance().write("######000000" + now_light_setting.get_brightness() + "00");
                    }
                    break;
                case R.id.btn_white:
                    if (is_open) {
                        color_button_disvistible();
                        now_light_setting.set_color("white");
                        //   open_color.setBackgroundColor(Color.WHITE);
                        s_color.setText("颜色为：" + now_light_setting.get_color());
                        BleManager.getInstance().write("######" + now_light_setting.get_brightness() + "00000000");
                    }
                    break;
                case R.id.btn_yellow:
                    if (is_open) {
                        color_button_disvistible();
                        now_light_setting.set_color("yellow");
                        // open_color.setBackgroundColor(Color.YELLOW);
                        s_color.setText("颜色为：" + now_light_setting.get_color());
                        BleManager.getInstance().write("######00" + now_light_setting.get_brightness() + "000000");
                    }
                    break;
                case R.id.title_update_progress:
                    title_name.setText("NOT ...");
                    updata.stopAnim();
                    mydevicer = null;
                    BleManager.getInstance().clearBLEDevice();
                    BleManager.getInstance().disconnect();
                    BleManager.getInstance().getmLeDeviceListAdapter().clear();
                    startScanAndBind();
                    if (Application.getInstance().getIs_QR())
                        mysanner();
                    else
                        list_come();
                    break;
                case R.id.title_light_name:
                    Dialog alertDialog = new AlertDialog.Builder(MainActivity.this).
                            setTitle("设备信息：").
                            setIcon(android.R.drawable.ic_menu_upload_you_tube).
                            setMessage("设备名字：\n" + BleManager.getInstance().getDeviceName() +
                                    "\n" +
                                    "设备地址：\n" + BleManager.getInstance().getDeviceAddress() +
                                    "\n" +
                                    "连接状态：\n" + BleManager.getInstance().getConnectionState() +
                                    "\n" +
                                    "信号：\n" + BleManager.getInstance().getRssi()).
                            setNegativeButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                }
                            }).
                            create();
                    alertDialog.show();
                default:
                    break;

            }
        }

    }

    private boolean mScanAndBindStarted = false;
    public static final long SCAN_PERIOD = 4 * 1000;

    /**
     * 开始扫描设备
     */

    public void startScanAndBind() {
        if (BleManager.getInstance() == null || mScanAndBindStarted == true) {
            return;
        }
        mScanAndBindStarted = true;
        BleManager.getInstance().clearData();
        BleManager.getInstance().scanLeDevice(true);
        registerReciver();

    }


    /**
     * 停止扫描设备
     */
    public void stopScanForBind() {

        if (BleManager.getInstance() == null)
            return;
        BleManager.getInstance().scanLeDevice(false);

        mScanAndBindStarted = false;

    }

    /***********************************************************************************
     * add code
     ***************************************************************************************************/


    // 广播
    public static final String BLE_ACTION_CONNECTION_CHANGE = "com.tchip.tchipblehelper.action_CONNECTION_CHANGE";
    public static final String BLE_ACTION_CHARACTERISTIC_CHANGE = "com.tchip.tchipblehelper.action_CHARACTERISTIC_CHANGE";
    public static final String BLE_ACTION_CHARACTERISTIC_READ = "com.tchip.tchipblehelper.action_CHARACTERISTIC_READ";
    public static final String BLE_ACTION_CHARACTERISTIC_WRITE_STATE = "com.tchip.tchipblehelper.action_CHARACTERISTIC_WRITE_STATE";
    public static final String BLE_ACTION_SERVICES_DISCOVERED = "com.tchip.tchipblehelper.action_SERVICES_DISCOVERED";
    public static final String BLE_FINISHU = "com.tchip.finishu";

    private int connTime = 0;
    private boolean is_tryconn = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            UUID uuid;
            String data;
            switch (intent.getAction()) {
                case BLE_ACTION_CONNECTION_CHANGE:

                    switch (BleManager.getInstance().getConnectionState()) {
                        case 0:
                            s_state.setText("状态为：0");
                            updata.stopAnim();
                            title_name.setText("NOT ...");
                            Log.i("BleDemo2", "蓝牙状态0");
                            if (Application.getInstance().getIs_conn() && mydevicer != null && connTime <= 4) {
                                new BleTasks().execute();
                                connTime++;
                                is_tryconn = true;
                                break;
                            } else if (is_tryconn == false) {
                                CountDownTimer tt = new CountDownTimer(60000, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {  //倒计时结束。
                                        connTime = 0;
                                    }
                                };
                                tt.start();
                            }

                            if (Application.getInstance().getIs_chose() && !Application.getInstance().getIs_conn()) {
                                mydevicer = null;
                                list_come();
                            }
                            break;
                        case 1:
                            s_state.setText("状态为：1");
                            BleManager.getInstance().onBLEDeviceConnectionChange(BleManager.getInstance().getmBLEDevice(), BleManager.STATE_CONNECTING, BleManager.STATE_CONNECTED);

                            Log.i("BleDemo2", "蓝牙状态1");
                            stopScanForBind();
                            break;
                        case 2:
                            s_state.setText("状态为：2");
                            Log.i("BleDemo2", "蓝牙状态2");
                            break;
                    }
                    break;
                case BLE_ACTION_CHARACTERISTIC_CHANGE:
                    uuid = UUID.fromString(intent.getStringExtra("uuid"));
                    data = intent.getStringExtra("data");
                    onCharacteristicChanged(uuid, data);
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "BLE_ACTION_CHARACTERISTIC_CHANGE", Toast.LENGTH_SHORT).show();
                    break;
                case BLE_ACTION_CHARACTERISTIC_READ:
                    uuid = UUID.fromString(intent.getStringExtra("uuid"));
                    data = intent.getStringExtra("data");
                    onCharacteristicRead(uuid, data);
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "BLE_ACTION_CHARACTERISTIC_READ", Toast.LENGTH_SHORT).show();
                    break;
                case BLE_ACTION_CHARACTERISTIC_WRITE_STATE:
                    uuid = UUID.fromString(intent.getStringExtra("uuid"));
                    int state = intent.getIntExtra("state", 0);
                    onCharacteristicWriteState(uuid, state);
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "BLE_ACTION_CHARACTERISTIC_WRITE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case BLE_ACTION_SERVICES_DISCOVERED:
                    onServicesDiscovered();
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "BLE_ACTION_SERVICES_DISCOVERED", Toast.LENGTH_SHORT).show();
                    break;
                case BLE_FINISHU:
                    unRegisterReciver();
                    finish();
                    if (Application.getInstance().get_is_mess())
                        Toast.makeText(MainActivity.this, "BLE_FINISHU", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 注册广播监听
     */
    public void registerReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BLE_ACTION_CHARACTERISTIC_CHANGE);
        filter.addAction(BLE_ACTION_CHARACTERISTIC_READ);
        filter.addAction(BLE_ACTION_CHARACTERISTIC_WRITE_STATE);
        filter.addAction(BLE_ACTION_CONNECTION_CHANGE);
        filter.addAction(BLE_ACTION_SERVICES_DISCOVERED);
        filter.addAction(BLE_FINISHU);
        this.registerReceiver(broadcastReceiver, filter);

    }


    /**
     * 取消广播监听
     */
    public void unRegisterReciver() {
        this.unregisterReceiver(broadcastReceiver);
    }


    public void onCharacteristicRead(UUID uuid, String data) {
        // TODO Auto-generated method stub
        System.out.println("Dlist onCharacteristicRead data=" + data);

    }

    public void onCharacteristicChanged(UUID uuid, String data) {
        // TODO Auto-generated method stub
        System.out.println("Dlist onCharacteristicChanged data=" + data);
    }

    public void onServicesDiscovered() {
        // TODO Auto-generated method stub
        System.out.println("Dlist onServicesDiscovered");

    }

    public void onCharacteristicWriteState(UUID uuid, int state) {
        // TODO Auto-generated method stub
        System.out.println("Dlist onCharacteristicWriteState ");

    }


    /*************************************************************************************************
     * end
     ******************************************************************************************************************/

    //Activity被销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销监听器
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
        BleManager.getInstance().clearBLEDevice();
        BleManager.getInstance().disconnect();
//        if (Application.getInstance().get_is_mess())
//            Toast.makeText(MainActivity.this, "应用被销毁并关闭蓝牙资源", Toast.LENGTH_SHORT).show();
    }

    //感应器事件监听器
    private SensorEventListener listener = new SensorEventListener() {

        //当感应器精度发生变化
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        //当传感器监测到的数值发生变化时
        @Override
        public void onSensorChanged(SensorEvent event) {
            // values数组中第一个值就是当前的光照强度
            env_light_value = event.values[0];
//            if (is_auto == true) {
//                now_light_setting.set_brightness((120 - (int) env_light_value) * 6);
//                now_light_setting.set_color("white");
//                s_brightness.setText(now_light_setting.get_brightness());
//                BleManager.getInstance().write("######"  + now_light_setting.get_brightness() +"00000000");
//                open_color.setBackgroundColor(Color.WHITE);
//            }

        }

    };


    void color_button_disvistible() {
        btn_red.setVisibility(View.INVISIBLE);
        btn_white.setVisibility(View.INVISIBLE);
        btn_green.setVisibility(View.INVISIBLE);
        btn_blue.setVisibility(View.INVISIBLE);
        btn_yellow.setVisibility(View.INVISIBLE);
    }

    void color_button_vistible() {
        btn_red.setVisibility(View.VISIBLE);
        btn_white.setVisibility(View.VISIBLE);
        btn_green.setVisibility(View.VISIBLE);
        btn_blue.setVisibility(View.VISIBLE);
        btn_yellow.setVisibility(View.VISIBLE);
    }


    public void send_Data1() {
        if (is_open) {
            switch (now_light_setting.get_color()) {
                case "red":
                    BleManager.getInstance().write("######0000" + now_light_setting.get_brightness() + "0000");
                    break;
                case "green":
                    BleManager.getInstance().write("######000000" + now_light_setting.get_brightness() + "00");
                    break;
                case "blue":
                    BleManager.getInstance().write("######00000000" + now_light_setting.get_brightness());
                    break;
                case "white":
                    BleManager.getInstance().write("######" + now_light_setting.get_brightness() + "00000000");
                    break;
                case "yellow":
                    BleManager.getInstance().write("######00" + now_light_setting.get_brightness() + "000000");
                    break;
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        BleManager.getInstance().clearBLEDevice();
        BleManager.getInstance().disconnect();
//        if (Application.getInstance().get_is_mess())
//            Toast.makeText(MainActivity.this, "应用停止并关闭蓝牙资源", Toast.LENGTH_SHORT).show();
    }

    void color_show() {
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.color_show);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                color_c.setVisibility(View.VISIBLE);
            }
        });
        color_c.startAnimation(anim);
        now_light_setting.set_brightness(50);
        is_rainbow = true;
        light_sb.setVisibility(View.INVISIBLE);
        send_color();
    }

    void color_hide() {
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.color_hide);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                color_c.setVisibility(View.GONE);


            }
        });
        is_rainbow = false;
        color_c.startAnimation(anim);
        now_light_setting.set_brightness(light_sb.getProgress());
        light_sb.setVisibility(View.VISIBLE);
        send_Data1();
    }

    void splash_go() {
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.push_right_out);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                mSplashView.setVisibility(View.GONE);

            }
        });
        mSplashView.startAnimation(anim);
    }

    void list_come() {
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.push_up_in);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                sellayoutView.setVisibility(View.VISIBLE);


            }
        });
        listview_devices.startAnimation(anim);
        sellayoutView.startAnimation(anim);
        is_choose = true;
        BleManager.getInstance().scanLeDevice(true);
        if (BleManager.getInstance().getDeviceName() != null) {
            title_name.setText(BleManager.getInstance().getDeviceName());
        }
    }

    void list_go() {
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.push_down_out);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                sellayoutView.setVisibility(View.GONE);


            }
        });
        listview_devices.startAnimation(anim);
        sellayoutView.startAnimation(anim);
        is_choose = false;

    }

    private void send_color() {
        BleManager.getInstance().write("######" + now_light_setting.get_brightness() + now_light_setting.get_temperature() + now_light_setting.getColor_R() + now_light_setting.getColor_G() + now_light_setting.getColor_B());
    }


    /**
     * 后台任务类
     */
    public class BleTasks extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                while (true) {
                    for (int i = 0; i < BleManager.getInstance().getmLeDeviceListAdapter().getCount(); i++) {

                        if (BleManager.getInstance().getmLeDeviceListAdapter().getItem(i).getDeviceName().matches(mydevicer) == true) {
                            for (int j = 0; j < 3; j++) {
                                BleManager.getInstance().prepareBLEDevice(uuidQppService, BleManager.getInstance());
                                BleManager.getInstance().setDevice(i);
                                BleManager.getInstance().connect();
                                if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTING) {
                                    BleManager.getInstance().onBLEDeviceConnectionChange(BleManager.getInstance().getmBLEDevice(), BleManager.STATE_CONNECTING, BleManager.STATE_CONNECTED);
                                    break;
                                }
                            }

                            if (BleManager.getInstance().getConnectionState() == BleManager.STATE_CONNECTING) {
                                BleManager.getInstance().onBLEDeviceConnectionChange(BleManager.getInstance().getmBLEDevice(), BleManager.STATE_CONNECTING, BleManager.STATE_CONNECTED);
                                return true;
                            }
                        } else if (mydevicer == null) {

                        }
                    }

                }
            } catch (Exception e) {

                return false;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {

                if (Application.getInstance().get_is_mess())
                    Toast.makeText(MainActivity.this, "连接上名为" + mydevicer + "的设备", Toast.LENGTH_SHORT).show();
                color_button_disvistible();
                mydevicer = BleManager.getInstance().getDeviceName();
                title_name.setText(mydevicer);
                list_go();
                color_hide();
                updata.startAnim();

            }
        }
    }

    private static final int REQUEST_CODE_SCAN = 0x0000;

    public void mysanner() {
        //开启摄像头权限
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
            return;
        }
        Intent intent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String content = bundle.getString("result");
            // Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
            mydevicer = content;
            new BleTasks().execute();
            // qrCodeImage.setImageBitmap(bitmap);
        }
    }

    //当用户选择是否允许
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            //证明申请到权限
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
            }
        }
    }

    public class GetWeatherTask extends AsyncTask<Void, Void, Integer> {
        private static final String BASE_URL = "http://sixweather.3gpk.net/SixWeather.aspx?city=%s";
        private Application mApplication;
        private String mCity;
        private static final int SCUESS = 0;
        private static final int FAIL = -1;


        public GetWeatherTask(String city) {
            mCity = city;
            mApplication = Application.getInstance();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                String url = String.format(BASE_URL,
                        URLEncoder.encode(mCity, "utf-8"));


                // 最后才执行网络请求
                String netResult = ApiClient.connServerForResult(url);
                if (!TextUtils.isEmpty(netResult)) {
                    WeatherInfo allWeather = XmlPullParseUtil
                            .parseWeatherInfo(netResult);
                    if (allWeather != null) {
                        mApplication.SetAllWeather(allWeather);

                    }
                    return SCUESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("weather", e.toString());
            }
            return FAIL;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case SCUESS:
                    Toast.makeText(MainActivity.this, Application.getInstance().GetAllWeather().getShiduNow(), Toast.LENGTH_SHORT).show();
                    wind.setText("风速为：" + Application.getInstance().GetAllWeather().getWinNow());
                    tempnow.setText("温度为：" + Application.getInstance().GetAllWeather().getTempNow());
                    break;
                case FAIL:
                    wind.setText("风速为：请联网");
                    tempnow.setText("温度为：请联网");
                    break;
            }

        }
    }

    private void buildTimerEditDialog() {
        final EditText text = new EditText(MainActivity.this);
        new AlertDialog.Builder(MainActivity.this).setTitle("设置时间").setView(text).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String t = text.getText().toString();
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(t);
                if (m.matches() && text != null) {

                    buildTimerEditDialog();

                    CountDownTimer tt = new CountDownTimer(Integer.parseInt(t.toString()) * 60000, 1000) {
                        public void onTick(long millisUntilFinished) {  //每秒执行一次进该方法

                        }

                        public void onFinish() {  //倒计时结束。
                            Toast.makeText(MainActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
                            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
                            alert.setIcon(R.drawable.ic_launcher);                            //设置对话框的图标
                            alert.setTitle("定时：");                                    //设置对话框的标题
                            alert.setMessage("设置时间到了...");        //设置要显示的内容
                            //添加确定按钮
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alert.show();
                            BleManager.getInstance().write("######0000000000");
                        }
                    };
                    tt.start();
                    if (Application.getInstance().getIs_noti()) {
                        Bitmap btm = BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher);
                        Intent intent = new Intent(MainActivity.this,
                                MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(
                                MainActivity.this, 0, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        Notification noti = new NotificationCompat.Builder(
                                MainActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("定时模式")
                                .setLargeIcon(btm)
                                .setNumber(13)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setContentIntent(pendingIntent)
                                .setStyle(
                                        new NotificationCompat.InboxStyle()
                                                .addLine("正在定时模式")
                                                .setBigContentTitle("1 new message")
                                                .setSummaryText("zhanghuaming@android.com")
                                )
                                .build();
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, noti);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("取消", null).show();
    }

}
