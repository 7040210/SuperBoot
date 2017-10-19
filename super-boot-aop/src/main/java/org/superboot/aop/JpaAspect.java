package org.superboot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.superboot.pub.Pub_Tools;
import org.superboot.pub.Pub_Utils;
import org.superboot.utils.DateUtils;

/**
 * <b> 增加JPA数据库操作前的AOP拦截，增加个性化操作 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 11:22
 * @Path org.superboot.aop.JpaAspect
 */
@Aspect
@Component
public class JpaAspect {


    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Autowired
    private Pub_Utils pubUtils;

    @Autowired
    private Pub_Tools pubTools;

    /**
     * 设置JPA切入点
     */
    @Pointcut("execution(public * org.superboot.repository.sql..*.*(..))")
    public void JpaAspect() {

    }

    @Before("JpaAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        logger.debug("*************************JPA操作开始*************************");
        logger.debug("调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        if (joinPoint.getSignature().getName().startsWith("save") ) {
            logger.debug("默认字段赋值开始");
            if (0 < joinPoint.getArgs().length) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    Object o = joinPoint.getArgs()[i];
                    //设置时间戳
                    joinPoint.getArgs()[i] = pubTools.setFieldValue("ts", DateUtils.getTimestamp(), o);
                    //设置删除标志
                    joinPoint.getArgs()[i] = pubTools.setFieldValue("dr", 0, o);
                    //对主键进行赋值
                    joinPoint.getArgs()[i] = pubUtils.setIdValue(o);
                }
            }
            logger.debug("默认字段赋值结束");
        }

    }

    @AfterReturning(returning = "ret", pointcut = "JpaAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("返回内容 : " + ret);
        logger.debug("共用耗时: " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
        logger.debug("*************************JPA操作结束*************************");
    }


}
