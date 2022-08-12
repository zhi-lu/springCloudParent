package com.luzhi.thread.service;

import cn.hutool.core.collection.CollUtil;
import com.luzhi.thread.pojo.Index;
import com.luzhi.thread.util.SpringContextUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/6/21
 * 生成股票服务层
 */

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexService {

    /**
     * 用于保存指数数据
     */
    private List<Index> indexList;

    @Resource
    RestTemplate restTemplate;

    /**
     * 更新当前Redis中的数据.如果第三方数据获取不到,则该方法回溯调用{@link #mapNotTransferIndex()}方法。
     *
     * @return 返回更新的数据列表.
     */
    @HystrixCommand(fallbackMethod = "mapNotTransferIndex")
    public List<Index> fresh() {
        indexList = parseMapTransferObject();
        IndexService indexService = SpringContextUtil.getBean(IndexService.class);
        indexService.remove();
        return indexService.store();
    }

    /**
     * 将列表中的map图解析成对象
     *
     * @return 结果详情参考方法mapTransferIndex(List < Map < String, String > >)方法.
     */
    @SuppressWarnings("unchecked")
    public List<Index> parseMapTransferObject() {
        List<Map<String, String>> mapList = restTemplate.getForObject("http://127.0.0.1:8099/indexes/codes.json", List.class);
        assert mapList != null;
        return mapTransferIndex(mapList);
    }

    /**
     * 清除缓存中所有的数据.
     */
    @CacheEvict(allEntries = true)
    public void remove() {
    }

    /**
     * 将Redis获取的数组数据转化为列表数据.
     *
     * @return 从Redis中获取的数据。
     */
    @Cacheable(key = "'allCodes'")
    public List<Index> get() {
        return CollUtil.toList();
    }

    /**
     * 保存数据,在redis中缓存数据。
     *
     * @return 返回指数数据
     */
    @Cacheable(key = "'allCodes'")
    public List<Index> store() {
        return indexList;
    }

    /**
     * 对获取json列表中对map进行解析成对象.
     *
     * @param mapList 获取关于{@link Map<>}的列表.
     * @return List<Index> 解析后对列表
     */
    @org.jetbrains.annotations.NotNull
    private List<Index> mapTransferIndex(List<Map<String, String>> mapList) {
        List<Index> indexList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            String code = map.get("code");
            String name = map.get("name");
            Index index = new Index();
            index.setCode(code);
            index.setName(name);
            indexList.add(index);
        }
        return indexList;
    }

    /**
     * 如果第三方数据不存在,则该方法被调用.
     *
     * @return List<Index> 列表集合
     */
    @SuppressWarnings("unused")
    private List<Index> mapNotTransferIndex() {
        System.out.println("由于第三方数据没有被获取,导致该方法被调用.");
        Index index = new Index();
        index.setCode("000000");
        index.setName("无效代码编号");
        return Collections.singletonList(index);
    }
}
