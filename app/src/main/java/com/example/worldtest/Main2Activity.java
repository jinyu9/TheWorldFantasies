
package com.example.worldtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.worldtest.ui.Report.Report_User;
import com.example.worldtest.ui.dashboard.DashboardFragment;
import com.example.worldtest.ui.home.HomeFragment;
import com.example.worldtest.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView nav_bottom;
    private List<Fragment> mFragments;

    private int lastPosition;//上次fragment的位置
    private Fragment currentFragment;//要显示的Fragment
    private Fragment hideFragment;//要隐藏的Fragment
    public static String username;
    public static int accountState;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("last_position",lastPosition);//activity重建时保存页面的位置
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastPosition = savedInstanceState.getInt("last_position");//获取重建时的fragment的位置
        setSelectedFragment(lastPosition);//恢复销毁前显示的fragment
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActivity(this);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("name");
        BmobQuery<Report_User> bmobQuery1 = new BmobQuery<Report_User>();
        bmobQuery1.addWhereEqualTo("user1", username);
        BmobQuery<Report_User> bmobQuery2 = new BmobQuery<Report_User>();
        bmobQuery2.addWhereEqualTo("state", 1);
        List<BmobQuery<Report_User>> bmobQuery = new ArrayList<BmobQuery<Report_User>>();
        bmobQuery.add(bmobQuery1);
        bmobQuery.add(bmobQuery2);
        BmobQuery<Report_User> MainQuery = new BmobQuery<Report_User>();
        MainQuery.and(bmobQuery);
        MainQuery.findObjects(new FindListener<Report_User>() {
            @Override
            public void done(List<Report_User> list, BmobException e) {
                if(list == null){
                    e.printStackTrace();
                }else {
                    if (list.size() == 0) {
                        accountState = 0;
                    } else {
                        accountState = 1;
                    }
                }
            }
        });
        initData();
        initView(savedInstanceState);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(DashboardFragment.newInstance());
        mFragments.add(NotificationsFragment.newInstance());
    }

    private void initView(Bundle savedInstanceState) {
        nav_bottom = (BottomNavigationView) findViewById(R.id.nav_view);
        if (savedInstanceState == null){
            //根据传入的Bundle对象判断是正常启动还是重建 true表示正常启动，false表示重建
            setSelectedFragment(0);
        }
        nav_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        setSelectedFragment(0);
                        break;
                    case R.id.navigation_dashboard:
                        setSelectedFragment(1);
                        break;
                    case R.id.navigation_notifications:
                        setSelectedFragment(2);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 根据位置选择Fragment
     * @param position 要选中的fragment的位置
     */
    private void  setSelectedFragment(int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        currentFragment = fragmentManager.findFragmentByTag("fragment"+position);//要显示的fragment(解决了activity重建时新建实例的问题)
        hideFragment = fragmentManager.findFragmentByTag("fragment" + lastPosition);//要隐藏的fragment(解决了activity重建时新建实例的问题)
        if (position != lastPosition){//如果位置不同
            if (hideFragment != null){//如果要隐藏的fragment存在，则隐藏
                transaction.hide(hideFragment);
            }
            if (currentFragment == null){//如果要显示的fragment不存在，则新加并提交事务
                currentFragment = mFragments.get(position);
                transaction.add(R.id.nav_host_fragment,currentFragment,"fragment"+position);
            }else {//如果要显示的存在则直接显示
                transaction.show(currentFragment);
            }
        }

        if (position == lastPosition){//如果位置相同
            if (currentFragment == null){//如果fragment不存在(第一次启动应用的时候)
                currentFragment = mFragments.get(position);
                transaction.add(R.id.nav_host_fragment,currentFragment,"fragment"+position);
            }//如果位置相同，且fragment存在，则不作任何操作
        }
        transaction.commit();//提交事务
        lastPosition = position;//更新要隐藏的fragment的位置
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
