package com.example.worldtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout mRlPhoneactivityTop;
    private EditText mEtPhoneactivityPhone;
    private EditText mEtPhoneactivityVerify;
    private ImageView mIvPhoneactivityBack;
    private LinearLayout mLlPhoneactivityTwo;
    private Button mBtPhoneactivityLogin;
    private Button mBtPhoneactivitycheck;
    private EditText mEtPhoneRegisteractivityPassword1;
    private EditText mEtPhoneRegisteractivityPassword2;
    private CheckBox mCBPhoneRegisteractivityVisible;
    private String phone_number;
    private String cord_number;
    private String password;
    EventHandler eventHandler;
    private boolean coreflag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        addActivity(this);
        initView();
        MobSDK.submitPolicyGrantResult(true, null);
    }
    private void initView() {
        // 初始化控件
        mBtPhoneactivityLogin = findViewById(R.id.bt_phoneactivity_login);
        //mRlPhoneactivityTop = findViewById(R.id.rl_phoneactivity_top);
        mEtPhoneactivityPhone = findViewById(R.id.et_phoneactivity_username);
        mEtPhoneactivityVerify = findViewById(R.id.et_phoneactivity_password);
        mLlPhoneactivityTwo = findViewById(R.id.ll_phoneactivity_two);
        mBtPhoneactivitycheck = findViewById(R.id.bt_phoneactivity_check);
        mEtPhoneRegisteractivityPassword1 = findViewById(R.id.et_phoneregisteractivity_password1);
        mEtPhoneRegisteractivityPassword2 = findViewById(R.id.et_phoneregisteractivity_password2);
        mCBPhoneRegisteractivityVisible  = findViewById(R.id.cb_phoneregisteractivity_visible);
        //mIvPhoneactivityBack = findViewById(R.id.iv_phoneactivity_back);
        // 设置点击事件监听器
        mBtPhoneactivityLogin.setOnClickListener(this);
        mBtPhoneactivitycheck.setOnClickListener(this);
        mCBPhoneRegisteractivityVisible.setOnClickListener(this);
    }
    protected void onDestroy() {//销毁
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        removeActivity(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_phoneactivity_check) {
            if(judPhone()){//去掉左右空格获取字符串，是正确的手机号
                SMSSDK.getVerificationCode("86",phone_number);//获取你的手机号的验证码
                Toast toast = Toast.makeText(getApplicationContext(),"验证码已发送至您的手机！",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
        }
    }

        else if(view.getId() == R.id.cb_phoneregisteractivity_visible){

            if(mCBPhoneRegisteractivityVisible.isChecked()){
                mEtPhoneRegisteractivityPassword1.setInputType(0x90);
                mEtPhoneRegisteractivityPassword2.setInputType(0x90);
            }else{
                mEtPhoneRegisteractivityPassword1.setInputType(0x81);
                mEtPhoneRegisteractivityPassword2.setInputType(0x81);
            }
        }
        else if (view.getId() == R.id.bt_phoneactivity_login) {
            password = mEtPhoneRegisteractivityPassword1.getText().toString().trim();
            if(judCord()) {//判断验证码
                if (isContainAll(password)&&!TextUtils.isEmpty(password)) {
                    if (password.length() >= 9 && password.length() <= 20) {
                        if ((mEtPhoneRegisteractivityPassword1.getText().toString().trim()).equals(mEtPhoneRegisteractivityPassword2.getText().toString().trim())) {
                            SMSSDK.submitVerificationCode("86", phone_number, cord_number);//提交手机号和验证码
                            Intent intent = new Intent(this,PhoneRegisterActivity.class);
                            intent.putExtra("password",password);
                            intent.putExtra("number",phone_number);
                            Toast.makeText(getApplicationContext(), "注册成功！请完善信息",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "两次输入的密码须一致", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "密码长度不得小于九位或大于二十位", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "密码至少包含大小写字母及数字中的两种", Toast.LENGTH_SHORT).show();
                }
            }
            coreflag=false;
        }
    }
    private boolean judPhone() {//判断手机号是否正确
        //不正确的情况
        if(TextUtils.isEmpty(mEtPhoneactivityPhone.getText().toString().trim()))//对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(getApplicationContext(),"请输入您的电话号码",Toast.LENGTH_LONG).show();
            mEtPhoneactivityPhone.requestFocus();//设置是否获得焦点。若有requestFocus()被调用时，后者优先处理。注意在表单中想设置某一个如EditText获取焦点，光设置这个是不行的，需要将这个EditText前面的focusable都设置为false才行。
            return false;
        }
        else if(mEtPhoneactivityPhone.getText().toString().trim().length()!=11){
            Toast.makeText(getApplicationContext(),"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            mEtPhoneactivityPhone.requestFocus();
            return false;
        }

        //正确的情况
        else{
            phone_number=mEtPhoneactivityPhone.getText().toString().trim();
            String num="[1][3578]\\d{9}";
            if(phone_number.matches(num)) {
                return true;
            }
            else{
                Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
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
    private boolean judCord() {//判断验证码是否正确
        judPhone();//先执行验证手机号码正确与否
        if(TextUtils.isEmpty(mEtPhoneactivityVerify.getText().toString().trim())) {//验证码
            Toast.makeText(getApplicationContext(), "请输入您的验证码", Toast.LENGTH_LONG).show();
            mEtPhoneactivityVerify.requestFocus();//聚集焦点
            return false;
        }
        else if(mEtPhoneactivityVerify.getText().toString().trim().length()!=6){
            Toast.makeText(getApplicationContext(),"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            mEtPhoneactivityVerify.requestFocus();
            return false;
        }
        else{
            cord_number=mEtPhoneactivityVerify.getText().toString().trim();
            return true;
        }
    }
    public void sms_verification(){
        //MobSDK.init(context, "28bc12fa236e4","44cb357655f252a8a75eac378b8283ad");
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();//创建了一个对象
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);//注册短信回调（记得销毁，避免泄露内存）*/
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                if(result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            //回调完成
            if (result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "验证码输入正确",Toast.LENGTH_LONG).show();
                }
            }else {//其他出错情况
                if(coreflag){
                    mBtPhoneactivitycheck.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

}
