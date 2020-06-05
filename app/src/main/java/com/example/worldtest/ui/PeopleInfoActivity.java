package com.example.worldtest.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;
import com.example.worldtest.ui.Report.Autogragh;
import com.example.worldtest.ui.Report.ReportUserActivity;
import com.example.worldtest.ui.dashboard.ListViewAdapter;
import com.example.worldtest.ui.dashboard.Moment;
import com.example.worldtest.ui.im.IMActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class PeopleInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView user_name;
    private TextView word;
    private Button report;
    private Button talk;
    private ImageView avatar;
    private TextView nothing;
    private TextView showmoment;
    private String name;
    private String auto;
    public static int size;
    public static String[][] a;
    public static int[] n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_info);
        addActivity(this);
        user_name = findViewById(R.id.name);
        report = findViewById(R.id.report);
        //talk = findViewById(R.id.talk);
        avatar = findViewById(R.id.avatar);
        nothing = findViewById(R.id.nothing);
        showmoment = findViewById(R.id.showmoment);
        word = findViewById(R.id.word);
        nothing.setVisibility(View.GONE);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.siyuansongti);
        showmoment.setTypeface(typeface);
        Intent intent = this.getIntent();
        name = intent.getStringExtra("user_name");
        user_name.setText(name);
        if(name.equals(Main2Activity.username)){
            showmoment.setText("我的动态");
    }
        BmobQuery<Autogragh> bmobQuery0 = new BmobQuery<Autogragh>();
        bmobQuery0.addWhereEqualTo("name", name);
        bmobQuery0.findObjects(new FindListener<Autogragh>() {
            @Override
            public void done(List<Autogragh> list, BmobException e) {
                if(e == null){
                    if(list.size() > 0){
                    auto = list.get(0).getWord();
                    word.setText(auto);}
                    else{
                        word.setText("TA很神秘，没有留下个性签名噢。");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    word.setText("出错了淦。");
                }
            }
        });



        BmobQuery<Moment> bmobQuery = new BmobQuery<Moment>();
        bmobQuery.addWhereEqualTo("user_name", name);
        ListView listView = findViewById(R.id.listview);
        bmobQuery.findObjects(new FindListener<Moment>() {  //按行查询
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    if(list.size()==0){
                        nothing.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }else {
                        //数据倒序显示,最新的数据在最上面
                        List<Moment> moments = new ArrayList<>(list);
                        //总共有多少条动态k
                        size = moments.size();
                        Log.w("nnn","总共有多少条朋友圈="+size);
                        a = new String[size][];
                        n = new int[size];

                        //每条动态的图片数量
                        for(int i = 0;i<size;i++) {
                            Log.w("nnn", "每条朋友圈的图片数量=" + moments.get(i).getN());
                            n[i] = moments.get(i).getN();
                            a[i] = new String[moments.get(i).getN()];
                            for (int j = 0; j < moments.get(i).getN(); j++) {
                                String temp[];
                                temp = moments.get(i).getPicture().split(";");
                                System.out.println(temp[j]);
                                a[i][j] = temp[j];
                                Log.w("nnn", "图片地址" + a[i][j]);
                            }
                        }
                        //Collections.reverse(list);

                        ListViewAdapter adapter = new ListViewAdapter(getBaseContext(), moments,2,Main2Activity.username);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                }
                else{
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.report) {
            if (Main2Activity.accountState == 1) {
                Toast.makeText(PeopleInfoActivity.this, "您的账号处于冻结状态，无法举报用户！", Toast.LENGTH_SHORT).show();
            } else {
                if (!name.equals(Main2Activity.username)) {
                    Intent intent = new Intent(getApplicationContext(), ReportUserActivity.class);
                    intent.putExtra("report_name", name);
                    startActivity(intent);
                    finish();
                } else if (name.equals(Main2Activity.username)) {
                    Toast.makeText(PeopleInfoActivity.this, "您不能举报自己！", Toast.LENGTH_SHORT).show();
                }
            }
        }
        /*if(view.getId() == R.id.talk) {
            if(name.equals(Main2Activity.username)) {
                Toast.makeText(PeopleInfoActivity.this, "您无法私信自己！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), IMActivity.class);
                intent.putExtra("user_name", name);
                startActivity(intent);
                finish();
            }
        }*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
