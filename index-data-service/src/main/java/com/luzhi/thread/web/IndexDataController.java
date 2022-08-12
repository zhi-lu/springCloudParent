package com.luzhi.thread.web;

import com.luzhi.thread.config.IpConfiguration;
import com.luzhi.thread.pojo.IndexData;
import com.luzhi.thread.service.IndexDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 股票指数数据控制器
 */
@RestController
public class IndexDataController {

    @Resource
    IndexDataService indexDataService;

    @Resource
    IpConfiguration ipConfiguration;

    @RequestMapping(value = "/data/{code}", produces = "application/json; charset=UTF-8")
    @CrossOrigin
    public List<IndexData> get(@PathVariable("code") String code) {
        System.out.println("当前提供实例服务端口是:" + ipConfiguration.getServerPort());
        return indexDataService.get(code);
    }
}
