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
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.worldtest.R;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;
    private ProgressDialog progressDialog;
    private RecognizerDialog iatDialog;


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Exception();
        showProgressDialog("提示", "正在加载......");

        final LinearLayout linearLayout=root.findViewById(R.id.line1);

        send(linearLayout);

        //下滑触发
        final ScrollView scrollView=root.findViewById(R.id.scrollView3);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (v.getScrollY() <= 0) {
                        } else if (scrollView.getChildAt(0).getMeasuredHeight() <= v.getHeight() + v.getScrollY()) {
                            showProgressDialog("提示", "正在加载......");
                            send(linearLayout);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }});
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

    private void send(final LinearLayout linearLayout) {
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
                        show(line.toString(),linearLayout);
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

    private void show(final String line, final LinearLayout linearLayout) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\t|\r|\n";
                String regTag = "<[^>]*>";
                final String text = line.replaceAll(regFormat, "").replaceAll(regTag, "");
                if (text.equals("")||text.trim().length()==0) {
                } else {
                    String [] spString = text.split("&nbsp;&nbsp;&nbsp;");
                    if(spString.length<3){}else {
                      //  System.out.println(spString.length);
                        String id = spString[0];
                        String name = spString[1];
                        String brief_infor = spString[2];
                        String regEx1 = "[\\u4e00-\\u9fa5]";
                        String chineName = matchResult(Pattern.compile(regEx1), name);
                        String EnglishName = name.replace(chineName, "");
                        String show = chineName + "\n" + EnglishName + "\n" + "简介：" + brief_infor;
                        send1(id, show, linearLayout);
                    }
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


    private void send1(final String id,final String show, final LinearLayout linearLayout) {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection  connection = null;
                BufferedReader reader=null;
                try {
                    String path = URLEncoder.encode(URLEncoder.encode(id, "utf-8"), "utf-8");
                    URL url1 = new URL("http://47.100.139.135:8080/TestLink/ImageServlet?id=" + path);
                  //  System.out.println(url1);
                    connection = (HttpURLConnection) url1.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //返回输入流
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    show1(id,show,result.toString(),linearLayout);
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

    private void show1(final String id,final String show,final String result,final LinearLayout linearLayout) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String regFormat = "\\s*|\t|\r|\n";
                String regTag = "<[^>]*>";
                String text = result.replaceAll(regFormat, "").replaceAll(regTag, "");
                try {
                    Bitmap bitmap;
                    byte[] data;
                    Drawable drawable;
                    data = GetUserHead(text);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    drawable = new BitmapDrawable(getResources(),bitmap);
                    drawable.setBounds(4,0,980,600);

                    Button button = new Button(getActivity());
                    button.setCompoundDrawables(null,drawable,null,null);
                    button.setCompoundDrawablePadding(5);
                    button.setText(show);
                    button.setHint(id);
                    linearLayout.addView(button);
                    button.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), Introduction.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("textId", id);
                            bundle.putString("path0",text);
                            bundle.putString("show",show);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                }
            }
        });
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new  HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static byte[] GetUserHead(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = StreamTool.readInputStream(inputStream); // 获得图片的二进制数据
        return data;

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