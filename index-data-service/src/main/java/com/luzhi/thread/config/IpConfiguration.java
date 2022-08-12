package com.luzhi.thread.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 此组件获取微服务集群使用不同的服务的端口.
 */

@Component
public class IpConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    /**
     * 获取当前微服务所提供服务的主机端口.
     */
    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getServerPort() {
        return serverPort;
    }
}
