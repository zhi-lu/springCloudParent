package com.luzhi.thread.pojo;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/8/1
 * 生成利润类
 */
public class Profit {

    /**
     * 股票交易日期
     */
    private String date;

    /**
     * 投机获取的盈利价值
     */
    private float value;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
