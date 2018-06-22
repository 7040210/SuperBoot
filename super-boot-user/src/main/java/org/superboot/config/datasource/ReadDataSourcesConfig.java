package org.superboot.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * <b> 从数据源提供读服务 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Configuration
@MapperScan(basePackages = ReadDataSourcesConfig.PACKAGES, sqlSessionFactoryRef = "readSqlSessionFactory")
public class ReadDataSourcesConfig {

    static final String PACKAGES = "org.superboot.dao.mybatis";

    private static final String MAPPER_LOCAL = "classpath:mapper/*.xml";

    private static final String CONFIG_LOCAL = "classpath:mybatis-config.xml";


    @Bean(name = "readSqlSessionFactory")
    public SqlSessionFactory readSqlSessionFactory(@Qualifier("readDruidDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        //加载配置文件
        sessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIG_LOCAL));
        //加载映射文件
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCAL));
        return sessionFactoryBean.getObject();
    }

}
