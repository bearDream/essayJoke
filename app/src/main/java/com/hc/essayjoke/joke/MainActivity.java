package com.hc.essayjoke.joke;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.example.administrator.framelibrary.BaseSkinActivity;
import com.hc.ExceptionCrashHandler;
import com.hc.baselibrary.ioc.CheckNet;
import com.hc.baselibrary.ioc.OnClick;
import com.hc.baselibrary.ioc.ViewById;
import com.hc.fixBug.FixDexManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import AsyncTask.NetAsyncTask;

public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.main_text)
    private TextView textView;

//    @ViewById(R.id.main_image)
//    private ImageView imageView;

    @ViewById(R.id.update_button)
    private Button mUpdateButton;

    @ViewById(R.id.main_progress)
    private ProgressBar mProgressBar;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        //将上次应用崩溃的信息上传至服务器
        crashGet();
        //使用ali提供的热修复技术
//        aliFix();
        fixDexBug();
    }

    //自己实现的热修复方式
    private void fixDexBug() {
        try {
            File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.dex");
            if (fixFile.exists()){
                FixDexManager fixDexManager = new FixDexManager(this);
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        textView.setText("IOC ...");
        ImageView image = (ImageView) findViewById(R.id.main_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TestActivity.class);
//                Toast.makeText(MainActivity.this,2 / 1 + " 有问题吗",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initTitle() {}

//    @OnClick(R.id.update_button)
//    private void onClick(){
//        NetAsyncTask asyncTask = new NetAsyncTask(textView, mProgressBar);
//        asyncTask.execute(1000);
//    }

    @OnClick(R.id.main_image)
    @CheckNet
    private void onClick(ImageView view) {
        Toast.makeText(this, "图片点击事件，", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.main_text)
    private void onClick(TextView textView) {
        Toast.makeText(this, "文字点击事件", Toast.LENGTH_LONG).show();
    }

    //ali提供的热修复技术
    private void aliFix(){
        //加载补丁包
        File patchFile = new File(Environment.getExternalStorageDirectory(),"patchFix.apatch");
        if(patchFile.exists()){
            try {
                BaseApplication.mPatchManager.addPatch(patchFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void crashGet(){
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if(crashFile.exists()){
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ( (len = read.read(buffer)) != -1 ){
                    String sb = new String(buffer,0,len);
                    Log.d("TAG",sb);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
