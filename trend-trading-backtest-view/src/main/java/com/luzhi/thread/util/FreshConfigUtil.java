package com.luzhi.thread.util;


import cn.hutool.http.HttpUtil;

import java.util.HashMap;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/8/2
 * 生成工具类,目的是使用POST方式访问地址:<a href="http://localhost:8041/actuator/bus-refresh">http://localhost:8041/actuator/bus-refresh</a>.
 * 而不是以GET形式进行访问
 */
public class FreshConfigUtil {

    public static void main(String[] args) {
        HashMap<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        System.out.println("因为通过git进行获取,还要刷新 index-config-service.反应比较慢,请耐心等待.");
        String result = HttpUtil.createPost("http://localhost:8041/actuator/bus-refresh").addHeaders(headers).execute().body();
        System.out.println("result:" + result);
        System.out.println("refresh 完成");
    }
}
