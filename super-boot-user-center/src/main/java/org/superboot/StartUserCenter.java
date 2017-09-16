package org.superboot;

import com.didispace.swagger.EnableSwagger2Doc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.superboot.config.RequestMappingHandlerConfig;
import org.superboot.entity.base.BaseApi;
import org.superboot.pub.Pub_Tools;
import org.superboot.repository.sql.base.BaseApiRepository;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
@EnableSwagger2Doc
@SpringBootApplication
@EnableCaching
public class StartUserCenter {


    @Autowired
    private RequestMappingHandlerConfig requestMappingHandlerConfig;

    @Autowired  //业务服务类
    private BaseApiRepository sysApiRepository;


    public static void main(String[] args) {
        SpringApplication.run(StartUserCenter.class, args);
    }

    /**
     * 扫描URL，如果数据库中不存在，则保存入数据库
     */
    @PostConstruct  //这个注解很重要，可以在每次启动的时候检查是否有URL更新，RequestMappingHandlerMapping只能在controller层用。这里我们放在主类中
    public void detectHandlerMethods(){
        final RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerConfig.requestMappingHandlerMapping ();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> mappings = map.keySet();
        for(RequestMappingInfo info : mappings) {
            HandlerMethod method = map.get(info);
            String moduleStr = method.toString();
            if(0<moduleStr.split("\\(").length){
                moduleStr = moduleStr.split("\\(")[0];
            }

            if(2<moduleStr.split(" ").length){
                moduleStr = moduleStr.split(" ")[2];
            }
            int i=moduleStr.lastIndexOf(".");
            moduleStr = moduleStr.substring(0,i);
            String urlparm = info.getPatternsCondition().toString();
            String url = urlparm.substring(1, urlparm.length()-1);
            String methodName = method.getMethod().getName();

            //获取API信息
            List<BaseApi> list = sysApiRepository.findByUrlAndMethodName(url,methodName);
            if(null == list || 0 == list.size() ){
                //只有项目用到的资源才需要添加
                if(moduleStr.startsWith("org.superboot.controller")){
                    BaseApi api = new BaseApi();
                    api.setUrl(url);
                    api.setMethodPath(moduleStr);
                    //获取字段的注解信息，此处用到的swagger配置的注解信息
                    try {
                        api.setMethodName(methodName);
                        //获取对象信息
                        Object o = Class.forName(moduleStr).newInstance();
                        //映射模块路径
                        RequestMapping requestMapping = o.getClass().getAnnotation(RequestMapping.class);
                        if(null != requestMapping){
                            api.setModulePath(StringUtils.join(requestMapping.value(),","));
                        }
                        //设置模块名称
                        Api api_annotation = o.getClass().getAnnotation(Api.class);
                        if(null != api_annotation){
                            api.setModuleName(StringUtils.join(api_annotation.tags(),","));
                        }
                        //根据API注解设置API名称及备注信息
                        Annotation operation_annotation = Pub_Tools.getMothodAnnotationByAnnotation(o, ApiOperation.class,methodName);
                        if(null != operation_annotation){
                            api.setApiName( ((ApiOperation) operation_annotation).value());
                            api.setRemark(((ApiOperation) operation_annotation).notes());
                        }
                        sysApiRepository.save(api);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
