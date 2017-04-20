package com.hc.essayjoke.joke;

import android.app.Application;
import android.util.Log;
import android.util.LogPrinter;

import com.hc.ExceptionCrashHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by soft01 on 2017/4/20.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate","初始化application");
        ExceptionCrashHandler.getInstance().init(this);
    }
}
