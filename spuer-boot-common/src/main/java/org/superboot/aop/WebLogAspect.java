package org.superboot.aop;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.*;
import org.superboot.pub.Pub_JWTUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
    private  long workerId;

    /**
     * 数据中心ID 0-31
     */
    @Value("${Snowflake.datacenterId}")
    private  long datacenterId;


    private JoinPoint joinPoint;


    private HttpServletRequest request;

    private String userName;

    private long userId;

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
            setUserName(token.getUsername());
            setUserId(token.getUserid());
        } else {
            setUserName("guest");
            setUserId(-1);
            //判断类是否不需要TOKEN验证
            Annotation classAnnotation = joinPoint.getSignature().getDeclaringType().getAnnotation(NotValidateToken.class);
            if (null == classAnnotation) {
                Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(joinPoint.getSignature().getName())) {
                        Annotation mAnnotation = method.getAnnotation(MethedNotValidateToken.class);
                        if (null == mAnnotation) {
                            throw new SuperBootException(SuperBootCode.UNAUTHORIZED);
                        }
                    }
                }

            }

        }


        // 记录下请求内容
        logger.debug("*************************请求开始*************************");
        logger.debug("请求地址 : " + request.getRequestURL().toString());
        logger.debug("请求类型 : " + request.getMethod());
        logger.debug("调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.debug("传入参数 : " + Arrays.toString(joinPoint.getArgs()));

        setJoinPoint(joinPoint);
        setRequest(request);

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.debug("返回内容 : " + ret);
        logger.debug("共用耗时: " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
        logger.debug("*************************请求结束*************************");
        MDC.remove("workerId");
        MDC.remove("datacenterId");
        MDC.remove("run.ServerIP");
        MDC.remove("run.ServerPort");
        MDC.remove("userName");
        MDC.remove("platform");
        MDC.remove("version");
        MDC.remove("lang");

        BasicDBObject logInfo = getBasicDBObject(getRequest(), getJoinPoint(), ret);
        logger.info(logInfo);

    }


    /**
     * 拼接日志信息
     *
     * @param request
     * @param joinPoint
     * @return
     */
    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint, Object ret) {
        // 基本信息
        BasicDBObject r = new BasicDBObject();
        r.append("requestURL", request.getRequestURL().toString());
        r.append("requestURI", request.getRequestURI());
        r.append("queryString", request.getQueryString());
        r.append("remoteAddr", request.getRemoteAddr());
        r.append("remoteHost", request.getRemoteHost());
        r.append("remotePort", request.getRemotePort());
        r.append("localAddr", request.getLocalAddr());
        r.append("localName", request.getLocalName());
        r.append("method", request.getMethod());
        r.append("headers", getHeadersInfo(request));

        r.append("workerId", String.valueOf(workerId));
        r.append("datacenterId", String.valueOf(datacenterId));
        r.append("serverIP",  System.getProperty("run.ServerIP"));
        r.append("ServerPort", System.getProperty("run.ServerPort"));
        r.append("userName",getUserName());
        r.append("userId", getUserId());

        r.append("parameters", request.getParameterMap());
        r.append("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        r.append("args", JSON.toJSON(joinPoint.getArgs()));
        r.append("return", JSON.toJSON(ret));

        r.append("execTime",(System.currentTimeMillis() - startTime.get()) + "毫秒");
        return r;
    }

    /**
     * 获取Header参数信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


    public JoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void setJoinPoint(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
