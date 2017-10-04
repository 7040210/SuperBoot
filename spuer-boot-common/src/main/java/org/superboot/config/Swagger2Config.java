package org.superboot.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.superboot.base.BaseConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * <b> Api文档配置中心 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/26
 * @time 12:33
 * @Path org.superboot.config.Swagger2Config
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {// 创建API基本信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //添加全局属性
                .globalOperationParameters(addGlobalOperationParameters())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.superboot.controller"))// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示
        return new ApiInfoBuilder()
                .title("RESTful接口文档")// API 标题
                .description("官网：https://github.com/7040210/SuperBoot")// API描述
                .contact(new Contact("Jesion", "https://github.com/7040210/SuperBoot", "7040210@qq.com"))
                .version("1.0")// 版本号
                .build();
    }

    /**
     * 定义全局属性
     *
     * @return
     */
    private List<Parameter> addGlobalOperationParameters() {
        List<Parameter> parameters = Lists.newArrayList();
        //添加Token字段
        parameters.add(new ParameterBuilder()
                .name(BaseConstants.TOKEN_KEY)//定义属性名
                .description("Token")//定义描述
                .modelRef(new ModelRef("string"))//定义数据类型
                .parameterType("header")//定义位置
                .required(false)
                .build());
        return parameters;
    }
}
