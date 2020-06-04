package com.example.worldtest;
/**
 * 纯粹实现登录注册功能，其它功能都被注释掉了
 * 起作用的代码（连带着packag、import算上） 共 73 行
 * 不多吧？
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.ui.dashboard.push.PushSquareActivity;
import com.example.worldtest.ui.notifications.DataSetActivity;

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

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    //private DBOpenHelper mDBOpenHelper;
    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlLoginactivityTop;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private LinearLayout mLlLoginactivityTwo;
    private Button mBtLoginactivityLogin;
    private TextView mTvLoginactivityPhone;
    private String info;
    private String name;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        Bmob.initialize(this,"e1f541a4a1129508aace8369f5432292");
        Person person = new Person();
        Person person2 = new Person();
        person2.setAddress("九江");
        person2.update("2ce3cdd867", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(loginActivity.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(loginActivity.this, "更改数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        });

         */
/*
        person.setName("meme3");
        person.setAddress("America");
        person.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(loginActivity.this, "添加数据成功，返回objectId为："+person.getObjectId(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(loginActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        });

        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject("2ce3cdd867", new QueryListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if(e==null){
                    String msg = person.toString();
                    Toast.makeText(loginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    System.out.println(msg);
                }else{
                    Toast.makeText(loginActivity.this, "查询失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        });

 */

        Intent intent = new Intent(loginActivity.this, PushSquareActivity.class);
        Intent intent0 = new Intent(loginActivity.this, DataSetActivity.class);
        intent0.putExtra("name",name);

        addActivity(this);
        initView();
        //mDBOpenHelper = new DBOpenHelper(this);
    }
    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.tv_loginactivity_register);
        mRlLoginactivityTop = findViewById(R.id.rl_loginactivity_top);
        mEtLoginactivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = findViewById(R.id.et_loginactivity_password);
        mLlLoginactivityTwo = findViewById(R.id.ll_loginactivity_two);
        mTvLoginactivityPhone = findViewById(R.id.tv_loginactivity_check);
        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);
        mTvLoginactivityPhone.setOnClickListener(this);
    }

    public  void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginactivity_register:
                System.out.print("登录啊");
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
            case R.id.tv_loginactivity_check:
                startActivity(new Intent(this, PhoneActivity.class));
                finish();
                break;
            case R.id.bt_loginactivity_login:
//                String name = mEtLoginactivityUsername.getText().toString().trim();
//                String password = mEtLoginactivityPassword.getText().toString().trim();
//                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
//                    ArrayList<User> data = mDBOpenHelper.getAllData();
//                    boolean match = false;
//                    for (int i = 0; i < data.size(); i++) {
//                        User user = data.get(i);
//                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
//                            match = true;
//                            break;
//                        } else {
//                            match = false;
//                        }
//                    }
//                    if (match) {
//                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(this, Main1Activity.class);
//                        intent.putExtra("name",name);
//                        intent.putExtra("password",password);
//                        startActivity(intent);
//                        finish();//销毁此Activity
//                    } else {
//                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
//                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            password = mEtLoginactivityPassword.getText().toString().trim();
                            name = mEtLoginactivityUsername.getText().toString().trim();
                            URL url = new URL("http://47.100.139.135:8080/TheWorldFantasies/UserLoginServlet?name=" + name + "&password=" + password);
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
                            info = text;
                            show(text.toString());
//                            if (result.equals("loginsuccess!")) {
//                                Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(loginActivity.this, Main1Activity.class);
//                                intent.putExtra("name",name);
//                                intent.putExtra("password",password);
//                                startActivity(intent);
//                                finish();//销毁此Activity
//                            } else {
//                                Toast.makeText(loginActivity.this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
//                            }

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
                break;
        }
    }

    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.print(result);
                if (result.equals("loginsuccess!")) {
                    Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, Main2Activity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(loginActivity.this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
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



