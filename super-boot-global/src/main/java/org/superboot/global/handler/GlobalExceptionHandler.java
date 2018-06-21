package org.superboot.global.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseMessage;
import org.superboot.base.StatusCode;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.common.pub.Pub_WebUtils;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Locale;

/**
 * <b> 统一异常处理 </b>
 * <p>
 * 功能描述:处理逻辑异常造成的错误
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    @Resource
    private Pub_Tools pubTools;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Pub_WebUtils webUtils;

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
    public BaseMessage defaultErrorHandler(HttpServletRequest req, Exception e) throws IOException {
        webUtils.saveErrLog(req, e);
        if (e.getCause() instanceof BaseException) {
            int code = ((BaseException) e).getCode();
            return pubTools.genNoMsg(code);
        }
        return pubTools.genNoMsg(StatusCode.EXCEPTION.getCode(), e.getMessage());
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
    public BaseMessage runtimeErrorHandler(HttpServletRequest req, RuntimeException e) throws IOException {
        //记录程序异常日志
        webUtils.saveErrLog(req, e);
        return pubTools.genNoMsg(StatusCode.EXCEPTION.getCode(), e.getMessage());
    }


    /**
     * 使用反射或者代理造成的异常需要根据异常类型单独处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = UndeclaredThrowableException.class)
    @ResponseBody
    public BaseMessage undeclaredThrowableException(HttpServletRequest req, UndeclaredThrowableException e) throws IOException {
        //记录程序异常日志
        webUtils.saveErrLog(req, e);
        //密文解密失败异常
        if (e.getCause() instanceof BadPaddingException) {
            return pubTools.genNoMsg(StatusCode.DECODE_FAIL.getCode(), e.getMessage());
        }
        return pubTools.genNoMsg(StatusCode.EXCEPTION.getCode(), e.getMessage());
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

        return pubTools.genNoMsg(StatusCode.PARAMS_NOT_VALIDATE, errorMsg.subSequence(0, errorMsg.length() - 1).toString());
    }


    /**
     * 自定义异常处理
     *
     * @param req 请求内容
     * @param e   异常信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public BaseMessage baseErrorHandler(HttpServletRequest req, Exception e) throws IOException {
        int code = ((BaseException) e).getCode();
        //记录程序异常日志
        if (StatusCode.EXCEPTION.getCode() == code) {
            webUtils.saveErrLog(req, e);
        } else {
            webUtils.saveLog(req, JSON.toJSONString(pubTools.genNoMsg(code)), req.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase()), null, 0);
        }
        return pubTools.genNoMsg(code, e.getMessage());
    }


    /**
     * 参数解析异常
     *
     * @param e 异常信息
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return pubTools.genNoMsg(StatusCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 不支持当前请求方法
     *
     * @param e 异常信息
     * @return
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseMessage handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return pubTools.genNoMsg(StatusCode.METHOD_NOT_ALLOWED, e.getMessage());
    }

    /**
     * 不支持当前媒体类型
     *
     * @param e 异常信息
     * @return
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseMessage handleHttpMediaTypeNotSupportedException(Exception e) {
        return pubTools.genNoMsg(StatusCode.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
    }


}
