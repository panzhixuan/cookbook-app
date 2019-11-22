package com.example.cookbook.util;

import android.util.Log;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class RequestUtils {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String HOSTPORT = "http://192.168.43.243:8090"; //后端端口号已设置为8010无需修改，ip地址改成当前的ipv4地址

    private static OkHttpClient client = new OkHttpClient();

    // POST请求
    public static String post(String url, Map<String, Object> params) throws IOException {

        // 拼接url
        url = HOSTPORT + url;

        // 将请求参数变成JSON字符串
        JSONObject json = new JSONObject(params);
        String jsonString = json.toString();

        Log.d("request", "url: " + url + ", params: " + jsonString);

        // 创建请求体和请求对象
        RequestBody body = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 获取响应并返回
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String get(String url) throws IOException {
        // 拼接url
        url = HOSTPORT + url;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
