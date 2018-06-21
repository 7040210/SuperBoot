package org.superboot.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <b> 动态切换数据源 </b>
 * <p>
 * 功能描述:提供对数据源动态切换的支持
 * </p>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        //可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();

        return lookupKey;
    }
}
