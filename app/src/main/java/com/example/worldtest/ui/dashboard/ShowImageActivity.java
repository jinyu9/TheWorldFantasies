package com.example.worldtest.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.worldtest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity {
    public List<View> listViews = new ArrayList<>();
    private DisplayImageOptions options;
    private AdapterViewPager adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Image image  = (Image)getIntent().getParcelableExtra("data");
        int id = image.getId();
        List<String> imageurl = image.getImageurl();
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        adapterViewPager = new AdapterViewPager(this,listViews);
        adapterViewPager.notifyDataSetChanged();
        vp.setAdapter(adapterViewPager);
        //vp.setCurrentItem(ListViewAdapter.init);
        adapterViewPager.notifyDataSetChanged();
        for (int i = 0; i < imageurl.size() ; i++) {  //for循环将试图添加到list中
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
            ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id

//            Log.w("BBB",image.getId(i));
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.ic_launcher)               //设置图片uri为空或者是错位的时候显示的图片
                    .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
                    .build();                                                     //创建配置或的DisplayImageOption对象

            //设置当前点击的图片
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageurl.get(i), iv, options);
            adapterViewPager.notifyDataSetChanged();

            listViews.add(view);
            adapterViewPager.notifyDataSetChanged();
        }
        vp.setCurrentItem(ListViewAdapter.init);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapterViewPager.notifyDataSetChanged();
    }
}
