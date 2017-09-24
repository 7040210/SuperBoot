package org.superboot;

import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.superboot.config.RequestMappingHandlerConfig;
import org.superboot.pub.utils.Pub_DBUtils;

import javax.annotation.PostConstruct;

/**
 * <b> 安全中心启动类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 14:19
 * @Path org.superboot.StartSecruityCenter
 */
@EnableSwagger2Doc
@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
public class StartSecruityCenter {


    @Autowired
    private RequestMappingHandlerConfig requestMappingHandlerConfig;



    public static void main(String[] args) {
        SpringApplication.run(StartSecruityCenter.class, args);
    }

    @Autowired
    private Pub_DBUtils pub_DBUtils;


    /**
     * 扫描URL，如果数据库中不存在，则保存入数据库
     */
    @PostConstruct  //这个注解很重要，可以在每次启动的时候检查是否有URL更新，RequestMappingHandlerMapping只能在controller层用。这里我们放在主类中
    public void detectHandlerMethods(){
        pub_DBUtils.addApiToDB(requestMappingHandlerConfig);
    }

}
