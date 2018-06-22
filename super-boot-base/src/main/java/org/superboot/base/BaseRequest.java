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
 */
@Data
@ApiModel("请求信息")
public class BaseRequest implements Serializable {

    @ApiModelProperty(value = "来源平台,\n" +
            "    PLATFORM_IOS(\"Ios\", \"苹果\"),\n" +
            "    PLATFORM_ANDROID(\"Android\", \"安卓\"),\n" +
            "    PLATFORM_WEB(\"Web\", \"PC端\"),\n" +
            "    PLATFORM_AJAX(\"Ajax\", \"AJAX\"),\n" +
            "    PLATFORM_OTHER(\"Other\", \"其他\")", required = true)
    @NotBlank
    private String platform;

    @ApiModelProperty(value = "客户端版本,根据项目实际信息填写", required = true)
    @NotBlank
    private String version;

    public BaseRequest() {
        super();
    }


}
