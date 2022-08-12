package com.luzhi.thread.web;

import com.luzhi.thread.config.IpConfiguration;
import com.luzhi.thread.pojo.Index;
import com.luzhi.thread.service.IndexService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成代码指数控制器
 */
@RestController
public class IndexController {

    @Resource
    IndexService indexService;

    @Resource
    IpConfiguration ipConfiguration;

    @RequestMapping(value = "/codes", produces = "application/json; charset=UTF-8")
    @CrossOrigin
    public List<Index> get() {
        System.out.println("当前提供实例服务端口是:" + ipConfiguration.getServerPort());
        return indexService.get();
    }
}
