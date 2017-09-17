package org.superboot;

import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.superboot.config.RequestMappingHandlerConfig;
import org.superboot.pub.utils.Pub_DBUtils;

import javax.annotation.PostConstruct;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 13:59
 * @Path org.superboot.StartOperationCenter
 */
@EnableSwagger2Doc
@SpringBootApplication
@EnableCaching
public class StartOperationCenter {
    @Autowired
    private RequestMappingHandlerConfig requestMappingHandlerConfig;


    public static void main(String[] args) {
        SpringApplication.run(StartOperationCenter.class, args);
    }

    @Autowired
    private Pub_DBUtils pub_DBUtils;


    /**
     * 扫描URL，如果数据库中不存在，则保存入数据库
     */
    @PostConstruct  //这个注解很重要，可以在每次启动的时候检查是否有URL更新，RequestMappingHandlerMapping只能在controller层用。这里我们放在主类中
    public void detectHandlerMethods(){
        final RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerConfig.requestMappingHandlerMapping ();
        pub_DBUtils.addApiToDB(requestMappingHandlerMapping);
    }
}
