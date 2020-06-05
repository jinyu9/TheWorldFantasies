package com.example.worldtest.ui.home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.R;
import com.example.worldtest.ui.notifications.Collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;
import static com.example.worldtest.Main2Activity.username;

public class Introduction extends AppCompatActivity {


    String id;
    String path0;
    String chinaName;
    String englishName;
    String briefInfo;
    private TextView textView;
    //private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView button;


    BmobQuery<Collect> bmobQuery = new BmobQuery<Collect>();

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        addActivity(this);
        showProgressDialog("提示", "正在加载......");
        Bmob.initialize(getApplicationContext(),"e1f541a4a1129508aace8369f5432292");

         textView=findViewById(R.id.introtext);

         //imageView=findViewById(R.id.introimage);
         imageView1=findViewById(R.id.introimage1);
         imageView2=findViewById(R.id.introimage2);
         imageView3=findViewById(R.id.introimage3);
         button=findViewById(R.id.prefer);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("textId");
        path0=bundle.getString("path0");
        chinaName=bundle.getString("chinaName");
        englishName=bundle.getString("englishName");
        briefInfo=bundle.getString("briefInfo");

        send1();
        send();

        bmobQuery.addWhereEqualTo("username",username);
        bmobQuery.findObjects(new FindListener<Collect>() {  //按行查询
            @Override
            public void done(List<Collect> list, BmobException e) {
                if (e == null) {
                    if(list.size()==0){
                        button.setImageResource(R.drawable.collect0);
                    }else {
                        int flag=0;
                        for(Collect s:list){
                            if(s.getAttractionId().equals(id)){
                                flag=1;
                                button.setImageResource(R.drawable.collect);
                            }
                        }
                        if(flag==0){
                            button.setImageResource(R.drawable.collect0);
                        }
                    }
                }else{
                    System.out.println("失败");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmobQuery.addWhereEqualTo("username",username);
                bmobQuery.findObjects(new FindListener<Collect>() {  //按行查询
                    @Override
                    public void done(List<Collect> list, BmobException e) {
                        if (e == null) {
                            if(list.size()==0){
                                Collect collect=new Collect();
                                collect.setAttractionId(id);
                                collect.setUsername(username);
                                collect.setPath0(path0);
                                collect.setChinaName(chinaName);
                                collect.setEnglishName(englishName);
                                collect.setBriefInfor(briefInfo);
                                collect.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId,BmobException e) {
                                        if(e==null){
                                            button.setImageResource(R.drawable.collect);
                                            Toast.makeText(getBaseContext(), "收藏成功，可前往主页-我的收藏查看！", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getBaseContext(), "收藏失败，稍后请重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                int flag=0;
                                for(Collect s:list){
                                    System.out.println("attractionId"+s.getAttractionId());
                                    System.out.println("id"+id);
                                    if(s.getAttractionId().equals(id)){
                                        flag=1;
                                        button.setImageResource(R.drawable.collect);
                                        s.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    button.setImageResource(R.drawable.collect0);
                                                    Toast.makeText(getBaseContext(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getBaseContext(), "取消收藏失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                                if(flag==0){
                                    Collect collect=new Collect();
                                    collect.setUsername(username);
                                    collect.setAttractionId(id);
                                    collect.setPath0(path0);
                                    collect.setChinaName(chinaName);
                                    collect.setEnglishName(englishName);
                                    collect.setBriefInfor(briefInfo);
                                    collect.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                                button.setImageResource(R.drawable.collect);
                                                Toast.makeText(getBaseContext(), "收藏成功，可前往主页-我的收藏查看！", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getBaseContext(), "收藏失败，稍后请重试！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }else{
                            System.out.println("失败");
                        }
                    }
                });
            }
        });

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
