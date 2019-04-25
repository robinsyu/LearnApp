package com.example.yurs.learnapp.http;

import android.provider.SyncStateContract;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @ClassName: CommonParamsInterceptor
 * @Description: 添加公共参数拦截器
 * @Author: yurs
 * @Date: 2019/4/24 19:54
 */
public class CommonParamsInterceptor implements Interceptor {

    private Gson mGson = new Gson();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到request
        Request request = chain.request();

        //获取到方法
        String method = request.method();

        //公共参数hashmap
        try {
            //添加公共参数
            HashMap<String, Object> commomParamsMap = new HashMap<>();
            //commomParamsMap.put(Constants.IMEI, DeviceUtils.getIMEI(mContext));
            //commomParamsMap.put(Constants.MODEL, DeviceUtils.getModel());
            //commomParamsMap.put(Constants.LANGUAGE, DeviceUtils.getLanguage());
            //commomParamsMap.put(Constants.os, DeviceUtils.getBuildVersionIncremental());
            //commomParamsMap.put(Constants.RESOLUTION, DensityUtil.getScreenW(mContext) + "*" + DensityUtil.getScreenH(mContext));
            //commomParamsMap.put(Constants.SDK, DeviceUtils.getBuildVersionSDK() + "");
            //commomParamsMap.put(Constants.DENSITY_SCALE_FACTOR, mContext.getResources().getDisplayMetrics().density + "");

            //get请求的封装
            if (method.equals("GET")) {
                //获取到请求地址api
                HttpUrl httpUrlurl = request.url();
                HashMap<String, Object> rootMap = new HashMap<>();
                //通过请求地址(最初始的请求地址)获取到参数列表
                Set<String> parameterNames = httpUrlurl.queryParameterNames();
                for (String key : parameterNames) {  //循环参数列表
                    /*if (Constants.PARM.equals(key)) {  //判断是否有匹配的字段  这个类似于  /xxx/xxx?p={}  匹配这个p
                        String oldParamsJson = httpUrlurl.queryParameter(Constants.PARM);
                        if (oldParamsJson != null) {  //因为有的是没有这个p={"page"：0}  而是直接/xxx/index的
                            HashMap<String, Object> p = mGson.fromJson(oldParamsJson, HashMap.class);  //原始参数
                            if (p != null) {
                                for (Map.Entry<String, Object> entry : p.entrySet()) {
                                    rootMap.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    } else {
                        rootMap.put(key, httpUrlurl.queryParameter(key));
                    }*/
                }


                //把原来请求的和公共的参数进行组装
                rootMap.put("publicParams", commomParamsMap);  //重新组装
                String newJsonParams = mGson.toJson(rootMap);  //装换成json字符串
                String url = httpUrlurl.toString();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                //url = url + "?" + SyncStateContract.Constants.PARM + "=" + newJsonParams;  //拼接新的url
                request = request.newBuilder().url(url).build();  //重新构建请求
                //post请求的封装
            } else if (method.equals("POST")) {

//           FormBody.Builder builder = new FormBody.Builder();
//            builder.addEncoded("phone","phone");

                RequestBody requestBody = request.body();
                HashMap<String, Object> rootMap = new HashMap<>();
                if (requestBody instanceof FormBody) {
                    for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                        rootMap.put(((FormBody) requestBody).encodedName(i), ((FormBody) requestBody).encodedValue(i));
                    }
                } else {
                    //buffer流
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String oldParamsJson = buffer.readUtf8();
                    rootMap = mGson.fromJson(oldParamsJson, HashMap.class);  //原始参数
                    rootMap.put("publicParams", commomParamsMap);  //重新组装
                    String newJsonParams = mGson.toJson(rootMap);  //装换成json字符串
                    request = request.newBuilder().post(RequestBody.create(JSON, newJsonParams)).build();
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        //最后通过chain.proceed(request)进行返回
        return chain.proceed(request);
    }

}
