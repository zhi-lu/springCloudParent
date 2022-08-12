package com.luzhi.thread.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.luzhi.thread.client.IndexDataClient;
import com.luzhi.thread.pojo.AnnualProfit;
import com.luzhi.thread.pojo.IndexData;
import com.luzhi.thread.pojo.Profit;
import com.luzhi.thread.pojo.Trade;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2022/7/22
 * 提供模拟回测服务
 */
@Service
public class BackTestService {

    @Resource
    IndexDataClient indexDataClient;

    public List<IndexData> indexDataList(String code) {
        List<IndexData> list = indexDataClient.getIndexData(code);
        Collections.reverse(list);
        return list;
    }

    /**
     * 模拟交易(购买和抛售)。
     *
     * @param indexDataList     对传入的{@link IndexData}列表进行操作.
     * @param serviceChargeRate 服务费率
     * @param buyRate           购买率
     * @param sellRate          出售率
     * @param movingAverage     要定义的Ma天数
     * @return 返回模拟交易图
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public Map<String, Object> simulate(int movingAverage, float sellRate, float buyRate, float serviceChargeRate, @NotNull List<IndexData> indexDataList) {
        List<Profit> profitList = new ArrayList<>();
        List<Trade> tradeList = new ArrayList<>();

        float initCash = 20000.0f;
        float cash = initCash;
        float share = 0.0f;
        float value;
        float init = 0.0f;

        int winCount = 0;
        int lossCount = 0;
        float totalWinRate = 0.0f;
        float averageWinRate;
        float totalLossRate = 0.0f;
        float averageLossRate;

        if (!indexDataList.isEmpty()) {
            init = indexDataList.get(0).getClosePoint();
        }
        for (int i = 0; i < indexDataList.size(); i++) {
            IndexData indexData = indexDataList.get(i);
            float closePoint = indexData.getClosePoint();
            float avg = getMovingAverage(i, movingAverage, indexDataList);
            float maxValue = getMaxBuck(i, movingAverage, indexDataList);

            float increaseRate = closePoint / avg;
            float decreaseRate = closePoint / maxValue;

            if (avg != 0) {
                // 如果超过Ma均线
                if (increaseRate > buyRate) {
                    // 进行购买
                    if (0 == share) {
                        share = cash / closePoint;
                        cash = 0;

                        Trade trade = new Trade();
                        trade.setBuyDate(indexData.getDate());
                        trade.setBuyClosePoint(indexData.getClosePoint());
                        trade.setSellDate("n/a");
                        trade.setSellClosePoint(0.0f);
                        tradeList.add(trade);
                    }
                }
                // 如果跌的小于规定出售率。
                else if (decreaseRate < sellRate) {
                    // 进行出售
                    if (0 != share) {
                        cash = closePoint * share * (1 - serviceChargeRate);
                        share = 0;

                        Trade trade = tradeList.get(tradeList.size() - 1);
                        trade.setSellDate(indexData.getDate());
                        trade.setSellClosePoint(indexData.getClosePoint());
                        trade.setRate(cash / initCash);

                        if (trade.getSellClosePoint() - trade.getBuyClosePoint() > 0) {
                            totalWinRate += (trade.getSellClosePoint() - trade.getBuyClosePoint()) / trade.getBuyClosePoint();
                            winCount++;
                        } else {
                            totalLossRate += (trade.getSellClosePoint() - trade.getBuyClosePoint()) / trade.getBuyClosePoint();
                            lossCount++;
                        }
                    }
                }
            }
            if (share != 0) {
                value = closePoint * share;
            } else {
                value = cash;
            }
            float rate = value / initCash;
            Profit profit = new Profit();
            profit.setDate(indexData.getDate());
            profit.setValue(rate * init);
            profitList.add(profit);
        }
        averageWinRate = totalWinRate / winCount;
        averageLossRate = totalLossRate / lossCount;
        List<AnnualProfit> annualProfitList = calculateAnnualProfitList(indexDataList, profitList);
        Map<String, Object> map = new HashMap<>(12);
        map.put("profitList", profitList);
        map.put("tradeList", tradeList);
        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("averageWinRate", averageWinRate);
        map.put("averageLossRate", averageLossRate);
        map.put("annualProfitList", annualProfitList);
        return map;
    }

    /**
     * 获取交易日产生的最高价
     *
     * @param indexDataList       对传入的{@link IndexData}列表进行操作.
     * @param tradingDay          需要比较成交价的交易日(和要定义的Ma天数相同)
     * @param currentlyTradingDay 当前交易日
     * @return 返回交易日产生的最高价.
     */
    private static float getMaxBuck(int currentlyTradingDay, int tradingDay, List<IndexData> indexDataList) {
        int start = currentlyTradingDay - 1 - tradingDay;
        if (start < 0) {
            return 0.0f;
        }
        int now = currentlyTradingDay - 1;
        float maxValue = 0.0f;
        for (int i = start; i < now; i++) {
            IndexData indexData = indexDataList.get(i);
            if (indexData.getClosePoint() > maxValue) {
                maxValue = indexData.getClosePoint();
            }
        }
        return maxValue;
    }

    /**
     * 求的Ma的值
     *
     * @param indexDataList       对传入的{@link IndexData}列表进行操作.
     * @param movingAverage       要定义的Ma天数
     * @param currentlyTradingDay 当前交易日
     * @return 返回平均值
     */
    private static float getMovingAverage(int currentlyTradingDay, int movingAverage, List<IndexData> indexDataList) {
        int start = currentlyTradingDay - 1 - movingAverage;
        int now = currentlyTradingDay - 1;
        // 不满足MovingAverage交易
        if (start < 0) {
            return 0.0f;
        }
        // 获取Ma交易收盘总值
        float sum = 0.0f;
        for (int i = start; i < now; i++) {
            IndexData indexData = indexDataList.get(i);
            sum += indexData.getClosePoint();
        }
        // 返回Ma平均值.
        return sum / (now - start);
    }

    /**
     * 用于计算当前的时间范围是多少年。
     *
     * @param indexDataList 被计算的{@link IndexData}列表数据.
     * @return 返回范围是多少年
     */
    public float getYear(@NotNull List<IndexData> indexDataList) {
        String getDateStart = indexDataList.get(0).getDate();
        String getDateEnd = indexDataList.get(indexDataList.size() - 1).getDate();
        Date dateStart = DateUtil.parse(getDateStart);
        Date dateEnd = DateUtil.parse(getDateEnd);
        long days = DateUtil.between(dateStart, dateEnd, DateUnit.DAY);
        return days / 365.0f;
    }

    /**
     * 获得数据日期的年份
     *
     * @param date 要筛选的日期
     * @return 返回数据日期的年份
     */
    private int getYear(@NotNull String date) {
        String stringYear = StrUtil.subBefore(date, "-", false);
        return Convert.toInt(stringYear);
    }

    /**
     * 计算某一年的的指数收益
     *
     * @param year          需要获取数据的年份
     * @param indexDataList 被计算的{@link IndexData}列表数据.
     * @return 返回指数收益结果.
     */
    private float getIndexIncome(int year, @NotNull List<IndexData> indexDataList) {
        IndexData indexStart = null;
        IndexData indexEnd = null;
        for (IndexData indexData : indexDataList) {
            String stringDate = indexData.getDate();
            int currentYear = getYear(stringDate);
            if (currentYear == year) {
                if (null == indexStart) {
                    indexStart = indexData;
                } else {
                    indexEnd = indexData;
                }
            }
        }
        assert indexStart != null;
        assert indexEnd != null;
        return (indexEnd.getClosePoint() - indexStart.getClosePoint()) / indexStart.getClosePoint();
    }

    /**
     * 计算某一年的趋势投资收益
     *
     * @param year       需要获取数据的年份
     * @param profitList 被计算的{@link Profit}列表数据.
     * @return 返回趋势收益结果.
     */
    private float getTrendIncome(int year, @NotNull List<Profit> profitList) {
        Profit profitStart = null;
        Profit profitEnd = null;
        for (Profit profit : profitList) {
            String stringDate = profit.getDate();
            int currentYear = getYear(stringDate);
            if (currentYear == year) {
                if (null == profitStart) {
                    profitStart = profit;
                } else {
                    profitEnd = profit;
                }
            }
            if (currentYear > year) {
                break;
            }
        }
        assert profitStart != null;
        assert profitEnd != null;
        return (profitEnd.getValue() - profitStart.getValue()) / profitStart.getValue();
    }

    /**
     * 计算完整时间范围内，每一年的指数投资收益和趋势投资收益
     *
     * @param profitList    被计算的{@link Profit}列表数据.
     * @param indexDataList 被计算的{@link IndexData}列表数据.
     * @return 返回计算后的AnnualProfit列表
     */
    private @NotNull List<AnnualProfit> calculateAnnualProfitList(@NotNull List<IndexData> indexDataList, List<Profit> profitList) {
        List<AnnualProfit> annualProfitList = new ArrayList<>();
        String stringDateStart = indexDataList.get(0).getDate();
        String stringDateEnd = indexDataList.get(indexDataList.size() - 1).getDate();
        int startYear = DateUtil.year(DateUtil.parse(stringDateStart));
        int endYear = DateUtil.year(DateUtil.parse(stringDateEnd));
        for (int year = startYear; year <= endYear; year++) {
            AnnualProfit annualProfit = new AnnualProfit();
            float indexIncome = getIndexIncome(year, indexDataList);
            float trendIncome = getTrendIncome(year, profitList);
            annualProfit.setYear(year);
            annualProfit.setIndexIncome(indexIncome);
            annualProfit.setTrendIncome(trendIncome);
            annualProfitList.add(annualProfit);
        }
        return annualProfitList;
    }
}
