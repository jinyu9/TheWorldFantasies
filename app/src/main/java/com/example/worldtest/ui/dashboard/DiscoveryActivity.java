package com.example.worldtest.ui.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.R;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class DiscoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        addActivity(this);
        Bmob.initialize(this,"e1f541a4a1129508aace8369f5432292");

        //插入一条数据

        Toast.makeText(this, "添加数据成功，返回objectId为：", Toast.LENGTH_SHORT).show();
        Moment moment = new Moment();
        moment.setUser_name("我啊");
        moment.setUser_avatar("Avatar");
        moment.setContent("哦哦哦啊啊啊呀呀呀呀呀呀");
        moment.setPicture(null);
        moment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(DiscoveryActivity.this,"insert success!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DiscoveryActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        loadDate();


    }

    private void loadDate() {
        BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
        ListView listView = (ListView) findViewById(R.id.listview);
        bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    //数据倒序显示,最新的数据在最上面
                    Collections.reverse(list);
                    ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int lv_item_position = (Integer) adapterView.getTag();
                        }
                    });
                }
                else{
                    System.out.println(e.getMessage());
                }
            }
        });
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
