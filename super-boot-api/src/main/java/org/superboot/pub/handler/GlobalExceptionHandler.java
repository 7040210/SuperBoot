package org.superboot.pub.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.superboot.base.BaseMessage;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.pub.Pub_LocalTools;
import org.superboot.pub.Pub_Tools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <b> 统一异常处理 </b>
 * <p>
 * 功能描述:处理逻辑异常造成的错误
 * </p>
 *
 * @author jesion
 * @date 2017/9/8
 * @time 13:37
 * @Path org.superboot.pub.handler.GlobalExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    @Resource
    Pub_Tools Pub_Tools;

    @Resource
    Pub_LocalTools local;

    /**
     * 默认异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseMessage defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.NO);
    }


    /**
     * 自定义异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = SuperBootException.class)
    @ResponseBody
    public BaseMessage superBootErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        int code = ((SuperBootException) e).getCode();
        String data = local.getMessage(code);
        return Pub_Tools.genNoMsg(code, data);
    }


    /**
     * 坏的凭据异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseBody
    public BaseMessage badCredentialsExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.LOGIN_INVALID);
    }


    /**
     * 用户找不到异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseBody
    public BaseMessage usernameNotFoundExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.ACCOUNT_NOT_FIND);
    }


    /**
     * 用户状态异常异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = AccountStatusException.class)
    @ResponseBody
    public BaseMessage accountStatusExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.USER_STATUS_EXCEPTION);
    }


    /**
     * 账户过期异常异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = AccountExpiredException.class)
    @ResponseBody
    public BaseMessage accountExpiredExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.ACCOUNT_EXPIRED);
    }

    /**
     * 账户锁定异常异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = LockedException.class)
    @ResponseBody
    public BaseMessage lockedExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.ACCOUNT_LOCKED);
    }


    /**
     * 账户不可用异常异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = DisabledException.class)
    @ResponseBody
    public BaseMessage disabledExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.ACCOUNT_DISABLED);
    }


    /**
     * 证书过期异常异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = CredentialsExpiredException.class)
    @ResponseBody
    public BaseMessage credentialsExpiredExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.TOKEN_INVALID);
    }


    /**
     * 配置TOKEN异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public BaseMessage accessDeniedExceptionErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Pub_Tools.genNoMsg(SuperBootCode.UNAUTHORIZED);
    }


}
