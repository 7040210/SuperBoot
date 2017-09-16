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
 * <b> 业务数据配置 </b>
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
@EnableJpaRepositories(entityManagerFactoryRef="entityManagerFactoryExt",
        transactionManagerRef="transactionManagerExt",
        basePackages= { "org.superboot.repository.sql.ext" })
public class RepositoryExtConfig {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired @Qualifier("dataSourceExt")
    private DataSource extDS;

    @Bean(name = "entityManagerExt")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryExt(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryExt")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryExt (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(extDS)
                .properties(getVendorProperties(extDS))
                .packages("org.superboot.entity.ext")
                .persistenceUnit("ExtPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerExt")
    PlatformTransactionManager transactionManagerExt(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryExt(builder).getObject());
    }

}
