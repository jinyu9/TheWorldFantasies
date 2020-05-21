package com.example.worldtest.ui.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.R;
import com.example.worldtest.ui.notifications.Collect;
import com.example.worldtest.ui.notifications.CollectionAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;
import static com.example.worldtest.ui.home.HomeFragment.matchResult;

public class find extends AppCompatActivity implements
        MyListViewUtils.LoadListener{

    private ProgressDialog progressDialog;
    private List<Collect> collects=new ArrayList<>();
    CollectionAdapter collectionAdapter;
    private MyListViewUtils listViewUtils;
    private String urlname1;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        addActivity(this);
        showProgressDialog("提示", "正在加载......");

        Bundle bundle = this.getIntent().getExtras();
        String name=bundle.getString("textname");
        String path = null;
        try {
            path = java.net.URLEncoder.encode(java.net.URLEncoder.encode(name,"utf-8"),"utf-8");
            urlname1="http://47.100.139.135:8080/TestLink/FindSevlet?name="+path;

            listViewUtils = (MyListViewUtils)findViewById(R.id.find_list);
            listViewUtils.setInteface(this);
            linearLayout=findViewById(R.id.findline);
            send(urlname1,linearLayout);
            System.out.println("collects.size:"+collects.size());
            collectionAdapter =new CollectionAdapter(getBaseContext(),collects);
            collectionAdapter.notifyDataSetChanged();
            listViewUtils.setAdapter(collectionAdapter);
            progressDialog.dismiss();
            listViewUtils.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), Introduction.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("textId", collects.get(position).getAttractionId());
                    bundle.putString("path0",collects.get(position).getPath0());
                    bundle.putString("show",collects.get(position).getShow());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    // 实现PullLoad接口
    @Override
    public void PullLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 这里处理请求返回的结果（这里使用模拟数据）
                collects.clear();
                send(urlname1,linearLayout);
                // 更新数据
                collectionAdapter.notifyDataSetChanged();
                // 加载完毕
                listViewUtils.loadComplete();
            }
        }, 3000);

    }

    // 实现onLoad接口
    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 这里处理请求返回的结果（这里使用模拟数据）
                // 更新数据
                send(urlname1,linearLayout);
                collectionAdapter.notifyDataSetChanged();
                // 加载完毕
                listViewUtils.loadComplete();
            }
        }, 3000);
    }
    public void send(final String urlname, final LinearLayout linearLayout) {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(urlname);
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //返回输入流
                    InputStream in = connection.getInputStream();
                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        show(line,linearLayout);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {//关闭连接
                        connection.disconnect();
                    }

                }
            }
        }).start();

    }
    private void show(final String line, final LinearLayout linearLayout) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\t|\r|\n";
                String regTag = "<[^>]*>";
                final String text = line.replaceAll(regFormat, "").replaceAll(regTag, "");
                if (text.equals("")||text.trim().length()==0) {
                } else if(text.equals("no_find")){

                    linearLayout.setGravity(Gravity.CENTER);
                    ImageView imageView=new ImageView(find.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200,200);
                    imageView.setLayoutParams(params);

                    imageView.setImageResource(R.drawable.jiazaishibai);
                    linearLayout.addView(imageView);

                    TextView textView=new TextView(find.this);
                    textView.setText("未搜索到相关信息");

                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextSize(25);
                    linearLayout.addView(textView);
                    progressDialog.dismiss();
                } else {
                    String [] spString = text.split("&nbsp;&nbsp;&nbsp;");
                        Collect collect=new Collect();
                        String [] attr= new String[4];
                        for(int i=0;i<spString.length;i++){
                            attr[i]=spString[i];
                        }
                        //System.out.println("attr.length"+attr.length);
                        String regEx1 = "[\\u4e00-\\u9fa5]";
                        String chineName;
                        String show;
                        if(attr[1]==null||attr[1].equals("")){
                           chineName = attr[1];
                             show = chineName + "\n" + "\n" + "简介：" + attr[2];
                        }else {
                           chineName = matchResult(Pattern.compile(regEx1), attr[1]);
                            String EnglishName = attr[1].replace(chineName, "");
                            show = chineName + "\n" + EnglishName + "\n" + "简介：" + attr[2];
                        }
                        collect.setAttractionId(attr[0]);
                        collect.setShow(show);
                        collect.setPath0(attr[3]);
                        collects.add(collect);
                        collectionAdapter.notifyDataSetChanged();
                    }

                }
        });

    }


    //加载提示
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(this,
                    title, message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
        progressDialog.show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
