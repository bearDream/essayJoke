package com.hc.baseactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hc.baselibrary.ioc.ViewUtils;

/**
 * Created by soft01 on 2017/4/19.
 * Description:所有activity的模版
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView();

        //一些特定通用的方法，也可以封装在这里
        ViewUtils.inject(this);
        //初始化头部
        initTitle();
        //初始化界面
        initView();
        //初始化数据
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView();

    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    /*
        返回的是findViewById
     */
    protected <T extends View> T viewById(int viewId){
        return (T) findViewById(viewId);
    }
}
