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
import org.superboot.config.datasource.DynamicDataSourceHolder;
import org.superboot.config.datasource.TargetDataSource;

import java.lang.reflect.Method;

/**
 * <b> 数据源切换注解的切面，先执行路径拦截，其次执行方法注解的拦截，最后执行JPA的拦截  </b>
 * <p>
 * 功能描述:此种方式为需要在方法上添加注解来指定数据源
 * </p>
 */
@Aspect
@Order(2)
@Component
public class TargetDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(org.superboot.config.datasource.TargetDataSource)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        //根据注解判断数据源
        if (targetMethod.isAnnotationPresent(TargetDataSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDataSource.class).dataSource();
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        }
        logger.debug("操作使用数据源：" + DynamicDataSourceHolder.getDataSource());
        //执行方法
        Object result = pjp.proceed();
        DynamicDataSourceHolder.clearDataSource();

        return result;

    }
}
