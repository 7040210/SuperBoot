package org.superboot.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> 多数据源注入配置 </b>
 * <p>
 * 功能描述:提供对多数据的支持，传统模式无法支持JPA的读写分离及多数据源事物
 * </p>
 */
@Configuration
public class DataSourceConfig {

    public final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";
    public final static String READ_DATASOURCE_KEY = "readDruidDataSource";


    @Autowired
    @Qualifier(READ_DATASOURCE_KEY)
    private DruidDataSource readDruidDataSource;

    @Autowired
    @Qualifier(WRITE_DATASOURCE_KEY)
    private DruidDataSource writeDruidDataSource;

    /**
     * 构造包含多数据源的路由数据源实例类
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "routingDataSource")
    public AbstractRoutingDataSource routingDataSource(
    ) throws Exception {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap();
        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource);
        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(writeDruidDataSource);
        return dataSource;
    }
}
