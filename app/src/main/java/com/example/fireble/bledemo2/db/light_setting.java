package com.example.fireble.bledemo2.db;

public class light_setting {
    boolean en;
    private String name;
    private int brightness;
    private int temperature;
    private String color = "white";
    private int color_R=0;
    private int color_G=0;
    private int color_B=0;

    public light_setting() {

    }

    public light_setting(boolean b, String name, int brightness, int temperature,
                         String color) {
        this.en = b;
        this.name = name;
        this.brightness = brightness;
        this.temperature = temperature;
        this.color = color;

    }



    public light_setting(boolean b, String name, int brightness, int temperature,
                         String color, int color_R, int color_G, int color_B) {
        this.en = b;
        this.name = name;
        this.brightness = brightness;
        this.temperature = temperature;
        this.color = color;
        this.color_R = color_R;
        this.color_G = color_G;
        this.color_B = color_B;

    }

    public void set_en(boolean en) {
        this.en = en;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public void set_brightness(int brightness) {
        this.brightness = brightness;
    }

    public void set_color(String color) {
        this.color = color;
    }

    public void set_temperature(int temperature) {
        this.temperature = temperature;
    }

    public String get_name() {
        return this.name;
    }

    public int get_brightness() {
        return this.brightness;
    }

    public String get_color() {
        return this.color;
    }

    public int get_temperature() {
        return this.temperature;
    }

    public boolean get_en() {
        return this.en;
    }

    public void setColor_R(int color_R) {
        this.color_R = color_R;
    }

    public int getColor_R() {
        return color_R;
    }

    public void setColor_G(int color_G) {
        this.color_G = color_G;
    }

    public int getColor_G() {
        return color_G;
    }

    public void setColor_B(int color_B) {
        this.color_B = color_B;
    }

    public int getColor_B() {
        return color_B;
    }

}
