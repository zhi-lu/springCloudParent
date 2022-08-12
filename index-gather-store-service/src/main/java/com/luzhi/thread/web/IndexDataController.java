package com.luzhi.thread.web;

import com.luzhi.thread.pojo.IndexData;
import com.luzhi.thread.service.IndexDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成股票指数IndexData的控制层.
 */
@RestController
public class IndexDataController {

    @Resource
    IndexDataService indexDataService;

    /**
     * 查找相关的股票数据.
     *
     * @param code 从地址中获取股票代码编号.
     * @return 返回从Redis获取的数据
     */
    @RequestMapping(value = "/getIndexData/{code}", produces = "application/json; charset=UTF-8")
    public List<IndexData> get(@PathVariable("code") String code) {
        return indexDataService.get(code);
    }

    /**
     * 删除相关的股票数据.
     *
     * @param code 从地址中获取股票代码编号
     */
    @GetMapping("/removeIndexData/{code}")
    public String remove(@PathVariable("code") String code) {
        indexDataService.remove(code);
        return "remove codes successfully";
    }

    /**
     * 更新删除相关的股票数据.
     *
     * @param code 从地址中获取股票代码编号.
     * @return 更新后的数据.
     */
    @RequestMapping(value = "/freshIndexData/{code}", produces = "application/json; charset=UTF-8")
    public List<IndexData> fresh(@PathVariable("code") String code) {
        return indexDataService.fresh(code);
    }
}
