
package com.example.worldtest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.worldtest.ui.Report.Report_User;
import com.example.worldtest.ui.dashboard.DashboardFragment;
import com.example.worldtest.ui.home.HomeFragment;
import com.example.worldtest.ui.im.bmob.BmobManager;
import com.example.worldtest.ui.im.entity.Constants;
import com.example.worldtest.ui.im.manager.HttpManager;
import com.example.worldtest.ui.im.service.CloudService;
import com.example.worldtest.ui.im.utils.SpUtils;
import com.example.worldtest.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    private Disposable disposable;

    public static String sex;
    public static String number;

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

        send();

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
                if(list.size()==0){
                    accountState = 0;
                }else{
                    accountState = 1;
                }
            }
        });
        initData();
        initView(savedInstanceState);
    }

    private void send() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://47.100.139.135:8080/TheWorldFantasies/GetUserByNameServlet?name="+username);
                    System.out.println(url);
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);

                    //返回输入流
                    InputStream in = connection.getInputStream();

                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String result1 = result.toString();
                    String regix = "\\s*|\t|\r|\n";
                    String regTag = "<[^>]*>";
                    String text = result1.replaceAll(regix, "").replaceAll(regTag, "");
                    show(text.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {//关闭连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void show(String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                name = result.substring(result.indexOf("名=")+2,result.indexOf(","));
//                password = result.substring(result.indexOf("码")+2,result.indexOf("性")-1);
                sex = result.substring(result.indexOf("别")+2,result.indexOf("手")-1);
                number = result.substring(result.indexOf("号")+2,result.indexOf("]"));
//                Log.i("worldtest","sex="+sex+";number="+number);
//                MTVInfoUser.setText("用户名："+name);
//                MTVInfosex.setText("性    别："+sex);
//                MTVInfonum.setText("手机号："+number);
//                System.out.print(result);
//                System.out.println("name="+name);
//                System.out.println("password="+password);
//                System.out.println("sex="+sex);
//                System.out.println("number="+number);
            }
        });

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
        //检查TOKEN
        //checkToken();
    }

    /**
     * 检查TOKEN
     */
    private void checkToken() {
        Log.i("worldtest", "checkToken");
        SpUtils.getInstance().initSp(this);
        //获取TOKEN 需要三个参数 1.用户ID 2.头像地址 3.昵称
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            Log.i("worldtest", "startCloudService");
            startService(new Intent(this, CloudService.class));
        } else {
            //1.有三个参数
//            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
            String tokenName = BmobManager.getInstance().getUser().getTokenNickName();
            if (!TextUtils.isEmpty(tokenName)) {
                //创建Token
                createToken();
            }
        }
    }

    private void createToken() {
        Log.i("worldtest" ,"createToken");
        /**
         * 1.去融云后台获取Token
         * 2.连接融云
         */
        final HashMap<String, String> map = new HashMap<>();
        map.put("userId", BmobManager.getInstance().getUser().getObjectId());
        map.put("name", BmobManager.getInstance().getUser().getTokenNickName());
//        map.put("portraitUri", BmobManager.getInstance().getUser().getTokenPhoto());

        //通过OkHttp请求Token
        disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //执行请求过程
                String json = HttpManager.getInstance().postCloudToken(map);
                Log.i("worldtest" ,"json:" + json);
                emitter.onNext(json);
                emitter.onComplete();
            }
            //线程调度
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i("worldtest", "s:"+s);
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
