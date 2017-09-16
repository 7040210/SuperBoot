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
 * <b> 主数据配置 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 15:26
 * @Path org.superboot.config.RepositoryBaseConfig
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="entityManagerFactoryBase",
        transactionManagerRef="transactionManagerBase",
        basePackages= { "org.superboot.repository.sql.base" })
public class RepositoryBaseConfig {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("dataSourceBase")
    private DataSource BaseDS;

    @Bean(name = "entityManagerBase")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryBase(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryBase")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBase (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(BaseDS)
                .properties(getVendorProperties(BaseDS))
                .packages("org.superboot.entity.base") //设置实体类所在位置
                .persistenceUnit("BasePersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerBase")
    @Primary
    PlatformTransactionManager transactionManagerBase(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryBase(builder).getObject());
    }
}
