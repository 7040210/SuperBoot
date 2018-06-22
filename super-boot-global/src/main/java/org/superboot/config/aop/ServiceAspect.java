package org.superboot.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
 * <b> 配置Service中数据源读写分离,先执行路径拦截，其次执行方法注解的拦截，最后执行JPA的拦截 </b>
 * <p>
 * 功能描述:Service方法要有严格约束，
 * 查询方法必须用 get或者find开头，
 * 更新用set或者update 开头
 * 添加用 add 开头
 * 删除用 del 开头
 * </p>
 */
@Aspect
@Order(1)
@Component
public class ServiceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Pointcut("execution( * org.superboot.service..*.*(..))")
    public void aspect() {
    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        //根据注解判断数据源
        if (targetMethod.isAnnotationPresent(TargetDataSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDataSource.class).dataSource();
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        } else {

            if (targetMethod.getName().startsWith("get") || targetMethod.getName().startsWith("find")) {
                //默认使用读数据源
                DynamicDataSourceHolder.setDataSource(DataSourceConfig.READ_DATASOURCE_KEY);
            } else {
                //除了读取外其他均用主数据源
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
