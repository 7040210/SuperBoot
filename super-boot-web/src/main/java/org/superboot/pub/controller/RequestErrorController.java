package org.superboot.pub.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.superboot.base.BaseMessage;
import org.superboot.base.SuperBootCode;
import org.superboot.pub.Pub_Tools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <b> 请求错误处理基类 </b>
 * <p>
 * 功能描述:处理请求路径不存在的异常问题
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 18:20
 * @Path org.superboot.pub.controller.RequestErrorController
 */
@RestController
public class RequestErrorController implements ErrorController {

    @Resource
    Pub_Tools Pub_Tools;

    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public BaseMessage getErrJsonByWeb(HttpServletRequest req) {
        int code = SuperBootCode.NOT_FIND.getCode();
        return Pub_Tools.genNoMsg(code, getStatus(req).getReasonPhrase());
    }


    /**
     * 获取请求响应状态
     *
     * @param request 求情内容
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

