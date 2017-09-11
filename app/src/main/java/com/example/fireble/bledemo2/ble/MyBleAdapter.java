package com.example.fireble.bledemo2.ble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fireble.bledemo2.R;

import java.util.List;

/**
 * Created by 10198 on 2016/8/14.
 */
public class MyBleAdapter extends ArrayAdapter<DeviceItem>{
    private int resourceId;
    public MyBleAdapter(Context context, int textViewResourceId, List<DeviceItem> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent){
        DeviceItem deviceItem = getItem(position);      //获取当前项的DeviceItem实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView textView_name = (TextView) view.findViewById(R.id.device_name);
        TextView textView_addr = (TextView) view.findViewById(R.id.device_address);
        TextView textView_rssi = (TextView) view.findViewById(R.id.device_rssi);
        textView_name.setText(deviceItem.getDeviceName());
        textView_addr.setText(deviceItem.getDeviceAddress());
        textView_rssi.setText(String.valueOf(deviceItem.getRssi())+"db");
        return view;
    }
}

//
