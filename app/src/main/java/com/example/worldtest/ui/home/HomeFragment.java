package com.example.worldtest.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.worldtest.R;
import com.example.worldtest.ui.notifications.Collect;
import com.example.worldtest.ui.notifications.CollectionAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeFragment extends Fragment implements
        MyListViewUtils.LoadListener {


    private HomeViewModel homeViewModel;
    private ProgressDialog progressDialog;
    private RecognizerDialog iatDialog;
    private List<Collect> collects=new ArrayList<>();
    private CollectionAdapter collectionAdapter;
    private MyListViewUtils listViewUtils;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Exception();
        showProgressDialog("提示", "正在加载......");

        final LinearLayout linearLayout=root.findViewById(R.id.line1);
        listViewUtils = (MyListViewUtils) root.findViewById(R.id.attract_list);
        listViewUtils.setInteface(this);
        send();

        collectionAdapter =new CollectionAdapter(getContext(),collects);
        collectionAdapter.notifyDataSetChanged();
        listViewUtils.setAdapter(collectionAdapter);
        listViewUtils.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), Introduction.class);
                Bundle bundle = new Bundle();
                //System.out.println("Id"+collects.get(position-1).getAttractionId()+"name"+collects.get(position).getChinaName());
                bundle.putString("textId", collects.get(position-1).getAttractionId());
                bundle.putString("path0", collects.get(position-1).getPath0());
                bundle.putString("chinaName", collects.get(position-1).getChinaName());
                bundle.putString("englishName", collects.get(position-1).getEnglishName());
                bundle.putString("briefInfo", collects.get(position-1).getBriefInfor());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final EditText editText=root.findViewById(R.id.text);
        final ImageView voiceRecognizer = root.findViewById(R.id.voiceRecognizer);
        voiceRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iatDialog = new RecognizerDialog(getActivity(), mInitListener);

                iatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
                iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                iatDialog.setParameter(SpeechConstant.VAD_BOS, "4500");
                iatDialog.setParameter(SpeechConstant.VAD_EOS, "1500");
                iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
                iatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED) {
                    iatDialog.setListener(new RecognizerDialogListener() {
                        String resultJson = "[";//放置在外边做类的变量则报错，会造成json格式不对（？）

                        @Override
                        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                            System.out.println("-----------------   onResult   -----------------");
                            if (!isLast) {
                                resultJson += recognizerResult.getResultString() + ",";
                            } else {
                                resultJson += recognizerResult.getResultString() + "]";
                            }
                            if (isLast) {
                                //解析语音识别后返回的json格式的结果
                                Gson gson = new Gson();
                                List<DictationResult> resultList = gson.fromJson(resultJson,
                                        new TypeToken<List<DictationResult>>() {
                                        }.getType());
                                String result = "";
                                for (int i = 0; i < resultList.size() - 1; i++) {
                                    result += resultList.get(i).toString();
                                }
                                editText.setText(result);
                                editText.requestFocus();
                                editText.setSelection(result.length());
                            }
                        }

                        @Override
                        public void onError(SpeechError speechError) {
                            speechError.getPlainDescription(true);
                        }
                    });
                    iatDialog.show();
                    TextView recorderTextView = (TextView)iatDialog.getWindow().getDecorView().findViewWithTag("textlink");
                    recorderTextView.setText("请说出您想查询的景点关键词");
                }else{
                    Intent intent = new Intent(getActivity(),PermissionActivity.class);
                    startActivity(intent);
                }
            }
        });
        //查找按钮
        final  Button b = root.findViewById(R.id.find);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textname=editText.getText().toString().trim();
                Intent intent=new Intent(getActivity(), find.class);
                Bundle bundle = new Bundle();
                bundle.putString("textname", textname);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());

        return root;
    }
    // 实现PullLoad接口
    @Override
    public void PullLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 这里处理请求返回的结果（这里使用模拟数据）
                collects.clear();
                send();
                // 更新数据
                collectionAdapter.notifyDataSetChanged();
                // 加载完毕
                listViewUtils.loadComplete();
            }
        }, 3000);

    }

    // 实现onLoad接口
    @Override
    public void onLoad() {
        // 设置延时三秒获取时局，用于显示加载效果
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 这里处理请求返回的结果（这里使用模拟数据）
                // 更新数据
                send();
                collectionAdapter.notifyDataSetChanged();
                // 加载完毕
                listViewUtils.loadComplete();
            }
        }, 3000);
    }


    private void send() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://47.100.139.135:8080/TestLink/RandServlet");
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
                    String line;
                    while ((line = reader.readLine()) != null) {
                        show(line);
                    }

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
                    progressDialog.dismiss();
                }
            }
        }).start();

    }

    private void show(final String line) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\t|\r|\n";
                String regTag = "<[^>]*>";
                final String text = line.replaceAll(regFormat, "").replaceAll(regTag, "");
               // System.out.println(text);
                if (text.equals("")||text.trim().length()==0) {
                } else {
                    String [] spString = text.split("&nbsp;&nbsp;&nbsp;");
                      //  System.out.println(spString.length);
                        Collect collect=new Collect();
                        String [] attr= new String[4];
                        for(int i=0;i<spString.length;i++){
                            attr[i]=spString[i];
                        }
                        String regEx1 = "[\\u4e00-\\u9fa5]";
                        String chineName;
                        if(attr[1]==null||attr[1].equals("")){
                            collect.setChinaName(attr[1]);
                            collect.setEnglishName("");
                        }else{
                            chineName = matchResult(Pattern.compile(regEx1), attr[1]);
                            String EnglishName = attr[1].replace(chineName, "");
                            collect.setChinaName(chineName);
                            collect.setEnglishName(EnglishName);
                        }
                        collect.setBriefInfor(attr[2]);
                        collect.setAttractionId(attr[0]);
                        collect.setPath0(attr[3]);
                        //System.out.println("ID"+collect.getAttractionId());
                        collects.add(collect);
                        //System.out.println("1:"+collects.get(0).getAttractionId());
                        collectionAdapter.notifyDataSetChanged();
                    }
                }


        });
    }
    public static String matchResult(Pattern p,String str)
    {
        StringBuilder sb = new StringBuilder();
        Matcher m = p.matcher(str);
        while (m.find())
            for (int i = 0; i <= m.groupCount(); i++)
            {
                sb.append(m.group());
            }
        return sb.toString();
    }
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new  HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * 提示加载
     */
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(getActivity(),
                    title, message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }
    public static final String TAG = "HomeFragment";
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getActivity(), "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void Exception(){
        //避免出现android.os.NetworkOnMainThreadException异常
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
    }
}