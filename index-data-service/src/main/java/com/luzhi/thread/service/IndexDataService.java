package com.luzhi.thread.service;

import cn.hutool.core.collection.CollUtil;
import com.luzhi.thread.pojo.IndexData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/22
 * 生成股票指数数据的服务层.
 */
@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService {

    @SuppressWarnings("unused")
    @Cacheable(key = "'indexData-data-' + #p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }
}
