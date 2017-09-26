package org.superboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * <b> 业务数据源配置 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 18:51
 * @Path org.superboot.config.businessDataSourceConfig
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "businessEntityManagerFactory",
        transactionManagerRef = "businessTransactionManager",
        basePackages = {"org.superboot.repository.sql.business"}) //设置Repository所在位置
public class businessDataSourceConfig {


    private static final String PACKAGES_TO_SCAN = "org.superboot.entity.business";


    @Autowired
    @Qualifier("businessDataSource")
    private DataSource dataSource;


    //配置EntityManager实体
    @Bean(name = "businessEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return businessEntityManagerFactory(builder).getObject().createEntityManager();
    }


    //配置EntityManager工厂实体
    @Bean(name = "businessEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean businessEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .properties(getVendorProperties(dataSource))
                .packages(PACKAGES_TO_SCAN.split(",")) //设置应用creditDataSource的基础包名
                .persistenceUnit("businessPersistenceUnit")
                .build();
    }


    //注入jpa配置实体
    @Autowired
    private JpaProperties jpaProperties;

    //获取jpa配置信息
    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }


    //配置事务
    @Primary
    @Bean(name = "businessTransactionManager")
    public PlatformTransactionManager transactionManagerBook(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(businessEntityManagerFactory(builder).getObject());
    }

}
