package com.hc.essayjoke.joke;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.framelibrary.BaseSkinActivity;
import com.hc.baselibrary.ioc.CheckNet;
import com.hc.baselibrary.ioc.OnClick;
import com.hc.baselibrary.ioc.ViewById;

import AsyncTask.NetAsyncTask;

public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.main_text)
    private TextView textView;

    @ViewById(R.id.main_image)
    private ImageView imageView;

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
        int i = 2/0;
    }

    @Override
    protected void initView() {
        textView.setText("IOC ...");
    }

    @Override
    protected void initTitle() {

    }

    @OnClick(R.id.update_button)
    private void onClick(){
        NetAsyncTask asyncTask = new NetAsyncTask(textView, mProgressBar);
        asyncTask.execute(1000);
    }

    @OnClick(R.id.main_image)
    @CheckNet
    private void onClick(ImageView view) {
        Toast.makeText(this, "图片点击事件，", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.main_text)
    private void onClick(TextView textView) {
        Toast.makeText(this, "文字点击事件", Toast.LENGTH_LONG).show();
    }
}
