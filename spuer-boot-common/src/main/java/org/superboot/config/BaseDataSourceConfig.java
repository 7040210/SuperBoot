package org.superboot.config;

import com.alibaba.druid.pool.DruidDataSource;
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

import javax.persistence.EntityManagerFactory;
import java.util.Map;

/**
 * <b> 主数据源配置 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 15:16
 * @Path org.superboot.config.BaseDataSourceConfig
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "baseEntityManagerFactory",
        transactionManagerRef = "baseTransactionManager",
        basePackages = {"org.superboot.repository.sql.base"}) //设置Repository所在位置
public class BaseDataSourceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("baseDataSource")
    private DruidDataSource dataSource;

    @Bean(name = "entityManagerFactoryBase")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBase(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .properties(getVendorProperties(dataSource))
                .packages("org.superboot.entity.base") //设置实体类所在位置
                .persistenceUnit("basePersistenceUnit")
                .build();
    }


    /**
     * EntityManagerFactory类似于Hibernate的SessionFactory,mybatis的SqlSessionFactory
     * 总之,在执行操作之前,我们总要获取一个EntityManager,这就类似于Hibernate的Session,
     * mybatis的sqlSession.
     *
     * @param builder
     * @return
     */


    @Bean(name = "baseEntityManagerFactory")
    public EntityManagerFactory baseEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.entityManagerFactoryBase(builder).getObject();
    }


    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "baseTransactionManager")
    @Primary
    public PlatformTransactionManager baseTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(baseEntityManagerFactory(builder));
    }


    private Map<String, String> getVendorProperties(DruidDataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }


}
