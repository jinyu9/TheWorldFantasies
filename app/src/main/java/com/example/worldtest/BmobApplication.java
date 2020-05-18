package com.example.worldtest;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.v3.Bmob;

public class BmobApplication extends Application {
    public static String APPID = "e1f541a4a1129508aace8369f5432292";

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(getBaseContext(), SpeechConstant.APPID + "=5ebce334");
        //提供以下两种方式进行初始化操作：
        //第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
		/*BmobConfig config =new BmobConfig.Builder(this)
		//设置appkey
		.setApplicationId(APPID)
		//请求超时时间（单位为秒）：默认15s
		.setConnectTimeout(30)
		//文件分片上传时每片的大小（单位字节），默认512*1024
		.setUploadBlockSize(1024*1024)
		//文件的过期时间(单位为秒)：默认1800s
		.setFileExpiration(5500)
		.build();
		Bmob.initialize(config);*/
        //第二：默认初始化
        Bmob.initialize(this,APPID);
        // 全局初始化此配置
        //初始化参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)                //线程优先级
                .denyCacheImageMultipleSizesInMemory()                   //当同一个url获取不用大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())  //将保存的时候的URI名称用MD5
                .tasksProcessingOrder(QueueProcessingType.FIFO)          //设置图片下载和显示的工作队列排序
                .writeDebugLogs()                                        //打印debug log
                .build();
        ImageLoader.getInstance().init(configuration);         // 创建配置过得DisplayImageOption对象
//		Bmob.resetDomain("http://open-vip.bmob.cn/8/");
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());


    }

}
