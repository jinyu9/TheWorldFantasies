package com.example.worldtest.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.worldtest.R;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        Intent intent = new Intent(DashboardFragment.this.getActivity(), DiscoveryActivity.class);
//        startActivity(intent);


        Bmob.initialize(getApplicationContext(),"e1f541a4a1129508aace8369f5432292");

        //插入一条数据
/*
        Toast.makeText(getApplicationContext(), "添加数据成功，返回objectId为：", Toast.LENGTH_SHORT).show();

        Moment moment = new Moment();
        moment.setUser_name("but");
        moment.setUser_avatar("Avatar");
        moment.setContent("so I can touch the sky.");
        moment.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588763354150&di=4692d52677d5db8f33caf40508d9e793&imgtype=0&src=http%3A%2F%2Ffile01.16sucai.com%2Fd%2Ffile%2F2013%2F0617%2F20130617104626550.jpg");
        moment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"insert success!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
        ListView listView = root.findViewById(R.id.listview);
        bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    //数据倒序显示,最新的数据在最上面
                    Collections.reverse(list);
                    ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int lv_item_position = (Integer) adapterView.getTag();
                        }
                    });
                }
                else{
                    System.out.println(e.getMessage());
                }
            }
        });
        final Button b = root.findViewById(R.id.findDisButton);
        b.setOnClickListener(v -> {
            final EditText editText=getView().findViewById(R.id.findDisText);
            String discoverName=editText.getText().toString().trim();
            Intent intent=new Intent(getActivity(), FindDiscover.class);
            Bundle bundle = new Bundle();
            bundle.putString("discoverName", discoverName);
            intent.putExtras(bundle);
            startActivity(intent);

        });
        return root;


    }
    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

}