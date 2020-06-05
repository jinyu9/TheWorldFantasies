package com.example.worldtest.ui.notifications;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.Main2Activity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;
import static java.util.regex.Pattern.compile;

public class updateInfoActivity extends AppCompatActivity implements View.OnClickListener{
    //private DBOpenHelper mDBOpenHelper;
    private RelativeLayout mRlUpdateactivityTop;
    //private EditText mEtUpdateactivityUsername;
    private EditText mEtUpdateactivityPassword;
    //private EditText mETUpdateactivitySex;
    private EditText mETUpdateactivityNum;
    private LinearLayout mLlUpdateactivityTwo;
    private Button mBtUpdateactivity;
    private ImageView mIVUpdateBack;
    private TextView mTVUpdateactivity;
    private RadioGroup mRBUpdateactivitySex;
    private RadioButton mRBUpdateactivityfemale;
    private RadioButton mRBUpdateactivitymale;
    private CheckBox mCBUpdateactivityvisible;

    private String name;
    private String password;
    private String sex;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        addActivity(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        sex = intent.getStringExtra("sex");
        number = intent.getStringExtra("number");
        initView();
        //mEtUpdateactivityUsername.setText(name);
        mTVUpdateactivity.setText("用户名："+name);
        mEtUpdateactivityPassword.setText(password);
        //根据初值设置默认选项
        if(sex.equals("男")){
        mRBUpdateactivitymale.setChecked(true);}
        else{
            mRBUpdateactivityfemale.setChecked(true);
        }
        //mETUpdateactivitySex.setText(sex);
        mETUpdateactivityNum.setText(number);
        //mDBOpenHelper = new DBOpenHelper(this);
    }
    private void initView(){
        mBtUpdateactivity = findViewById(R.id.bt_updateactivity);
        mRlUpdateactivityTop = findViewById(R.id.rl_updateactivity_top);
        mIVUpdateBack = findViewById(R.id.iv_updateactivity_back);
        //mEtUpdateactivityUsername = findViewById(R.id.et_updateactivity_username);
        mEtUpdateactivityPassword = findViewById(R.id.et_updateactivity_password);
        //mETUpdateactivitySex = findViewById(R.id.et_updateactivity_sex);
        mETUpdateactivityNum = findViewById(R.id.et_updateactivity_number);
        mLlUpdateactivityTwo = findViewById(R.id.ll_updateactivity_two);
        mTVUpdateactivity = findViewById(R.id.tv_updateactivity_username);
        mRBUpdateactivitySex = findViewById(R.id.UpdateSexRadio);
        mRBUpdateactivityfemale = findViewById(R.id.rb_updateactivity_female);
        mRBUpdateactivitymale = findViewById(R.id.rb_updateactivity_male);
        mCBUpdateactivityvisible = findViewById(R.id.cb_updateactivity_visible);
        mIVUpdateBack.setOnClickListener(this);
        mBtUpdateactivity.setOnClickListener(this);
        mCBUpdateactivityvisible.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_updateactivity_back) {
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("password",password);
            intent.putExtra("sex",sex);
            intent.putExtra("number",number);
            Main2Activity.sex = sex;
            Main2Activity.number = number;
            startActivity(intent);
            finish();
        }else if(view.getId() == R.id.cb_updateactivity_visible){
            if(mCBUpdateactivityvisible.isChecked()){
                mEtUpdateactivityPassword.setInputType(0x90);
            }else{
                mEtUpdateactivityPassword.setInputType(0x81);
            }
        }
        else if(view.getId() == R.id.bt_updateactivity){
            //name = mEtUpdateactivityUsername.getText().toString().trim();
            password = mEtUpdateactivityPassword.getText().toString().trim();
            //sex = mETUpdateactivitySex.getText().toString().trim();
            number = mETUpdateactivityNum.getText().toString().trim();
            if(mRBUpdateactivityfemale.isChecked()){
                sex = "女";
            }else{
                sex = "男";
            }
            System.out.println("sex:"+sex);
            try {
                sex = URLEncoder.encode(URLEncoder.encode(sex, "utf-8"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(password) ){
                if(isContainAll(password)){
                    if(password.length()>=9&&password.length()<=20){
                        if(number.length() == 11&&IsNumber(number)){
                        //开启线程，发送请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpURLConnection connection = null;
                                    BufferedReader reader = null;
                                    try {
                                        URL url = new URL("http://47.100.139.135:8080/TheWorldFantasies/UserUpdateServlet?name="+name+ "&password=" + password + "&sex=" + sex + "&number=" + number);
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
            }else{
                            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "密码长度不得小于九位或大于二十位", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "密码至少包含大小写字母及数字中的两种", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "修改后的密码不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent2 = new Intent(updateInfoActivity.this, InfoActivity.class);
                intent2.putExtra("name", name);
                intent2.putExtra("password", password);
                intent2.putExtra("sex", sex);
                intent2.putExtra("number", number);
                System.out.println("sex:"+sex);
                startActivity(intent2);
                finish();
                Toast.makeText(updateInfoActivity.this, "修改信息成功！", Toast.LENGTH_SHORT).show();
            }
            });
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
    public static boolean IsNumber(String str) {
        Pattern p = compile("[0-9]*");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
