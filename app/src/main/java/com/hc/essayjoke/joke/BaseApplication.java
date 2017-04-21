package com.hc.essayjoke.joke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;
import com.hc.ExceptionCrashHandler;

/**
 * Created by soft01 on 2017/4/20.
 */

public class BaseApplication extends Application {

    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate","初始化application");
        ExceptionCrashHandler.getInstance().init(this);

        //获取版本号以及补丁包
        mPatchManager = new PatchManager(this);
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = null;
            info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            mPatchManager.init(version);//current version
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mPatchManager.loadPatch();
    }
}
