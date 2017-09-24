package org.superboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * <b> 监控中心 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 13:59
 * @Path org.superboot.StartOperationCenter
 */
@EnableHystrixDashboard
@SpringBootApplication
@EnableTurbine
public class StartOperationCenter {


    public static void main(String[] args) {
        SpringApplication.run(StartOperationCenter.class, args);
    }

}
