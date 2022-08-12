package com.luzhi.thread;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/8/8
 * 生成配置服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableConfigServer
public class IndexConfigApplication {

    /**
     * 设置该微服务的服务端口为该主机的8051端口.
     */
    private static final int CLIENT_PORT = 8051;

    /**
     * 检查<em>Eureka</em>微服务注册中心管理是否打开
     */
    private static final int SERVER_PORT = 8761;

    public static void main(String[] args) {

        int port = CLIENT_PORT;
        // 判读微服务中心是否打开.
        if (NetUtil.isUsableLocalPort(SERVER_PORT)) {
            System.err.format("当前微服务注册中心对应服务的端口:%d,它没有被启用.%n故此", SERVER_PORT);
            System.exit(1);
        }
        // 通过参数来修改服务端口.
        if (args != null && args.length != 0) {
            for (String arg : args) {
                if (arg.startsWith("port=")) {
                    String stringPort = StrUtil.subAfter(arg, "port=", true);
                    if (NumberUtil.isNumber(stringPort)) {
                        port = Convert.toInt(stringPort);
                    }
                }
            }
        }
        // 判端端口是否被占用.
        if (!NetUtil.isUsableLocalPort(port)) {
            System.err.format("服务端口%d不可使用,因为已经被占用.导致微服务启动失败.", port);
            System.exit(1);
        }

        new SpringApplicationBuilder(IndexConfigApplication.class).properties("server.port=" + port).run(args);
    }
}
