package com.luzhi.thread;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/23
 * 模拟回测视图应用类
 */
@SpringBootApplication
@EnableEurekaClient
public class TrendTradingBackTestViewApplication {

    /**
     * 检查<em>Eureka</em>微服务注册中心管理是否打开
     */
    private static final int SERVER_PORT = 8761;

    /**
     * 检查RabbitMq消息队列是否启动
     */
    private static final int RABBIT_MQ_PORT = 5672;


    /**
     * 设置该微服务的服务端口为该主机的8041端口.
     */
    private static final int CLIENT_PORT = 8041;

    /**
     * 设置该微服务的配置服务端口为该主机的8051端口.
     */
    private static final int SERVER_CONFIG_PORT = 8051;


    public static void main(String[] args) {
        int truePort = 0;
        // 检测微服务注册管理中心是否启动
        if (NetUtil.isUsableLocalPort(SERVER_PORT)) {
            System.err.format("当前微服务注册中心对应服务的端口:%d,它没有被启用.故此无法提供服务.%n", SERVER_PORT);
            System.exit(1);
        }
        // 检测配置服务是否启动
        if (NetUtil.isUsableLocalPort(SERVER_CONFIG_PORT)) {
            System.err.format("当前服务配置对应的服务端口:%d,它没有启动,故此无法提供服务.%n", SERVER_CONFIG_PORT);
            System.exit(1);
        }
        // 检测RabbitMq是否启动.
        if (NetUtil.isUsableLocalPort(RABBIT_MQ_PORT)) {
            System.err.format("当前RabbitMq服务器端口:%d,它没有启动,故此无法提供服务.%n", RABBIT_MQ_PORT);
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
        // 手动输入获取端口
        if (truePort == 0) {
            Future<Integer> integerFuture = ThreadUtil.execAsync(() -> {
                int port;
                System.out.format("请在7秒之内确认端口,默认推荐的端口是:%d哦!,不然使用默认端口%d. %n", CLIENT_PORT, CLIENT_PORT);
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String stringPort = scanner.nextLine();
                    if (!NumberUtil.isNumber(stringPort)) {
                        System.err.println("只能是数字哦!");
                    } else {
                        port = Convert.toInt(stringPort);
                        if (NetUtil.isValidPort(port)) {
                            scanner.close();
                            break;
                        }
                        System.err.println("输入的数字必须是整数且范围在 (0,65535) 开括号");
                    }
                }
                return port;
            });
            try {
                truePort = integerFuture.get(7, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException exception) {
                truePort = CLIENT_PORT;
            }
        }
        // 判读端口是否被占用.
        if (!NetUtil.isUsableLocalPort(truePort)) {
            System.err.format("服务端口%d不可使用,因为已经被占用.导致微服务启动失败. %n", truePort);
            System.exit(1);
        }
        new SpringApplicationBuilder(TrendTradingBackTestViewApplication.class).properties("server.port=" + truePort).run(args);
    }

    /**
     * 增加Sampler,使用{@link Sampler#ALWAYS_SAMPLE}表示一直取样。
     */
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
