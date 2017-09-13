package org.superboot.pub.handler;

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
     * 配置异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseMessage defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        int code = SuperBootCode.NO.getCode();
        String message = e.toString();
        Object data = e.toString();
        return Pub_Tools.genNoMsg(code, message, data);
    }


    /**
     * 配置异常处理
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


}
