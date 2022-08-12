package com.luzhi.thread.service;

import com.luzhi.thread.pojo.Index;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 生成Index股票指数服务层(基于Redis操作)
 */
@Service
@CacheConfig(cacheNames = "indexes")
public class IndexService {

    @Cacheable(key = "'allCodes'")
    public List<Index> get() {
        Index index = new Index();
        index.setCode("000000");
        index.setName("无效代码编号.");
        return Collections.singletonList(index);
    }
}
