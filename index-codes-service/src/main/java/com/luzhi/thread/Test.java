package com.luzhi.thread;


/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * 产生多个SpringApplication启动类
 */
public class Test {

    /**
     * 设置该微服务的服务端口(储存的容器为字符串数组类型),可以使用多个端口.
     */
    private static final String[] PORT_ARRAY_ONE = {"port=8002"};

    public static void main(String[] args) {
        IndexCodesServiceApplication.main(PORT_ARRAY_ONE);
    }

}
