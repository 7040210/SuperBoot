package org.superboot.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <b> 请求消息基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 19:17
 * @Path org.superboot.base.api.BaseRequest
 */
@Data
@ApiModel("请求消息基础信息")
public class BaseRequest implements Serializable {

    @ApiModelProperty(value = "来源平台,参考 SuperBootPlatForm类")
    @NotBlank
    private String platform;

    @ApiModelProperty(value = "客户端版本,根据项目实际信息填写")
    @NotBlank
    private String version;

    @ApiModelProperty(value = "语言，国际化支持，如中文填写 zh_CN")
    @NotBlank
    private String lang;


    public BaseRequest(){
        super();
    }
}
