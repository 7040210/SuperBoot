package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <b> 配置中心 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/23
 * @time 12:31
 * @Path org.superboot.StartConfigCenter
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class StartConfigCenter {
    public static void main(String[] args) {
        SpringApplication.run(StartConfigCenter.class, args);
    }
}
