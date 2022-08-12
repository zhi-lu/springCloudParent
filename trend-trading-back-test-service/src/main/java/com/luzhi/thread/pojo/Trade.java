package com.luzhi.thread.pojo;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/27
 * 生成交易类.
 */
@SuppressWarnings("unused")
public class Trade {

    /**
     * 股票购买的日期
     */
    private String buyDate;

    /**
     * 股票出售的日期
     */
    private String sellDate;

    /**
     * 交易的股票收盘价(购入)
     */
    private float buyClosePoint;

    /**
     * 交易的股票收盘价(售出)
     */
    private float sellClosePoint;

    /**
     * 投资的利润率
     */
    private float rate;


    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public float getBuyClosePoint() {
        return buyClosePoint;
    }

    public void setBuyClosePoint(float buyClosePoint) {
        this.buyClosePoint = buyClosePoint;
    }

    public float getSellClosePoint() {
        return sellClosePoint;
    }

    public void setSellClosePoint(float sellClosePoint) {
        this.sellClosePoint = sellClosePoint;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
