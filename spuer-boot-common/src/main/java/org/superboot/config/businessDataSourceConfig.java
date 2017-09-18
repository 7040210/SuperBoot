package org.superboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
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

    @Autowired
    private JpaProperties jpaProperties;


    @Autowired
    @Qualifier("businessDataSource")
    private DruidDataSource dataSource;

    @Bean(name = "entityManagerFactoryBusiness")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBusiness(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .properties(getVendorProperties(dataSource))
                .packages("org.superboot.entity.business")//设置实体类所在位置
                .persistenceUnit("businessPersistenceUnit")
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

    @Bean(name = "businessEntityManagerFactory")
    public EntityManagerFactory businessEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.entityManagerFactoryBusiness(builder).getObject();
    }


    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "businessTransactionManager")
    public PlatformTransactionManager businessTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(businessEntityManagerFactory(builder));
    }


    private Map<String, String> getVendorProperties(DruidDataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

}
