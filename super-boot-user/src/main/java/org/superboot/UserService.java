package org.superboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.superboot.common.pub.Pub_DBUtils;

import javax.annotation.PostConstruct;

/**
 * <b> 用户中心 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableAsync
@ServletComponentScan
public class UserService {

    public static void main(String[] args) {
        SpringApplication.run(UserService.class, args);
    }

    @Autowired
    private Pub_DBUtils pubDbUtils;

    /**
     * 扫描URL，如果数据库中不存在，则保存入数据库
     */
    //这个注解很重要，可以在每次启动的时候检查是否有URL更新，RequestMappingHandlerMapping只能在controller层用。这里我们放在主类中
    @PostConstruct
    public void detectHandlerMethods() {
        pubDbUtils.addApiToDB();
    }
}
