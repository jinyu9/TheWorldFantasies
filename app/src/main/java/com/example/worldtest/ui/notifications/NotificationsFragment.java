package com.example.worldtest.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.worldtest.R;
import com.example.worldtest.loginActivity;
import com.example.worldtest.myinfo.SettingsByPreferenceActivity;

import static com.example.worldtest.ActivityCollectorUtil.finishAllActivity;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private String name;
    private String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);
        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        if(name != null&&password != null){
            final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);
            tv_main_hello.setText("欢迎您，用户："+name);}
        else{
            Intent intent2 = getActivity().getIntent();
            name = intent2.getStringExtra("username");
            String password2 = intent2.getStringExtra("password");
            final TextView tv_main_hello = root.findViewById(R.id.tv_main_hello);
            tv_main_hello.setText("欢迎您，用户："+name);
        }
        final  Button mBtMainLogout = root.findViewById(R.id.bt_main_logout);
        mBtMainLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAllActivity();
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), loginActivity.class);
                startActivity(intent);

            }
        });
        final  Button mBtMainInfo = root.findViewById(R.id.bt_main_info);
        mBtMainInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), InfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                startActivity(intent);

            }
        });
        final Button mBtMainSetting = root.findViewById(R.id.bt_main_setting);
        mBtMainSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsFragment.this.getActivity(), SettingsByPreferenceActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
    public static NotificationsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}