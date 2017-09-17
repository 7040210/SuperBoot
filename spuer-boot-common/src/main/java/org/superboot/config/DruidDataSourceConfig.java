package org.superboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.util.Properties;

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
    public DruidDataSource baseDataSource() {
        return DruidDataSourceBuilder.create().build();
    }



    @ConfigurationProperties(prefix = "spring.datasource.business")
    @Qualifier("businessDataSource")
    @Bean(name = "businessDataSource")
    public DruidDataSource  extDataSource(){
        return DruidDataSourceBuilder.create().build();
    }



//    @Bean(name = "baseDataSource")
//    @Qualifier("baseDataSource")
//    @Primary
//    public DataSource baseDataSource(Environment env) {
//        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
//        Properties prop = build(env, "spring.datasource.base.");
//        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
//        ds.setUniqueResourceName("baseDB");
//        ds.setPoolSize(5);
//        ds.setXaProperties(prop);
//        return ds;
//
//    }
//
//    @Qualifier("businessDataSource")
//    @Bean(name = "businessDataSource")
//    public DataSource businessDataSource(Environment env) {
//
//        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
//        Properties prop = build(env, "spring.datasource.business.");
//        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
//        ds.setUniqueResourceName("businessDB");
//        ds.setPoolSize(5);
//        ds.setXaProperties(prop);
//
//        return ds;
//    }

    @Bean("sysJdbcTemplate")
    public JdbcTemplate sysJdbcTemplate(@Qualifier("baseDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean("busJdbcTemplate")
    public JdbcTemplate busJdbcTemplate(@Qualifier("businessDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    private Properties build(Environment env, String prefix) {

        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driver-class-name", ""));
        prop.put("initialSize", env.getProperty(prefix + "initial-size", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "max-active", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "min-idle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "max-wait", Integer.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "pool-prepared-statements", Boolean.class));

        prop.put("maxPoolPreparedStatementPerConnectionSize",
                env.getProperty(prefix + "max-pool-prepared-statement-per-connection-size", Integer.class));

        prop.put("validationQuery", env.getProperty(prefix + "validation-query"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validation-query-timeout", Integer.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "test-on-borrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "test-on-return", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "test-while-idle", Boolean.class));
        prop.put("timeBetweenEvictionRunsMillis",
                env.getProperty(prefix + "time-between-eviction-runs-millis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "min-evictable-idle-time-millis", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));

        return prop;
    }
}
