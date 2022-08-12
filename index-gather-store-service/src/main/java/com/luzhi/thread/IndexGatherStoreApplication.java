package com.luzhi.thread;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author apple
 * @version jdk1.8
 * // TODO 2021/7/21
 * 声明一个启动类,注册到Eureka微服务中.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableCaching
public class IndexGatherStoreApplication {

    /**
     * 检查<em>Eureka</em>微服务注册中心管理是否打开
     */
    private static final int SERVER_PORT = 8761;

    /**
     * 检测redis是否已经启动.
     */
    private static final int REDIS_PORT = 6379;

    /**
     * 设置该微服务的服务端口为该主机的9000端口.
     */
    private static final int CLIENT_PORT = 9001;

    public static void main(String[] args) {
        int truePort = CLIENT_PORT;
        // 检测微服务注册管理中心是否启动
        if (NetUtil.isUsableLocalPort(SERVER_PORT)) {
            System.err.format("当前微服务注册中心对应服务的端口:%d,它没有被启用.故此无法提供服务.%n", SERVER_PORT);
            System.exit(1);
        }
        // 检测redis是否打开
        if (NetUtil.isUsableLocalPort(REDIS_PORT)) {
            System.err.format("当前Redis服务对应的端口:%d,没有被启用,故此无法提供服务.%n", REDIS_PORT);
            System.exit(1);
        }
        // 通过参数来修改服务端口.
        if (args != null && args.length != 0) {
            for (String arg : args) {
                if (arg.startsWith("port=")) {
                    String paramPort = StrUtil.subAfter(arg, "port=", true);
                    if (NumberUtil.isNumber(paramPort)) {
                        truePort = Convert.toInt(paramPort);
                    }
                }
            }
        }
        // 判读端口是否被占用.
        if (!NetUtil.isUsableLocalPort(truePort)) {
            System.err.format("服务端口%d不可使用,因为已经被占用.导致微服务启动失败.", truePort);
            System.exit(1);
        }

        new SpringApplicationBuilder(IndexGatherStoreApplication.class).properties("server.port=" + truePort).run(args);
    }


    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
