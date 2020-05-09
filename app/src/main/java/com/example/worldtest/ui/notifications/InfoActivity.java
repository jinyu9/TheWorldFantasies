package com.example.worldtest.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {
    //private DBOpenHelper mDBOpenHelper;
    private Button MBtMainUpdate;
    private ImageView MBtMainLogout;
    private ImageView MBtInfoPic;
    private RelativeLayout MRIInfoTop;
    private LinearLayout MLLInfoTwo;
    private TextView MTVInfoUser;
    private TextView MTVInfosex;
    private TextView MTVInfonum;
    private String name;
    private String sex;
    private String number;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        addActivity(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        //password = intent.getStringExtra("password");
        initView();
//        mDBOpenHelper = new DBOpenHelper(this);
//        ArrayList<User> data = mDBOpenHelper.getAllData();
//        for (int i = 0; i < data.size(); i++) {
//            User user = data.get(i);
//            if (name.equals(user.getName())) {
//                password = user.getPassword();
//                sex = user.getSex();
//                number = user.getNumber();
//                break;
//            }
//        }
        send();
//        MTVInfoUser.setText("用户名："+name);
//        MTVInfosex.setText("性    别："+sex);
//        MTVInfonum.setText("手机号："+number);
        //MTVInfopwd.setText("密    码："+password);
    }
    private void initView(){
        MLLInfoTwo = findViewById(R.id.ll_infoactivity_two);
        MTVInfoUser = findViewById(R.id.tv_infoactivity_username);
        MTVInfosex = findViewById(R.id.tv_infoactivity_sex);
        MTVInfonum = findViewById(R.id.tv_infoactivity_number);
        MBtMainUpdate = findViewById(R.id.bt_infoactivity_update);
        MRIInfoTop = findViewById(R.id.rl_infoactivity_top);
        MBtMainLogout = findViewById(R.id.iv_infoactivity_back);
        MBtInfoPic = findViewById(R.id.iv_infoacticity_pic);
        MBtMainUpdate.setOnClickListener(this);
        MBtMainLogout.setOnClickListener(this);
        MBtInfoPic.setOnClickListener(this);
    }

    public void onClick(View view){
        if (view.getId() == R.id.bt_infoactivity_update) {
            Intent intent = new Intent(this, updateInfoActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("password",password);
            intent.putExtra("sex",sex);
            intent.putExtra("number",number);
            startActivity(intent);
            finish();
        }
        else if(view.getId() == R.id.iv_infoactivity_back){
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("name",name);
            intent.putExtra("password",password);
            startActivity(intent);
            finish();
        }else if(view.getId() == R.id.iv_infoacticity_pic){
            Toast.makeText(InfoActivity.this,"别戳我..现在还上传不了头像",Toast.LENGTH_SHORT).show();
        }
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
    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name = result.substring(result.indexOf("名=")+2,result.indexOf(","));
                password = result.substring(result.indexOf("码")+2,result.indexOf("性")-1);
                sex = result.substring(result.indexOf("别")+2,result.indexOf("手")-1);
                number = result.substring(result.indexOf("号")+2,result.indexOf("]"));
                MTVInfoUser.setText("用户名："+name);
                MTVInfosex.setText("性    别："+sex);
                MTVInfonum.setText("手机号："+number);
                System.out.print(result);
                System.out.println("name="+name);
                System.out.println("password="+password);
                System.out.println("sex="+sex);
                System.out.println("number="+number);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
