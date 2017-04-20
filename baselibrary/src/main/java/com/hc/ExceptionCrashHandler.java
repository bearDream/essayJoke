package com.hc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by soft01 on 2017/4/20.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;

    private Context context;

    private static final String TAG = "ExceptionCrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultUncaughtHandler;

    public static ExceptionCrashHandler getInstance(){
        if (mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null)
                    mInstance = new ExceptionCrashHandler();
            };
        }

        return mInstance;
    }

    public void init(Context context){
        this.context = context;
        //设置全局异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultUncaughtHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG,"出错了");

        //获取信息
        //  崩溃信息
        //  手机信息
        //  版本信息
        //写入文件

        String crashFileName = saveInfoToSD(e);

        //缓存崩溃的日志文件
        cacheCrashFile(crashFileName);

        mDefaultUncaughtHandler.uncaughtException(t,e);
    }

    //缓存崩溃日志文件
    private void cacheCrashFile(String crashFileName) {
        SharedPreferences sp = context.getSharedPreferences("crash",Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME",crashFileName).commit();
    }

    public File getCrashFile() {
        String crashFileName = context.getSharedPreferences("crash",Context.MODE_PRIVATE).getString("CRASH_FILE_NAME","");
        return new File(crashFileName);
    }

    //将所有信息写入sd卡中
    private String saveInfoToSD(Throwable e) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        // 手机信息 + 应用信息
        for (Map.Entry<String,String> entry: obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        //崩溃的详细信息
        sb.append(obtainException(e));

        //保存文件  只能拿手机应用目录，不可以去拿sd卡目录，如果那sd卡目录则需要动态申请权限
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(context.getFilesDir() + File.separator + "crash" + File.separator);

            //删除之前的异常信息
            if(dir.exists()){
                deleteDir(dir);
            }

            if(!dir.exists())
                dir.mkdir();

            try {
                fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm")+".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        return "";
    }

    //格式化日期
    private String getAssignTime(String dateFormatStr) {
        DateFormat date = new SimpleDateFormat(dateFormatStr);
        return date.format(new Date());
    }

    //获取一些手机的系统信息
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String,String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo =mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName",mPackageInfo.versionName);
        map.put("versionCode",""+mPackageInfo.versionCode);
        map.put("MODEL", ""+Build.MODEL);
        map.put("SDK_INT", ""+Build.VERSION.SDK_INT);
        map.put("PRODUCT",""+Build.PRODUCT);
        map.put("MODEL_INFO",""+getMobileInfo());
        return map;
    }

    //获取系统未捕捉的错误信息
    private String obtainException(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace();
        pw.close();
        return sw.toString();
    }

    //获取手机信息
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        Field[] fields = Build.class.getFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private boolean deleteDir(File dir){
        if(dir.isDirectory()){
            File[] children  = dir.listFiles();
            for (File child : children) {
                child.delete();
            }
        }
        return true;
    }
}
