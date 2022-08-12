package com.luzhi.thread;

/**
 * @author apple
 * @version jdk1.8
 * // TODO: 2021/8/8
 * 生成测试类生成两个启动类
 */
public class Test {

    /**
     * 设置该微服务的服务端口(储存的容器为字符串数组类型),可以使用多个端口.
     */
    private static final String[] RUN_PORT = new String[]{"port=8012"};

    public static void main(String[] args) {
        IndexDataApplication.main(RUN_PORT);
    }
}
