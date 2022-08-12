package com.luzhi.thread.job;

import cn.hutool.core.date.DateUtil;
import com.luzhi.thread.pojo.Index;
import com.luzhi.thread.service.IndexDataService;
import com.luzhi.thread.service.IndexService;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成定时器,定时更新股票指数和下面相关的数据.
 */
@Component
public class IndexDataSyncJob extends QuartzJobBean {

    @Resource
    IndexService indexService;

    @Resource
    IndexDataService indexDataService;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {
        System.out.println("定时启动:" + DateUtil.now());
        List<Index> indexList = indexService.fresh();
        for (Index index : indexList) {
            indexDataService.fresh(index.getCode());
        }
        System.out.println("定时结束:" + DateUtil.now());
    }
}
