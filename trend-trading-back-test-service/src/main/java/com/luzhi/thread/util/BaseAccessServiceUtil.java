package com.luzhi.thread.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/8/4
 * 不断访问,获取监控信息.
 */
public abstract class BaseAccessServiceUtil {

    public static void main(String[] args) {
        //noinspection InfiniteLoopStatement
        while (true) {
            ThreadUtil.sleep(3000);
            accessPort(8021);
            accessPort(8022);
        }
    }

    public static void accessPort(int port) {
        try {
            String html = HttpUtil.get(String.format("http://127.0.0.1:%d/simulate/000001/20/1.01/0.99/0/null/null/", port));
            System.out.format("%d:端口地址的模拟回测服务访问成功,返回大小是 %d%n",port,html.length());
        } catch (Exception exception) {
            System.out.println("异常原因:" + exception.getMessage());
            System.err.format("%d:端口地址的模拟回测服务无法访问%n",port);
        }
    }
}
