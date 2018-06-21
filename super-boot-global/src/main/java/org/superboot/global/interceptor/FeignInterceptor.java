package org.superboot.global.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.BaseConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * <b> 微服务调用重构Header信息 </b>
 * <p>
 * 功能描述:在微服务之间进行调用服务的时候，Header信息无法传递，此处是重构header信息，方便在调用的时候保持Header信息
 * </p>
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {

        //设置内部服务授权key
        template.header(BaseConstants.FEIGN_KEY, BaseConstants.DEFAULT_FEIGN_TOKEN);

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            //获取请求自带的Header信息,进行Header信息传递，内部调用不再进行数据加密操作
            while (headerNames.hasMoreElements()) {
                String key = "" + headerNames.nextElement();
                //设置消息传输ID
                if (BaseConstants.GLOBAL_KEY.equals(key) || BaseConstants.GLOBAL_KEY.toLowerCase().equals(key)) {
                    String value = request.getHeader(key);
                    template.header(key, value);
                }
                //设置TOKEN信息
                if (BaseConstants.TOKEN_KEY.equals(key) || BaseConstants.TOKEN_KEY.toLowerCase().equals(key)) {
                    String value = request.getHeader(key);
                    template.header(key, value);
                }

                //设置第三方TOKEN信息
                if (BaseConstants.OTHER_TOKEN_KEY.equals(key) || BaseConstants.OTHER_TOKEN_KEY.toLowerCase().equals(key)) {
                    String value = request.getHeader(key);
                    template.header(key, value);
                }

                //设置第三方消息传输ID信息
                if (BaseConstants.OTHER_MESSAGE_ID.equals(key) || BaseConstants.OTHER_MESSAGE_ID.toLowerCase().equals(key)) {
                    String value = request.getHeader(key);
                    template.header(key, value);
                }


            }
        }

    }
}
