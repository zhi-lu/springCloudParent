package com.luzhi.thread.client;

import com.luzhi.thread.pojo.IndexData;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO: 2021/7/22
 * 生成Feign拦截器,如果通过访问获取不到数据(发生熔断)则使用该拦截器"自定义数据"
 */
@Component
public class IndexDataClientFeignHystrix implements IndexDataClient {
    @Override
    public List<IndexData> getIndexData(String code) {
        IndexData indexData = new IndexData();
        indexData.setDate("0000-00-00");
        indexData.setClosePoint(0.0f);
        return Collections.singletonList(indexData);
    }
}
