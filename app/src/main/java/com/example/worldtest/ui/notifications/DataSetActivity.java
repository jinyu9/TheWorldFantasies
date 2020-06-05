package com.example.worldtest.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DataSetActivity extends AppCompatActivity {
    private String name;
    private String sex;
    private String number;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Log.i("worldtest","DataSetActivity");
        send();
    }

    private void send() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://47.100.139.135:8080/TheWorldFantasies/GetUserByNameServlet?name="+name);
                    System.out.println(url);
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
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String result1 = result.toString();
                    String regix = "\\s*|\t|\r|\n";
                    String regTag = "<[^>]*>";
                    String text = result1.replaceAll(regix, "").replaceAll(regTag, "");
                    show(text.toString());

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

    private void show(String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name = result.substring(result.indexOf("名=")+2,result.indexOf(","));
//                password = result.substring(result.indexOf("码")+2,result.indexOf("性")-1);
                sex = result.substring(result.indexOf("别")+2,result.indexOf("手")-1);
                number = result.substring(result.indexOf("号")+2,result.indexOf("]"));
                Log.i("worldtest","name:"+name+";sex:"+sex+";number:"+number);
//                MTVInfoUser.setText("用户名："+name);
//                MTVInfosex.setText("性    别："+sex);
//                MTVInfonum.setText("手机号："+number);
//                System.out.print(result);
//                System.out.println("name="+name);
//                System.out.println("password="+password);
//                System.out.println("sex="+sex);
//                System.out.println("number="+number);
            }
        });
    }
}
