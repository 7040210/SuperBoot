package org.superboot.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.superboot.base.BaseConstants;
import org.superboot.config.datasource.DataSourceConfig;
import org.superboot.config.datasource.DynamicDataSourceHolder;
import org.superboot.config.datasource.TargetDataSource;

import java.lang.reflect.Method;

/**
 * <b> 实现动态切换数据源，先执行路径拦截，其次执行方法注解的拦截，最后执行JPA的拦截  </b>
 * <p>
 * 功能描述:根据方法名称动态识别切换数据源，
 * 目前项目用到读写分离所以直接在数据库操作的时候根据操作方式判断数据源
 * 如果项目需要根据服务定义的话可以配置其他的拦截模式
 * </p>
 */
@Aspect
@Order(3)
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("execution(public * org.superboot.dao.jpa..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        //根据注解判断数据源
        if (targetMethod.isAnnotationPresent(TargetDataSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDataSource.class).dataSource();
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        } else {
            //保存使用写数据源
            if (pjp.getSignature().getName().startsWith("save")) {
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.WRITE_DATASOURCE_KEY);
            }
            //删除使用写数据源
            else if (pjp.getSignature().getName().startsWith("delete")) {
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.WRITE_DATASOURCE_KEY);
            }

            //查询使用读数据源
            else if (pjp.getSignature().getName().startsWith("find")) {
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.READ_DATASOURCE_KEY);
            }

            //保存使用写数据源
            else if (pjp.getSignature().getName().startsWith("get")) {
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.READ_DATASOURCE_KEY);
            } else {
                //默认使用写数据源
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.WRITE_DATASOURCE_KEY);
            }
        }
        logger.info("操作使用数据源：" + DynamicDataSourceHolder.getDataSource());
        //执行方法
        Object result = pjp.proceed();
        DynamicDataSourceHolder.clearDataSource();

        return result;

    }


}
