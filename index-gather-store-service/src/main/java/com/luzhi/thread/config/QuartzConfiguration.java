package com.luzhi.thread.config;

import com.luzhi.thread.job.IndexDataSyncJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成定时器配置类.
 */
@Configuration
public class QuartzConfiguration {

    /**
     * 相关任务更新的间隔时间(单位为:分钟)
     */
    private static final int INTERVAL_TIME = 30;

    @Bean
    public JobDetail weatherDataJobDetail() {
        return JobBuilder.newJob(IndexDataSyncJob.class).withIdentity("IndexDataSyncJob").storeDurably().build();
    }

    @Bean
    public Trigger weatherDataSyncTrigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(INTERVAL_TIME).repeatForever();

        return TriggerBuilder.newTrigger().forJob(weatherDataJobDetail()).withIdentity("IndexDataSyncTrigger").withSchedule(simpleScheduleBuilder).build();
    }
}
