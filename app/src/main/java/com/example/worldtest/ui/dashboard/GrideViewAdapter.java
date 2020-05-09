package com.example.worldtest.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.worldtest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

class GrideViewAdapter extends BaseAdapter {
    private Context context;
    private String [] picture;
    private DisplayImageOptions options;
    public  GrideViewAdapter (Context context, String [] picture){
        this.context = context;
        this.picture = picture;
    }
    @Override
    public int getCount() {
        return picture.length;
    }

    @Override
    public Object getItem(int position) {
        return picture[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        System.out.println("there goes the gridview!");
        View view = View.inflate(context, R.layout.item_nine_grid, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)               //设置图片uri为空或者是错位的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
                .build();                                                     //创建配置或的DisplayImageOption对象

        Log.w("ppp",picture[position]);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(picture[position], imageView, options);

        return view;
    }

}
