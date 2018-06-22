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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * <b> Api文档配置中心 </b>
 * <p>
 * 功能描述:配置API接口后，可用直接在地址后 添加：/swagger-ui.html 访问接口
 * </p>
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {// 创建API基本信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //添加全局属性
                .globalOperationParameters(addGlobalOperationParameters())
                .select()
                // 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .apis(RequestHandlerSelectors.basePackage(BaseConstants.CONTROLLER_BASE_PATH))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        // 创建API的基本信息，这些信息会在Swagger UI中进行显示
        return new ApiInfoBuilder()
                // API 标题
                .title("接口文档服务")
                // API描述
                .description("默认接口调用需要使用TOKEN，请传入TOKEN后进行接口验证")
                .contact(new Contact("SuperBoot", "http://www.superboot.org", "7040210@gmail.com"))
                // 版本号
                .version("1.0-SNAPSHOT")
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
                //定义属性名
                .name(BaseConstants.TOKEN_KEY)
                //定义描述
                .description("TOKEN")
                //定义数据类型
                .modelRef(new ModelRef("string"))
                //定义位置
                .parameterType("header")
                .required(false)
                .build());


        //添加第三方接入字段
        parameters.add(new ParameterBuilder()
                //定义属性名
                .name(BaseConstants.OTHER_TOKEN_KEY)
                //定义描述
                .description("第三方接入TOKEN")
                //定义数据类型
                .modelRef(new ModelRef("string"))
                //定义位置
                .parameterType("header")
                .required(false)
                .build());


        //添加数据加密密钥字段
        parameters.add(new ParameterBuilder()
                //定义属性名
                .name(BaseConstants.SECRET_KEY)
                //定义描述
                .description("数据加密密钥")
                //定义数据类型
                .modelRef(new ModelRef("string"))
                //定义位置
                .parameterType("header")
                .required(false)
                .build());

        return parameters;
    }
}
