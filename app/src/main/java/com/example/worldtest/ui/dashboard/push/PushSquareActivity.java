package com.example.worldtest.ui.dashboard.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.worldtest.R;
import com.example.worldtest.ui.dashboard.Moment;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PushSquareActivity extends AppCompatActivity{
    //输入框
    private EditText et_content;
    //文字数量
    private TextView tv_content_size;
    //相机
    private LinearLayout ll_ablum;
    //相册
    public static final int ALBUM_REQUEST_CODE = 1005;
    //要上传的文件
    private File uploadFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_square);

        Bmob.initialize(this, "e1f541a4a1129508aace8369f5432292");

        et_content = (EditText) findViewById(R.id.et_content);
        tv_content_size = (TextView) findViewById(R.id.tv_content_size);
        //输入框监听
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_content_size.setText(s.length() + "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ll_ablum = (LinearLayout) findViewById(R.id.ll_ablum);
        ll_ablum.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toAlbum();
            }
        });
    }

    private void toAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            Uri uri = data.getData();
//            String path = FileHelper.getInstance().
//                    getRealPathFromURI(PushSquareActivity.this, uri);
            String path = getRealPathFromURI(PushSquareActivity.this, uri);

            if (!TextUtils.isEmpty(path)) {
                uploadFile = new File(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 通过Uri去系统查询真实地址
     *
     * @param uri
     */
    public String getRealPathFromURI(Context mContext, Uri uri) {
        String realPath = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(mContext, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    realPath = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_input:
                inputSquare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void inputSquare() {
//        //上传文件
//        BmobFile bmobFile = new BmobFile(uploadFile);
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    Toast.makeText(PushSquareActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(PushSquareActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
    /**
     * 发布到广场
     */
    private void inputSquare() {
        final String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content) && uploadFile == null) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uploadFile != null) {
            //上传文件
            BmobFile bmobFile = new BmobFile(uploadFile); //uploadFile = new File(path);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
//                        push(content, bmobFile.getFileUrl());
                        Toast.makeText(PushSquareActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(PushSquareActivity.this, uploadFile.toString()+"   "+bmobFile.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(PushSquareActivity.this, uploadFile.toString()+" "+bmobFile.toString()+" "+e.toString(), Toast.LENGTH_SHORT).show();
//                        LogUtils.e(e.toString()+" "+bmobFile.getFilename());
                    }
                }
            });
        } else {
            push(content, "");
        }
    }

    /**
     * 发布广场
     *
     *
     * @param text      文本
     * @param path      路径
     */
    public void pushSquare(String text, String path, SaveListener<String> listener) {
        Moment moment = new Moment();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        moment.setUser_name(name);
        moment.setContent(text);
        moment.setPicture(path);
        moment.save(listener);

    }

    /**
     * 发表
     *
     * @param content
     * @param path
     */
    private void push(String content, String path) {
        //纯文本
        pushSquare(content, path, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(PushSquareActivity.this, "发布失败" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
