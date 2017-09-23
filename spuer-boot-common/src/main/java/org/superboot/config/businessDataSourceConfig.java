package org.superboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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
@EnableJpaRepositories(entityManagerFactoryRef = "businessEntityManagerFactory",
        transactionManagerRef = "businessTransactionManager",
        basePackages = {"org.superboot.repository.sql.business"}) //设置Repository所在位置
public class businessDataSourceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;


    @Autowired
    @Qualifier("businessDataSource")
    private DataSource dataSource;


    /**
     * EntityManagerFactory类似于Hibernate的SessionFactory,mybatis的SqlSessionFactory
     * 总之,在执行操作之前,我们总要获取一个EntityManager,这就类似于Hibernate的Session,
     * mybatis的sqlSession.
     *
     * @return
     */
    @Bean(name = "businessEntityManagerFactory")
    public EntityManagerFactory businessEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("org.superboot.entity.business");
        factory.setDataSource(dataSource);//数据源

        factory.setJpaPropertyMap(jpaProperties.getProperties());
        factory.afterPropertiesSet();//在完成了其它所有相关的配置加载以及属性设置后,才初始化
        return factory.getObject();
    }

    /**
     * 获取事务管理器
     * @return
     */
    @Bean(name = "businessTransactionManager")
    public PlatformTransactionManager businessTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(this.businessEntityManagerFactory());
        return jpaTransactionManager;
    }


}
