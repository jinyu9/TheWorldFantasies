package com.example.worldtest.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.worldtest.R;

import java.text.SimpleDateFormat;

/**
 *
 * @author 自定义listview实现上拉刷新和下拉加载
 * 2018-08-25 刘明昆 q571039838
 *
 */
public class MyListViewUtils extends ListView implements AbsListView.OnScrollListener{

    private View bottomview; //尾文件
    private View headview; //头文件
    private int totaItemCounts;//用于表示是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private LoadListener loadListener; //接口回调
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int Yload;//位置
    boolean isLoading;//加载状态
    public static final int PULL_TO_REFRESH = 0;// 下拉刷新
    public static final int RELEASE_REFRESH = 1;// 释放刷新
    public static final int REFRESHING = 2; // 刷新中
    private int currentState = PULL_TO_REFRESH; // 当前刷新模式
    private TextView headtxt;//头文件textview显示加载文字
    private TextView headtime;//头文件textview显示加载时间
    private ProgressBar progressBar;//加载进度

    public MyListViewUtils(Context context) {
        super(context);
        Init(context);
    }

    public MyListViewUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public MyListViewUtils(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        //拿到头布局文件xml
        headview=LinearLayout.inflate(context, R.layout.head, null);
        headtxt=(TextView) headview.findViewById(R.id.headtxt);
        headtime=(TextView) headview.findViewById(R.id.timetxt);
        progressBar=(ProgressBar) headview.findViewById(R.id.headprogress);
        headtime.setText("上次更新时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        //拿到尾布局文件
        bottomview=LinearLayout.inflate(context, R.layout.bottom, null);
        //测量尾文件高度
        bottomview.measure(0,0);
        //拿到高度
        bottomHeight=bottomview.getMeasuredHeight();
        //隐藏view
        bottomview.setPadding(0, -bottomHeight, 0, 0);
        headview.measure(0, 0);
        headHeight=headview.getMeasuredHeight();
        headview.setPadding(0,-headHeight, 0, 0);
        //添加listview底部
        this.addFooterView(bottomview);
        //添加到listview头部
        this.addHeaderView(headview);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Yload=(int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY=(int) ev.getY();
                if(currentState == REFRESHING){
                    return super.onTouchEvent(ev);
                }
                float offset = moveY - Yload;
                // 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
                if(offset > 0 && getFirstVisiblePosition() == 0){
                    int paddingY = (int)(-headHeight + offset);
                    headview.setPadding(0,paddingY,0,0);
                    if(paddingY >=0 && currentState != RELEASE_REFRESH){
                        // 切换成释放刷新模式
                        currentState = RELEASE_REFRESH;
                        updateHeader(); // 根据最新的状态值更新头布局内容
                    } else if(paddingY < 0 && currentState != PULL_TO_REFRESH){ // 头布局不完全显示
                        // 切换成下拉刷新模式
                        currentState = PULL_TO_REFRESH;
                        updateHeader(); // 根据最新的状态值更新头布局内容
                    }
                return true; // 当前事件被我们处理并消费
                }
                break;
            case MotionEvent.ACTION_UP:
                // 根据刚刚设置状态
                if(currentState == PULL_TO_REFRESH){
                    headview.setPadding(0, -headHeight, 0, 0);
                }else if(currentState == RELEASE_REFRESH){
                    headview.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    updateHeader();
                }else{
                    //防止“下拉刷新”和“加载更多”一起出现：
                    if (currentState==REFRESHING && isLoading){
                        currentState=PULL_TO_REFRESH;
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 根据状态更新头布局内容
     */
    private void updateHeader() {
        switch (currentState) {
            case PULL_TO_REFRESH: // 切换回下拉刷新
                headtxt.setText("下拉刷新........");
                progressBar.setVisibility(View.GONE);
                break;
            case RELEASE_REFRESH: // 切换成释放刷新
                // 做动画, 改标题
                headtxt.setText("松开刷新........");
                progressBar.setVisibility(View.GONE);
                break;
            case REFRESHING: // 刷新中...
                headtxt.setText("正在刷新.......");
                progressBar.setVisibility(View.VISIBLE);
                loadListener.PullLoad();
                break;
            default:
                break;
        }
    }
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(totaItemCounts==lassVisible&&scrollState==SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading=true;
                bottomview.setPadding(0, 0, 0, 0);
                //加载数据
                loadListener.onLoad();
            }
        }
        Log.i("TGA", "firstVisible----"+firstVisible);
        Log.i("TGA", "状态？"+(firstVisible==0));

        if(firstVisible==0&&scrollState==SCROLL_STATE_IDLE){
            headview.setPadding(0, 0, 0, 0);
            headtxt.setText("正在刷新.......");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.PullLoad();
        }
    }


    //接口回调
    public interface LoadListener{
        void onLoad();
        void PullLoad();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.firstVisible=firstVisibleItem;
        this.lassVisible=firstVisibleItem+visibleItemCount;
        this.totaItemCounts=totalItemCount;
    }

    //加载完成
    public void loadComplete(){
        isLoading=false;
        bottomview.setPadding(0, -bottomHeight, 0, 0);
        headview.setPadding(0, -headHeight, 0, 0);
    }

    public void setInteface(LoadListener loadListener){
        this.loadListener=loadListener;
    }

}
