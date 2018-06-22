package org.superboot.config.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.common.pub.Pub_WebUtils;
import org.superboot.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <b> 对WEB请求进行统一的AOP拦截处理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Aspect
@Order(0)
@Component
public class ControllerAspect {

    private Logger log = LoggerFactory.getLogger(getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Value("${spring.application.name}")
    private String module_id;


    @Autowired
    private Pub_RedisUtils redisUtils;

    @Autowired
    private Pub_WebUtils webUtils;

    private JoinPoint joinPoint;

    @Autowired
    private Pub_Tools pubTools;

    @Pointcut("execution(  * org.superboot.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        setJoinPoint(joinPoint);

        log.debug("--------------执行权限检查------------------");
        boolean checkToken = true;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //判断类是否为免TOKEN验证类
        Annotation classAnnotation = methodSignature.getDeclaringType().getAnnotation(NotValidateToken.class);
        if (null != classAnnotation) {
            checkToken = false;
        }

        //判断方法是否为免TOKEN验证类
        Method targetMethod = methodSignature.getMethod();
        Annotation mAnnotation = targetMethod.getAnnotation(MethedNotValidateToken.class);
        if (null != mAnnotation) {
            checkToken = false;
        }

        log.debug("--------------执行获取MessageID操作------------------");
        //获取消息ID
        String messageID = request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase());
        //判断是否需要Token验证
        if (checkToken) {
            log.debug("--------------执行校验权限操作------------------");
            //需要验证的请求发起必须来源网关，否则视为无权限请求操作
            if (StringUtils.isBlank(messageID)) {
                //此处逻辑主要为了内部测试使用，使用前需要先调用网关进行用户登陆操作
                String token = request.getHeader(BaseConstants.TOKEN_KEY);
                if (StringUtils.isNotBlank(token)) {
                    BaseToken baseToken = redisUtils.getTokenInfo(token);
                    if (null != baseToken) {
                        if (null != baseToken.getErrCode()) {
                            throw new BaseException(baseToken.getErrCode());
                        }
                        //读取Redis中用户的信息
                        if (null != baseToken) {

                            //判断账号是否已经停用
                            if (StrUtil.isNotBlank(baseToken.getEndTime())) {
                                long num = DateUtils.getSecondDiffer(DateUtils.getCurrentDateTime(), baseToken.getEndTime());
                                if (0 < num) {
                                    throw new BaseException(StatusCode.ACCOUNT_EXPIRED);
                                }
                            }

                            if (!checkAuth(methodSignature, baseToken)) {

                                //检验是否授权服务内部之间的调用
                                String feignKey = attributes.getRequest().getHeader(BaseConstants.FEIGN_KEY);
                                if (StrUtil.isBlank(feignKey)) {
                                    throw new BaseException(StatusCode.UNAUTHORIZED_OPERATION);
                                }

                            }
                        } else {
                            throw new BaseException(StatusCode.UNAUTHORIZED);
                        }
                    } else {
                        throw new BaseException(StatusCode.UNAUTHORIZED);
                    }
                } else {
                    //判断是否为内部调用，非内部调用则直接走第三方判断
                    String feignKey = attributes.getRequest().getHeader(BaseConstants.FEIGN_KEY);
                    if (StrUtil.isBlank(feignKey)) {
                        //判断调用接口的服务是否为第三方系统
                        String oId = request.getHeader(BaseConstants.OTHER_MESSAGE_ID);
                        if (StrUtil.isBlank(oId)) {
                            String oToken = request.getHeader(BaseConstants.OTHER_TOKEN_KEY);
                            if (StrUtil.isBlank(oToken)) {
                                throw new BaseException(StatusCode.UNAUTHORIZED);
                            }
                            //验证TOKEN有效性
                            pubTools.checkOtherToken(oToken);

                        }

                        //校验第三方系统是否有权限直接访问此接口
                        if (!checkOtherAuth(methodSignature)) {
                            throw new BaseException(StatusCode.UNAUTHORIZED);
                        }
                    } else {
                        if (!BaseConstants.DEFAULT_FEIGN_TOKEN.equals(feignKey)) {
                            throw new BaseException(StatusCode.UNAUTHORIZED);
                        }
                    }


                }

            } else {
                //校验用户身份信息
                BaseToken token = redisUtils.getSessionInfo(messageID);
                if (null == token) {
                    throw new BaseException(StatusCode.UNAUTHORIZED);
                }

                //判断账号是否已经停用
                if (StrUtil.isNotBlank(token.getEndTime())) {
                    long num = DateUtils.getSecondDiffer(DateUtils.getCurrentDateTime(), token.getEndTime());
                    if (0 < num) {
                        throw new BaseException(StatusCode.ACCOUNT_EXPIRED);
                    }
                }

                if (!checkAuth(methodSignature, token)) {
                    throw new BaseException(StatusCode.UNAUTHORIZED_OPERATION);
                }
            }
            log.debug("--------------校验权限操作完成------------------");
        }

    }


    /**
     * 判断接口是否允许第三方接入访问
     *
     * @param methodSignature
     * @return
     */
    public boolean checkOtherAuth(MethodSignature methodSignature) {
        AuthRoles cAuth = methodSignature.getMethod().getAnnotation(AuthRoles.class);
        if (null == cAuth) {
            cAuth = (AuthRoles) methodSignature.getDeclaringType().getAnnotation(AuthRoles.class);
        }

        if (null != cAuth) {
            if (0 < cAuth.roles().length) {
                String[] roles = cAuth.roles();
                for (String role : roles) {
                    if (BaseConstants.OTHER_USER_NAME.equals(role)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 根据注解判断接口是否限定了特殊访问的角色,配置级别方法级大于类级
     *
     * @param methodSignature 切面对象
     * @param token           用户信息
     * @return
     */
    public boolean checkAuth(MethodSignature methodSignature, BaseToken token) {
        //判断方法是否需要权限认证
        boolean success = false;
        AuthRoles cAuth = methodSignature.getMethod().getAnnotation(AuthRoles.class);
        if (null != cAuth) {
            success = checkAuthRole(cAuth, token);
            if (success) {
                return success;
            }
        }
        //方法校验未通过校验类
        if (!success && null == cAuth) {
            //判断类里所有方法是否全部需要权限认证
            cAuth = (AuthRoles) methodSignature.getDeclaringType().getAnnotation(AuthRoles.class);
            if (null != cAuth) {
                success = checkAuthRole(cAuth, token);
                if (success) {
                    return success;
                }
            }
        }

        //类校验不通过校验授权
        if (!success) {
            //判断用户分配的菜单关联的权限信息
            String key = module_id + methodSignature.getDeclaringTypeName() + methodSignature.getName();
            //判断用户是否有执行的权限
            if (null == redisUtils.getUserResource(token.getUserId())) {
                throw new BaseException(StatusCode.UNAUTHORIZED);
            }

            return redisUtils.getUserResource(token.getUserId()).containsKey(key);
        }

        return success;
    }

    /**
     * 校验用户是否有权限访问接口角色
     *
     * @param authRoles 注解信息
     * @param token     用户信息
     * @return
     */
    public boolean checkAuthRole(AuthRoles authRoles, BaseToken token) {
        if (null != authRoles) {
            if (0 < authRoles.roles().length) {
                String[] roles = authRoles.roles();
                for (String role : roles) {
                    //如果接口不限时访问角色则直接放行
                    if (BaseConstants.ALL_USER_NAME.equals(role)) {
                        return true;
                    }

                    //如果接口设置授权用户可以访问则直接放行
                    if (BaseConstants.GEN_USER_NAME.equals(role)) {
                        return true;
                    }

                    if (-1 != token.getUserRole().indexOf(role)) {
                        return true;
                    }
                }
            }
            return false;
        }

        return true;
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String retMsg = "";
        //获取消息ID
        String messageID = request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase());

        if (ret instanceof BaseResponse) {
            BaseResponse baseResponse = ((BaseResponse) ret);
            //独立处理返回结果
            int statusCode = baseResponse.getCode();
            //获取响应数据
            Object data = baseResponse.getData();
            if (null == data) {
                data = new JSONObject();
            }
            retMsg = JSON.toJSONString(pubTools.genOkMsg(statusCode, JSONObject.toJSON(data)));

        } else if (ret instanceof BaseMessage) {
            retMsg = JSON.toJSONString(ret);
        }//只有项目上的才需要单独处理,主要用于
        else if (joinPoint.getSignature().getDeclaringTypeName().startsWith(BaseConstants.BASE_PACKAGE)) {
            //独立处理返回结果
            if (null != ret) {
                retMsg = ret.toString();
            }

        }

        //记录日志信息
        webUtils.saveLog(request, retMsg, messageID, getJoinPoint(), (System.currentTimeMillis() - startTime.get()));

    }


    public JoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void setJoinPoint(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

}
