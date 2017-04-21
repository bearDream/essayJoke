package com.hc.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by soft01 on 2017/4/21.
 */

public class FixDexManager {

    private static final String TAG = "FixDexManager";
    private Context mContext;

    private File mDexDir;

    public FixDexManager(Context context){
        this.mContext = context;
        //获取应用可以访问的dex目录
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    /*
        修复dex包
     */
    public void fixDex(String fixFile) throws Exception {
        //1、获取正在运行的的dexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();

        Object applicationDexElement = getDexElementByClassCloader(applicationClassLoader);
        //2、获取下载好的dexElement

        File srcFile = new File(fixFile);
        if (!srcFile.exists()){
            throw new FileNotFoundException(fixFile);
        }


        File destFile = new File(mDexDir,srcFile.getName());
        if (destFile.exists()){
            Log.d(TAG,"patch["+fixFile+"has be loaded");
            return;
        }
        copyFile(srcFile,destFile);
        //3、classcloader读取dixDex路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        File optimizedDirectory = new File(mDexDir,"odex");
        if (!optimizedDirectory.exists()){
            optimizedDirectory.mkdirs();
        }

        //dex路径，optimizedDirectory解压路径，so库，父ClassLoader
        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//在应用目下的odex/.... dex文件
                    optimizedDirectory,
                    null,
                    applicationClassLoader
            );
            Object fixDexElement = getDexElementByClassCloader(fixClassLoader);
            //4、把补丁的dexElement 插到正在运行的dexElement的最前面
            applicationDexElement = combineArray(fixDexElement, applicationDexElement);
        }

        //把合并的数组注入到原来的applicationClassLoader中
        injectDexElement(applicationClassLoader, applicationDexElement);
    }

    //把合并的数组注入到原来的applicationClassLoader中
    private void injectDexElement(ClassLoader classLoader, Object dexElement) throws Exception {
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        //再获取pathList的dexElements
        Field dexElementsField = pathList .getClass().getField("dexElements");
        dexElementsField.setAccessible(true);

        dexElementsField.set(pathList, dexElement);
    }

    /*
           合并数组
     *     @param arrayLhs左边的（前面的数组）
     *     @param arrayRhs右边的（后面的数组）
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs){
        Class<?> loaderClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = Array.getLength(arrayRhs);
        //合并后的新数组
        Object result = Array.newInstance(loaderClass,j);
        for (int k = 0 ; k < j ; ++k){
            if (k < i)
                Array.set(result, k, Array.get(arrayLhs, k));
            else
                Array.set(result, k, Array.get(arrayRhs, k-i));
        }
        return result;
    }

    //复制文件
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if(!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = (new FileInputStream(src)).getChannel();
            outChannel = (new FileOutputStream(dest)).getChannel();
            inChannel.transferTo(0L, inChannel.size(), outChannel);
        } finally {
            if(inChannel != null) {
                inChannel.close();
            }
            if(outChannel != null) {
                outChannel.close();
            }
        }
    }


    /*
        根据classcloader获取DexElement
     */
    private Object getDexElementByClassCloader(ClassLoader classLoader) throws Exception {
        //先回去pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        //再获取pathList的dexElements
        Field dexElementsField = pathList .getClass().getField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }
}
