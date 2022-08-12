package com.luzhi.thread;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成第三方数据更新器
 */
@SpringBootApplication
@EnableEurekaClient
public class ThirdPartIndexUpdateApplication {

    /**
     * 检查<em>Eureka</em>微服务注册中心管理是否打开
     */
    private static final int SERVER_PORT = 8761;

    /**
     * 设置该微服务的服务端口为该主机的9000端口.
     */
    private static final int CLIENT_PORT = 8762;

    public static void main(String[] args) {
        int port = CLIENT_PORT;
        // 判读微服务中心是否打开.
        if (NetUtil.isUsableLocalPort(SERVER_PORT)) {
            System.err.format("当前微服务注册中心对应服务的端口:%d,它没有被启用.%n故此该微服务无法正常启动,请检查Eureka-server是否启动", SERVER_PORT);
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
        new SpringApplicationBuilder(ThirdPartIndexUpdateApplication.class).properties("server.port=" + port).run(args);
    }
}
