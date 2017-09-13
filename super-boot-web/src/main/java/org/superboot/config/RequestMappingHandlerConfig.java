package org.superboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
 * <b> 请求映射配置类 </b>
 * <p>
 * 功能描述:获取请求的全部映射信息
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 17:53
 * @Path org.superboot.config.RequestMappingHandlerConfig
 */
@Configuration
public class RequestMappingHandlerConfig {
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        return mapping;
    }

}