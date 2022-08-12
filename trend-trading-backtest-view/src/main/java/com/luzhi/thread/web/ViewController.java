package com.luzhi.thread.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/23
 * 回测数据控制类
 */
@Controller
@RefreshScope
public class ViewController {

    /**
     * 添加{@link Value}注解获取版本信息
     */
    @Value("${version}")
    String version;

    @RequestMapping(value = "/index", produces = "application/json; charset=UTF-8")
    public String view(@NotNull Model model) throws Exception{
        model.addAttribute("version", version);
        return "fore/view";
    }
}
