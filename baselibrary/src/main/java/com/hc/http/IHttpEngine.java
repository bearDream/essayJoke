package com.hc.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soft01 on 2017/4/24.
 * 邮箱：450848477@qq.com
 * Version:1.0
 * Description: http引擎规范
 */

public interface IHttpEngine {

    //Post请求
    void get(Context context, String url, Map<String,Object> params, EnginCallBack callBack);
    //Get请求
    void post(Context context, String url, Map<String,Object> params, EnginCallBack callBack);
    //上传文件

    //下载文件

    //https  添加证书
}
