package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * <b> 项目共用方法类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/10/17
 * @time
 * @Path org.superboot.StartSleuthCenter
 */
@SpringBootApplication
@EnableZipkinStreamServer
@RefreshScope
public class StartSleuthCenter {

    public static void main(String[] args) {
        SpringApplication.run(StartSleuthCenter.class, args);
    }
}
