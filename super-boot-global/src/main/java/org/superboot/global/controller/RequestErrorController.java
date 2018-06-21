package org.superboot.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.BaseMessage;
import org.superboot.base.StatusCode;
import org.superboot.common.pub.Pub_Tools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <b> 请求错误处理基类 </b>
 * <p>
 * 功能描述:处理请求路径不存在的异常问题
 * </p>
 */
@RestController
public class RequestErrorController implements ErrorController {

    @Resource
    private Pub_Tools pubTools;

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public BaseMessage getErrJsonByWeb(HttpServletRequest req) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(req);
        Map<String, Object> map = errorAttributes.getErrorAttributes(requestAttributes, true);
        if (map.containsKey("status")) {
            int code = Integer.valueOf(map.get("status").toString());
            Object data = map.get("message");
            return pubTools.genNoMsg(code, data.toString());
        } else {
            return pubTools.genNoMsg(StatusCode.NOT_FIND);
        }

    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

