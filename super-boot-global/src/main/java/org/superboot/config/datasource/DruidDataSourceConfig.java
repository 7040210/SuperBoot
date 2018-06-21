package org.superboot.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * <b> 数据源配置类 </b>
 * <p>
 * 功能描述:配置数据源的实例创建及维护
 * </p>
 */
@Configuration
public class DruidDataSourceConfig {

    @Inject
    private Environment env;

    /**
     * DataSource 读数据源配置
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.readDruidDataSource")
    @Qualifier("readDruidDataSource")
    @Bean(name = "readDruidDataSource")
    public DruidDataSource readDruidDataSource() {
        return DruidDataSourceBuilder.create().build(env, "spring.datasource.readDruidDataSource.");
    }


    /**
     * DataSource 写数据源配置
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.writeDruidDataSource")
    @Bean(name = "writeDruidDataSource")
    @Qualifier("writeDruidDataSource")
    @Primary
    public DruidDataSource writeDruidDataSource() {

        return DruidDataSourceBuilder.create().build(env, "spring.datasource.writeDruidDataSource.");
    }


}
