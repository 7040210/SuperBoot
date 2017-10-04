package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.superboot.filter.GateWayErrorFilter;
import org.superboot.filter.GateWayPostFilter;
import org.superboot.filter.GateWayPreFilter;
import org.superboot.filter.GateWayRoutingFilter;

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
@SpringCloudApplication
@EnableZuulProxy
@RefreshScope
public class StartGatewayCenter {


    public static void main(String[] args) {
        SpringApplication.run(StartGatewayCenter.class, args);
    }


    @Bean
    public GateWayPreFilter preFilter() {
        return new GateWayPreFilter();
    }

    @Bean
    public GateWayErrorFilter errorFilter() {
        return new GateWayErrorFilter();
    }

    @Bean
    public GateWayRoutingFilter routingFilter() {
        return new GateWayRoutingFilter();
    }


    @Bean
    public GateWayPostFilter postFilter() {
        return new GateWayPostFilter();
    }
}
