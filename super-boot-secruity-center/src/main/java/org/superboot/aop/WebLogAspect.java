package org.superboot.aop;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.*;
import org.superboot.pub.Pub_JWTUtils;
import org.superboot.pub.Pub_Tools;
import org.superboot.utils.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * <b> 对WEB请求进行统一的AOP拦截处理 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/6
 * @time 9:48
 * @Path org.superboot.aop.WebLogAspect
 */
@Aspect
@Component
public class WebLogAspect {

    private Logger logger = LogManager.getLogger(getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private Pub_JWTUtils jwtTokenUtil;

    /**
     * 工作中心ID 0-31
     */
    @Value("${Snowflake.workerId}")
    private static long workerId;

    /**
     * 数据中心ID 0-31
     */
    @Value("${Snowflake.datacenterId}")
    private static long datacenterId;


    @Pointcut("execution(public * org.superboot.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        MDC.put("workerId", String.valueOf(workerId));
        MDC.put("datacenterId", String.valueOf(datacenterId));


        String serverIP = System.getProperty("run.ServerIP");
        MDC.put("serverIP", serverIP);


        String serverPort = System.getProperty("run.ServerPort");
        MDC.put("serverPort", serverPort);

        //根据TOKEN设置用户名
        BaseToken token = jwtTokenUtil.getTokenInfo(request);
        if (token != null) {
            MDC.put("userName", token.getUsername());
        } else {
            //判断类是否不需要TOKEN验证
            Annotation classAnnotation = Pub_Tools.getClassAnnotationByAnnotation(joinPoint.getSignature(), NotValidateToken.class);
        }


        if (null != joinPoint.getArgs() && 0 < joinPoint.getArgs().length) {

            //检查必传参数信息
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                if (joinPoint.getArgs()[i] instanceof BaseRequest) {
                    String platform = (String) ReflectionUtils.getFieldValue(joinPoint.getArgs()[i], "platform");
                    if (StringUtils.isBlank(platform)) {
                        throw new SuperBootException(SuperBootCode.PLATFORM_NOT_FIND);
                    }
                    MDC.put("platform", platform);

                    String version = (String) ReflectionUtils.getFieldValue(joinPoint.getArgs()[i], "version");
                    if (StringUtils.isBlank(version)) {
                        throw new SuperBootException(SuperBootCode.VERSION_NOT_FIND);
                    }
                    MDC.put("version", version);

                    String lang = (String) ReflectionUtils.getFieldValue(joinPoint.getArgs()[i], "lang");
                    //如果语言为空使用默认
                    if (StringUtils.isBlank(lang)) {
                        lang = LocaleContextHolder.getLocale().toString();
                    }
                    MDC.put("lang", lang);
                }

            }

        } else {
            throw new SuperBootException(SuperBootCode.PARAMS_NOT_FIND);
        }


        // 记录下请求内容
        logger.info("*************************请求开始*************************");
        logger.info("请求地址 : " + request.getRequestURL().toString());
        logger.info("请求类型 : " + request.getMethod());
        logger.info("调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("传入参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("返回内容 : " + ret);
        logger.info("共用耗时: " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
        logger.info("*************************请求结束*************************");
        MDC.remove("workerId");
        MDC.remove("datacenterId");
        MDC.remove("run.ServerIP");
        MDC.remove("run.ServerPort");
        MDC.remove("userName");
        MDC.remove("platform");
        MDC.remove("version");
        MDC.remove("lang");
    }
}
