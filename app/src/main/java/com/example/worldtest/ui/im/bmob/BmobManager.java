package com.example.worldtest.ui.im.bmob;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class BmobManager {
    private static final String BMOB_SDK_ID = "e1f541a4a1129508aace8369f5432292";
    private static final String BMOB_NEW_DOMAIN = "http://sdk.ntutn.top/8/";

    private volatile static BmobManager mInstance = null;

    private BmobManager() {

    }

    public static BmobManager getInstance() {
        if (mInstance == null) {
            synchronized (BmobManager.class) {
                if (mInstance == null) {
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Bmob
     *
     * @param mContext
     */
    public void initBmob(Context mContext) {
        //如果Bmob绑定独立域名，则需要在初始化之前重置
        Bmob.resetDomain(BMOB_NEW_DOMAIN);
        Bmob.initialize(mContext, BMOB_SDK_ID);
    }

    /**
     * 获取本地对象
     *
     * @return
     */
    public IMUser getUser() {
        return BmobUser.getCurrentUser(IMUser.class);
    }
}
