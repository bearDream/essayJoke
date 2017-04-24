package com.hc.http;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by soft01 on 2017/4/24.
 * 邮箱：450848477@qq.com
 * Version:1.0
 * Description: okHttp默认引擎
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EnginCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        Log.e("GET 请求地址：",url);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .tag(context);
        Request request = requestBuilder.build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().toString();
                        Log.e("GET 返回结果",data);
                        callBack.onSuccess(data);
                    }
                }
        );
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EnginCallBack callBack) {
        //Log打印请求地址
        final String jointURL = HttpUtils.jointParams(url, params);
        Log.e("Post 请求地址：",jointURL);

        RequestBody requestBody = appendBody(params);
        final Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override  
                    public void onFailure(Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().toString();
                        Log.e("Post 返回结果",data);
                        callBack.onSuccess(data);
                    }
                }
        );
    }



    //组装post请求的body体
    protected RequestBody appendBody(Map<String, Object> params){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()){
            for (String key : params.keySet()){
                builder.addFormDataPart(key, params.get(key)+"");
                Object value = params.get(key);
                //判断是上传文件还是list
                if (value instanceof File){
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(),
                            RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                }else if (value instanceof List){
                    try {
                        List<File> lisFile = (List<File>) value;
                        for (int i = 0; i < lisFile.size(); i++) {
                            //获取list中单个文件
                            File file = lisFile.get(i);
                            builder.addFormDataPart(key + i, file.getName(),
                                    RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //猜测文件类型
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor (path);
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
