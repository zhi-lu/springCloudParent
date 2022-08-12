package com.luzhi.thread.job;

import cn.hutool.core.date.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhilu
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成定时器,定时将Python获取的数据刷入third-part-index-data-project中
 */
@Component
public class IndexDataUpdateSyncJob extends QuartzJobBean {

    @Resource
    ScriptRun scriptRun;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {
        System.out.println("定时启动:" + DateUtil.now());
        scriptRun.start();
        System.out.println("定时结束:" + DateUtil.now());
    }
}
