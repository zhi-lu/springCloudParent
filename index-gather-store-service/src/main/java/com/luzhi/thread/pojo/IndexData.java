package com.luzhi.thread.pojo;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成具体指数数据的POJO
 */
@SuppressWarnings("unused")
public class IndexData {

    /**
     * 股票交易日期
     */
    private String date;

    /**
     * 股票交易的收盘价格
     */
    private float closePoint;

    public float getClosePoint() {
        return closePoint;
    }

    public String getDate() {
        return date;
    }

    public void setClosePoint(float closePoint) {
        this.closePoint = closePoint;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
