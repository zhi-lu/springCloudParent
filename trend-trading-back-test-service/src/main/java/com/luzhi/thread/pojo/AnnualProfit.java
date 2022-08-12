package com.luzhi.thread.pojo;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/25
 * 每年收益实体类
 */
public class AnnualProfit {

    /**
     * 时间单位:年
     */
    private int year;

    /**
     * 指数投资收益
     */
    private float indexIncome;

    /**
     * 趋势投资收益
     */
    private float trendIncome;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getIndexIncome() {
        return indexIncome;
    }

    public void setIndexIncome(float indexIncome) {
        this.indexIncome = indexIncome;
    }

    public float getTrendIncome() {
        return trendIncome;
    }

    public void setTrendIncome(float trendIncome) {
        this.trendIncome = trendIncome;
    }

    @Override
    public String toString() {
        return "AnnualProfit [year=" + getYear() + ", indexIncome=" + getIndexIncome() + ", trendIncome=" + getTrendIncome() + "]";
    }
}
