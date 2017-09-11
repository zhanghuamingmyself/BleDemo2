package com.example.fireble.bledemo2.app;

import com.example.fireble.bledemo2.myweather.WeatherInfo;

public class Application extends android.app.Application {

    private static Application mApplication;

    private String[] settingname = new String[]{"auto", "child", "computer",
            "movie", "music", "sleep", "strong", "sun", "moon", "book", "handle"};
    private int[] settingbrightness = new int[]{};
    private int[] settingtemperature = new int[]{};
    private String[] settingcolor = new String[]{};
    public static int mNetWorkState;
    private boolean is_mess = true;
    private boolean is_noti = true;
    private boolean is_sound = true;
    private boolean is_conn = true;
    private boolean is_chose = false;
    private boolean is_QR = true;
    private boolean is_key = true;

    public static synchronized Application getInstance() {
        return mApplication;
    }

    //    private SharedPreferences sp;
//    private Context ctx = this;
    private WeatherInfo allweather;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
//        sp = ctx.getSharedPreferences("set", MODE_PRIVATE);
//        is_chose=sp.getBoolean("chose_Value",true);
//        is_conn=sp.getBoolean("conn_Value",true);
//        is_mess=sp.getBoolean("mess_Value",true);
//        is_noti=sp.getBoolean("notu_Value",true);
//        is_QR=sp.getBoolean("load_way_Value",true);
//        is_key=sp.getBoolean("key_Value",true);
//        is_sound=sp.getBoolean("sound_Value",true);
    }

    @Override
    public void onTerminate() {

        super.onTerminate();

    }

    public boolean get_is_mess() {
        return is_mess;
    }

    public void setIs_mess(boolean is_mess) {
        this.is_mess = is_mess;
    }

    public boolean getIs_sound() {
        return is_sound;
    }

    public void setIs_sound(boolean is_sound) {
        this.is_sound = is_sound;
    }

    public boolean getIs_key() {
        return is_key;
    }

    public void setIs_key(boolean is_key) {
        this.is_key = is_key;
    }

    public boolean getIs_conn() {
        return is_conn;
    }

    public void setIs_conn(boolean is_conn) {
        this.is_conn = is_conn;
    }

    public boolean getIs_chose() {
        return is_chose;
    }

    public void setIs_chose(boolean is_chose) {
        this.is_chose = is_chose;
    }

    public void setIs_noti(boolean is_noti) {
        this.is_noti = is_noti;
    }

    public boolean getIs_noti() {
        return is_noti;
    }

    public boolean getIs_QR() {
        return is_QR;
    }

    public void setIs_QR(boolean is_QR) {
        this.is_QR = is_QR;
    }
    public WeatherInfo GetAllWeather()
    {
        return allweather;
    }
    public void SetAllWeather(WeatherInfo allweather)
    {
        this.allweather=allweather;
    }
}
