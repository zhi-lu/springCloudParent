package com.luzhi.thread.web;

import com.luzhi.thread.pojo.Index;
import com.luzhi.thread.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成控制器
 */
@RestController
public class IndexController {

    @Resource
    IndexService indexService;

    /**
     * 对应映射的地址为/getCodes
     *
     * @return 返回解析后的列表
     */
    @RequestMapping(value = "/getCodes", produces = {"application/json; charset=UTF-8"})
    public List<Index> get() {
        return indexService.get();
    }

    /**
     * 对应映射的地址为/freshCodes
     *
     * @return 更新后的对象.
     */
    @RequestMapping(value = "/freshCodes", produces = {"application/json; charset=UTF-8"})
    public List<Index> fresh() {
        return indexService.fresh();
    }

    /**
     * 对应映射的地址是/removeCodes
     *
     * @return 删除返回的提醒
     */
    @GetMapping("/removeCodes")
    public String remove() {
        indexService.remove();
        return "remove codes successfully";
    }
}
