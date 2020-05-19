package com.example.worldtest.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class FindDiscover extends AppCompatActivity {
    String discoverName;
    public static String username;
    private String user_name;
    public static int size;
    public static String[][] a;
    public static int[] n;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_discover);
        Bundle bundle = this.getIntent().getExtras();
        discoverName=bundle.getString("discoverName");
        username =this.getIntent().getStringExtra("user_name");
        System.out.println("user_name:"+username);
        System.out.println("discoverName:"+discoverName);

        Bundle bundle1 = this.getIntent().getExtras();
        user_name = bundle1.getString("name");

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout)findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
                bmobQuery.addWhereEqualTo("user_name", discoverName);
                bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
                    @Override
                    public void done(List<Moment> list, BmobException e) {
                        if (e == null) {
                            if(list.size()==0){
                                LinearLayout linearLayout=findViewById(R.id.discoverlayout);
                                LinearLayout linearLayout2=findViewById(R.id.listviewlayout);
                                System.out.println("done: 没有查到这个用户的ID");
                                linearLayout.setGravity(Gravity.CENTER);
                                linearLayout.setPadding(0,100,0,0);
                                ImageView imageView=new ImageView(FindDiscover.this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000,800);
                                imageView.setLayoutParams(params);
                                linearLayout2.setVisibility(View.GONE);
                                imageView.setImageResource(R.drawable.no_infor);
                                linearLayout.addView(imageView);
                            }else {
                                //数据倒序显示,最新的数据在最上面
                                ListView listView = findViewById(R.id.listview);
                                List<Moment> moments = new ArrayList<>(list);
                                //总共有多少条动态k
                                size = moments.size();
                                Log.w("nnn","总共有多少条朋友圈="+size);
                                a = new String[size][];
                                n = new int[size];

                                //每条动态的图片数量
                                for(int i = 0;i<size;i++) {
                                    Log.w("nnn", "每条朋友圈的图片数量=" + moments.get(i).getN());
                                    n[i] = moments.get(i).getN();
                                    a[i] = new String[moments.get(i).getN()];
                                    for (int j = 0; j < moments.get(i).getN(); j++) {
                                        String temp[];
                                        temp = moments.get(i).getPicture().split(";");
                                        System.out.println(temp[j]);
                                        a[i][j] = temp[j];
                                        Log.w("nnn", "图片地址" + a[i][j]);
                                    }
                                }
                                //Collections.reverse(list);

                                ListViewAdapter adapter = new ListViewAdapter(getBaseContext(), moments,1, Main2Activity.username);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);
                            }
                        }
                        else{
                            System.out.println(e.getMessage());
                        }
                    }
                });
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String t = "";
                t = ("最后刷新时间：\n"+simpleDateFormat.format(date));
                Toast toast = Toast.makeText(getApplicationContext(),t,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                new Task().execute();
            }
        });
        BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
        bmobQuery.addWhereEqualTo("user_name", discoverName);
        ListView listView = findViewById(R.id.listview);
        bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    if(list.size()==0){
                        LinearLayout linearLayout=findViewById(R.id.discoverlayout);
                        LinearLayout linearLayout2=findViewById(R.id.listviewlayout);
                        System.out.println("done: 没有查到这个用户的ID");
                        linearLayout.setGravity(Gravity.CENTER);
                        linearLayout.setPadding(0,100,0,0);
                        ImageView imageView=new ImageView(FindDiscover.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000,800);
                        imageView.setLayoutParams(params);
                        linearLayout2.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.no_infor);
                        linearLayout.addView(imageView);
                    }else {
                        //数据倒序显示,最新的数据在最上面
                        List<Moment> moments = new ArrayList<>(list);
                        //总共有多少条动态k
                        size = moments.size();
                        Log.w("nnn","总共有多少条朋友圈="+size);
                        a = new String[size][];
                        n = new int[size];

                        //每条动态的图片数量
                        for(int i = 0;i<size;i++) {
                            Log.w("nnn", "每条朋友圈的图片数量=" + moments.get(i).getN());
                            n[i] = moments.get(i).getN();
                            a[i] = new String[moments.get(i).getN()];
                            for (int j = 0; j < moments.get(i).getN(); j++) {
                                String temp[];
                                temp = moments.get(i).getPicture().split(";");
                                System.out.println(temp[j]);
                                a[i][j] = temp[j];
                                Log.w("nnn", "图片地址" + a[i][j]);
                            }
                        }
                        //Collections.reverse(list);

                        ListViewAdapter adapter = new ListViewAdapter(getBaseContext(), moments,1,Main2Activity.username);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                }
                else{
                    System.out.println(e.getMessage());
                }
            }
        });
    }
    public class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }
}