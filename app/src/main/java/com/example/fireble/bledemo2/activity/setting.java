package com.example.fireble.bledemo2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fireble.bledemo2.R;
import com.example.fireble.bledemo2.app.Application;
import com.example.fireble.bledemo2.utils.PushSlideSwitchView;
import com.example.fireble.bledemo2.utils.PushSlideSwitchView.OnSwitchChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/11.
 */
public class setting extends Activity {
    private ListView listview;
    List<Map<String, Object>> tempListData = new ArrayList<Map<String, Object>>();
    private String[] items_string;
    private String[] items;
    private boolean isOrNot[];
    private boolean is_mess;
    private boolean is_sound;
    private boolean is_conn;
    private boolean is_chose;
    private boolean is_key;
    private Context ctx = setting.this;
    private SharedPreferences sp;
    private int CODE=0X717;

    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlayout);
        initView();
        sp = ctx.getSharedPreferences("set", MODE_PRIVATE);

            items_string=new String[]{"提示","通知栏","音量键控制","断开自动连接","连接失败重新选择","声音","二维码登陆"};
            items=new String[]{"mess_Value","noti_Value","key_Value","conn_Value","chose_Value","sound_Value","load_way_Value"};
        for (int i = 0; i < 7; i++) {
            HashMap datamap = new HashMap<String, Object>();

            datamap.put("index", items_string[i]);

            datamap.put("checked", sp.getBoolean(items[i], true));

            tempListData.add(datamap);
        }

        MyAdapter adapter = new MyAdapter(setting.this);
        listview.setAdapter(adapter);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listView_set);
    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return tempListData.size();
        }

        @Override
        public Object getItem(int position) {
            return tempListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void modifyStates(int position) {

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.setitem, null);
                holder = new ViewHolder();

                holder.index = (TextView) convertView.findViewById(R.id.item_idex);
                holder.SlideSwitchView = (PushSlideSwitchView) convertView.findViewById(R.id.item_SwitchView);

                // 使用tag来存储数据
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.index.setText(tempListData.get(position).get("index") + "");
            holder.SlideSwitchView.setChecked((Boolean) tempListData.get(position).get("checked"));
            holder.SlideSwitchView.setOnChangeListener(new OnSwitchChangedListener() {
                @Override
                public void onSwitchChange(PushSlideSwitchView switchView, boolean isChecked) {
                    tempListData.get(position).put("checked", isChecked);
                    SharedPreferences.Editor editor = sp.edit();
                    switch (position)
                    {

                        case 0:
                            Application.getInstance().setIs_mess(isChecked);
                            editor.putString("mess_Key", "string");
                            editor.putBoolean("mess_Value", isChecked);
                            editor.commit();
                            break;
                        case 1:
                            Application.getInstance().setIs_noti(isChecked);
                            editor.putString("noti_Key", "string");
                            editor.putBoolean("noti_Value", isChecked);
                            editor.commit();
                            break;
                        case 2:
                            Application.getInstance().setIs_key(isChecked);
                            editor.putString("key_Key", "string");
                            editor.putBoolean("key_Value", isChecked);
                            editor.commit();
                            break;
                        case 3:
                            Application.getInstance().setIs_conn(isChecked);
                            editor.putString("conn_Key", "string");
                            editor.putBoolean("conn_Value", isChecked);
                            editor.commit();
                            break;
                        case 4:
                            Application.getInstance().setIs_chose(isChecked);
                            editor.putString("chose_Key", "string");
                            editor.putBoolean("chose_Value", isChecked);
                            editor.commit();
                            break;
                        case 5:
                            Application.getInstance().setIs_sound(isChecked);
                            editor.putString("sound_Key", "string");
                            editor.putBoolean("sound_Value", isChecked);
                            editor.commit();
                        case 6:
                            Application.getInstance().setIs_QR(isChecked);
                            editor.putString("load_way_Key", "string");
                            editor.putBoolean("load_way_Value", isChecked);
                            editor.commit();
                            break;
                        default:
                            break;
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        public PushSlideSwitchView SlideSwitchView;
        public TextView index;

    }

}
