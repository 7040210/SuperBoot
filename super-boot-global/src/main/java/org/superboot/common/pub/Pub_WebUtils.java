package org.superboot.common.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.types.Predicate;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseToken;
import org.superboot.base.StatusCode;
import org.superboot.dao.mongo.DataInfoDAO;
import org.superboot.dao.mongo.ErrinfoDAO;
import org.superboot.entity.mongo.DataInfo;
import org.superboot.entity.mongo.ErrorInfo;
import org.superboot.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <b> 配置与网络有关的公用方法 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Async
@Component
public class Pub_WebUtils {


    @Autowired
    private DataInfoDAO dataInfoDAO;

    @Autowired
    private ErrinfoDAO errinfoDAO;

    @Autowired
    private Pub_RedisUtils redisUtils;

    @Autowired
    private Pub_Tools pubTools;


    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String serName;

    @Value("${eureka.instance.appname}")
    private String appName;


    /**
     * 工作中心ID 0-31
     */
    @Value("${Snowflake.workerId}")
    private long workerId;

    /**
     * 数据中心ID 0-31
     */
    @Value("${Snowflake.datacenterId}")
    private long dataCenterId;


    public String getUrl() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new BaseException(StatusCode.EXCEPTION, e);
        }
        return host;
    }

    /**
     * 存储调用日志
     *
     * @param request   请求信息
     * @param returnMsq 返回信息
     * @param messageId 消息ID
     * @param joinPoint 切入点
     * @param count     耗时（毫秒）
     * @return
     */
    public DataInfo saveLog(HttpServletRequest request, String returnMsq, String messageId, JoinPoint joinPoint, long count) throws IOException {
        DataInfo info = new DataInfo();
        info.setDataId(messageId);
        info.setExecDate(DateUtils.getCurrentDate());
        info.setExecTime(DateUtils.getCurrentDateTime());

        info.setLocalName(request.getLocalName());
        info.setLocalAddr(request.getLocalAddr());
        info.setReturnMsq(returnMsq);
        info.setExecCount(count);

        info.setUserName("匿名用户");
        info.setUserId("guest");

        String otherMassgeId = request.getHeader(BaseConstants.OTHER_MESSAGE_ID);
        if (StrUtil.isBlank(otherMassgeId)) {
            String otherToken = request.getHeader(BaseConstants.OTHER_TOKEN_KEY);
            if (StrUtil.isBlank(otherToken)) {
                //校验用户身份信息
                if (StringUtils.isBlank(messageId)) {
                    String tokenStr = request.getHeader(BaseConstants.TOKEN_KEY);
                    if (StringUtils.isNotBlank(tokenStr)) {
                        BaseToken baseToken = redisUtils.getTokenInfo(tokenStr);
                        if (null != baseToken) {
                            info.setUserName(baseToken.getUserName());
                            info.setUserId(String.valueOf(baseToken.getUserId()));
                        }
                    }
                } else {
                    BaseToken token = redisUtils.getSessionInfo(messageId);
                    if (null != token) {
                        info.setUserName(token.getUserName());
                        info.setUserId(String.valueOf(token.getUserId()));
                    }
                }
            } else {
                info.setUserName("第三方接入系统");
                info.setUserId("otherServer");
            }
        } else {
            info.setUserName("第三方接入系统");
            info.setUserId("otherServer");
        }


        info.setWorkerId(workerId);
        info.setDataCenterId(dataCenterId);


        info.setSerUrl(getUrl());
        info.setSerPort(port);
        info.setAppName(appName);
        info.setAppSer(serName);

        info.setReqUrl(request.getRequestURI());
        info.setHeaders(JSON.toJSONString(getHeadersInfo(request)));
        info.setCookie(JSON.toJSONString(request.getCookies()));


        if (null != joinPoint) {
            info.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

            //获取方法名称
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = methodSignature.getMethod();
            ApiOperation mAnnotation = targetMethod.getAnnotation(ApiOperation.class);
            if (null != mAnnotation) {
                info.setInterfaceName(mAnnotation.value());
            }


            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                if (joinPoint.getArgs()[i] instanceof HttpServletRequest) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("HttpServletRequest", "HttpServletRequest");
                    joinPoint.getArgs()[i] = jsonObject;
                }

                if (joinPoint.getArgs()[i] instanceof HttpServletResponse) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("HttpServletResponse", "HttpServletResponse");
                    joinPoint.getArgs()[i] = jsonObject;
                }

                if (joinPoint.getArgs()[i] instanceof Predicate) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("where", "查询条件不存储");
                    joinPoint.getArgs()[i] = jsonObject;
                }
                if (joinPoint.getArgs()[i] instanceof MultipartFile) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("MultipartFile", "MultipartFile");
                    joinPoint.getArgs()[i] = jsonObject;
                }
            }

            //判断用户请求是否设计敏感信息
            if (!filterMap().containsValue(info.getClassMethod())) {
                info.setArgs(JSON.toJSONString(joinPoint.getArgs()));
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", "请求内容包含敏感信息，日志不进行存储");
                info.setArgs(jsonObject.toJSONString());
            }


        }


        info.setParameters(JSON.toJSONString(request.getParameterMap()));

        return dataInfoDAO.insert(info);

    }

    /**
     * 指定部分日志过滤规则，比如此处指定用户登陆、注册、修改密码等操作不进行日志记录，防止出现日志信息造成用户密码的泄露
     *
     * @return
     */
    public HashMap<String, String> filterMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        //添加组织管理员
        hashMap.put("org.superboot.controller.sys.SysUserController.addGroupAdmin", "org.superboot.controller.sys.SysUserController.addGroupAdmin");

        //添加系统管理员
        hashMap.put("org.superboot.controller.sys.SysUserController.addAdmin", "org.superboot.controller.sys.SysUserController.addAdmin");

        //添加普通用户
        hashMap.put("org.superboot.controller.UserController.addUser", "org.superboot.controller.UserController.addUser");

        //修改密码
        hashMap.put("org.superboot.controller.UserController.password", "org.superboot.controller.UserController.password");

        //登陆
        hashMap.put("org.superboot.controller.UserController.login", "org.superboot.controller.UserController.login");

        //添加组织管理员
        hashMap.put("org.superboot.controller.group.GroupUserController.addGroupAdmin", "org.superboot.controller.group.GroupUserController.addGroupAdmin");

        //添加组织人员
        hashMap.put("org.superboot.controller.group.GroupUserController.addGroupUser", "org.superboot.controller.group.GroupUserController.addGroupUser");

        //网关用户注册
        hashMap.put("org.superboot.controller.TokenController.register", "org.superboot.controller.TokenController.register");

        //网关用户登陆
        hashMap.put("org.superboot.controller.TokenController.login", "org.superboot.controller.TokenController.login");

        return hashMap;


    }

    /**
     * 存储调用日志
     *
     * @param request 请求信息
     * @param e       错误信息
     * @return
     */
    public ErrorInfo saveErrLog(HttpServletRequest request, Throwable e) throws IOException {
        ErrorInfo info = new ErrorInfo();
        info.setDataId(request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase()));
        info.setExecDate(DateUtils.getCurrentDate());
        info.setExecTime(DateUtils.getCurrentDateTime());

        info.setLocalName(request.getLocalName());
        info.setLocalAddr(request.getLocalAddr());

        info.setWorkerId(workerId);
        info.setDataCenterId(dataCenterId);


        info.setSerUrl(getUrl());
        info.setSerPort(port);
        info.setAppName(appName);
        info.setAppSer(serName);

        info.setReqUrl(request.getRequestURI());
        info.setHeaders(JSON.toJSONString(getHeadersInfo(request)));
        info.setCookie(JSON.toJSONString(request.getCookies()));

        info.setParameters(JSON.toJSONString(request.getParameterMap()));

        info.setErrMsg(getException(e));


        return errinfoDAO.insert(info);

    }


    /**
     * 获取Header参数信息
     *
     * @param request
     * @return
     */
    public Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public JSONObject getRequestJsonString(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if ("GET".equals(submitMehtod)) {
            return new JSONObject();
        } else {
            String data = getRequestPostStr(request);
            return JSON.parseObject(data);
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

    /**
     * @param e Exception
     * @return 异常内容
     * @category 获取try-catch中的异常内容
     */
    public String getException(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception ex) {

        }
        return ret;
    }

}
