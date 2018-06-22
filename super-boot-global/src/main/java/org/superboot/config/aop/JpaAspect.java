package org.superboot.config.aop;

import org.superboot.base.BaseConstants;
import org.superboot.base.BaseToken;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.common.pub.Pub_Tools;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <b> 增加JPA数据库操作前的AOP拦截，增加个性化操作 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Aspect
@Order(4)
@Component
public class JpaAspect {


    private Logger logger = LoggerFactory.getLogger(getClass());


    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private Pub_Tools pubTools;


    @Autowired
    private Pub_RedisUtils redisUtils;

    /**
     * 设置JPA切入点
     */
    @Pointcut("execution(public * org.superboot.dao.jpa..*.*(..))")
    public void JpaAspect() {

    }

    @Before("JpaAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        logger.debug("*************************JPA操作开始*************************");
        logger.debug("调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //特殊业务保存的时候进行赋值操作
        if (joinPoint.getSignature().getName().startsWith("save")) {
            logger.debug("默认字段赋值开始");
            // 接收到请求，记录请求内容
            BaseToken token = null;
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                String messageID = request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase());
                if (StringUtils.isNotBlank(messageID)) {
                    token = redisUtils.getSessionInfo(messageID);
                } else {
                    String tokenStr = request.getHeader(BaseConstants.TOKEN_KEY);
                    token = redisUtils.getTokenInfo(tokenStr);
                }

            }

            if (0 < joinPoint.getArgs().length) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    //如果传入对象是数组，则需要循环处理里边每个对象
                    if (joinPoint.getArgs()[i] instanceof List) {
                        List list = (List) joinPoint.getArgs()[i];
                        for (int j = 0; j < list.size(); j++) {
                            ((List) joinPoint.getArgs()[i]).set(j, setValue(list.get(j), token));
                        }

                    } else {
                        joinPoint.getArgs()[i] = setValue(joinPoint.getArgs()[i], token);
                    }
                }
            }
            logger.debug("默认字段赋值结束");
        }

    }

    /**
     * 设置字段默认值
     *
     * @param o     VO对象信息
     * @param token 用户信息
     * @return
     * @throws Throwable
     */
    private Object setValue(Object o, BaseToken token) throws Throwable {

        //设置时间戳 每次操作都刷新时间戳
        pubTools.setFieldValue("ts", DateUtils.getTimestamp(), o);
        //设置删除标志
        if (null == pubTools.getFieldValue("dr", o)) {
            pubTools.setFieldValue("dr", BaseConstants.DATA_STATUS_OK, o);
        }
        //判断ID列是否有值 如果有则为修改 否则为创建 理论上不运行代码里对主键进行赋值操作
        if (null == pubTools.getIdValue(o)) {
            //对主键进行赋值
            pubTools.setIdValue(o);

            //设置创建人
            if (null == pubTools.getFieldValue("createby", o)) {
                //判断当前用户是否为空，不为空则设置值
                if (null != token) {
                    pubTools.setFieldValue("createby", token.getUserId(), o);
                }
            }
            //设置创建时间
            if (null == pubTools.getFieldValue("createtime", o)) {
                pubTools.setFieldValue("createtime", DateUtils.getTimestamp(), o);
            }


        } else {

            //设置创建时间
            if (null == pubTools.getFieldValue("createtime", o)) {
                pubTools.setFieldValue("createtime", DateUtils.getTimestamp(), o);
            }

            //设置修改人
            if (null != token) {
                //如果创建人为空，设置修改人为创建人
                if (null == pubTools.getFieldValue("createby", o)) {
                    pubTools.setFieldValue("createby", token.getUserId(), o);
                }
                pubTools.setFieldValue("lastmodifyby", token.getUserId(), o);
            }

            //设置修改时间
            pubTools.setFieldValue("lastmodifytime", DateUtils.getTimestamp(), o);
        }

        return o;
    }

    @AfterReturning(returning = "ret", pointcut = "JpaAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("返回内容 : " + ret);
        logger.debug("共用耗时: " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
        logger.debug("*************************JPA操作结束*************************");
    }


}
