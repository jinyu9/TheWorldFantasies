package com.example.worldtest.ui.notifications;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;
import com.example.worldtest.loginActivity;
import com.example.worldtest.myinfo.SettingsByPreferenceActivity;
import com.example.worldtest.ui.Report.Autogragh;
import com.example.worldtest.ui.dashboard.FindDiscover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.worldtest.ActivityCollectorUtil.finishAllActivity;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    public String name_moment;
    public static String username;
    private String name;
    private String password;
    private String number;
    private String sex;
    private String word;

    private ImageView mHBack;
    private ImageView mHHead;
    private TextView mHWord;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mHBack = root.findViewById(R.id.h_back);
        mHHead = root.findViewById(R.id.h_head);
        mHWord = root.findViewById(R.id.word);
        setData();

        //final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);

        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        number = Main2Activity.number;
        if(name != null&&password != null){
            final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);
            tv_main_hello.setText(name);}
        else{
            Intent intent2 = getActivity().getIntent();
            name = intent2.getStringExtra("username");
            String password2 = intent2.getStringExtra("password");
            final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);
            tv_main_hello.setText(name);
        }
        BmobQuery<Autogragh> bmobQuery0 = new BmobQuery<Autogragh>();
        bmobQuery0.addWhereEqualTo("name", name);
        bmobQuery0.findObjects(new FindListener<Autogragh>() {
            @Override
            public void done(List<Autogragh> list, BmobException e) {
                if(e == null){
                    if(list.size() > 0){
                        word = list.get(0).getWord();
                        mHWord.setText(word);}
                    else{
                        mHWord.setText("TA很神秘，没有留下个性签名噢。");
                    }
                }else{
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    mHWord.setText("出错了。");
                }
            }
        });
        final TextView tv_infoactivity_number = root.findViewById(R.id.tv_infoactivity_number);
        tv_infoactivity_number.setText(number);
        final  ItemView mBtMainLogout = root.findViewById(R.id.bt_main_logout);
        mBtMainLogout.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                finishAllActivity();
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), loginActivity.class);
                startActivity(intent);
            }
        });
        final  ItemView mBtMainInfo = root.findViewById(R.id.bt_main_info);
        mBtMainInfo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), InfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });
        final  ItemView mBtMainAuto = root.findViewById(R.id.self_signature);
        mBtMainAuto.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), InfoActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        final ItemView mBtMainSetting = root.findViewById(R.id.bt_main_setting);
        mBtMainSetting.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), SettingsByPreferenceActivity.class);
                startActivity(intent);
            }
        });
        final ItemView mBtMainMoment = root.findViewById(R.id.bt_main_moment);
        mBtMainMoment.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), FindDiscover.class);
                intent.putExtra("user_name",name);
                intent.putExtra("discoverName",name);
                username = name;
                startActivity(intent);
            }
        });
        final ItemView button = root.findViewById(R.id.mycollect);
        button.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), MyCollection.class);
                startActivity(intent);
            }
        });
        final ImageView sex_logo = root.findViewById(R.id.sex_logo);
        if (Main2Activity.sex.equals("女")) {
            sex_logo.setImageDrawable(ContextCompat.getDrawable(this.getActivity(),R.drawable.ic_girl));
        } else if(Main2Activity.sex.equals("男")) {
            sex_logo.setImageDrawable(ContextCompat.getDrawable(this.getActivity(),R.drawable.ic_boy));
        }
        return root;
    }



    private void setData() {

        Glide.with(this.getActivity()).load(R.drawable.aa)
                .bitmapTransform(new BlurTransformation(this.getActivity(), 25), new CenterCrop(this.getActivity()))
                .into(mHBack);
//        //设置背景磨砂效果
//        Glide.with(this).load(R.drawable.cat)
//                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
//                .into(mHBack);
        //设置圆形图像
        Glide.with(this.getActivity()).load(R.drawable.aa)
                .bitmapTransform(new CropCircleTransformation(this.getActivity()))
                .into(mHHead);


    }

    public static NotificationsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}