package com.luzhi.thread.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.luzhi.thread.pojo.IndexData;
import com.luzhi.thread.util.SpringContextUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成具体指数数据服务类
 */
@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService {

    /**
     * 股票指数代码(indexCode-to-indexData)映射的数据hashMap.
     */
    private final Map<String, List<IndexData>> codeNameMapIndexDataList = new HashMap<>(1024);

    @Resource
    RestTemplate restTemplate;

    /**
     * 更新当前Redis中的数据.如果第三方数据获取不到,则该方法回溯调用{@link #mapNotTransferIndexData(String)} 方法.
     *
     * @param code 对应股票指数代码编号
     * @return 返回更新后的列表.
     */
    @HystrixCommand(fallbackMethod = "mapNotTransferIndexData")
    public List<IndexData> fresh(String code) {
        List<IndexData> indexDataList = parseMapTransferObject(code);
        codeNameMapIndexDataList.put(code, indexDataList);
        System.out.format("股票代码编号: %s, 总共有的数据有 %d 条. %n", code, indexDataList.size());
        IndexDataService indexDataService = SpringContextUtil.getBean(IndexDataService.class);
        indexDataService.remove(code);
        return indexDataService.store(code);
    }

    /**
     * 删除Redis中缓存的数据
     *
     * @param code 对应股票指数代码编号
     */
    @SuppressWarnings("unused")
    @CacheEvict(key = "'indexData-data-' + #p0")
    public void remove(String code) {
    }

    /**
     * 保存数据,在redis中缓存数据。
     *
     * @param code 对应股票指数代码编号
     */
    @Cacheable(key = "'indexData-data-' + #p0")
    public List<IndexData> store(String code) {
        return codeNameMapIndexDataList.get(code);
    }

    /**
     * 将Redis获取的数组数据转化为列表数据
     *
     * @param code 对应股票指数代码编号
     */
    @SuppressWarnings("unused")
    @Cacheable(key = "'indexData-data-' + #p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }

    /**
     * 进行解析,详情请看{@link #mapTransferIndexData(List)}
     *
     * @param code 需要解析对股票代码编号.
     * @return 返回解析好的对象.
     */
    @SuppressWarnings("unchecked")
    public List<IndexData> parseMapTransferObject(String code) {
        List<Map<String, Object>> mapListForIndexData = restTemplate.getForObject("http://127.0.0.1:8099/indexes/" + code + ".json", List.class);
        assert mapListForIndexData != null;
        return mapTransferIndexData(mapListForIndexData);
    }

    /**
     * 具体对列表中对图进行解析成对象.
     *
     * @param mapListForIndexData 关于存放IndexData的hash表
     * @return 解析后的列表对象
     */
    @NotNull
    private List<IndexData> mapTransferIndexData(@NotNull List<Map<String, Object>> mapListForIndexData) {
        List<IndexData> indexDataList = new ArrayList<>();
        for (Map<String, Object> map : mapListForIndexData) {
            String date = map.get("date").toString();
            float closePoint = Convert.toFloat(map.get("closePoint"));
            IndexData indexData = new IndexData();
            indexData.setDate(date);
            indexData.setClosePoint(closePoint);
            indexDataList.add(indexData);
        }
        return indexDataList;
    }

    /**
     * 当第三方数据不存在时候。
     *
     * @return 返回可序列化的"自定义数据"列表。
     */
    @SuppressWarnings("unused")
    private @NotNull @Unmodifiable List<IndexData> mapNotTransferIndexData(String code) {
        System.out.println("由于第三方数据没有被获取,导致该方法被调用.");
        IndexData indexData = new IndexData();
        indexData.setDate("n/a");
        indexData.setClosePoint(0.0f);
        return Collections.singletonList(indexData);
    }
}
