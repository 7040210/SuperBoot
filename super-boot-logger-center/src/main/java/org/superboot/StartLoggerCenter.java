package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 13:58
 * @Path org.superboot.StartLoggerCenter
 */
@SpringCloudApplication
@RefreshScope
public class StartLoggerCenter {

    public static void main(String[] args) {
        SpringApplication.run(StartLoggerCenter.class, args);
    }

}
