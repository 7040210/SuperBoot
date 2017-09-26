package org.superboot.pub.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import java.util.List;
import java.util.Locale;

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
    private Pub_Tools Pub_Tools;

    @Resource
    private Pub_LocalTools local;

    @Autowired
    private MessageSource messageSource;

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
    public BaseMessage defaultErrorHandler(HttpServletRequest req, Exception e) {
        return Pub_Tools.genNoMsg(SuperBootCode.NO.getCode(), e.getMessage());
    }

    /**
     * 默认运行异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public BaseMessage runtimeErrorHandler(HttpServletRequest req, RuntimeException e) {
        return Pub_Tools.genNoMsg(SuperBootCode.NO.getCode(), e.getMessage());
    }


    /**
     * 字段验证异常处理,统一处理增加国际化支持
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseMessage methodArgumentNotValidExceptionErrorHandler(HttpServletRequest req, Exception e) {
        MethodArgumentNotValidException c = (MethodArgumentNotValidException) e;
        List<FieldError> errors = c.getBindingResult().getFieldErrors();
        Locale locale = LocaleContextHolder.getLocale();
        StringBuffer errorMsg = new StringBuffer();
        for (FieldError err : errors) {
            String message = messageSource.getMessage(err, locale);
            errorMsg.append(err.getField() + ":" + message + ",");
        }
        return Pub_Tools.genNoMsg(SuperBootCode.PARAMS_NOT_VALIDATE, errorMsg.subSequence(0, errorMsg.length() - 1));
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
    public BaseMessage superBootErrorHandler(HttpServletRequest req, Exception e) {
        int code = ((SuperBootException) e).getCode();
        String data = local.getMessage(code);
        return Pub_Tools.genNoMsg(code, data);
    }


}
