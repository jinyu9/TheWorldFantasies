package com.example.worldtest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;
import static java.util.regex.Pattern.compile;

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String realCode;
    //private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityNumber;
    private EditText mEtRegisteractivityPhonecodes;
    private ImageView mIvRegisteractivityShowcode;
    private RelativeLayout mRlRegisteractivityBottom;
    private RadioGroup mRBRegisteractivitySex;
    private RadioButton mRBRegisteractivityfemale;
    private RadioButton mRBRegisteractivitymale;
    private CheckBox mCBRegisteractivityVisible;

    private String name;
    private String password;
    private String sex;
    private String number;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addActivity(this);
        initView();

        //mDBOpenHelper = new DBOpenHelper(this);

        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

    }

    private void initView(){
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
       // mRlRegisteractivityTop = findViewById(R.id.rl_registeractivity_top);
       // mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityNumber = findViewById(R.id.et_registeractivity_number);
        mEtRegisteractivityPhonecodes = findViewById(R.id.et_registeractivity_phoneCodes);
        mIvRegisteractivityShowcode = findViewById(R.id.iv_registeractivity_showCode);
        mRlRegisteractivityBottom = findViewById(R.id.rl_registeractivity_bottom);
        mRBRegisteractivitySex = findViewById(R.id.SexRadio);
        mRBRegisteractivityfemale = findViewById(R.id.rb_registeractivity_female);
        mRBRegisteractivitymale = findViewById(R.id.rb_registeractivity_male);
        mCBRegisteractivityVisible  = findViewById(R.id.cb_registeractivity_visible);
        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
     //   mIvRegisteractivityBack.setOnClickListener(this);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);
        mCBRegisteractivityVisible.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_registeractivity_visible:
                if(mCBRegisteractivityVisible.isChecked()){
                    mEtRegisteractivityPassword1.setInputType(0x90);
                    mEtRegisteractivityPassword2.setInputType(0x90);
                }else{
                    mEtRegisteractivityPassword1.setInputType(0x81);
                    mEtRegisteractivityPassword2.setInputType(0x81);
                }
                break;
            case R.id.iv_registeractivity_showCode:    //改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码

                            name = mEtRegisteractivityUsername.getText().toString().trim();
                            password = mEtRegisteractivityPassword1.getText().toString().trim();
                            phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                            number = mEtRegisteractivityNumber.getText().toString().trim();
                            sex = "";
                            //注册验证
                            if (mRBRegisteractivityfemale.isChecked()) {
                                sex = "女";
                            } else if (mRBRegisteractivitymale.isChecked()) {
                                sex = "男";
                            }
                try {
                    sex = URLEncoder.encode(URLEncoder.encode(sex, "utf-8"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode) && !TextUtils.isEmpty(number)) {
                    if (number.length() == 11 && IsNumber(number)) {
                        if (isContainAll(password)) {
                            if (password.length() >= 9 && password.length() <= 20) {
                                if ((mEtRegisteractivityPassword1.getText().toString().trim()).equals(mEtRegisteractivityPassword2.getText().toString().trim())) {
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
                                        Toast.makeText(RegisterActivity.this, "验证码错误,注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "两次输入的密码须一致", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "密码长度不得小于九位或大于二十位", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "密码至少包含大小写字母及数字中的两种", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        if(str==null){
            return false;
        }else{
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
    }
    public static boolean IsNumber(String str) {
        if(str==null||str.length()==0){
            return false;
        }else {
            Pattern p = compile("[0-9]*");
            Matcher m = p.matcher(str);
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }
    }
    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.print(result);
                Intent intent2 = new Intent(RegisterActivity.this, Main2Activity.class);
                intent2.putExtra("username", name);
                intent2.putExtra("password", password);
                startActivity(intent2);
                finish();
                Toast.makeText(RegisterActivity.this, "验证通过，注册成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onDestroy() {//销毁
        super.onDestroy();
        removeActivity(this);
    }


}

