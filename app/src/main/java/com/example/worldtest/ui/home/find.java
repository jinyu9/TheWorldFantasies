package com.example.worldtest.ui.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.R;

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
import java.util.regex.Pattern;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;
import static com.example.worldtest.ui.home.HomeFragment.matchResult;

public class find extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        addActivity(this);
        showProgressDialog("提示", "正在加载......");

        final LinearLayout linearLayout1=findViewById(R.id.findline);
        Bundle bundle = this.getIntent().getExtras();
        String name=bundle.getString("textname");
        System.out.println(name);
        String path = null;
        try {
            path = java.net.URLEncoder.encode(java.net.URLEncoder.encode(name,"utf-8"),"utf-8");
            final String urlname1="http://47.100.139.135:8080/TestLink/FindSevlet?name="+path;
            send(urlname1,linearLayout1);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                    if(spString.length<3){}else {
                        String id = spString[0];
                        String name = spString[1];
                        //  System.out.println(id);
                        //  System.out.println(name);
                        String brief_infor = spString[2];
                        String regEx1 = "[\\u4e00-\\u9fa5]";
                        String chineName = matchResult(Pattern.compile(regEx1), name);
                        String EnglishName = name.replace(chineName, "");
                        String show = chineName + "\n" + EnglishName + "\n" + "简介：" + brief_infor;
                        send1(id, show, linearLayout);
                    }

                }
            }

        });

    }
    private void send1(final String id,final String name, final LinearLayout linearLayout) {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection  connection = null;
                BufferedReader reader=null;
                try {
                    String path = URLEncoder.encode(URLEncoder.encode(id, "utf-8"), "utf-8");
                    URL url1 = new URL("http://47.100.139.135:8080/TestLink/ImageServlet?id=" + path);
                    System.out.println(url1);
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
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    show1(id,name,result.toString(),linearLayout);
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

    private void show1(final String id,final String name,final String result,final LinearLayout linearLayout) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                final String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                try {
                    Bitmap bitmap;
                    byte[] data;
                    Drawable drawable;

                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    drawable = new BitmapDrawable(getResources(),bitmap);
                    drawable.setBounds(-25,-5,950,600);

                    Button button = new Button(find.this);
                    button.setCompoundDrawables(null,drawable,null,null);
                    button.setCompoundDrawablePadding(5);
                    button.setText(name);
                    button.setHint(id);
                    linearLayout.addView(button);
                    button.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(find.this, Introduction.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("textId", id);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
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
