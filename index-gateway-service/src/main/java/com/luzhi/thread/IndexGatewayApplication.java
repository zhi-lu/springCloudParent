package com.luzhi.thread;

import brave.sampler.Sampler;
import cn.hutool.core.util.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * 生成跨域访问网关启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableZuulProxy
public class IndexGatewayApplication {

    /**
     * 检查<em>Eureka</em>微服务注册中心管理是否打开
     */
    private static final int SERVER_PORT = 8761;

    /**
     * 设置该微服务的服务端口为该主机的8031端口.
     */
    private static final int CLIENT_PORT = 8031;

    public static void main(String[] args) {
        // 检测微服务注册管理中心是否启动
        if (NetUtil.isUsableLocalPort(SERVER_PORT)) {
            System.err.format("当前微服务注册中心对应服务的端口:%d,它没有被启用.故此无法提供服务.%n", SERVER_PORT);
            System.exit(1);
        }
        // 判端端口是否被占用.
        if (!NetUtil.isUsableLocalPort(CLIENT_PORT)) {
            System.err.format("服务端口%d不可使用,因为已经被占用.导致微服务启动失败.", CLIENT_PORT);
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexGatewayApplication.class).properties("server.port=" + CLIENT_PORT).run(args);
    }

    /**
     * 增加Sampler,使用{@link Sampler#ALWAYS_SAMPLE}表示一直取样。
     */
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
