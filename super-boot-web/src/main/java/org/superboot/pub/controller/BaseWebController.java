package org.superboot.pub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * <b> 前端框架基类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 16:08
 * @Path org.superboot.pub.controller.BaseWebController
 */
@Controller
public class BaseWebController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.server.host}")
    private String serverHost;

    @Value("${api.server.port}")
    private String serverPort;

    /**
     * 获取服务器端地址
     * @return
     */
    public String getServerUrl(){
        return serverHost+":"+serverPort;
    }

    /**
     * 获取RESTful服务
     * @return
     */
    public RestTemplate getRestTemplate(){
        return restTemplate;
    }


}
