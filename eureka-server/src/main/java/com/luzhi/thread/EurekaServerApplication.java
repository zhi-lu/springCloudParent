package com.luzhi.thread;

import cn.hutool.core.util.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author apple
 * @version jdk1.8
 * <p>
 * // TODO : 2021/7/15
 * <p>
 * 生成注册中心启动服务器;
 * 注解详解:
 * One:{@link SpringBootApplication} 代表该类是一个启动类
 * Two:{@link EnableEurekaServer} 代表它是一个注册中心服务器.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    /**
     * 设计Eurake服务注册端口为8761
     */
    private static final int PORT = 8761;

    public static void main(String[] args) {
        if (!NetUtil.isUsableLocalPort(PORT)) {
            System.err.printf("当前服务端口%d,已经被占用.导致无法启动注册中心启动服务器%n", PORT);
            System.exit(1);
        }
        
        new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port=" + PORT).run(args);
    }
}
