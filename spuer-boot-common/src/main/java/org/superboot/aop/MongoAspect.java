package org.superboot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.superboot.pub.Pub_Tools;
import org.superboot.pub.Pub_Utils;

/**
 * <b> Mongo数据操作拦截器 </b>
 * <p>
 * 功能描述:提供对Mongo数据库操作的时候的批处理
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 9:11
 * @Path org.superboot.aop.MongoAspect
 */
@Aspect
@Component
public class MongoAspect {

    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 设置JPA切入点
     */
    @Pointcut("execution(public * org.superboot.repository.mongodb..*.*(..))")
    public void MongoAspect() {

    }

    @Before("MongoAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        logger.debug("*************************Mongo操作开始*************************");
        logger.debug("调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        if (joinPoint.getSignature().getName().startsWith("save") ||joinPoint.getSignature().getName().startsWith("insert") ) {
            logger.debug("默认字段赋值开始");
            if (0 < joinPoint.getArgs().length) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    Object o = joinPoint.getArgs()[i];
                    //设置删除标志
                    joinPoint.getArgs()[i] = Pub_Tools.setFieldValue("dr", 0, o);
                    //对主键进行赋值
                    joinPoint.getArgs()[i] = Pub_Utils.setIdValue(o);
                }
            }
            logger.debug("默认字段赋值结束");
        }

    }

    @AfterReturning(returning = "ret", pointcut = "MongoAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("返回内容 : " + ret);
        logger.debug("共用耗时: " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
        logger.debug("*************************Mongo操作结束*************************");
    }

}
