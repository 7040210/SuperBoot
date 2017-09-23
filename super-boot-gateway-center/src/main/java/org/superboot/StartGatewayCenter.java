package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * <b> 网关中心 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/23
 * @time 9:42
 * @Path org.superboot.StartGatewayCenter
 */
@SpringBootApplication
@EnableZuulProxy
@RefreshScope
public class StartGatewayCenter {


    public static void main(String[] args) {
        SpringApplication.run(StartGatewayCenter.class, args);
    }
}
