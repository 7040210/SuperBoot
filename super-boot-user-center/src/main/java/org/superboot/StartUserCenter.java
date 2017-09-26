package org.superboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.superboot.pub.utils.Pub_DBUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

/**
 * <b> 用户中心启动类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:05
 * @Path org.superboot.StartUserCenter
 */
@EnableCaching
@SpringCloudApplication
@EnableFeignClients
@RefreshScope
@EnableSwagger2
public class StartUserCenter {


    public static void main(String[] args) {
        SpringApplication.run(StartUserCenter.class, args);
    }


    @Autowired
    private Pub_DBUtils pub_DBUtils;


    /**
     * 扫描URL，如果数据库中不存在，则保存入数据库
     */
    @PostConstruct  //这个注解很重要，可以在每次启动的时候检查是否有URL更新，RequestMappingHandlerMapping只能在controller层用。这里我们放在主类中
    public void detectHandlerMethods() {
       // pub_DBUtils.addApiToDB();
    }

}
