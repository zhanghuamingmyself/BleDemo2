package com.example.fireble.bledemo2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.fireble.bledemo2.R;
import com.example.fireble.bledemo2.db.DBHelper;

/**
 * Created by Administrator on 2016/9/11.
 */
public class settinglist extends Activity {
    /** Called when the activity is first created. */
    final int CODE=0X517;
    private Button btn;
    private int result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysetting);
        final DBHelper helper = new DBHelper(settinglist.this);
        ListView lv = (ListView) findViewById(R.id.myseettingitem);// 获得列表视图
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(settinglist.this, R.layout.list_item, new String[]{"设置1","设置2","设置3","设置4"});
        lv.setAdapter(fileList);// 设置列表适配器
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            result=position;
            }
        });
        btn=(Button)findViewById(R.id.ok_myset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("data",result);
                intend.putExtras(bundle);
                setResult(CODE,intend);
                finish();
            }
        });
    }
}