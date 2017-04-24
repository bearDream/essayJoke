package com.hc.http;

import android.content.Context;
import android.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soft01 on 2017/4/24.
 * 邮箱：450848477@qq.com
 * Version:1.0
 * Description: 自己实现的http框架
 */

public class HttpUtils{

    //URL
    private String mUrl;
    //请求方式
    private int mType = GET_TYPE;
    public static final int POST_TYPE = 0X0011;
    public static final int GET_TYPE = 0X0012;

    private Map<String, Object> mParams;

    private Context mContext;
    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }
    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }

    //post提交
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    //get提交
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    //添加参数
    public HttpUtils addParam(String key, Object val){
        mParams.put(key,val);
        return this;
    }
    public HttpUtils addParams(Map<String, Object> params){
        mParams.putAll(params);
        return this;
    }

    //添加回调
    public void execute(EnginCallBack callBack){
        if (callBack == null){
            callBack = EnginCallBack.DEFAULT_CALL_BACK;
        }

        if (mType == POST_TYPE){
            post(mContext, mUrl, mParams, callBack);
        }
        if (mType == GET_TYPE){
            get(mContext, mUrl, mParams, callBack);
        }

    }

    public void execute(){
        execute(null);
    }
    //默认使用OkHttpEngine
    private static IHttpEngine mhttpEngine = new OkHttpEngine();

    //初始化引擎
    public static void init(IHttpEngine httpEngine){
        mhttpEngine = httpEngine;
    }

    public void exchangeEngine(IHttpEngine httpEngine){
        mhttpEngine = httpEngine;
    }

    private void get(Context context, String url, Map<String, Object> params, EnginCallBack callBack) {
        mhttpEngine.get(context, url, params, callBack);
    }

    private void post(Context context, String url, Map<String, Object> params, EnginCallBack callBack) {
        mhttpEngine.post(context, url, params, callBack);
    }

    //拼接参数
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() < 0){
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")){
            sb.append("?");
        }else if (!url.endsWith("&")){
            sb.append("&");
        }

        for (Map.Entry<String, Object> entry : params.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        sb.deleteCharAt(sb.length() - 1);

        System.out.println(sb.toString());

        return sb.toString();
    }

}
