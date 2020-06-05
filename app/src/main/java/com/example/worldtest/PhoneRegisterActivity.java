package com.example.worldtest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class PhoneRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private String realCode;
    private Button mBtPhoneRegisteractivityRegister;
    private RelativeLayout mRlPhoneRegisteractivityTop;
    private ImageView mIvPhoneRegisteractivityBack;
    private LinearLayout mLlPhoneRegisteractivityBody;
    private EditText mEtPhoneRegisteractivityUsername;
    private EditText mEtPhoneRegisteractivityPhonecodes;
    private ImageView mIvPhoneRegisteractivityShowcode;
    private RadioGroup mRBPhoneRegisteractivitySex;
    private RadioButton mRBPhoneRegisteractivityfemale;
    private RadioButton mRBPhoneRegisteractivitymale;


    private String name;
    private String password;
    private String sex;
    private String number;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        addActivity(this);
        initView();
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        password = intent.getStringExtra("password");
        mIvPhoneRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }
    private void initView(){
        mBtPhoneRegisteractivityRegister = findViewById(R.id.bt_phoneregisteractivity_register);
       // mRlPhoneRegisteractivityTop = findViewById(R.id.rl_phoneregisteractivity_top);
       // mIvPhoneRegisteractivityBack = findViewById(R.id.iv_phoneregisteractivity_back);
        mLlPhoneRegisteractivityBody = findViewById(R.id.ll_phoneregisteractivity_body);
        mEtPhoneRegisteractivityUsername = findViewById(R.id.et_phoneregisteractivity_username);

        mEtPhoneRegisteractivityPhonecodes = findViewById(R.id.et_phoneregisteractivity_phoneCodes);
        mIvPhoneRegisteractivityShowcode = findViewById(R.id.iv_phoneregisteractivity_showCode);
        mRBPhoneRegisteractivitySex = findViewById(R.id.SexRadio);
        mRBPhoneRegisteractivityfemale = findViewById(R.id.rb_phoneregisteractivity_female);
        mRBPhoneRegisteractivitymale = findViewById(R.id.rb_phoneregisteractivity_male);

        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        //mIvPhoneRegisteractivityBack.setOnClickListener(this);
        mIvPhoneRegisteractivityShowcode.setOnClickListener(this);
        mBtPhoneRegisteractivityRegister.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phoneregisteractivity_showCode:    //改变随机验证码的生成
                mIvPhoneRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_phoneregisteractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                name = mEtPhoneRegisteractivityUsername.getText().toString().trim();
                phoneCode = mEtPhoneRegisteractivityPhonecodes.getText().toString().toLowerCase();
                sex = "";
                //注册验证
                if (mRBPhoneRegisteractivityfemale.isChecked()) {
                    sex = "女";
                } else if (mRBPhoneRegisteractivitymale.isChecked()) {
                    sex = "男";
                }
                try {
                    sex = URLEncoder.encode(URLEncoder.encode(sex, "utf-8"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode) && !TextUtils.isEmpty(number)) {
                                    if (phoneCode.equals(realCode)) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                HttpURLConnection connection = null;
                                                BufferedReader reader = null;
                                                try {
                                                    URL url = new URL("http://47.100.139.135:8080/TheWorldFantasies/UserInsertServlet?name=" + name + "&password=" + password + "&sex=" + sex + "&number=" + number);
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

                                                }
                                                catch (MalformedURLException e) {
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
                                    } else {
                                        Toast.makeText(getApplicationContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                    }

                } else {
                    Toast.makeText(getApplicationContext(), "请完善信息！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.print(result);
                Intent intent2 = new Intent(getApplicationContext(), Main2Activity.class);
                intent2.putExtra("username", name);
                intent2.putExtra("password", password);
                startActivity(intent2);
                finish();
                Toast.makeText(getApplicationContext(), "完善信息成功，欢迎您！", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
