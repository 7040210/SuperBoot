package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * <b> 注册中心 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistryService {

    public static void main(String[] args) {
        SpringApplication.run(RegistryService.class, args);
    }

}
