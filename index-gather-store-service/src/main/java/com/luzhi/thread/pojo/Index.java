package com.luzhi.thread.pojo;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/6/27
 * 生成股票指数信息的pojo
 */
@SuppressWarnings("unused")
public class Index {

    /**
     * 股票指数代码
     */
    private String code;

    /**
     * 股票指数名称
     */
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
