package com.example.worldtest.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.worldtest.R;
import com.example.worldtest.ui.dashboard.FindDiscover;
import com.example.worldtest.ui.home.Introduction;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.worldtest.Main2Activity.username;

public class MyCollection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        Bmob.initialize(getApplicationContext(),"e1f541a4a1129508aace8369f5432292");
        BmobQuery<Collect> bmobQuery = new BmobQuery<Collect>();
        bmobQuery.addWhereEqualTo("username",username);
        ListView listView = findViewById(R.id.collection_list);
        bmobQuery.findObjects(new FindListener<Collect>() {  //按行查询
            @Override
            public void done(List<Collect> list, BmobException e) {
                if (e == null) {
                    if(list.size()==0){
                        LinearLayout linearLayout=findViewById(R.id.collect);
                        LinearLayout linearLayout2=findViewById(R.id.listlayout);
                        System.out.println("done: 该用户没有收藏");
                        linearLayout.setGravity(Gravity.CENTER);
                        linearLayout.setPadding(0,100,0,0);
                        ImageView imageView=new ImageView(MyCollection.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000,800);
                        imageView.setLayoutParams(params);
                        linearLayout2.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.no_infor);
                        linearLayout.addView(imageView);
                    }else {
                        CollectionAdapter collectionAdapter =new CollectionAdapter(getBaseContext(),list);
                        collectionAdapter.notifyDataSetChanged();
                        listView.setAdapter(collectionAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent();
                                intent.setClass(getBaseContext(), Introduction.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("textId", list.get(position).getAttractionId());
                                bundle.putString("path0",list.get(position).getPath0());
                                bundle.putString("show",list.get(position).getShow());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }

                }
            }
        });

    }
}
