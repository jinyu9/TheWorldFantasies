package com.example.worldtest.ui.notifications;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.worldtest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class CollectionAdapter extends BaseAdapter {
    Context context;
    List<Collect> data;
    private DisplayImageOptions options;

    public CollectionAdapter(Context context, List<Collect> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_collection, null);
            holder = new ViewHolder();
            holder.collectImage = (ImageView) convertView.findViewById(R.id.collectImage);
            holder.collectText = (TextView) convertView.findViewById(R.id.collectText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        Collect collect=data.get(position);
        holder.collectText.setText(collect.getShow());

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)               //设置图片uri为空或者是错位的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
                .build();                                                     //创建配置或的DisplayImageOption对象

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(collect.getPath0(), holder.collectImage, options);

        return convertView;

    }



    public static class ViewHolder{
        TextView collectText;
        ImageView collectImage;

    }
}
