package com.luzhi.thread.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.luzhi.thread.pojo.AnnualProfit;
import com.luzhi.thread.pojo.IndexData;
import com.luzhi.thread.pojo.Profit;
import com.luzhi.thread.pojo.Trade;
import com.luzhi.thread.service.BackTestService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * 提供模拟回测控制
 */
@RestController
public class BackTestController {

    @Resource
    BackTestService backTestService;

    /**
     * 模拟回测访问
     *
     * @param code              股票代码
     * @param buyRate           定义的购买率
     * @param sellRate          定义的出售率
     * @param movingAverage     定义的Ma均线
     * @param stringStartDate   获取指定数据的起始时间
     * @param stringEndDate     获取指定数据的结束时间
     * @param serviceChargeRate 收取的服务费率
     * @return 返回股票代码和相关数据的映射图
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/simulate/{code}/{movingAverage}/{buyRate}/{sellRate}/{serviceChargeRate}/{stringStartDate}/{stringEndDate}", produces = "application/json; charset=UTF-8")
    @CrossOrigin
    public Map<String, Object> backTest(@PathVariable("code") String code, @PathVariable("stringStartDate") String stringStartDate, @PathVariable("stringEndDate") String stringEndDate,
                                        @PathVariable("movingAverage") int movingAverage, @PathVariable("buyRate") float buyRate, @PathVariable("sellRate") float sellRate, @PathVariable("serviceChargeRate") float serviceChargeRate) {
        List<IndexData> indexDataList = backTestService.indexDataList(code);
        String indexStartDate = indexDataList.get(0).getDate();
        String indexEndDate = indexDataList.get(indexDataList.size() - 1).getDate();
        indexDataList = filterRangeByDate(indexDataList, stringStartDate, stringEndDate);
        Map<String, ?> simulateResult = backTestService.simulate(movingAverage, sellRate, buyRate, serviceChargeRate, indexDataList);
        List<Profit> profitList = (List<Profit>) simulateResult.get("profitList");
        List<Trade> tradeList = (List<Trade>) simulateResult.get("tradeList");
        List<AnnualProfit> annualProfitList = (List<AnnualProfit>) simulateResult.get("annualProfitList");
        int winCount = (Integer) simulateResult.get("winCount");
        int lossCount = (Integer) simulateResult.get("lossCount");
        float averageWinRate = (Float) simulateResult.get("averageWinRate");
        float averageLossRate = (Float) simulateResult.get("averageLossRate");
        float years = backTestService.getYear(indexDataList);
        float indexIncomeTotal = (indexDataList.get(indexDataList.size() - 1).getClosePoint() - indexDataList.get(0).getClosePoint()) / indexDataList.get(0).getClosePoint();
        float indexIncomeAnnual = (float) Math.pow(1 + indexIncomeTotal, 1 / years) - 1;
        float trendIncomeTotal = (profitList.get(profitList.size() - 1).getValue() - profitList.get(0).getValue()) / profitList.get(0).getValue();
        float trendIncomeAnnual = (float) Math.pow(1 + trendIncomeTotal, 1 / years) - 1;
        Map<String, Object> result = new HashMap<>(20);
        result.put("indexDatas", indexDataList);
        result.put("indexStartDate", indexStartDate);
        result.put("indexEndDate", indexEndDate);
        result.put("profits", profitList);
        result.put("trades", tradeList);
        result.put("years", years);
        result.put("indexIncomeTotal", indexIncomeTotal);
        result.put("indexIncomeAnnual", indexIncomeAnnual);
        result.put("trendIncomeTotal", trendIncomeTotal);
        result.put("trendIncomeAnnual", trendIncomeAnnual);
        result.put("winCount", winCount);
        result.put("lossCount", lossCount);
        result.put("averageWinRate", averageWinRate);
        result.put("averageLossRate", averageLossRate);
        result.put("annualProfits", annualProfitList);
        return result;
    }

    /**
     * 进行起始和结束时间筛选
     *
     * @param stringStartDate 结束时间
     * @param stringEndDate   起始时间
     * @param indexDataList   通过起始结束规定的时间来筛选的整个股票指数数据.
     * @return 关于IndexData数据筛选完成的List(列表)
     */
    private static List<IndexData> filterRangeByDate(List<IndexData> indexDataList, String stringStartDate, String stringEndDate) {
        if (StrUtil.isBlankOrUndefined(stringStartDate) || StrUtil.isBlankOrUndefined(stringEndDate)) {
            return indexDataList;
        }
        List<IndexData> result = new ArrayList<>();
        Date dateOfStart = DateUtil.parse(stringStartDate);
        Date dateOfEnd = DateUtil.parse(stringEndDate);
        for (IndexData indexData : indexDataList) {
            Date dateOfData = DateUtil.parse(indexData.getDate());
            boolean dateOfDataMoreThanOrEqualDateOfStart = dateOfStart.getTime() <= dateOfData.getTime();
            boolean dateOfDataLessThanOrEqualDateOfEnd = dateOfData.getTime() <= dateOfEnd.getTime();
            if (dateOfDataMoreThanOrEqualDateOfStart && dateOfDataLessThanOrEqualDateOfEnd) {
                result.add(indexData);
            }
        }
        return result;
    }
}
