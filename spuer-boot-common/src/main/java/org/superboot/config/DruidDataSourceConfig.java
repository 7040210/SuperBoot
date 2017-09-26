package org.superboot.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * <b> 多数据源配置 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 16:11
 * @Path org.superboot.config.DruidDataSourceConfig
 */
@Configuration
public class DruidDataSourceConfig {


    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.base")
    @Qualifier("baseDataSource")
    @Bean(name = "baseDataSource")
    public DataSource baseDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @ConfigurationProperties(prefix = "spring.datasource.business")
    @Qualifier("businessDataSource")
    @Bean(name = "businessDataSource")
    public DataSource extDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

}
