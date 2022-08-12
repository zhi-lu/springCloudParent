package com.luzhi.thread.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 通过全局作用域获取Bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 禁止任何人对该类进行Instance.
     */
    private SpringContextUtil() {
        System.out.println("SpringContextUtil()");
    }

    private static ApplicationContext applicationContext;

    /**
     * 获取当前的全局作用域对象
     *
     * @param applicationContext 全局作用域对象
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext:" + applicationContext);
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取该类存在对bean对象.
     *
     * @return 返回类对象存在对bean对象.
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
