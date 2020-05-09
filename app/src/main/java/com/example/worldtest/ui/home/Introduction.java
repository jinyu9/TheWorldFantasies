package com.example.worldtest.ui.home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class Introduction extends AppCompatActivity {


    String id;
    private TextView textView;
    //private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;


    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        addActivity(this);
        showProgressDialog("提示", "正在加载......");


         textView=findViewById(R.id.introtext);

         //imageView=findViewById(R.id.introimage);
         imageView1=findViewById(R.id.introimage1);
         imageView2=findViewById(R.id.introimage2);
         imageView3=findViewById(R.id.introimage3);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("textId");
        send1();
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
                    String path = URLEncoder.encode(URLEncoder.encode(id, "utf-8"), "utf-8");
                    URL url = new URL("http://47.100.139.135:8080/TestLink/IntroServlet?id=" + path);
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
                    show(result.toString());
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

    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
              //  System.out.println(text);
                textView.setText(text);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextSize(15);

            }
        });

    }

    private void send1() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection  connection = null;
                BufferedReader reader=null;
                try {
                    String path = URLEncoder.encode(URLEncoder.encode(id, "utf-8"), "utf-8");
                    URL url1 = new URL("http://47.100.139.135:8080/TestLink/AllImageServlet?id=" + path);
                    connection = (HttpURLConnection) url1.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //返回输入流
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    int i=0;
                    while ((line = reader.readLine()) != null) {
                        if(i==0||i==1||i==2||i==3||i==4||i==5||i==6||i==7){//System.out.println("i="+i+":"+line);
                            i++;}
                       /* else if(i==7){
                            i++;
                            System.out.println("i==7"+line);
                            show1(line);
                        }*/
                        else if (i==8){
                            i++;
                            //System.out.println("i==8"+line);
                            show1_1(line);
                        }
                        else if (i==9){
                            i++;
                            //System.out.println("i==9"+line);
                            show1_2(line);
                        }
                        else if (i==10){
                            i++;
                            //System.out.println("i==10"+line);
                            show1_3(line);
                        }
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
                    progressDialog.dismiss();

                }
            }
        }).start();
    }

/*
    private void show1(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                System.out.println("show1:"+text);
                try {
                    Bitmap bitmap;
                    byte[] data;
                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }*/
    private void show1_1(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                System.out.println("show1_1:"+text);
                try {
                    Bitmap bitmap;
                    byte[] data;
                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void show1_2(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                System.out.println("show1_2:"+text);
                try {
                    Bitmap bitmap;
                    byte[] data;
                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void show1_3(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                System.out.println("show1_3:"+text);
                try {
                    Bitmap bitmap;
                    byte[] data;
                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView3.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public static byte[] GetUserHead(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = StreamTool.readInputStream(inputStream); // 获得图片的二进制数据
        return data;

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
