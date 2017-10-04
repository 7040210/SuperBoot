package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <b> 注册中心 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 13:59
 * @Path org.superboot.StartRegistryCenter
 */
@EnableEurekaServer
@SpringBootApplication
public class StartRegistryCenter {
    public static void main(String[] args) {
        SpringApplication.run(StartRegistryCenter.class, args);
    }


}
