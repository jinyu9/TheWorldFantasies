package com.example.worldtest.ui.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.worldtest.R;
import com.example.worldtest.ui.dashboard.push.PushSquareActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import lumenghz.com.pullrefresh.PullToRefreshView;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class DashboardFragment extends Fragment{
    private DashboardViewModel dashboardViewModel;
    public static int size;
    public static String[][] a;
    public static int[] n;
    private PullToRefreshView mPullToRefreshView;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    String user_name;
    //发布按钮
    private FloatingActionButton iv_push;
    //状态码
    private static final int REQUEST_CODE = 1000;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Bundle bundle1 = getActivity().getIntent().getExtras();
        user_name = bundle1.getString("name");
        Bmob.initialize(getApplicationContext(),"e1f541a4a1129508aace8369f5432292");
        /*
        //插入测试
        Moment moment = new Moment();
        moment.setUser_name("a");
        moment.setUser_avatar("Avatar");
        moment.setContent("测试一下图片？");
        moment.setPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1590174536896&di=18134892f337c39cfd0b74ea09d9c50d&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F9%2F58b0f41ad200b.jpg");
        moment.setN(1);
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
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout)root.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                //new Task().execute();
                BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
                ListView listView = root.findViewById(R.id.listview);
                bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
                    @Override
                    public void done(List<Moment> list, BmobException e) {
                        if (e == null) {
                            //数据倒序显示,最新的数据在最上面
                            //总共有多少条动态
                            size = list.size();
                            Log.w("nnn","总共有多少条朋友圈="+size);
                            a = new String[size][];
                            n = new int[size];

                            //每条动态的图片数量
                            for(int i = 0;i<size;i++){
                                Log.w("nnn","每条朋友圈的图片数量="+list.get(i).getN());
                                n[i] = list.get(i).getN();
                                a[i] = new String[list.get(i).getN()];
                                for(int j =0;j<list.get(i).getN();j++){
                                    String temp[];
                                    temp = list.get(i).getPicture().split(";");
                                    System.out.println(temp[j]);
                                    a[i][j] = temp[j];
                                    Log.w("nnn","图片地址"+a[i][j]);
                                }
                            }
                            //Collections.reverse(list);
                            ListViewAdapter adapter = new ListViewAdapter(getContext(), list,0,user_name);
                            listView.setAdapter(adapter);
                        }
                        else{
                            System.out.println(e.getMessage());
                        }
                    }
                });
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String t = "";
                t = ("最后刷新时间：\n"+simpleDateFormat.format(date));
                Toast toast = Toast.makeText(getApplicationContext(),t,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                new Task().execute();
            }
        });


        BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
        ListView listView = root.findViewById(R.id.listview);
        listView.deferNotifyDataSetChanged();
        bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    //数据倒序显示,最新的数据在最上面
                    //总共有多少条动态
                    size = list.size();
                    Log.w("nnn","总共有多少条朋友圈="+size);
                    a = new String[size][];
                    n = new int[size];

                    //每条动态的图片数量
                    for(int i = 0;i<size;i++){
                        Log.w("nnn","每条朋友圈的图片数量="+list.get(i).getN());
                        n[i] = list.get(i).getN();
                        a[i] = new String[list.get(i).getN()];
                        for(int j =0;j<list.get(i).getN();j++){
                            String temp[];
                            temp = list.get(i).getPicture().split(";");
                            System.out.println(temp[j]);
                            a[i][j] = temp[j];
                            Log.w("nnn","图片地址"+a[i][j]);
                        }
                    }
                    //Collections.reverse(list);
                    ListViewAdapter adapter = new ListViewAdapter(getContext(), list,0,user_name);
                    listView.setAdapter(adapter);

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


        iv_push = (FloatingActionButton) root.findViewById(R.id.iv_push);
        iv_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent0 = getActivity().getIntent();
                String name = intent0.getStringExtra("name");
                Intent intent = new Intent(getActivity(), PushSquareActivity.class);
                intent.putExtra("name",name);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return root;
    }


    public class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }
    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

}