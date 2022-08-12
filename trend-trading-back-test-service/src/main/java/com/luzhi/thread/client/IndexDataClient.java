package com.luzhi.thread.client;

import com.luzhi.thread.pojo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * 获取股票数据
 */
@FeignClient(value = "index-data-service", fallback = IndexDataClientFeignHystrix.class)
public interface IndexDataClient {

    /**
     * 获取数据(声明式)
     *
     * @return List<IndexData> 返回请求的结果.如果访问失败从IndexDataClientFeignHystrix类获取数据
     * @param code 股票代码编号
     */
    @RequestMapping(value = "/data/{code}", produces = "application/json; charset=UTF-8")
    List<IndexData> getIndexData(@PathVariable("code") String code);
}
